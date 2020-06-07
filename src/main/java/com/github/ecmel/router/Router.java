package com.github.ecmel.router;

import java.io.IOException;
import java.util.ArrayDeque;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

public class Router extends HandlerCollection
{
    private ArrayDeque<String> context = new ArrayDeque<>();

    public String getBody(HttpServletRequest req) throws IOException
    {
        StringBuilder builder = new StringBuilder();
        req.getReader().lines().forEach((e) -> builder.append(e));
        return builder.toString();
    }

    private String getContext(String path)
    {
        StringBuilder builder = new StringBuilder();
        context.descendingIterator().forEachRemaining((e) -> builder.append(e));
        builder.append(path);

        return builder.toString().replaceAll("/+", "/");
    }

    public Router use(WebSocketCreator creator)
    {
        return use("", creator);
    }

    public Router use(String path, WebSocketCreator creator)
    {
        addHandler(new RouteWebSocketHandler(getContext(path), creator));
        return this;
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
        addHandler(new RouteHandler("ALL", getContext(path), route, false));
        return this;
    }

    public Router get(Route route)
    {
        return get("", route);
    }

    public Router get(String path, Route route)
    {
        addHandler(new RouteHandler("GET", getContext(path), route, true));
        return this;
    }

    public Router put(Route route)
    {
        return put("", route);
    }

    public Router put(String path, Route route)
    {
        addHandler(new RouteHandler("PUT", getContext(path), route, true));
        return this;
    }

    public Router post(Route route)
    {
        return post("", route);
    }

    public Router post(String path, Route route)
    {
        addHandler(new RouteHandler("POST", getContext(path), route, true));
        return this;
    }

    public Router delete(Route route)
    {
        return delete("", route);
    }

    public Router delete(String path, Route route)
    {
        addHandler(new RouteHandler("DELETE", getContext(path), route, true));
        return this;
    }
}
