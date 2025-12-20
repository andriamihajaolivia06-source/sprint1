package com.sprint1;

@Controller("/upload")
public class UploadController {
    
    @GetUrl("/form")
    public ModelView showUploadForm() {
        ModelView mv = new ModelView();
        mv.setView("uploadForm.jsp");
        return mv;
    }
    
    @PostUrl("/save")
    public ModelView handleUpload(@RequestParam("fichier") Upload fichier) {
        System.out.println("=== UPLOAD RECU ===");
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
}