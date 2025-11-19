<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head><title>Page avec données</title></head>
<body>
    <h1 style="color: purple;">Page JSP chargée via ModelView !</h1>

    <p>Bonjour <strong><%= request.getAttribute("user") %></strong> !</p>
    <p>Tu as <%= request.getAttribute("age") %> ans.</p>

    <% 
        Boolean isAdmin = (Boolean) request.getAttribute("isAdmin");
        if (isAdmin != null && isAdmin) {
    %>
        <p style="color: green;">Tu es administrateur !</p>
    <% } else { %>
        <p style="color: gray;">Tu n'es pas admin.</p>
    <% } %>

    <p>Heure : <%= new java.util.Date() %></p>
</body>
</html>