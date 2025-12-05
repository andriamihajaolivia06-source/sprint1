package com.sprint1;
import java.util.Map;

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

    @GetUrl("/formulaire")
    public ModelView showForm() {
        ModelView mv = new ModelView();
        mv.setView("test.jsp");
        return mv;
    }


    @PostUrl("/save")
    public ModelView savePersonne(
            @RequestParam("nom") String nom,
            @RequestParam("age") String ageStr) {

        if (nom == null || nom.trim().isEmpty()) {
            ModelView mv = new ModelView();
            mv.setView("test.jsp");
            mv.setData("error", "Le nom est obligatoire !");
            return mv;
        }

        int age;
        try {
            age = Integer.parseInt(ageStr.trim());
            if (age < 0 || age > 150) throw new Exception();
        } catch (Exception e) {
            ModelView mv = new ModelView();
            mv.setView("test.jsp");
            mv.setData("error", "Âge invalide ! Doit être entre 0 et 150.");
            mv.setData("nom", nom);
            return mv;
        }

        ModelView mv = new ModelView();
        mv.setView("result.jsp");
        mv.setData("message", "Personne sauvegardée avec succès !");
        mv.setData("nom", nom);
        mv.setData("age", age);
        return mv;
    }


    @GetUrl("/bonjour")
    public String direBonjour() {
        return "Bonjour <3";
    }


    @GetUrl("/ftest")
    public ModelView Form() {
        ModelView mv = new ModelView();
        mv.setView("FormulaireTest.jsp");
        return mv;
    }


    @PostUrl("/savePersonne")
    public ModelView savePersonne(Map<String, Object> form) {
        ModelView mv = new ModelView("ResultatTest.jsp");
        
        // Utilisez setData() au lieu de addObject()
        mv.setData("form", form);
        mv.setData("message", "Données reçues avec succès !");
        
        return mv;
    }

    

}