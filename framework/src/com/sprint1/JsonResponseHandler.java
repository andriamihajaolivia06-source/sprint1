package com.sprint1;

import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class JsonResponseHandler {
    
    // Méthode principale pour gérer les réponses JSON
    public static void handleJsonResponse(Object result, HttpServletResponse response) 
            throws Exception {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        
        try {
            // Si c'est une List directement, l'encapsuler dans un objet avec count
            if (result instanceof List) {
                List<?> list = (List<?>) result;
                Map<String, Object> wrapped = new HashMap<>();
                wrapped.put("count", list.size());
                wrapped.put("data", list);
                
                String json = convertToJson(wrapped);
                out.print(json);
                System.out.println("JSON liste envoyé: count=" + list.size());
                
            } else if (result instanceof Collection) {
                // Pour les autres collections
                Collection<?> collection = (Collection<?>) result;
                Map<String, Object> wrapped = new HashMap<>();
                wrapped.put("count", collection.size());
                wrapped.put("data", collection);
                
                String json = convertToJson(wrapped);
                out.print(json);
                System.out.println("JSON collection envoyé: count=" + collection.size());
                
            } else {
                // Sinon, convertir normalement
                String json = convertToJson(result);
                out.print(json);
                System.out.println("JSON envoyé: " + (json.length() > 100 ? json.substring(0, 100) + "..." : json));
            }
            
        } catch (Exception e) {
            System.err.println("Erreur conversion JSON: " + e.getMessage());
            e.printStackTrace();
            
            // Retourner une erreur JSON
            out.print("{\"error\": true, \"message\": \"" + 
                      escapeJson(e.getMessage()) + "\"}");
        }
    }
    
    // Méthode pour échapper les caractères JSON
    public static String escapeJson(String str) {
        if (str == null) return "";
        
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\b", "\\b")
                  .replace("\f", "\\f")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
    
    // Méthode principale de conversion en JSON
    public static String convertToJson(Object obj) throws Exception {
        if (obj == null) {
            return "null";
        }
        
        // Si c'est déjà une chaîne, vérifier si c'est du JSON
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.trim().startsWith("{") || str.trim().startsWith("[")) {
                return str; // Déjà du JSON
            }
        }
        
        // Si c'est une Map, convertir en JSON object
        if (obj instanceof Map) {
            return mapToJson((Map<?, ?>) obj);
        }
        
        // Si c'est une Collection ou un tableau, convertir en JSON array
        if (obj instanceof Collection || obj.getClass().isArray()) {
            return collectionToJson(obj);
        }
        
        // Si c'est un ModelView, extraire les données
        if (obj instanceof ModelView) {
            ModelView mv = (ModelView) obj;
            Map<String, Object> data = mv.getData();
            return mapToJson(data);
        }
        
        // Pour les objets Java simples (Eleve, Note, etc.)
        return objectToJson(obj);
    }
    
    // Conversion d'une Map en JSON
    private static String mapToJson(Map<?, ?> map) {
        StringBuilder json = new StringBuilder("{");
        boolean first = true;
        
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (!first) {
                json.append(",");
            }
            first = false;
            
            json.append("\"").append(escapeJson(entry.getKey().toString())).append("\":");
            json.append(valueToJson(entry.getValue()));
        }
        
        json.append("}");
        return json.toString();
    }
    
    // Conversion d'une collection en JSON array
    private static String collectionToJson(Object collection) {
        StringBuilder json = new StringBuilder("[");
        
        if (collection instanceof List) {
            List<?> list = (List<?>) collection;
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) json.append(",");
                json.append(valueToJson(list.get(i)));
            }
        } else if (collection instanceof Set) {
            Set<?> set = (Set<?>) collection;
            boolean first = true;
            for (Object item : set) {
                if (!first) json.append(",");
                first = false;
                json.append(valueToJson(item));
            }
        } else if (collection.getClass().isArray()) {
            Object[] array = (Object[]) collection;
            for (int i = 0; i < array.length; i++) {
                if (i > 0) json.append(",");
                json.append(valueToJson(array[i]));
            }
        }
        
        json.append("]");
        return json.toString();
    }
    
    // Conversion d'un objet en JSON
    private static String objectToJson(Object obj) throws Exception {
        // Si c'est un type simple
        if (obj instanceof String) {
            return "\"" + escapeJson((String) obj) + "\"";
        } else if (obj instanceof Number) {
            return obj.toString();
        } else if (obj instanceof Boolean) {
            return obj.toString();
        } else if (obj instanceof Date) {
            return "\"" + obj.toString() + "\""; // Format simple
        }
        
        // Pour les objets complexes, utiliser la réflexion
        Class<?> clazz = obj.getClass();
        StringBuilder json = new StringBuilder("{");
        
        // Récupérer tous les getters
        Method[] methods = clazz.getMethods();
        boolean first = true;
        
        for (Method method : methods) {
            String methodName = method.getName();
            
            // Vérifier si c'est un getter (commence par "get" et a 0 paramètres)
            if (methodName.startsWith("get") && method.getParameterCount() == 0 
                && !methodName.equals("getClass")) {
                
                String fieldName = methodName.substring(3);
                if (fieldName.length() > 0) {
                    fieldName = Character.toLowerCase(fieldName.charAt(0)) + 
                               fieldName.substring(1);
                }
                
                try {
                    Object value = method.invoke(obj);
                    
                    if (!first) {
                        json.append(",");
                    }
                    first = false;
                    
                    json.append("\"").append(fieldName).append("\":");
                    json.append(valueToJson(value));
                    
                } catch (Exception e) {
                    // Ignorer ce champ en cas d'erreur
                }
            }
        }
        
        json.append("}");
        return json.toString();
    }
    
    // Conversion d'une valeur en JSON
    private static String valueToJson(Object value) {
        try {
            if (value == null) {
                return "null";
            } else if (value instanceof String) {
                return "\"" + escapeJson((String) value) + "\"";
            } else if (value instanceof Number) {
                return value.toString();
            } else if (value instanceof Boolean) {
                return value.toString();
            } else if (value instanceof Map) {
                return mapToJson((Map<?, ?>) value);
            } else if (value instanceof Collection || value.getClass().isArray()) {
                return collectionToJson(value);
            } else {
                // Pour les objets complexes
                return objectToJson(value);
            }
        } catch (Exception e) {
            return "\"Error converting value: " + e.getMessage() + "\"";
        }
    }
    
    // Méthode utilitaire pour vérifier si une méthode retourne JSON
    public static boolean isJsonResponse(Method method) {
        return method.isAnnotationPresent(Json.class);
    }
    
    // Méthode pour obtenir une erreur JSON formatée
    public static String getJsonError(String message) {
        return "{\"error\": true, \"message\": \"" + escapeJson(message) + "\"}";
    }
    
    // Méthode pour obtenir une erreur JSON formatée avec code
    public static String getJsonError(String message, int code) {
        return "{\"error\": true, \"code\": " + code + ", \"message\": \"" + escapeJson(message) + "\"}";
    }
}