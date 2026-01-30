package com.sprint1;

import java.util.Map;


@Controller("/admin")
@Role("admin")
public class AdminController {
    
    @GetUrl("/users")
    public String manageUsers() {
        return "<h1>👥 Gestion des Utilisateurs</h1>" +
               "<p>Cette page est réservée aux administrateurs.</p>" +
               "<h3>Liste des utilisateurs:</h3>" +
               "<ul>" +
               "  <li>admin (Administrateur)</li>" +
               "  <li>user1 (Utilisateur standard)</li>" +
               "  <li>user2 (Utilisateur standard)</li>" +
               "  <li>manager (Manager)</li>" +
               "</ul>" +
               "<p><a href='/sprint1/secure/dashboard'>Retour au tableau de bord</a></p>";
    }
    
    @GetUrl("/settings")
    public String adminSettings() {
        return "<h1>⚙️ Paramètres Admin</h1>" +
               "<p>Configuration du système (admin only).</p>" +
               "<p><a href='/sprint1/admin/users'>Gestion utilisateurs</a></p>";
    }
}