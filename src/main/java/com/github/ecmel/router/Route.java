package com.github.ecmel.router;

@FunctionalInterface
public interface Route
{
    void handle(HttpRequest req, HttpResponse res) throws Exception;
}
