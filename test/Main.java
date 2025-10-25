package com.sprint1;

import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) {
               try {
            Employer employer = new Employer();
            String result = employer.getDetails();
            System.out.println("Method result: " + result);

            Method method = Employer.class.getMethod("getDetails");
            PathAnnotation path = method.getAnnotation(PathAnnotation.class);
            if (path != null) {
                System.out.println("URL from @Path: " + path.value());
            } else {
                System.out.println("No @Path annotation found on method");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}