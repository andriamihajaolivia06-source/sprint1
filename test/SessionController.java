package com.sprint1;

import java.util.Map;

@Controller("/session")
public class SessionController {
    
    @GetUrl("/form")
    public ModelView showForm(@Session Map<String, Object> session) {
        System.out.println("=== AFFICHAGE FORMULAIRE SESSION ===");
        
        // Récupérer les valeurs actuelles de la session
        String username = (String) session.get("username");
        Integer counter = (Integer) session.get("counter");
        
        if (counter == null) {
            counter = 0;
        }
        
        System.out.println("Username actuel: " + username);
        System.out.println("Counter actuel: " + counter);
        
        ModelView mv = new ModelView();
        mv.setView("sessionForm.jsp");
        mv.setData("username", username);
        mv.setData("counter", counter);
        
        return mv;
    }
    
    @PostUrl("/save")
    public ModelView saveSession(@Session Map<String, Object> session,
                                @RequestParam("username") String username) {
        
        System.out.println("=== SAUVEGARDE SESSION ===");
        System.out.println("Nouveau username: " + username);
        
        // Récupérer et incrémenter le compteur
        Integer counter = (Integer) session.get("counter");
        if (counter == null) {
            counter = 1;
        } else {
            counter = counter + 1;
        }
        
        // Sauvegarder dans la session
        session.put("username", username);
        session.put("counter", counter);
        
        System.out.println("Username sauvegardé: " + username);
        System.out.println("Counter sauvegardé: " + counter);
        
        ModelView mv = new ModelView();
        mv.setView("sessionResult.jsp");
        mv.setData("username", username);
        mv.setData("counter", counter);
        
        return mv;
    }
    
    @GetUrl("/view")
    public ModelView viewSession(@Session Map<String, Object> session) {
        System.out.println("=== AFFICHAGE SESSION ===");
        
        ModelView mv = new ModelView();
        mv.setView("sessionView.jsp");
        mv.setData("session", session);
        
        return mv;
    }
    
    @GetUrl("/clear")
    public String clearSession(@Session Map<String, Object> session) {
        System.out.println("=== EFFACEMENT SESSION ===");
        
        session.clear();
        
        return "Session effacée! <a href='/sprint1/session/form'>Retour au formulaire</a>";
    }
    
    @GetUrl("/increment")
    public String incrementCounter(@Session Map<String, Object> session) {
        System.out.println("=== INCREMENTATION COMPTEUR ===");
        
        Integer counter = (Integer) session.get("counter");
        if (counter == null) {
            counter = 1;
        } else {
            counter = counter + 1;
        }
        
        session.put("counter", counter);
        
        return "Compteur incrémenté: " + counter + 
               " <a href='/sprint1/session/form'>Retour</a>";
    }
    
    @GetUrl("/remove")
    public String removeUsername(@Session Map<String, Object> session) {
        System.out.println("=== SUPPRESSION USERNAME ===");
        
        String username = (String) session.remove("username");
        
        return "Username supprimé: " + username + 
               " <a href='/sprint1/session/form'>Retour</a>";
    }
}