package com.sprint1;

import java.lang.reflect.Method;
import jakarta.ws.rs.Path;

public class Main {
    public static void main(String[] args) {
        try {
            // Instancier Employer
            Employer employer = new Employer();
            
            // Appeler la méthode
            String result = employer.getDetails();
            System.out.println("Method result: " + result);
            
            // Récupérer l'annotation @Path de la méthode
            Method method = Employer.class.getMethod("getDetails");
            Path pathAnnotation = method.getAnnotation(Path.class);
            if (pathAnnotation != null) {
                System.out.println("URL from @Path: " + pathAnnotation.value());
            } else {
                System.out.println("No @Path annotation found on method");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}