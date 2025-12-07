<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Résultat Élève</title>
    <style>
        body { font-family: Arial; background: #f0f8ff; padding: 40px; }
        .container { max-width: 600px; margin: auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
        h1 { color: #4CAF50; text-align: center; }
        .info { background: #f9f9f9; padding: 20px; border-radius: 5px; margin: 20px 0; }
        .info h3 { margin-top: 0; color: #333; }
        .field { margin: 10px 0; }
        .field label { font-weight: bold; display: inline-block; width: 100px; }
        .back-btn { display: inline-block; background: #4CAF50; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; }
        .back-btn:hover { background: #45a049; }
    </style>
</head>
<body>
<div class="container">
    <h1>✅ Succès !</h1>
    
    <div class="info">
        <h3>Message :</h3>
        <p>${message}</p>
    </div>
    
    <div class="info">
        <h3>Élève :</h3>
        <div class="field">
            <label>Nom :</label>
            ${eleve.nom}
        </div>
        <div class="field">
            <label>Âge :</label>
            ${eleve.age} ans
        </div>
        
        <c:if test="${not empty eleve.note}">
        <h3>Note :</h3>
        <div class="field">
            <label>Matière :</label>
            ${eleve.note.matiere}
        </div>
        <div class="field">
            <label>Note :</label>
            ${eleve.note.valeur}/20
        </div>
        </c:if>
    </div>
    
    <div style="text-align: center; margin-top: 30px;">
        <a href="/sprint1/eleve/form" class="back-btn">← Nouvel élève</a>
    </div>

</div>
</body>
</html>