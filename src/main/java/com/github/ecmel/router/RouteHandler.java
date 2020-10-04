package com.github.ecmel.router;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.markmcguill.express.routing.ExpressRoute;
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
        boolean handled = method.equals(baseRequest.getMethod());

        if ((handled || method.equals("ALL")) && path.matches(target))
        {
            try
            {
                HttpRequest req = (HttpRequest) request;
                HttpResponse res = (HttpResponse) response;

                req.setPathParameters(path.getParametersFromPath(target));
                route.handle(req, res);

                if (handled && !baseRequest.isHandled())
                {
                    baseRequest.setHandled(true);
                }
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
