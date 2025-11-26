package com.sprint1;

import java.lang.reflect.Method;

public class RouteMapping {
    public final String httpMethod;  // "GET", "POST" ou "ANY"
    public final String path;
    public final Method method;
    public final Object controller;

    public RouteMapping(String httpMethod, String path, Method method, Object controller) {
        this.httpMethod = httpMethod;
        this.path = path;
        this.method = method;
        this.controller = controller;
    }
}