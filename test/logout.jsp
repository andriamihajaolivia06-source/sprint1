<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Déconnexion</title>
    <style>
        body { font-family: Arial; padding: 40px; background: #f5f5f5; }
        .container { max-width: 500px; margin: auto; background: white; padding: 40px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); text-align: center; }
        h1 { color: #333; }
        .logout-icon { font-size: 60px; color: #f44336; margin: 20px 0; }
        .actions { margin-top: 30px; }
        .actions a { display: inline-block; margin: 10px; padding: 12px 24px; background: #2196F3; color: white; text-decoration: none; border-radius: 5px; }
        .actions a:hover { background: #0b7dda; }
    </style>
</head>
<body>
<div class="container">
    <div class="logout-icon">👋</div>
    <h1>Déconnexion réussie</h1>
    
    <p>Vous avez été déconnecté avec succès.</p>
    <p>Merci d'avoir utilisé notre application.</p>
    
    <div class="actions">
        <a href="/sprint1/auth/login">Se reconnecter</a>
        <a href="/sprint1/public/home">Accueil public</a>
    </div>
</div>
</body>
</html>