package com.sprint1;

import java.util.Map;

@Controller("/upload")
public class UploadController {
    
    @GetUrl("/form")
    public ModelView showUploadForm() {
        ModelView mv = new ModelView();
        mv.setView("uploadForm.jsp");
        return mv;
    }

    @GetUrl("/map-form")
    public ModelView showMapUploadForm() {
        ModelView mv = new ModelView();
        mv.setView("uploadMapForm.jsp");
        return mv;
    }
    
    
    @PostUrl("/save")
    public ModelView handleUpload(@RequestParam("fichier") Upload fichier) {
        System.out.println("=== UPLOAD SIMPLE RECU ===");
        if (fichier != null) {
            System.out.println("Nom: " + fichier.getOriginalFileName());
            System.out.println("Taille: " + fichier.getSize());
            System.out.println("Type: " + fichier.getContentType());
        } else {
            System.out.println("Fichier NULL !");
        }
        
        ModelView mv = new ModelView();
        mv.setView("uploadResult.jsp");
        mv.setData("fichier", fichier);
        
        return mv;
    }
    
    @PostUrl("/save-multiple")
    public ModelView handleMultipleUpload(Map<String, byte[]> fichiers) {
        System.out.println("=== UPLOAD MULTIPLE AVEC MAP RECU ===");
        
        if (fichiers != null && !fichiers.isEmpty()) {
            System.out.println("Nombre de fichiers reçus: " + fichiers.size());
            
            for (Map.Entry<String, byte[]> entry : fichiers.entrySet()) {
                String fileName = entry.getKey();
                byte[] fileData = entry.getValue();
                System.out.println("Fichier: " + fileName + " (taille: " + 
                    (fileData != null ? fileData.length : 0) + " bytes)");
            }
        } else {
            System.out.println("Map NULL ou vide !");
        }
        
        ModelView mv = new ModelView();
        mv.setView("uploadMapResult.jsp");
        mv.setData("fichiers", fichiers);
        
        return mv;
    }
}