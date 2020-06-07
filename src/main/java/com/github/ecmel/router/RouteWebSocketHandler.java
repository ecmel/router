package com.github.ecmel.router;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class RouteWebSocketHandler extends WebSocketHandler
{
    private final String path;
    private final WebSocketCreator creator;

    public RouteWebSocketHandler(String path, WebSocketCreator creator)
    {
        this.path = path;
        this.creator = creator;
    }

    @Override
    public void configure(WebSocketServletFactory factory)
    {
        factory.setCreator(creator);
    }

    @Override
    public void handle(
        String target,
        Request baseRequest,
        HttpServletRequest request,
        HttpServletResponse response) throws IOException, ServletException
    {
        if (path.equals(target))
        {
            super.handle(target, baseRequest, request, response);
        }
    }
}
