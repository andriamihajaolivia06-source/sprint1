package com.sprint1;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

public class FrontServlet extends HttpServlet {

    private static final Map<String, Method> routes = new HashMap<>();
    private static final Map<String, Object> controllers = new HashMap<>();
    private static boolean initialized = false;

    private void servicePersonnalisee(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!initialized) {
            System.out.println("INITIALISATION : scan des routes...");
            scanRoutes();
            System.out.println("ROUTES DÉTECTÉES : " + routes.keySet());
            initialized = true;
        }

        String fullUri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String relativeUri = fullUri.substring(contextPath.length());
        if (relativeUri.startsWith("/")) relativeUri = relativeUri.substring(1);

        String routePath = "/" + relativeUri;
        String normalized = normalizePath(routePath);

        System.out.println("TEST URL : " + routePath + " → normalisé : " + normalized);

       
        if (routes.containsKey(normalized)) {
            Method method = routes.get(normalized);
            Object controller = controllers.get(method.getDeclaringClass().getName());

            try {
                Object result = method.invoke(controller);

                if (result instanceof ModelView) {
                    ModelView mv = (ModelView) result;
                    String viewPath = "/" + mv.getView();

                    System.out.println("DISPATCHER VERS : " + viewPath);

                    if (getServletContext().getResource(viewPath) != null) {
                        request.getRequestDispatcher(viewPath).forward(request, response);
                    } else {
                        response.sendError(404, "Vue introuvable : " + viewPath);
                    }
                    return;
                }

               
                if (result instanceof String) {
                    response.setContentType("text/html; charset=UTF-8");
                    try (PrintWriter out = response.getWriter()) {
                        out.println("<html><head><title>Résultat</title></head><body>");
                        out.println("<h1 style='color:blue'>" + result + "</h1>");
                        out.println("<p>URL : <strong>" + routePath + "</strong></p>");
                        out.println("</body></html>");
                    }
                    return;
                }

              
                response.setContentType("text/html; charset=UTF-8");
                try (PrintWriter out = response.getWriter()) {
                    out.println("<html><head><title>OK</title></head><body>");
                    out.println("<h1 style='color:green'>existe bien</h1>");
                    out.println("<p>URL : <strong>" + routePath + "</strong></p>");
                    out.println("</body></html>");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(500, "Erreur d'exécution");
            }
            return;
        }

       
        boolean isPartialRoute = false;
        for (String route : routes.keySet()) {
            if (normalized.startsWith(route + "/") || normalized.equals(route)) {
                isPartialRoute = true;
                break;
            }
        }
        if (isPartialRoute) {
            response.setContentType("text/html; charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                out.println("<html><head><title>404</title></head><body>");
                out.println("<h1 style='color:red'>n'existe pas</h1>");
                out.println("<p>URL : <strong>" + routePath + "</strong></p>");
                out.println("</body></html>");
            }
            return;
        }

       
        String resourcePath = "/" + (relativeUri.isEmpty() ? "index.html" : relativeUri);
        if (getServletContext().getResource(resourcePath) != null) {
            if (resourcePath.endsWith(".jsp")) {
                getServletContext().getNamedDispatcher("jsp").forward(request, response);
                return;
            }
            request.getRequestDispatcher(resourcePath).forward(request, response);
            return;
        }

       
        response.setContentType("text/html; charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<html><head><title>FrontServlet</title></head><body>");
            out.println("<h1>URL saisie : " + relativeUri + "</h1>");
            out.println("</body></html>");
        }
    }

    private void scanRoutes() {
        try {
            String packageName = "com.sprint1";
            String path = packageName.replace('.', '/');
            URL resource = getServletContext().getClassLoader().getResource(path);
            if (resource == null) return;

            if (resource.getProtocol().equals("file")) {
                File dir = new File(resource.toURI());
                for (File file : dir.listFiles()) {
                    if (file.getName().endsWith(".class") && !file.getName().contains("$")) {
                        String className = packageName + "." + file.getName().replace(".class", "");
                        Class<?> cls = Class.forName(className);
                        if (cls.isAnnotationPresent(Controller.class)) {
                            Controller controller = cls.getAnnotation(Controller.class);
                            String basePath = controller.value();
                            Object instance = cls.getDeclaredConstructor().newInstance();
                            controllers.put(cls.getName(), instance);

                            for (Method method : cls.getDeclaredMethods()) {
                                if (method.isAnnotationPresent(PathAnnotation.class)) {
                                    PathAnnotation pa = method.getAnnotation(PathAnnotation.class);
                                    String fullPath = normalizePath(basePath + pa.value());
                                    routes.put(fullPath, method);
                                    System.out.println("ROUTE : " + fullPath + " → " + method.getName());
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String normalizePath(String path) {
        return path.replaceAll("/+", "/").replaceAll("/$", "");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        servicePersonnalisee(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        servicePersonnalisee(request, response);
    }
}
