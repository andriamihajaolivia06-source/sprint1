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

    @PathAnnotation("/page")
    public ModelView showPage() {
        ModelView mv = new ModelView();
        mv.setView("aa.jsp");

      
        mv.setData("user", "Olivia");
        mv.setData("age", 25);
        mv.setData("isAdmin", true);

        return mv;
    }

    @PathAnnotation("/bye")
    public void sayBye() {
        System.out.println("Bye /hello!");
    }

    @PathAnnotation("/okay/{id}")
    public String ItsOkay() {
    return "How are you!";
    }

    @PathAnnotation("/good/{id}")
    public String saygood(String id) {
        return "ID capturé : <strong>" + id + "</strong> → Tout est parfait !";
    }
}





