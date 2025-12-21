<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>
<%
    Map<String, byte[]> fichiers = (Map<String, byte[]>) request.getAttribute("fichiers");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Résultat Upload Map</title>
    <style>
        body { font-family: Arial; padding: 40px; }
        .container { max-width: 800px; margin: auto; padding: 20px; border: 1px solid #ccc; }
        h1 { color: #4CAF50; }
        .file-card { margin: 15px 0; padding: 15px; border: 1px solid #ddd; border-radius: 5px; }
        .file-card h3 { color: #2196F3; margin-top: 0; }
        .field { margin: 8px 0; }
        .field label { font-weight: bold; display: inline-block; width: 150px; }
        .success { color: #4CAF50; }
        .error { color: #f44336; }
    </style>
</head>
<body>
<div class="container">
    <h1>Résultat Upload (Map&lt;String, byte[]&gt;)</h1>
    
    <% if (fichiers != null && !fichiers.isEmpty()) { %>
        <h3 class="success">✅ ${fichiers.size()} fichier(s) uploadé(s) avec succès</h3>
        
        <% 
        int index = 1;
        for (Map.Entry<String, byte[]> entry : fichiers.entrySet()) { 
            String fileName = entry.getKey();
            byte[] fileData = entry.getValue();
        %>
            <div class="file-card">
                <h3>Fichier <%= index++ %></h3>
                <div class="field">
                    <label>Nom du fichier :</label>
                    <%= fileName %>
                </div>
                <div class="field">
                    <label>Taille :</label>
                    <%= (fileData != null ? fileData.length : 0) %> octets
                </div>
                <div class="field">
                    <label>Taille lisible :</label>
                    <%
                        long size = (fileData != null ? fileData.length : 0);
                        String readableSize;
                        if (size < 1024) {
                            readableSize = size + " B";
                        } else if (size < 1024 * 1024) {
                            readableSize = String.format("%.2f KB", size / 1024.0);
                        } else {
                            readableSize = String.format("%.2f MB", size / (1024.0 * 1024.0));
                        }
                    %>
                    <%= readableSize %>
                </div>
            </div>
        <% } %>
        
    <% } else { %>
        <div class="error">
            <h3>❌ Aucun fichier reçu</h3>
            <p>Les fichiers n'ont pas pu être uploadés.</p>
        </div>
    <% } %>
    
    <div style="margin-top: 30px;">
        <p><a href="/sprint1/upload/map-form">← Nouvel upload multiple</a></p>
        <p><a href="/sprint1/upload/form">← Upload simple</a></p>
    </div>
</div>
</body>
</html>