<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Résultat</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: linear-gradient(135deg, #ff9a9e 0%, #fad0c4 100%);
            height: 100vh;
            margin: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            color: #333;
        }
        .card {
            background: white;
            padding: 50px;
            border-radius: 20px;
            box-shadow: 0 20px 40px rgba(0,0,0,0.1);
            text-align: center;
            max-width: 500px;
        }
        h1 {
            color: #e91e63;
            font-size: 48px;
            margin-bottom: 20px;
        }
        p {
            font-size: 24px;
            margin: 15px 0;
        }
        .success {
            color: #4caf50;
            font-weight: bold;
        }
        a {
            display: inline-block;
            margin-top: 30px;
            padding: 12px 30px;
            background: #e91e63;
            color: white;
            text-decoration: none;
            border-radius: 50px;
            font-size: 18px;
        }
        a:hover {
            background: #c2185b;
        }
    </style>
</head>
<body>
<div class="card">
    <h1>Résultat</h1>

    <% if (request.getAttribute("message") != null) { %>
        <p class="success"><%= request.getAttribute("message") %></p>
    <% } %>

    <% if (request.getAttribute("nom") != null) { %>
        <p>Nom : <strong><%= request.getAttribute("nom") %></strong></p>
        <p>Âge : <strong><%= request.getAttribute("age") %></strong> ans</p>
    <% } %>

    <br>
    <!-- <a href="/sprint1/personne/form">Retour au formulaire</a> -->
</div>
</body>
</html>