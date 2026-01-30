package com.sprint1;

import java.util.Map;

@Controller("/manager")
@Role("manager")
public class ManagerController {
    
    @GetUrl("/reports")
    public String viewReports() {
        return "<h1>📈 Rapports Manager</h1>" +
               "<p>Cette page est réservée aux managers.</p>" +
               "<h3>Rapports disponibles:</h3>" +
               "<ul>" +
               "  <li>Rapport de ventes</li>" +
               "  <li>Rapport d'activité</li>" +
               "  <li>Rapport financier</li>" +
               "</ul>" +
               "<p><a href='/sprint1/secure/dashboard'>Retour au tableau de bord</a></p>";
    }
    
    @GetUrl("/team")
    @Role("manager") // Redondant mais explicite
    public String manageTeam() {
        return "<h1>👥 Gestion d'Équipe</h1>" +
               "<p>Gestion de l'équipe (manager only).</p>";
    }
}