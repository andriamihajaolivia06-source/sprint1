<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Résultat</title>
    <style>
        body { font-family: Arial; text-align: center; margin-top: 60px; background: #f5f5f5; }
        .box { padding: 30px; background: white; border-radius: 15px; box-shadow: 0 4px 20px rgba(0,0,0,0.1); display: inline-block; }
        h1 { font-size: 40px; margin: 10px; }
        .success { color: #27ae60; }
        .error { color: #e74c3c; }
        .info { background: #f0f0f0; padding: 15px; border-radius: 10px; margin: 20px; font-family: monospace; }
        a { color: #3498db; font-size: 18px; text-decoration: none; }
    </style>
</head>
<body>

<div class="box">
    <% 
        String error = (String) request.getAttribute("error");
        String nom = (String) request.getAttribute("nom");
        String age = (String) request.getAttribute("age");
        String email = (String) request.getAttribute("email");
        String methode = (String) request.getAttribute("methode");
    %>

    <% if ("incorrecte".equals(error)) { %>
        <h1 class="error">incorrecte</h1>
        <div class="info"><%= request.getAttribute("errorMessage") %></div>
    <% } else { %>
        <h1 class="success">Salut <%= nom != null ? nom : "Inconnu" %> !</h1>
        <% if (age != null) { %><p>Âge : <%= age %></p><% } %>
        <% if (email != null) { %><p>Email : <%= email %></p><% } %>
        <div class="info"><%= methode != null ? methode : "Méthode inconnue" %></div>
        <p style="color:green; font-weight:bold;">Super, tout a été reçu correctement !</p>
    <% } %>

    <br><br>
    <a href="/sprint1/personne/form">← Retour au formulaire</a>
</div>

</body>
</html>