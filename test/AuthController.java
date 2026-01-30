package com.sprint1;

import java.util.Map;

@Controller("/auth")
public class AuthController {
    
    // Base de données simulée d'utilisateurs
    private static final User[] USERS = {
        new User("admin", "admin123", "admin", "admin@example.com"),
        new User("user1", "user123", "user", "user1@example.com"),
        new User("user2", "user456", "user", "user2@example.com"),
        new User("manager", "manager123", "manager", "manager@example.com")
    };
    
    @GetUrl("/login")
    public ModelView showLoginForm() {
        ModelView mv = new ModelView();
        mv.setView("login.jsp");
        return mv;
    }
    
    @PostUrl("/login")
    public ModelView processLogin(@RequestParam("username") String username,
                                 @RequestParam("password") String password,
                                 @Session Map<String, Object> session) {
        
        System.out.println("=== TENTATIVE DE CONNEXION ===");
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        
        // Rechercher l'utilisateur
        User user = findUser(username, password);
        
        if (user != null) {
            // Sauvegarder l'utilisateur dans la session
            session.put("user", user);
            session.put("username", user.getUsername());
            session.put("role", user.getRole());
            
            System.out.println("✅ Connexion réussie: " + user);
            
            ModelView mv = new ModelView();
            mv.setView("loginSuccess.jsp");
            mv.setData("user", user);
            return mv;
        } else {
            System.out.println("❌ Échec de connexion");
            
            ModelView mv = new ModelView();
            mv.setView("login.jsp");
            mv.setData("error", "Nom d'utilisateur ou mot de passe incorrect");
            return mv;
        }
    }
    
    @GetUrl("/logout")
    public ModelView logout(@Session Map<String, Object> session) {
        System.out.println("=== DÉCONNEXION ===");
        
        // Invalider la session
        session.clear();
        
        ModelView mv = new ModelView();
        mv.setView("logout.jsp");
        return mv;
    }
    
    @GetUrl("/profile")
    @Authentified
    public ModelView showProfile(@Session Map<String, Object> session) {
        User user = (User) session.get("user");
        
        ModelView mv = new ModelView();
        mv.setView("profile.jsp");
        mv.setData("user", user);
        return mv;
    }
    
    private User findUser(String username, String password) {
        for (User user : USERS) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
}