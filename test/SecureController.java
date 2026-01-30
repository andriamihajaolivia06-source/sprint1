package com.sprint1;

import java.util.Map;

@Controller("/secure")
@Authentified
public class SecureController {
    
    @GetUrl("/dashboard")
    public String dashboard(@Session Map<String, Object> session) {
        User user = (User) session.get("user");
        
        return "<h1>📊 Tableau de bord</h1>" +
               "<p>Bienvenue " + user.getUsername() + "!</p>" +
               "<p>Votre rôle: " + user.getRole() + "</p>" +
               "<ul>" +
               "  <li><a href='/sprint1/secure/profile'>Votre profil</a></li>" +
               "  <li><a href='/sprint1/admin/users'>Gestion utilisateurs (Admin)</a></li>" +
               "  <li><a href='/sprint1/manager/reports'>Rapports (Manager)</a></li>" +
               "  <li><a href='/sprint1/auth/logout'>Se déconnecter</a></li>" +
               "</ul>" +
               "<p><a href='/sprint1/public/home'>Retour à l'accueil public</a></p>";
    }
    
    @GetUrl("/profile")
    public String profile(@Session Map<String, Object> session) {
        User user = (User) session.get("user");
        
        return "<h1>👤 Votre Profil</h1>" +
               "<p><strong>Nom d'utilisateur:</strong> " + user.getUsername() + "</p>" +
               "<p><strong>Rôle:</strong> " + user.getRole() + "</p>" +
               "<p><strong>Email:</strong> " + user.getEmail() + "</p>" +
               "<p><a href='/sprint1/secure/dashboard'>Retour au tableau de bord</a></p>" +
               "<p><a href='/sprint1/auth/logout'>Se déconnecter</a></p>";
    }
}