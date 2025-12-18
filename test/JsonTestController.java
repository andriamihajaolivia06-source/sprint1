package com.sprint1;

import java.util.*;

@Controller("/apitest")
public class JsonTestController {
    
    
    @Json
    @GetUrl("/eleves")
    public Map<String, Object> getAllEleves() {
        System.out.println("API: getAllEleves()");
        
       
        List<Eleve> eleves = new ArrayList<>();
        
        
        Eleve eleve1 = new Eleve();
        eleve1.setNom("Dupont");
        eleve1.setAge(15);
        Note note1 = new Note();
        note1.setMatiere("Maths");
        note1.setValeur(16.5);
        eleve1.setNote(note1);
        eleves.add(eleve1);
        
        // Élève 2
        Eleve eleve2 = new Eleve();
        eleve2.setNom("Martin");
        eleve2.setAge(16);
        Note note2 = new Note();
        note2.setMatiere("Français");
        note2.setValeur(14.0);
        eleve2.setNote(note2);
        eleves.add(eleve2);
        
        // Élève 3
        Eleve eleve3 = new Eleve();
        eleve3.setNom("Durand");
        eleve3.setAge(14);
        // Pas de note pour cet élève
        eleves.add(eleve3);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("count", eleves.size());
        response.put("eleves", eleves);
        
        return response;
    }
    
    @Json
    @GetUrl("/stats")
    public Map<String, Object> getStats() {
        System.out.println("API: getStats()");
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalEleves", 156);
        stats.put("moyenneGenerale", 12.5);
        stats.put("meilleureNote", 19.5);
        stats.put("matieres", Arrays.asList("Maths", "Français", "Histoire", "Physique"));
        stats.put("active", true);
        
        return stats;
    }
    

    
    @Json
    @GetUrl("/geteleves")
    public List<Eleve> getEleves() {
        List<Eleve> eleves = new ArrayList<>();
        
        
        for (int i = 1; i <= 5; i++) {
            Eleve eleve = new Eleve();
            eleve.setNom("Élève " + i);
            eleve.setAge(15 + i);
            
            Note note = new Note();
            note.setMatiere("Matière " + i);
            note.setValeur(10 + i);
            eleve.setNote(note);
            
            eleves.add(eleve);
        }
        
        return eleves; 
    }
}