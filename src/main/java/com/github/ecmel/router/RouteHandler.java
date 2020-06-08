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

    public RouteHandler(String method, String path, Route route)
    {
        this.method = method;
        this.path = new ExpressRoute(path);
        this.route = route;
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
            try
            {
                HttpRequest req = new HttpRequest(request, path.getParametersFromPath(target));
                HttpResponse res = new HttpResponse(response);

                route.handle(req, res);

                baseRequest.setHandled(req.isHandled());
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
    }
}
