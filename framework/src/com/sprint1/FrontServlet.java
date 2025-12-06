package com.sprint1;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.*;
import java.lang.reflect.*;
import java.net.URL;
import java.util.*;
import java.util.regex.*;

public class FrontServlet extends HttpServlet {

    private static final List<RouteMapping> routes = new ArrayList<>();
    private static final Map<String, Object> controllers = new HashMap<>();
    private static boolean initialized = false;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        servicePersonnalisee(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        servicePersonnalisee(req, resp);
    }

    private void servicePersonnalisee(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!initialized) {
            System.out.println("INITIALISATION : scan des routes...");
            scanRoutes();
            System.out.println("ROUTES DÉTECTÉES : " + routes.size());
            initialized = true;
        }

        String fullUri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String relativeUri = fullUri.substring(contextPath.length());
        if (relativeUri.startsWith("/")) relativeUri = relativeUri.substring(1);
        String routePath = "/" + relativeUri;
        String normalized = normalizePath(routePath);
        String currentMethod = request.getMethod();

        System.out.println("TEST URL : " + routePath + " → normalisé : " + normalized);

        // === 1. ROUTE EXACTE ===
        for (RouteMapping r : routes) {
            if (r.path.equals(normalized) && ("ANY".equals(r.httpMethod) || r.httpMethod.equals(currentMethod))) {
                executeRoute(r, request, response, routePath);
                return;
            }
        }

        // === 2. ROUTE AVEC {id}, {nom}, etc. ===
        for (RouteMapping r : routes) {
            if (r.path.contains("{") && r.path.contains("}") && ("ANY".equals(r.httpMethod) || r.httpMethod.equals(currentMethod))) {
                String pattern = r.path.replaceAll("\\{[^/]+\\}", "([^/]+)");
                pattern = "^" + pattern + "$";
                if (normalized.matches(pattern)) {
                    Matcher matcher = Pattern.compile(pattern).matcher(normalized);
                    if (matcher.matches()) {
                        Matcher varMatcher = Pattern.compile("\\{([^}]+)\\}").matcher(r.path);
                        int group = 1;
                        while (varMatcher.find()) {
                            String varName = varMatcher.group(1);
                            request.setAttribute(varName, matcher.group(group++));
                        }
                    }
                    executeRoute(r, request, response, routePath);
                    return;
                }
            }
        }

        // === LE RESTE DE TON CODE ORIGINAL 100 % INTACT ===
        boolean isPartialRoute = false;
        for (RouteMapping r : routes) {
            if (normalized.startsWith(r.path + "/") || normalized.equals(r.path)) {
                isPartialRoute = true;
                break;
            }
        }
        if (isPartialRoute) {
            sendNotFound(response, routePath, "n'existe pas");
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

    private void executeRoute(RouteMapping mapping, HttpServletRequest request, HttpServletResponse response, String routePath)
            throws ServletException, IOException {
        Method method = mapping.method;
        Object controller = controllers.get(method.getDeclaringClass().getName());
        try {
            Parameter[] parameters = method.getParameters();
            Object[] args = new Object[parameters.length];

            String fullUri = request.getRequestURI();
            String contextPath = request.getContextPath();
            String uri = fullUri.substring(contextPath.length());

            Map<String, String> pathVariables = new HashMap<>();
            String regex = mapping.path.replaceAll("\\{[^/]+\\}", "([^/]+)");
            regex = "^" + regex + "$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(uri);
            if (matcher.matches()) {
                Pattern varPattern = Pattern.compile("\\{([^}]+)\\}");
                Matcher varMatcher = varPattern.matcher(mapping.path);
                int groupIndex = 1;
                while (varMatcher.find()) {
                    String varName = varMatcher.group(1);
                    if (groupIndex <= matcher.groupCount()) {
                        pathVariables.put(varName, matcher.group(groupIndex));
                    }
                    groupIndex++;
                }
            }

            // === NOUVELLE FONCTIONNALITÉ POUR Map<String, Object> ===
            for (int i = 0; i < parameters.length; i++) {
                Parameter param = parameters[i];

                // SI LE PARAMÈTRE EST Map<String, Object> → ON LE REMPLIT AUTOMATIQUEMENT
                if (Map.class.isAssignableFrom(param.getType())) {
                    Type genericType = param.getParameterizedType();
                    if (genericType instanceof ParameterizedType pt
                            && pt.getActualTypeArguments().length == 2
                            && pt.getActualTypeArguments()[0] == String.class
                            && pt.getActualTypeArguments()[1] == Object.class) {

                        Map<String, Object> formData = new HashMap<>();
                        Enumeration<String> paramNames = request.getParameterNames();
                        
                        while (paramNames.hasMoreElements()) {
                            String name = paramNames.nextElement();
                            String[] values = request.getParameterValues(name);
                            
                            // Gestion spéciale pour les paramètres multiples (checkbox, select multiple)
                            if (values != null && values.length > 1) {
                                // Pour les checkbox avec plusieurs valeurs sélectionnées
                                formData.put(name, values);
                            } 
                            // Si une seule valeur non vide
                            else if (values != null && values.length == 1 && !values[0].isEmpty()) {
                                formData.put(name, values[0]);
                            }
                            // Si vide ou null
                            else {
                                formData.put(name, "");
                            }
                        }
                        
                        args[i] = formData;
                        continue; // on passe au paramètre suivant
                    }
                }

                // === CODE ORIGINAL (RequestParam, path variables, etc.) ===
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
            // === FIN DE LA BOUCLE ===

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

    private void handleResult(Object result, HttpServletRequest request, HttpServletResponse response, String routePath)
            throws ServletException, IOException {
        if (result instanceof ModelView) {
            ModelView mv = (ModelView) result;
            String viewPath = "/" + mv.getView();
            
            // Ajouter les données du ModelView à la requête
            for (Map.Entry<String, Object> entry : mv.getData().entrySet()) {
                request.setAttribute(entry.getKey(), entry.getValue());
            }
            
            // DEBUG: Afficher les données transmises
            System.out.println("=== TRANSMISSION À LA JSP ===");
            Enumeration<String> attrNames = request.getAttributeNames();
            while (attrNames.hasMoreElements()) {
                String attrName = attrNames.nextElement();
                Object attrValue = request.getAttribute(attrName);
                System.out.println(attrName + " = " + attrValue);
                if (attrValue != null && attrValue.getClass().isArray()) {
                    System.out.println("  Type: " + attrValue.getClass().getName());
                    Object[] array = (Object[]) attrValue;
                    System.out.println("  Longueur: " + array.length);
                    for (Object item : array) {
                        System.out.println("  - " + item);
                    }
                }
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

    private void sendNotFound(HttpServletResponse response, String routePath, String message) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<html><head><title>404</title></head><body>");
            out.println("<h1 style='color:red'>" + message + "</h1>");
            out.println("<p>URL : <strong>" + routePath + "</strong></p>");
            out.println("</body></html>");
        }
    }

    private String normalizePath(String path) {
        return path.replaceAll("/+", "/").replaceAll("/$", "");
    }

    private void scanRoutes() {
        try {
            String packageName = "com.sprint1";
            String path = packageName.replace('.', '/');
            URL resource = getServletContext().getClassLoader().getResource(path);
            if (resource == null) return;

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
                            String fullPath;

                            if (method.isAnnotationPresent(GetUrl.class)) {
                                fullPath = normalizePath(basePath + method.getAnnotation(GetUrl.class).value());
                                routes.add(new RouteMapping("GET", fullPath, method, instance));
                            }
                            if (method.isAnnotationPresent(PostUrl.class)) {
                                fullPath = normalizePath(basePath + method.getAnnotation(PostUrl.class).value());
                                routes.add(new RouteMapping("POST", fullPath, method, instance));
                            }
                            if (method.isAnnotationPresent(PathAnnotation.class)) {
                                PathAnnotation pa = method.getAnnotation(PathAnnotation.class);
                                fullPath = normalizePath(basePath + pa.value());
                                routes.add(new RouteMapping("ANY", fullPath, method, instance));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Classe interne pour stocker les informations de route
    private static class RouteMapping {
        String httpMethod;
        String path;
        Method method;
        Object controller;
        
        RouteMapping(String httpMethod, String path, Method method, Object controller) {
            this.httpMethod = httpMethod;
            this.path = path;
            this.method = method;
            this.controller = controller;
        }
        
        @Override
        public String toString() {
            return httpMethod + " " + path + " -> " + method.getName();
        }
    }
}