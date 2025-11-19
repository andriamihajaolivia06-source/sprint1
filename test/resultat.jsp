<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Résultat</title>
    <style>
        body { font-family: Arial, sans-serif; text-align: center; margin-top: 80px; background: #f9f9f9; }
        .container { padding: 40px; background: white; border-radius: 15px; box-shadow: 0 4px 20px rgba(0,0,0,0.1); display: inline-block; }
        .success { color: #27ae60; font-size: 32px; font-weight: bold; }
        .error { color: #e74c3c; font-size: 32px; font-weight: bold; }
        .detail { font-size: 18px; margin: 20px; color: #555; }
        a { color: #3498db; text-decoration: none; font-size: 18px; }
        a:hover { text-decoration: underline; }
    </style>
</head>
<body>

<div class="container">
    <%
        String status = (String) request.getAttribute("status");
        String message = (String) request.getAttribute("message");
        String detail = (String) request.getAttribute("detail");
    %>

    <% if ("success".equals(status)) { %>
        <div class="success"><%= message %></div>
        <p>Tout est bon !</p>
    <% } else { %>
        <div class="error"><%= message %></div>
        <% if (detail != null) { %>
            <div class="detail"><%= detail %></div>
        <% } %>
    <% } %>

    <br><br>
    <a href="/sprint1/personne/form">Recommencer</a>
</div>

</body>
</html>