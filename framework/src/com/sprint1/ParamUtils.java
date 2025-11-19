package com.sprint1;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class ParamUtils {

    // Cache des paramètres (pour éviter de reparcourir à chaque fois)
    private static final Map<HttpServletRequest, Map<String, String>> paramCache = new HashMap<>();

    // Fonction 1 : Vérifie si un paramètre existe (notre propre version)
    public static boolean hasParam(HttpServletRequest request, String name) {
        Map<String, String> params = getAllParams(request);
        return params.containsKey(name);
    }

    // Fonction 2 : Récupère la valeur d'un paramètre (notre propre version)
    public static String getParamValue(HttpServletRequest request, String name) {
        Map<String, String> params = getAllParams(request);
        return params.get(name);
    }

    // Fonction 3 : Retourne tous les paramètres sous forme de Map (notre propre version)
    public static Map<String, String> getAllParams(HttpServletRequest request) {
        return paramCache.computeIfAbsent(request, req -> {
            Map<String, String> map = new HashMap<>();
            Enumeration<String> names = req.getParameterNames();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                map.put(name, req.getParameter(name)); // On l'appelle UNE SEULE FOIS ici
            }
            return Collections.unmodifiableMap(map);
        });
    }

    // Fonction bonus : Vider le cache si besoin (ex: fin de requête)
    public static void clearCache(HttpServletRequest request) {
        paramCache.remove(request);
    }
}