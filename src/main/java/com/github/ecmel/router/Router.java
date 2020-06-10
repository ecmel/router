package com.github.ecmel.router;

import java.io.IOException;
import java.util.ArrayDeque;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

public class Router extends HandlerCollection
{
    private ArrayDeque<String> context = new ArrayDeque<>();

    private String getContext(String path)
    {
        StringBuilder builder = new StringBuilder();
        context.descendingIterator().forEachRemaining((e) -> builder.append(e));
        builder.append(path);

        return builder.toString().replaceAll("/+", "/");
    }

    public Router use(RouteGroup group)
    {
        return use("", group);
    }

    public Router use(String path, RouteGroup group)
    {
        context.push(path);
        group.init();
        context.pop();
        return this;
    }

    public Router all(Route route)
    {
        return all("", route);
    }

    public Router all(String path, Route route)
    {
        addHandler(new RouteHandler("ALL", getContext(path), route));
        return this;
    }

    public Router get(Route route)
    {
        return get("", route);
    }

    public Router get(String path, Route route)
    {
        addHandler(new RouteHandler("GET", getContext(path), route));
        return this;
    }

    public Router put(Route route)
    {
        return put("", route);
    }

    public Router put(String path, Route route)
    {
        addHandler(new RouteHandler("PUT", getContext(path), route));
        return this;
    }

    public Router post(Route route)
    {
        return post("", route);
    }

    public Router post(String path, Route route)
    {
        addHandler(new RouteHandler("POST", getContext(path), route));
        return this;
    }

    public Router delete(Route route)
    {
        return delete("", route);
    }

    public Router delete(String path, Route route)
    {
        addHandler(new RouteHandler("DELETE", getContext(path), route));
        return this;
    }

    public Router socket(WebSocketCreator creator)
    {
        return socket("", creator);
    }

    public Router socket(String path, WebSocketCreator creator)
    {
        addHandler(new RouteWebSocketHandler(getContext(path), creator));
        return this;
    }

    @Override
    public void handle(
        String target,
        Request baseRequest,
        HttpServletRequest request,
        HttpServletResponse response) throws IOException, ServletException
    {
        super.handle(target, baseRequest, new HttpRequest(request), new HttpResponse(response));
    }
}
