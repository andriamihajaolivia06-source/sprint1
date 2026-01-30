package com.sprint1;

import java.util.Map;

@Controller("/public")
public class PublicController {
    
    @GetUrl("/home")
    public String publicHome() {
        return "<h1>🏠 Page Publique</h1>" +
               "<p>Cette page est accessible à tout le monde.</p>" +
               "<p><a href='/sprint1/auth/login'>Se connecter</a></p>" +
               "<p><a href='/sprint1/secure/dashboard'>Accès sécurisé</a></p>";
    }
    
    @GetUrl("/about")
    public String about() {
        return "<h1>ℹ️ À propos</h1>" +
               "<p>Ceci est une page publique d'information.</p>" +
               "<p><a href='/sprint1/public/home'>Retour à l'accueil</a></p>";
    }
}