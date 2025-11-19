package com.sprint1;

@Controller("/personne")
public class Personne {

    @PathAnnotation("/form")
    public ModelView afficherFormulaire() {
        ModelView mv = new ModelView();
        mv.setView("formulaire.jsp");
        return mv;
    }

    @PathAnnotation("/saluer")
    public ModelView saluer(@RequestParam("nom") String name) {
        ModelView mv = new ModelView();
        mv.setView("resultat.jsp");
        mv.setData("nom", name);
        return mv;
    }
}