<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sprint1.User" %>
<%
    User user = (User) request.getAttribute("user");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Connexion réussie</title>
    <style>
        body { font-family: Arial; padding: 40px; background: #f5f5f5; }
        .container { max-width: 500px; margin: auto; background: white; padding: 40px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); text-align: center; }
        h1 { color: #4CAF50; }
        .success-icon { font-size: 60px; color: #4CAF50; margin: 20px 0; }
        .user-info { background: #e8f5e9; padding: 20px; border-radius: 5px; margin: 30px 0; }
        .actions { margin-top: 30px; }
        .actions a { display: inline-block; margin: 10px; padding: 12px 24px; text-decoration: none; border-radius: 5px; }
        .btn-primary { background: #2196F3; color: white; }
        .btn-primary:hover { background: #0b7dda; }
        .btn-secondary { background: #4CAF50; color: white; }
        .btn-secondary:hover { background: #45a049; }
    </style>
</head>
<body>
<div class="container">
    <div class="success-icon">✅</div>
    <h1>Connexion réussie!</h1>
    
    <div class="user-info">
        <h2>Bienvenue <%= user.getUsername() %>!</h2>
        <p><strong>Rôle:</strong> <%= user.getRole() %></p>
        <p><strong>Email:</strong> <%= user.getEmail() %></p>
    </div>
    
    <p>Vous êtes maintenant connecté avec succès.</p>
    
    <div class="actions">
        <a href="/sprint1/secure/dashboard" class="btn-primary">📊 Tableau de bord</a>
        <a href="/sprint1/public/home" class="btn-secondary">🏠 Accueil public</a>
    </div>
    
    <p style="margin-top: 30px; color: #666;">
        <a href="/sprint1/auth/logout">Se déconnecter</a>
    </p>
</div>
</body>
</html>