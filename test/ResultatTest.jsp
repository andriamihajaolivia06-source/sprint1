<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Résultat - Reçu !</title>
    <style>
        body { font-family: Arial; background: #e8f5e9; padding: 40px; text-align: center; }
        .box { background: white; max-width: 700px; margin: auto; padding: 30px; border-radius: 15px; box-shadow: 0 10px 30px rgba(0,0,0,0.1); }
        h1 { color: #1b5e20; }
        ul { text-align: left; display: inline-block; background: #f1f8e9; padding: 20px; border-radius: 10px; }
        li { margin: 10px 0; font-size: 18px; }
    </style>
</head>
<body>
<div class="box">
    <h1>Résultat</h1>
    <p><strong>${message}</strong></p>

    <h2>Données reçues :</h2>
    <ul>
        <li><strong>Nom :</strong> ${form.nom}</li>
        <li><strong>Âge :</strong> ${form.age}</li>
        <li><strong>Sexe :</strong> ${form.sexe}</li>
        <li><strong>Commentaire :</strong> ${form.commentaire}</li>
        <li><strong>Loisirs :</strong>
            <c:choose>
                <%-- Si les loisirs sont un tableau --%>
                <c:when test="${form.loisir.getClass().simpleName == 'String[]'}">
                    <c:set var="loisirsArray" value="${form.loisir}" />
                    <c:forEach items="${loisirsArray}" var="loisir" varStatus="status">
                        ${loisir}<c:if test="${not status.last}">, </c:if>
                    </c:forEach>
                </c:when>
                <%-- Si les loisirs sont une simple chaîne --%>
                <c:when test="${not empty form.loisir}">
                    ${form.loisir}
                </c:when>
                <%-- Si aucun loisir --%>
                <c:otherwise>
                    Aucun loisir sélectionné
                </c:otherwise>
            </c:choose>
        </li>
    </ul>

    <p><a href="/sprint1/personne/ftest">← Retour au formulaire</a></p>
</div>
</body>
</html>