package com.sprint1;

import java.util.HashMap;
import java.util.Map;

public class PathVariableExtractor {

    public static Map<String, String> extract(String routePattern, String actualUri) {
        Map<String, String> values = new HashMap<>();

        // Normaliser les chemins
        String[] patternParts = routePattern.replaceAll("^/|/$", "").split("/");
        String[] uriParts = actualUri.replaceAll("^/|/$", "").split("/");

        if (patternParts.length != uriParts.length) {
            return values; // pas de correspondance
        }

        for (int i = 0; i < patternParts.length; i++) {
            String part = patternParts[i];
            if (part.startsWith("{") && part.endsWith("}")) {
                String varName = part.substring(1, part.length() - 1);
                values.put(varName, uriParts[i]);
            }
        }

        return values;
    }
}