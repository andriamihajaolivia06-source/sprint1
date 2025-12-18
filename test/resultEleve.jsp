<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Résultat Élève</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            /* background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); */
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }

        .container {
            max-width: 650px;
            width: 100%;
            background: white;
            padding: 40px;
            border-radius: 16px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            animation: slideIn 0.4s ease-out;
        }

        @keyframes slideIn {
            from {
                opacity: 0;
                transform: translateY(-20px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        h1 {
            color: #10b981;
            text-align: center;
            margin-bottom: 30px;
            font-size: 32px;
            font-weight: 600;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 10px;
        }

        .success-icon {
            width: 50px;
            height: 50px;
            background: linear-gradient(135deg, #10b981 0%, #059669 100%);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 28px;
            animation: scaleIn 0.5s ease-out 0.2s both;
        }

        @keyframes scaleIn {
            from {
                transform: scale(0);
            }
            to {
                transform: scale(1);
            }
        }

        .info {
            background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
            padding: 25px;
            border-radius: 12px;
            margin: 20px 0;
            border-left: 4px solid #667eea;
            transition: transform 0.2s ease;
        }

        .info:hover {
            transform: translateX(5px);
        }

        .info h3 {
            margin: 0 0 15px 0;
            color: #2d3748;
            font-size: 18px;
            font-weight: 600;
            padding-bottom: 10px;
            border-bottom: 2px solid #e2e8f0;
        }

        .info p {
            color: #4a5568;
            line-height: 1.6;
            font-size: 15px;
        }

        .field {
            margin: 12px 0;
            display: flex;
            align-items: baseline;
            padding: 8px 0;
        }

        .field label {
            font-weight: 600;
            display: inline-block;
            min-width: 120px;
            color: #4a5568;
            font-size: 14px;
        }

        .field-value {
            color: #2d3748;
            font-size: 15px;
        }

        .note-section {
            margin-top: 20px;
            padding-top: 20px;
            border-top: 2px solid #e2e8f0;
        }

        .note-section h3 {
            margin-bottom: 15px;
            color: #2d3748;
            font-size: 16px;
        }

        .button-container {
            text-align: center;
            margin-top: 30px;
            padding-top: 25px;
            border-top: 1px solid #e2e8f0;
        }

        .back-btn {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 14px 30px;
            text-decoration: none;
            border-radius: 8px;
            font-weight: 600;
            font-size: 15px;
            transition: all 0.3s ease;
            box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
        }

        .back-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(102, 126, 234, 0.6);
            gap: 12px;
        }

        .back-btn:active {
            transform: translateY(0);
        }

        .back-btn::before {
            content: '←';
            font-size: 18px;
            transition: transform 0.3s ease;
        }

        .back-btn:hover::before {
            transform: translateX(-3px);
        }

        .badge {
            display: inline-block;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 4px 12px;
            border-radius: 12px;
            font-size: 13px;
            font-weight: 600;
            margin-left: 10px;
        }

        @media (max-width: 600px) {
            .container {
                padding: 30px 20px;
            }

            h1 {
                font-size: 26px;
            }

            .field {
                flex-direction: column;
                gap: 5px;
            }

            .field label {
                min-width: auto;
            }
        }
    </style>
</head>
<body>
<div class="container">
    <h1>
        <div class="success-icon">✓</div>
        Succès !
    </h1>
    
    <div class="info">
        <h3>Message</h3>
        <p>${message}</p>
    </div>
    
    <div class="info">
        <h3>Informations de l'élève</h3>
        <div class="field">
            <label>Nom :</label>
            <span class="field-value">${eleve.nom}</span>
        </div>
        <div class="field">
            <label>Âge :</label>
            <span class="field-value">${eleve.age} ans</span>
        </div>
        
        <c:if test="${not empty eleve.note}">
        <div class="note-section">
            <h3>Note obtenue</h3>
            <div class="field">
                <label>Matière :</label>
                <span class="field-value">${eleve.note.matiere}</span>
            </div>
            <div class="field">
                <label>Note :</label>
                <span class="field-value">
                    ${eleve.note.valeur}/20
                    <span class="badge">${eleve.note.valeur >= 10 ? 'Réussi' : 'À améliorer'}</span>
                </span>
            </div>
        </div>
        </c:if>
    </div>
    
    <div class="button-container">
        <a href="/sprint1/eleve/form" class="back-btn">Nouvel élève</a>
    </div>

</div>
</body>
</html>