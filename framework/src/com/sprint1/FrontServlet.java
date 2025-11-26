package com.sprint1;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.HashMap;
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

        // === CORRECTION CRUCIALE : on ne prend que la partie après le context path ===
        String fullUri = request.getRequestURI();                    // ex: /sprint1/personne/good/777
        String contextPath = request.getContextPath();                // ex: /sprint1
        String uri = fullUri.substring(contextPath.length());         // → /personne/good/777

        // === EXTRACTION DES PATH VARIABLES AVEC REGEX (MAGIE) ===
        Map<String, String> pathVariables = new HashMap<>();

        String regex = routeKey.replaceAll("\\{[^/]+\\}", "([^/]+)");
        regex = "^" + regex + "$";  // ex: ^/good/([^/]+)$

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(uri);  // ← ON UTILISE "uri", PAS request.getRequestURI()

        if (matcher.matches()) {
            Pattern varPattern = Pattern.compile("\\{([^}]+)\\}");
            Matcher varMatcher = varPattern.matcher(routeKey);

            int groupIndex = 1;
            while (varMatcher.find()) {
                String varName = varMatcher.group(1);
                if (groupIndex <= matcher.groupCount()) {
                    pathVariables.put(varName, matcher.group(groupIndex));
                }
                groupIndex++;
            }
        }

        // Debug temporaire (tu peux l'enlever après)
        System.out.println("URI utilisée : " + uri);
        System.out.println("RouteKey : " + routeKey);
        System.out.println("PathVariables trouvés : " + pathVariables);

        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            RequestParam rp = param.getAnnotation(RequestParam.class);

            String value = null;

            if (rp != null && !rp.value().isEmpty()) {
                value = request.getParameter(rp.value());
            } else {
                String paramName = param.getName();

                if (pathVariables.containsKey(paramName)) {
                    value = pathVariables.get(paramName);
                } else if (!paramName.startsWith("arg")) {
                    value = request.getParameter(paramName);
                } else {
                    int index = 0;
                    for (String val : pathVariables.values()) {
                        if (index == i) {
                            value = val;
                            break;
                        }
                        index++;
                    }
                }
            }

            if (value == null || value.isEmpty()) {
                if (rp != null && !rp.value().isEmpty()) {
                    request.setAttribute("error", "incorrecte");
                    request.setAttribute("errorMessage", "Le champ <strong>name=\"" + rp.value() + "\"</strong> est manquant.");
                    request.getRequestDispatcher("/resultat.jsp").forward(request, response);
                    return;
                }
                value = "";
            }

            args[i] = value;
        }

        Object result = method.invoke(controller, args);

        if (result instanceof String) {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<!DOCTYPE html>");
            out.println("<html><head><meta charset='UTF-8'><title>ID Capturé</title></head>");
            out.println("<body style='font-family:Arial;text-align:center;margin-top:150px;background:#e8f5e9;'>");
            out.println("<h1 style='color:#1b5e20;font-size:60px;'>" + result + "</h1>");
            out.println("<p><a href='/sprint1/personne/form' style='color:#2e7d32;font-size:22px;text-decoration:none;'>Retour</a></p>");
            out.println("</body></html>");
        } else {
            handleResult(result, request, response, routePath);
        }

    } catch (Exception e) {
        e.printStackTrace();
        response.getWriter().println("<h1 style='color:red;'>Erreur : " + e.getMessage() + "</h1>");
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
