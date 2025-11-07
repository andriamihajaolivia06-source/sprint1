package com.sprint1;

@Controller("/api")
public class Class1 {

    @PathAnnotation("/hello")
    public String sayHello() {
        return "Bonjour depuis le serveur !";
    }

    @PathAnnotation("/data")
    public String getData() {
        return "Voici les données demandées.";
    }

 
    @PathAnnotation("/bye")
    public void sayBye() {
        System.out.println("Bye !");
    }
}


