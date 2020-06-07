package com.github.ecmel.router;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@FunctionalInterface
public interface Route
{
    void handle(HttpServletRequest req, HttpServletResponse res) throws Exception;
}
