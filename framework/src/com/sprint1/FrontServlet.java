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


        private Object createAndPopulateObject(Class<?> clazz, HttpServletRequest request) 
            throws Exception {
        Object obj = clazz.getDeclaredConstructor().newInstance();
        
        // Parcourir tous les paramètres de la requête
        Enumeration<String> paramNames = request.getParameterNames();
        
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String paramValue = request.getParameter(paramName);
            
            if (paramValue != null && !paramValue.isEmpty()) {
                // Gérer les paramètres imbriqués : note.matiere, note.valeur
                if (paramName.contains(".")) {
                    String[] parts = paramName.split("\\.");
                    String objectName = parts[0]; // "note"
                    String fieldName = parts[1]; // "matiere" ou "valeur"
                    
                    // Trouver l'objet imbriqué
                    try {
                        // Getter pour l'objet imbriqué
                        String getterName = "get" + objectName.substring(0, 1).toUpperCase() + 
                                        objectName.substring(1);
                        Method getter = clazz.getMethod(getterName);
                        Object nestedObj = getter.invoke(obj);
                        
                        // Si l'objet imbriqué n'existe pas encore, le créer
                        if (nestedObj == null) {
                            // Trouver le type de l'objet imbriqué
                            Class<?> nestedType = getter.getReturnType();
                            nestedObj = nestedType.getDeclaredConstructor().newInstance();
                            
                            // Setter pour initialiser l'objet imbriqué
                            String setterName = "set" + objectName.substring(0, 1).toUpperCase() + 
                                            objectName.substring(1);
                            Method setter = clazz.getMethod(setterName, nestedType);
                            setter.invoke(obj, nestedObj);
                        }
                        
                        // Définir la valeur sur l'objet imbriqué
                        setFieldOnObject(nestedObj, fieldName, paramValue);
                        
                    } catch (Exception e) {
                        System.err.println("Erreur paramètre imbriqué " + paramName + ": " + e.getMessage());
                    }
                } else {
                    // Paramètre simple
                    setFieldOnObject(obj, paramName, paramValue);
                }
            }
        }
        
        return obj;
    }

    private void setFieldOnObject(Object obj, String fieldName, String value) {
        try {
            String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + 
                            fieldName.substring(1);
            
            // Chercher le setter
            for (Method method : obj.getClass().getMethods()) {
                if (method.getName().equals(setterName) && method.getParameterCount() == 1) {
                    Class<?> paramType = method.getParameterTypes()[0];
                    Object convertedValue = convertValue(value, paramType);
                    
                    if (convertedValue != null) {
                        method.invoke(obj, convertedValue);
                    }
                    return;
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur setFieldOnObject " + fieldName + ": " + e.getMessage());
        }
    }

    private Object convertValue(String value, Class<?> targetType) {
        try {
            if (targetType == String.class) {
                return value;
            } else if (targetType == int.class || targetType == Integer.class) {
                return Integer.parseInt(value);
            } else if (targetType == double.class || targetType == Double.class) {
                return Double.parseDouble(value);
            } else if (targetType == boolean.class || targetType == Boolean.class) {
                return Boolean.parseBoolean(value);
            }
        } catch (Exception e) {
            System.err.println("Erreur conversion " + value + " vers " + targetType.getName());
        }
        return null;
    }

    private Map<String, Object> buildFormData(HttpServletRequest request) {
        Map<String, Object> formData = new HashMap<>();
        Enumeration<String> paramNames = request.getParameterNames();
        
        while (paramNames.hasMoreElements()) {
            String name = paramNames.nextElement();
            String[] values = request.getParameterValues(name);
            
            if (values != null && values.length > 1) {
                formData.put(name, values);
            } else if (values != null && values.length == 1 && !values[0].isEmpty()) {
                formData.put(name, values[0]);
            } else {
                formData.put(name, "");
            }
        }
        
        return formData;
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

            // === GESTION DES PARAMÈTRES ===
            for (int i = 0; i < parameters.length; i++) {
                Parameter param = parameters[i];
                Class<?> paramType = param.getType();

                System.out.println("Traitement paramètre " + i + ": " + param.getName() + 
                                " de type: " + paramType.getName());

                // 1. SI LE PARAMÈTRE EST Map<String, Object>
                if (Map.class.isAssignableFrom(paramType)) {
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
                            
                            if (values != null && values.length > 1) {
                                formData.put(name, values);
                            } else if (values != null && values.length == 1 && !values[0].isEmpty()) {
                                formData.put(name, values[0]);
                            } else {
                                formData.put(name, "");
                            }
                        }
                        
                        args[i] = formData;
                        System.out.println("Map créée avec " + formData.size() + " éléments");
                        continue;
                    }
                }

                // 2. SI C'EST UN OBJET COMPLEXE (Eleve, Note, etc.)
                if (!paramType.isPrimitive() && 
                    !paramType.isArray() && 
                    !paramType.equals(String.class) &&
                    !Number.class.isAssignableFrom(paramType) &&
                    !paramType.equals(Boolean.class)) {
                    
                    try {
                        System.out.println("Tentative création objet: " + paramType.getName());
                        Object obj = createAndPopulateObject(paramType, request);
                        args[i] = obj;
                        System.out.println("Objet créé avec succès: " + obj);
                        continue;
                    } catch (Exception e) {
                        System.err.println("Erreur création objet " + paramType.getName() + ": " + e.getMessage());
                        e.printStackTrace();
                        args[i] = null;
                        continue;
                    }
                }

                // 3. SI C'EST UN PARAMÈTRE SIMPLE (@RequestParam ou nom simple)
                RequestParam rp = param.getAnnotation(RequestParam.class);
                String value = null;
                
                if (rp != null && !rp.value().isEmpty()) {
                    value = request.getParameter(rp.value());
                    System.out.println("RequestParam trouvé: " + rp.value() + " = " + value);
                } else {
                    String paramName = param.getName();
                    if (pathVariables.containsKey(paramName)) {
                        value = pathVariables.get(paramName);
                        System.out.println("PathVariable trouvé: " + paramName + " = " + value);
                    } else if (!paramName.startsWith("arg")) {
                        value = request.getParameter(paramName);
                        System.out.println("Paramètre par nom: " + paramName + " = " + value);
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
                
                // Convertir la valeur selon le type du paramètre
                System.out.println("Conversion valeur: '" + value + "' vers " + paramType.getName());
                
                if (paramType == String.class) {
                    args[i] = value;
                } else if (paramType == int.class || paramType == Integer.class) {
                    try {
                        args[i] = Integer.parseInt(value);
                    } catch (NumberFormatException e) {
                        args[i] = 0;
                    }
                } else if (paramType == double.class || paramType == Double.class) {
                    try {
                        String normalizedValue = value.replace(',', '.');
                        args[i] = Double.parseDouble(normalizedValue);
                    } catch (NumberFormatException e) {
                        args[i] = 0.0;
                    }
                } else if (paramType == boolean.class || paramType == Boolean.class) {
                    args[i] = Boolean.parseBoolean(value);
                } else if (paramType == float.class || paramType == Float.class) {
                    try {
                        String normalizedValue = value.replace(',', '.');
                        args[i] = Float.parseFloat(normalizedValue);
                    } catch (NumberFormatException e) {
                        args[i] = 0.0f;
                    }
                } else if (paramType == long.class || paramType == Long.class) {
                    try {
                        args[i] = Long.parseLong(value);
                    } catch (NumberFormatException e) {
                        args[i] = 0L;
                    }
                } else {
                    args[i] = value;
                }
                
                System.out.println("Valeur finale: " + args[i] + " (type: " + 
                    (args[i] != null ? args[i].getClass().getName() : "null") + ")");
            }

            // DEBUG: Afficher tous les arguments
            System.out.println("=== ARGUMENTS FINAUX ===");
            for (int i = 0; i < args.length; i++) {
                System.out.println("Arg " + i + ": " + 
                    (args[i] != null ? args[i].getClass().getName() + " = " + args[i] : "null"));
            }

            // VÉRIFIER SI LA MÉTHODE A L'ANNOTATION @Json
            boolean isJsonResponse = JsonResponseHandler.isJsonResponse(method);
            if (isJsonResponse) {
                System.out.println("Méthode annotée avec @Json détectée");
            }

            // Appeler la méthode du contrôleur
            System.out.println("Appel de la méthode: " + method.getName());
            Object result = method.invoke(controller, args);
            
            // === GESTION DE LA RÉPONSE JSON ===
            if (isJsonResponse) {
                System.out.println("Annotation @Json détectée, conversion en JSON");
                JsonResponseHandler.handleJsonResponse(result, response);
                return;
            }
            
            // === CODE EXISTANT POUR LES AUTRES TYPES DE RÉPONSE ===
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
            
        } catch (InvocationTargetException e) {
            // Cette exception contient la vraie erreur de la méthode
            Throwable cause = e.getCause();
            System.err.println("=== ERREUR DANS L'INVOCATION ===");
            System.err.println("Méthode: " + method.getName());
            System.err.println("Contrôleur: " + controller.getClass().getName());
            System.err.println("Message: " + cause.getMessage());
            cause.printStackTrace();
            
            // Vérifier si la méthode était annotée @Json pour retourner une erreur JSON
            if (method.isAnnotationPresent(Json.class)) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(JsonResponseHandler.getJsonError(cause.getMessage()));
            } else {
                response.setContentType("text/html; charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.println("<html><head><title>Erreur</title></head><body>");
                out.println("<h1 style='color:red'>Erreur dans le contrôleur</h1>");
                out.println("<p><strong>" + cause.getClass().getSimpleName() + ":</strong> " + cause.getMessage() + "</p>");
                out.println("<pre>");
                cause.printStackTrace(out);
                out.println("</pre>");
                out.println("</body></html>");
            }
            
        } catch (Exception e) {
            System.err.println("Erreur générale dans executeRoute: " + e.getMessage());
            e.printStackTrace();
            
            // Vérifier si la méthode était annotée @Json
            if (method != null && method.isAnnotationPresent(Json.class)) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().print(JsonResponseHandler.getJsonError(e.getMessage()));
            } else {
                response.getWriter().println("<h1 style='color:red'>Erreur : " + e.getMessage() + "</h1>");
            }
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