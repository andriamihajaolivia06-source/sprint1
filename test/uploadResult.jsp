<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sprint1.Upload" %>
<%
    Upload fichier = (Upload) request.getAttribute("fichier");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Résultat Upload</title>
    <style>
        body { font-family: Arial; padding: 40px; }
        .container { max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ccc; }
        h1 { color: #4CAF50; }
        .info { margin: 20px 0; padding: 15px; background: #f9f9f9; }
        .field { margin: 10px 0; }
        .field label { font-weight: bold; display: inline-block; width: 150px; }
    </style>
</head>
<body>
<div class="container">
    <h1>Résultat Upload</h1>
    
    <% if (fichier != null) { %>
        <div class="info">
            <h3>✅ Fichier uploadé avec succès</h3>
            <div class="field">
                <label>Nom original :</label>
                <%= fichier.getOriginalFileName() %>
            </div>
            <div class="field">
                <label>Nom fichier :</label>
                <%= fichier.getFileName() %>
            </div>
            <div class="field">
                <label>Taille :</label>
                <%= fichier.getSize() %> octets
            </div>
            <div class="field">
                <label>Type :</label>
                <%= fichier.getContentType() %>
            </div>
            <div class="field">
                <label>Chemin :</label>
                <%= fichier.getUploadPath() %>
            </div>
        </div>
    <% } else { %>
        <div class="info" style="background: #ffe6e6;">
            <h3>❌ Aucun fichier reçu</h3>
            <p>Le fichier n'a pas pu être uploadé.</p>
        </div>
    <% } %>
    
    <p><a href="/sprint1/upload/form">← Nouvel upload</a></p>
</div>
</body>
</html>