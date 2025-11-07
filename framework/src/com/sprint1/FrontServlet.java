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
    private static boolean initialized = false;

    @Override
    public void init() throws ServletException {
        
    }

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

        if (relativeUri.startsWith("/")) {
            relativeUri = relativeUri.substring(1);
        }

        String routePath = "/" + relativeUri;
        String normalized = normalizePath(routePath);

        System.out.println("TEST URL : " + routePath + " → normalisé : " + normalized);

       
        if (routes.containsKey(normalized)) {
            System.out.println("ROUTE EXACTE TROUVÉE !");
            response.setContentType("text/html; charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                out.println("<html><head><title>OK</title></head><body>");
                out.println("<h1 style='color:green'>existe bien</h1>");
                out.println("<p>URL : <strong>" + routePath + "</strong></p>");
                out.println("</body></html>");
            }
            return;
        }

        // === 3. ROUTE PARTIELLE : n'existe pas (ex: /api sans /hello) ===
        boolean isUnderKnownRoute = false;
        for (String route : routes.keySet()) {
            if (normalized.startsWith(route + "/") || normalized.equals(route)) {
                isUnderKnownRoute = true;
                break;
            }
        }

        if (isUnderKnownRoute) {
            System.out.println("ROUTE PARTIELLE : n'existe pas");
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
            System.out.println("FICHIER STATIQUE TROUVÉ : " + resourcePath);
            if (resourcePath.endsWith(".jsp")) {
                getServletContext().getNamedDispatcher("jsp").forward(request, response);
                return;
            }
            request.getRequestDispatcher(resourcePath).forward(request, response);
            return;
        }

        
        System.out.println("AUCUNE CORRESPONDANCE : URL saisie");
        response.setContentType("text/html; charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<html>");
            out.println("<head><title>FrontServlet</title></head>");
            out.println("<body>");
            out.println("<h1>URL saisie : " + relativeUri + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    
    private void scanRoutes() {
        try {
            String packageName = "com.sprint1";
            String path = packageName.replace('.', '/');
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> resources = classLoader.getResources(path);

            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                System.out.println("SCAN : " + resource);
                if (resource.getProtocol().equals("file")) {
                    File dir = new File(resource.toURI());
                    for (File file : dir.listFiles()) {
                        if (file.getName().endsWith(".class") && !file.getName().contains("$")) {
                            String className = packageName + "." + file.getName().replace(".class", "");
                            System.out.println("CLASSE TROUVÉE : " + className);
                            Class<?> cls = Class.forName(className);
                            if (cls.isAnnotationPresent(Controller.class)) {
                                Controller controller = cls.getAnnotation(Controller.class);
                                String basePath = controller.value();
                                System.out.println("CONTROLLER : " + cls.getSimpleName() + " → " + basePath);

                                for (Method method : cls.getDeclaredMethods()) {
                                    if (method.isAnnotationPresent(PathAnnotation.class)) {
                                        PathAnnotation pathAnnotation = method.getAnnotation(PathAnnotation.class);
                                        String fullPath = normalizePath(basePath + pathAnnotation.value());
                                        routes.put(fullPath, method);
                                        System.out.println("ROUTE AJOUTÉE : " + fullPath);
                                    }
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
