package com.github.ecmel.router;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class HttpResponse implements HttpServletResponse
{
    private final HttpServletResponse delegate;

    public HttpResponse(HttpServletResponse delegate)
    {
        this.delegate = delegate;
    }

    @Override
    public void addCookie(Cookie cookie)
    {
        delegate.addCookie(cookie);
    }

    @Override
    public boolean containsHeader(String name)
    {
        return delegate.containsHeader(name);
    }

    @Override
    public String encodeURL(String url)
    {
        return delegate.encodeURL(url);
    }

    @Override
    public String getCharacterEncoding()
    {
        return delegate.getCharacterEncoding();
    }

    @Override
    public String encodeRedirectURL(String url)
    {
        return delegate.encodeRedirectURL(url);
    }

    @Override
    public String getContentType()
    {
        return delegate.getContentType();
    }

    @Override
    @Deprecated
    public String encodeUrl(String url)
    {
        return delegate.encodeUrl(url);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException
    {
        return delegate.getOutputStream();
    }

    @Override
    @Deprecated
    public String encodeRedirectUrl(String url)
    {
        return delegate.encodeRedirectUrl(url);
    }

    @Override
    public void sendError(int sc, String msg) throws IOException
    {
        delegate.sendError(sc, msg);
    }

    @Override
    public PrintWriter getWriter() throws IOException
    {
        return delegate.getWriter();
    }

    @Override
    public void sendError(int sc) throws IOException
    {
        delegate.sendError(sc);
    }

    @Override
    public void setCharacterEncoding(String charset)
    {
        delegate.setCharacterEncoding(charset);
    }

    @Override
    public void sendRedirect(String location) throws IOException
    {
        delegate.sendRedirect(location);
    }

    @Override
    public void setContentLength(int len)
    {
        delegate.setContentLength(len);
    }

    @Override
    public void setContentLengthLong(long len)
    {
        delegate.setContentLengthLong(len);
    }

    @Override
    public void setDateHeader(String name, long date)
    {
        delegate.setDateHeader(name, date);
    }

    @Override
    public void setContentType(String type)
    {
        delegate.setContentType(type);
    }

    @Override
    public void addDateHeader(String name, long date)
    {
        delegate.addDateHeader(name, date);
    }

    @Override
    public void setHeader(String name, String value)
    {
        delegate.setHeader(name, value);
    }

    @Override
    public void setBufferSize(int size)
    {
        delegate.setBufferSize(size);
    }

    @Override
    public void addHeader(String name, String value)
    {
        delegate.addHeader(name, value);
    }

    @Override
    public void setIntHeader(String name, int value)
    {
        delegate.setIntHeader(name, value);
    }

    @Override
    public int getBufferSize()
    {
        return delegate.getBufferSize();
    }

    @Override
    public void addIntHeader(String name, int value)
    {
        delegate.addIntHeader(name, value);
    }

    @Override
    public void flushBuffer() throws IOException
    {
        delegate.flushBuffer();
    }

    @Override
    public void setStatus(int sc)
    {
        delegate.setStatus(sc);
    }

    @Override
    public void resetBuffer()
    {
        delegate.resetBuffer();
    }

    @Override
    public boolean isCommitted()
    {
        return delegate.isCommitted();
    }

    @Override
    @Deprecated
    public void setStatus(int sc, String sm)
    {
        delegate.setStatus(sc, sm);
    }

    @Override
    public void reset()
    {
        delegate.reset();
    }

    @Override
    public int getStatus()
    {
        return delegate.getStatus();
    }

    @Override
    public String getHeader(String name)
    {
        return delegate.getHeader(name);
    }

    @Override
    public void setLocale(Locale loc)
    {
        delegate.setLocale(loc);
    }

    @Override
    public Collection<String> getHeaders(String name)
    {
        return delegate.getHeaders(name);
    }

    @Override
    public Collection<String> getHeaderNames()
    {
        return delegate.getHeaderNames();
    }

    @Override
    public Locale getLocale()
    {
        return delegate.getLocale();
    }
}
