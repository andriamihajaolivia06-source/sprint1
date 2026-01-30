<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Connexion</title>
    <style>
        body { font-family: Arial; padding: 40px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 100vh; display: flex; align-items: center; }
        .container { max-width: 400px; margin: auto; background: white; padding: 40px; border-radius: 10px; box-shadow: 0 10px 40px rgba(0,0,0,0.1); }
        h1 { color: #333; text-align: center; margin-bottom: 30px; }
        .form-group { margin: 20px 0; }
        label { display: block; margin-bottom: 8px; color: #555; }
        input[type="text"], input[type="password"] { width: 100%; padding: 12px; border: 1px solid #ddd; border-radius: 5px; font-size: 16px; box-sizing: border-box; }
        button { width: 100%; background: #4CAF50; color: white; padding: 14px; border: none; border-radius: 5px; cursor: pointer; font-size: 16px; font-weight: bold; }
        button:hover { background: #45a049; }
        .error { background: #ffebee; color: #c62828; padding: 10px; border-radius: 5px; margin-bottom: 20px; text-align: center; }
        .info { background: #e3f2fd; padding: 15px; border-radius: 5px; margin-top: 30px; }
        .users-list { margin-top: 20px; font-size: 14px; color: #666; }
        .users-list h4 { margin-bottom: 10px; }
    </style>
</head>
<body>
<div class="container">
    <h1>🔐 Connexion</h1>
    
    <% if (error != null) { %>
        <div class="error">
            ⚠️ <%= error %>
        </div>
    <% } %>
    
    <form method="post" action="/sprint1/auth/login">
        <div class="form-group">
            <label for="username">Nom d'utilisateur:</label>
            <input type="text" id="username" name="username" required>
        </div>
        
        <div class="form-group">
            <label for="password">Mot de passe:</label>
            <input type="password" id="password" name="password" required>
        </div>
        
        <button type="submit">Se connecter</button>
    </form>
    
    <div class="info">
        <p><strong>📋 Comptes de test:</strong></p>
        <div class="users-list">
            <h4>Administrateur:</h4>
            <p>Username: <strong>admin</strong> | Password: <strong>admin123</strong> | Rôle: Admin</p>
            
            <h4>Manager:</h4>
            <p>Username: <strong>manager</strong> | Password: <strong>manager123</strong> | Rôle: Manager</p>
            
            <h4>Utilisateurs standards:</h4>
            <p>Username: <strong>user1</strong> | Password: <strong>user123</strong> | Rôle: User</p>
            <p>Username: <strong>user2</strong> | Password: <strong>user456</strong> | Rôle: User</p>
        </div>
        
        <p style="margin-top: 15px;">
            <a href="/sprint1/public/home">🏠 Accueil public</a>
        </p>
    </div>
</div>
</body>
</html>