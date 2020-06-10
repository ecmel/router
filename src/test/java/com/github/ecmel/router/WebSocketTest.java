package com.github.ecmel.router;

import static org.junit.Assert.*;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WebSocketTest
{
    private static String uri = "ws://localhost:8080";
    private WebSocketClient client;
    private Server server;
    private Router router;

    @Before
    public void setUp() throws Exception
    {
        router = new Router();
        server = new Server(8080);
        client = new WebSocketClient();

        router.socket((req, res) -> new ServerSocket());

        server.setHandler(router);

        server.start();
        client.start();
    }

    @After
    public void tearDown() throws Exception
    {
        client.stop();
        server.stop();
    }

    @WebSocket
    public class ServerSocket
    {
        @OnWebSocketMessage
        public void message(Session session, String message) throws IOException
        {
            session.getRemote().sendString(message);
        }
    }

    @WebSocket
    public class ClientSocket
    {
        private final CountDownLatch latch = new CountDownLatch(1);
        private String message;

        public String get() throws InterruptedException
        {
            latch.await(5, TimeUnit.SECONDS);
            return message;
        }

        @OnWebSocketConnect
        public void connected(Session session) throws IOException
        {
            session.getRemote().sendString("Hello");
        }

        @OnWebSocketClose
        public void closed(Session session, int statusCode, String reason)
        {
            latch.countDown();
        }

        @OnWebSocketMessage
        public void message(Session session, String message) throws IOException
        {
            this.message = message;
            session.close();
        }
    }

    @Test
    public void shouldReturnHello() throws Exception
    {
        ClientSocket socket = new ClientSocket();
        client.connect(socket, new URI(uri));

        assertEquals("Hello", socket.get());
    }
}
