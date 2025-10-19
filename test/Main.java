package com.sprint1;

import java.lang.reflect.Method;
import jakarta.ws.rs.Path;

public class Main {
    public static void main(String[] args) {
        try {
            
            Employer employer = new Employer();
            
          
            String result = employer.getDetails();
            System.out.println("Method result: " + result);
            
            
            Path pathAnnotation = Employer.class.getAnnotation(Path.class);
            if (pathAnnotation != null) {
                System.out.println("URL from @Path: " + pathAnnotation.value());
            } else {
                System.out.println("No @Path annotation found on class");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}