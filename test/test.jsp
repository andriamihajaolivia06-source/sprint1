<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Formulaire POST</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            height: 100vh;
            margin: 0;
            display: flex;
            justify-content: center;
            align-items: center;
        }
        .container {
            background: white;
            padding: 40px;
            border-radius: 15px;
            box-shadow: 0 15px 35px rgba(0,0,0,0.1);
            width: 400px;
        }
        h1 {
            text-align: center;
            color: #5e35b1;
            margin-bottom: 30px;
        }
        input[type="text"], input[type="number"] {
            width: 100%;
            padding: 12px;
            margin: 10px 0;
            border: 2px solid #ddd;
            border-radius: 8px;
            font-size: 16px;
        }
        input:focus {
            border-color: #667eea;
            outline: none;
        }
        button {
            width: 100%;
            padding: 14px;
            background: #667eea;
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 18px;
            cursor: pointer;
            margin-top: 20px;
        }
        button:hover {
            background: #5a67d8;
        }
        .error {
            color: red;
            font-weight: bold;
            text-align: center;
            margin: 15px 0;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Ajouter une Personne</h1>

    <%-- Affichage de l'erreur si champ manquant --%>
    <% if (request.getAttribute("error") != null) { %>
        <div class="error">
            <%= request.getAttribute("errorMessage") %>
        </div>
    <% } %>

    <form action="/sprint1/personne/save" method="post">
        <input type="text" name="nom" placeholder="Votre nom" required>
        <input type="number" name="age" placeholder="Votre âge" min="1" required>
        <button type="submit">Envoyer</button>
    </form>

    <br>
    <!-- <p style="text-align:center;">
        <a href="/sprint1/personne/bonjour/777" style="color:#667eea;">Tester GET avec ID 777</a>
    </p> -->
</div>
</body>
</html>