package com.github.ecmel.router;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

class RouteHandler extends AbstractHandler
{
    private final String method;
    private final ExpressRoute path;
    private final Route route;
    private final boolean handled;

    public RouteHandler(String method, String path, Route route, boolean handled)
    {
        this.method = method;
        this.path = new ExpressRoute(path);
        this.route = route;
        this.handled = handled;
    }

    private void handle(
        HttpServletRequest request,
        HttpServletResponse response) throws IOException, ServletException
    {
        try
        {
            route.handle(request, response);
        }
        catch (IOException | ServletException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new ServletException(e);
        }
    }

    @Override
    public void handle(
        String target,
        Request baseRequest,
        HttpServletRequest request,
        HttpServletResponse response) throws IOException, ServletException
    {
        if ((method.equals("ALL")
            || method.equals(baseRequest.getMethod()))
            && path.matches(target))
        {
            path.getParametersFromPath(target).forEach((k, v) -> request.setAttribute(k, v));
            handle(request, response);
            baseRequest.setHandled(handled);
        }
    }
}
