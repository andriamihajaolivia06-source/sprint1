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
    public void saluer(@RequestParam("nom") String name) {

    }
}