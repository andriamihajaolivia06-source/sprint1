// package com.sprint1;

// import java.lang.reflect.Method;

// public class Main {
//     public static void main(String[] args) {
//                try {
//             Employer employer = new Employer();
//             String result = employer.getDetails();
//             System.out.println("Method result: " + result);

//             Method method = Employer.class.getMethod("getDetails");
//             PathAnnotation path = method.getAnnotation(PathAnnotation.class);
//             if (path != null) {
//                 System.out.println("URL from @Path: " + path.value());
//             } else {
//                 System.out.println("No @Path annotation found on method");
//             }
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }
// }

package com.sprint1;

import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) {
        Class<?>[] classes = { Class1.class, Class2.class, Class3.class };

        System.out.println("------ CLASSES AVEC @Controller -----");
        for (Class<?> cls : classes) {
            if (cls.isAnnotationPresent(Controller.class)) {
                Controller controller = cls.getAnnotation(Controller.class);
                System.out.println("Controller: " + cls.getSimpleName() + " → " + controller.value());
            }
        }

        System.out.println("------ METHODES AVEC Mapping ------");
        for (Class<?> cls : classes) {
            for (Method m : cls.getDeclaredMethods()) {
                if (m.isAnnotationPresent(PathAnnotation.class)) {
                    PathAnnotation path = m.getAnnotation(PathAnnotation.class);
                    String className = cls.getSimpleName();
                    String methodName = m.getName();
                    String fullPath = path.value();

                   
                    if (cls.isAnnotationPresent(Controller.class)) {
                        Controller controller = cls.getAnnotation(Controller.class);
                        fullPath = controller.value() + path.value();
                    }

                    System.out.println("Méthode: " + className + "." + methodName + "() → " + fullPath);
                }
            }
        }
    }
}

