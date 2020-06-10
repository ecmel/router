package com.github.ecmel.router;

import static org.junit.Assert.*;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RouterTest
{
    private static String uri = "http://localhost:8080";
    private Router router;
    private Server server;
    private HttpClient client;

    @Before
    public void setUp() throws Exception
    {
        router = new Router();
        server = new Server(8080);
        client = new HttpClient();

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

    @Test
    public void shouldHandleGetRequest() throws Exception
    {
        router.get("/get", (req, res) -> res.getWriter().print("get"));

        ContentResponse res = client
            .newRequest(uri)
            .method(HttpMethod.GET)
            .path("/get")
            .send();

        assertEquals(200, res.getStatus());
        assertEquals("get", res.getContentAsString());
    }

    @Test
    public void shouldHandlePostRequest() throws Exception
    {
        router.post("/post", (req, res) -> res.getWriter().print("post"));

        ContentResponse res = client
            .newRequest(uri)
            .method(HttpMethod.POST)
            .path("/post")
            .send();

        assertEquals(200, res.getStatus());
        assertEquals("post", res.getContentAsString());
    }

    @Test
    public void shouldHandlePutRequest() throws Exception
    {
        router.put("/put", (req, res) -> res.getWriter().print("put"));

        ContentResponse res = client
            .newRequest(uri)
            .method(HttpMethod.PUT)
            .path("/put")
            .send();

        assertEquals(200, res.getStatus());
        assertEquals("put", res.getContentAsString());
    }

    @Test
    public void shouldHandleDeleteRequest() throws Exception
    {
        router.delete("/delete", (req, res) -> res.getWriter().print("delete"));

        ContentResponse res = client
            .newRequest(uri)
            .method(HttpMethod.DELETE)
            .path("/delete")
            .send();

        assertEquals(200, res.getStatus());
        assertEquals("delete", res.getContentAsString());
    }

    @Test
    public void shouldHandleRoutesSequentially() throws Exception
    {
        router
            .use("/api", () -> router
                .all("/*", (req, res) -> res.getWriter().print("1"))
                .get("/get", (req, res) -> res.getWriter().print("2"))
                .get("/get", (req, res) -> res.getWriter().print("3"))
                .all("/*", (req, res) -> res.getWriter().print("4")))
            .get("/*", (req, res) -> res.getWriter().print("5"));

        ContentResponse res = client
            .newRequest(uri)
            .method(HttpMethod.GET)
            .path("/api/get")
            .send();

        assertEquals(200, res.getStatus());
        assertEquals("12345", res.getContentAsString());
    }

    @Test
    public void shouldDecodePathParams() throws Exception
    {
        router.get("/get/:id", (req, res) -> res.getWriter().print(req.getPathParameter("id")));

        ContentResponse res = client
            .newRequest(uri)
            .method(HttpMethod.GET)
            .path("/get/1")
            .send();

        assertEquals(200, res.getStatus());
        assertEquals("1", res.getContentAsString());
    }

    @Test
    public void shouldNormalizePath() throws Exception
    {
        router.get("//get///me/now", (req, res) -> res.getWriter().print("get"));

        ContentResponse res = client
            .newRequest(uri)
            .method(HttpMethod.GET)
            .path("/get/me/now")
            .send();

        assertEquals(200, res.getStatus());
        assertEquals("get", res.getContentAsString());
    }

    @Test
    public void shouldRespondNotFound() throws Exception
    {
        router.all("/get", (req, res) -> res.getWriter().print("get"));

        ContentResponse res = client
            .newRequest(uri)
            .method(HttpMethod.GET)
            .accept("application/json")
            .path("/get")
            .send();

        assertEquals(404, res.getStatus());
        assertTrue(res.getContentAsString().startsWith("{"));
    }

    @Test
    public void shouldHandleManyRoutes() throws Exception
    {
        for (int i = 0; i < 1000; i++)
        {
            router.get("/get/" + i, (req, res) -> res.getWriter().print("get"));
        }

        ContentResponse res = null;

        for (int i = 0; i < 1000; i++)
        {
            res = client
                .newRequest(uri)
                .method(HttpMethod.GET)
                .path("/get/" + i)
                .send();

            assertEquals(200, res.getStatus());
            assertEquals("get", res.getContentAsString());
        }

        long startTime = System.nanoTime();

        for (int i = 0; i < 100; i++)
        {
            res = client
                .newRequest(uri)
                .method(HttpMethod.GET)
                .path("/get/0")
                .send();

            assertEquals(200, res.getStatus());
        }

        long elapsedTime = System.nanoTime() - startTime;

        System.out.format("Get first route: %d ms %n", elapsedTime / 1000000);

        startTime = System.nanoTime();

        for (int i = 0; i < 100; i++)
        {
            res = client
                .newRequest(uri)
                .method(HttpMethod.GET)
                .path("/get/999")
                .send();

            assertEquals(200, res.getStatus());
        }

        elapsedTime = System.nanoTime() - startTime;

        System.out.format("Get last route : %d ms %n", elapsedTime / 1000000);
    }
}
