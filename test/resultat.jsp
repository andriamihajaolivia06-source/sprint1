<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Résultat</title>
    <style>
        body { font-family: Arial; text-align: center; margin-top: 80px; background: #f8f8f8; }
        h1 { font-size: 40px; }
        .error { color: #e74c3c; }
        .success { color: #27ae60; }
        a { color: #3498db; text-decoration: none; font-size: 18px; }
        a:hover { text-decoration: underline; }
    </style>
</head>
<body>

    <% 
        String error = (String) request.getAttribute("error");
        String nom = (String) request.getAttribute("nom");
    %>

    <% if ("incorrecte".equals(error)) { %>
        <h1 class="error">incorrecte</h1>
        <p style="font-size:20px;"><%= request.getAttribute("errorMessage") %></p>
    <% } else { %>
        <!-- <h1 class="success">Salut <%= nom %> !</h1> -->
        <p>Super, ton nom a bien été reçu !</p>
    <% } %>

    <br><br>
    <a href="/sprint1/personne/form">Recommencer</a>

</body>
</html>