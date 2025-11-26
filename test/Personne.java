package com.sprint1;

@Controller("/personne")
public class Personne {

    @PathAnnotation("/form")
    public ModelView afficherFormulaire() {
        ModelView mv = new ModelView();
        mv.setView("formulaire.jsp");
        return mv;
    }

    @PathAnnotation("/test1")
    public ModelView test1(@RequestParam("nom") String n) {
        ModelView mv = new ModelView();
        mv.setView("resultat.jsp");
        mv.setData("nom", n);
        mv.setData("methode", "test1 – @RequestParam(\"nom\")");
        return mv;
    }

    @PathAnnotation("/test2")
    public ModelView test2(String nom) {
        ModelView mv = new ModelView();
        mv.setView("resultat.jsp");
        mv.setData("nom", nom);
        mv.setData("methode", "test2 – String nom (sans annotation)");
        return mv;
    }

    @PathAnnotation("/test3")
    public ModelView test3(@RequestParam("prenom") String nom) {
        ModelView mv = new ModelView();
        mv.setView("resultat.jsp");
        mv.setData("nom", nom);
        mv.setData("methode", "test3 – @RequestParam(\"prenom\") String nom → INTELLIGENT !");
        return mv;
    }

    

}