<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String username = (String) request.getAttribute("username");
    Integer counter = (Integer) request.getAttribute("counter");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Session Sauvegardée</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body { 
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: lightgray;
            min-height: 100vh;
            padding: 40px 20px;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        
        .container { 
            max-width: 650px;
            width: 100%;
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.3);
            overflow: hidden;
            animation: slideIn 0.5s ease;
        }
        
        @keyframes slideIn {
            from {
                opacity: 0;
                transform: translateY(-30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }
        
        @keyframes checkmark {
            0% {
                transform: scale(0) rotate(0deg);
            }
            50% {
                transform: scale(1.2) rotate(180deg);
            }
            100% {
                transform: scale(1) rotate(360deg);
            }
        }
        
        .success-header {
            background: linear-gradient(135deg, #10b981 0%, #059669 100%);
            padding: 50px 30px;
            text-align: center;
            color: white;
        }
        
        .success-icon { 
            width: 100px;
            height: 100px;
            background: white;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 25px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.2);
            animation: checkmark 0.6s ease;
        }
        
        .success-icon::before {
            content: "✓";
            font-size: 60px;
            color: #10b981;
            font-weight: bold;
        }
        
        .success-header h1 { 
            font-size: 28px;
            font-weight: 600;
            margin-bottom: 10px;
        }
        
        .success-header p {
            opacity: 0.95;
            font-size: 15px;
        }
        
        .content {
            padding: 40px 35px;
        }
        
        .info-box { 
            background: linear-gradient(135deg, #ecfdf5 0%, #d1fae5 100%);
            padding: 30px;
            border-radius: 12px;
            margin-bottom: 30px;
            border-left: 4px solid #10b981;
        }
        
        .info-box h2 {
            color: #065f46;
            font-size: 20px;
            margin-bottom: 20px;
            font-weight: 600;
            display: flex;
            align-items: center;
            gap: 10px;
        }
        
        .info-box h2::before {
            content: "📋";
            font-size: 24px;
        }
        
        .data-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 15px 20px;
            background: white;
            border-radius: 8px;
            margin-bottom: 12px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.05);
        }
        
        .data-item:last-child {
            margin-bottom: 0;
        }
        
        .data-item strong {
            color: #374151;
            font-weight: 500;
            font-size: 14px;
        }
        
        .data-item .value {
            color: #10b981;
            font-weight: 600;
            font-size: 16px;
            background: #ecfdf5;
            padding: 6px 16px;
            border-radius: 20px;
        }
        
        .message {
            text-align: center;
            color: #6b7280;
            line-height: 1.6;
            margin-bottom: 30px;
            padding: 20px;
            background: #f9fafb;
            border-radius: 8px;
            font-size: 15px;
        }
        
        .actions {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 12px;
            padding: 0 0 10px 0;
        }
        
        .actions a { 
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
            padding: 14px 20px;
            background: white;
            color: #667eea;
            text-decoration: none;
            border-radius: 10px;
            font-weight: 600;
            font-size: 14px;
            transition: all 0.3s ease;
            border: 2px solid #667eea;
            text-align: center;
        }
        
        .actions a:hover { 
            background: #667eea;
            color: white;
            transform: translateY(-3px);
            box-shadow: 0 8px 20px rgba(102, 126, 234, 0.3);
        }
        
        .actions a:active {
            transform: translateY(-1px);
        }
        
        .actions a.primary {
            background: black;
            color: white;
            border: none;
        }
        
        .actions a.primary:hover {
            transform: translateY(-3px);
            box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
        }
        
        @media (max-width: 600px) {
            .success-header {
                padding: 40px 20px;
            }
            
            .success-header h1 {
                font-size: 24px;
            }
            
            .success-icon {
                width: 80px;
                height: 80px;
            }
            
            .success-icon::before {
                font-size: 50px;
            }
            
            .content {
                padding: 30px 20px;
            }
            
            .actions {
                grid-template-columns: 1fr;
            }
            
            .data-item {
                flex-direction: column;
                gap: 8px;
                text-align: center;
            }
        }
    </style>
</head>
<body>
<div class="container">
    <div class="success-header">
        <div class="success-icon"></div>
        <h1>Session Sauvegardée avec Succès!</h1>
        <p>Vos données ont été enregistrées en toute sécurité</p>
    </div>
    
    <div class="content">
        <div class="info-box">
            <h2>Données Enregistrées</h2>
            <div class="data-item">
                <strong>Nom d'utilisateur</strong>
                <span class="value"><%= username %></span>
            </div>
            <div class="data-item">
                <strong>Compteur de visites</strong>
                <span class="value"><%= counter %></span>
            </div>
        </div>
        
        <div class="message">
            <p>Ces données sont maintenant stockées dans votre session et seront conservées pendant toute la durée de votre navigation.</p>
        </div>
        
        <div class="actions">
            <a href="/sprint1/session/form" class="primary">✏️ Modifier</a>
            <a href="/sprint1/session/view">Voir la session</a>
            <a href="/sprint1/upload/form">Aller à l'upload</a>
        </div>
    </div>
</div>
</body>
</html>