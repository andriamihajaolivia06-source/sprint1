package com.sprint1;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
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

        // === 1. ROUTE EXACTE ===
        if (routes.containsKey(normalized)) {
            executeRoute(normalized, request, response, routePath);
            return;
        }

        // === 2. ROUTE AVEC {id} (ex: /okay/1, /okay/20) ===
    for (String routeKey : routes.keySet()) {
        if (routeKey.contains("{id}")) {
            String pattern = routeKey.replace("{id}", "([^/]+)");
            if (normalized.matches(pattern)) {
                // EXTRAIRE L'ID
                String[] urlParts = normalized.split("/");
                String[] routeParts = routeKey.split("/");
                for (int i = 0; i < routeParts.length; i++) {
                    if ("{id}".equals(routeParts[i])) {
                        String idValue = urlParts[i];
                        request.setAttribute("id", idValue);  // L'id est disponible !
                        System.out.println("ID capturé : " + idValue);
                        break;
                    }
                }
                executeRoute(routeKey, request, response, routePath);
                return;
            }
        }
    }

        // === 3. ROUTE PARTIELLE : n'existe pas ===
        boolean isPartialRoute = false;
        for (String route : routes.keySet()) {
            if (normalized.startsWith(route + "/") || normalized.equals(route)) {
                isPartialRoute = true;
                break;
            }
        }
        if (isPartialRoute) {
            sendNotFound(response, routePath, "n'existe pas");
            return;
        }

        // === 4. FICHIER STATIQUE / JSP ===
        String resourcePath = "/" + (relativeUri.isEmpty() ? "index.html" : relativeUri);
        if (getServletContext().getResource(resourcePath) != null) {
            if (resourcePath.endsWith(".jsp")) {
                getServletContext().getNamedDispatcher("jsp").forward(request, response);
                return;
            }
            request.getRequestDispatcher(resourcePath).forward(request, response);
            return;
        }

        // === 5. URL SAISIE ===
        response.setContentType("text/html; charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<html><head><title>FrontServlet</title></head><body>");
            out.println("<h1>URL saisie : " + relativeUri + "</h1>");
            out.println("</body></html>");
        }
    }

    // === EXÉCUTION D'UNE ROUTE (exacte ou avec {id}) ===
private void executeRoute(String routeKey, HttpServletRequest request, HttpServletResponse response, String routePath)
        throws ServletException, IOException {

    Method method = routes.get(routeKey);
    Object controller = controllers.get(method.getDeclaringClass().getName());

    try {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];

        // VÉRIFICATION + INJECTION
        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            RequestParam rp = param.getAnnotation(RequestParam.class);

            if (rp != null) {
                args[i] = request.getParameter(rp.value());
            } else {
                if (param.isNamePresent()) {
                    String expectedName = param.getName();
                    String value = request.getParameter(expectedName);

                    if (value == null) {
                        // NOM INCORRECT → ON BLOQUE TOUT
                        request.setAttribute("error", "incorrecte");
                        request.setAttribute("errorMessage", 
                            "Le champ du formulaire doit avoir exactement <strong>name=\"" + expectedName + "\"</strong><br>" +
                            "Mais vous avez utilisé un autre nom ou aucun champ.");
                        request.getRequestDispatcher("/resultat.jsp").forward(request, response);
                        return; // ON SORT COMPLÈTEMENT
                    }
                    args[i] = value;
                }
            }
        }

        // TOUT EST BON → on exécute la méthode
        Object result = method.invoke(controller, args);

        // On gère le résultat normalement
        handleResult(result, request, response, routePath);

    } catch (Exception e) {
        // ON NE FAIT PLUS DE FORWARD ICI SI ON A DÉJÀ AFFICHÉ L'ERREUR
        // Sinon on écrase le message "incorrecte"
        e.printStackTrace();
        request.setAttribute("error", "incorrecte");
        request.setAttribute("errorMessage", "Erreur interne : " + e.getMessage());
        request.getRequestDispatcher("/resultat.jsp").forward(request, response);
    }
}

    // === GESTION DU RÉSULTAT (String, ModelView, void) ===
    private void handleResult(Object result, HttpServletRequest request, HttpServletResponse response, String routePath)
            throws ServletException, IOException {

        if (result instanceof ModelView) {
            ModelView mv = (ModelView) result;
            String viewPath = "/" + mv.getView();

            // Transfert des données
            for (Map.Entry<String, Object> entry : mv.getData().entrySet()) {
                request.setAttribute(entry.getKey(), entry.getValue());
            }

            if (getServletContext().getResource(viewPath) != null) {
                request.getRequestDispatcher(viewPath).forward(request, response);
            } else {
                response.sendError(404, "Vue introuvable : " + viewPath);
            }
        }
        else if (result instanceof String) {
            response.setContentType("text/html; charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                out.println("<html><head><title>Résultat</title></head><body>");
                out.println("<h1 style='color:blue'>" + result + "</h1>");
                out.println("<p>URL : <strong>" + routePath + "</strong></p>");
                out.println("</body></html>");
            }
        }
        else {
            response.setContentType("text/html; charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                out.println("<html><head><title>OK</title></head><body>");
                out.println("<h1 style='color:green'>existe bien</h1>");
                out.println("<p>URL : <strong>" + routePath + "</strong></p>");
                out.println("</body></html>");
            }
        }
    }

    // === 404 PERSONNALISÉ ===
    private void sendNotFound(HttpServletResponse response, String routePath, String message) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<html><head><title>404</title></head><body>");
            out.println("<h1 style='color:red'>" + message + "</h1>");
            out.println("<p>URL : <strong>" + routePath + "</strong></p>");
            out.println("</body></html>");
        }
    }

    // === SCAN DES ROUTES ===
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
                                    System.out.println("ROUTE : " + fullPath);
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
