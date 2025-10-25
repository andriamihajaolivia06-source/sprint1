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

        for (Class<?> cls : classes) {
            if (cls.isAnnotationPresent(Controller.class)) {
                Controller controller = cls.getAnnotation(Controller.class);
                String basePath = controller.value();
                System.out.println("Controller: " + cls.getSimpleName() + " → " + basePath);

                for (Method m : cls.getDeclaredMethods()) {
                    if (m.isAnnotationPresent(PathAnnotation.class)) {
                        PathAnnotation path = m.getAnnotation(PathAnnotation.class);
                        String fullPath = basePath + path.value();
                        System.out.println("  → " + m.getName() + " : " + fullPath);
                    }
                }
                System.out.println();
            }
        }
    }
}
