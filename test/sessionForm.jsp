<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String username = (String) request.getAttribute("username");
    Integer counter = (Integer) request.getAttribute("counter");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Formulaire Session</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body { 
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background:lightgrey;
            min-height: 100vh;
            padding: 40px 20px;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        
        .container { 
            max-width: 700px;
            width: 100%;
            background: white;
            border-radius: 16px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.3);
            overflow: hidden;
        }
        
        .header {
            background: black;
            color: white;
            padding: 30px;
            text-align: center;
        }
        
        .header h1 { 
            font-size: 28px;
            font-weight: 600;
            margin-bottom: 5px;
        }
        
        .header p {
            opacity: 0.9;
            font-size: 14px;
        }
        
        .content {
            padding: 35px;
        }
        
        .info-box { 
            background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
            padding: 25px;
            border-radius: 12px;
            margin-bottom: 30px;
            border-left: 4px solid #667eea;
        }
        
        .info-box h3 {
            color: #333;
            font-size: 18px;
            margin-bottom: 15px;
            font-weight: 600;
        }
        
        .info-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 12px 0;
            border-bottom: 1px solid rgba(0,0,0,0.05);
        }
        
        .info-item:last-child {
            border-bottom: none;
        }
        
        .info-item strong {
            color: #555;
            font-weight: 500;
        }
        
        .info-item span {
            color: #667eea;
            font-weight: 600;
            background: white;
            padding: 5px 15px;
            border-radius: 20px;
            font-size: 14px;
        }
        
        .form-section {
            background: #f8f9fa;
            padding: 25px;
            border-radius: 12px;
            margin-bottom: 30px;
        }
        
        .form-group { 
            margin-bottom: 20px;
        }
        
        .form-group:last-child {
            margin-bottom: 0;
        }
        
        label { 
            display: block;
            margin-bottom: 10px;
            font-weight: 600;
            color: #333;
            font-size: 14px;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }
        
        input[type="text"] { 
            width: 100%;
            padding: 14px 18px;
            border: 2px solid #e0e0e0;
            border-radius: 8px;
            font-size: 16px;
            transition: all 0.3s ease;
            font-family: inherit;
        }
        
        input[type="text"]:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }
        
        button { 
            width: 100%;
            background: black;
            color: white;
            padding: 14px 24px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-size: 16px;
            font-weight: 600;
            transition: all 0.3s ease;
            text-transform: uppercase;
            letter-spacing: 1px;
        }
        
        button:hover { 
            transform: translateY(-2px);
            box-shadow: 0 10px 25px rgba(102, 126, 234, 0.4);
        }
        
        button:active {
            transform: translateY(0);
        }
        
        .actions {
            padding: 30px 35px;
            background: #f8f9fa;
        }
        
        .actions h3 {
            color: #333;
            font-size: 18px;
            margin-bottom: 20px;
            font-weight: 600;
        }
        
        .action-grid {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 12px;
        }
        
        .actions a { 
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 12px 20px;
            background: white;
            color: #667eea;
            text-decoration: none;
            border-radius: 8px;
            font-weight: 500;
            font-size: 14px;
            transition: all 0.3s ease;
            border: 2px solid #667eea;
            text-align: center;
        }
        
        .actions a:hover { 
            background: #667eea;
            color: white;
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.3);
        }
        
        .actions a.danger { 
            border-color: #ef4444;
            color: #ef4444;
        }
        
        .actions a.danger:hover { 
            background: #ef4444;
            color: white;
            box-shadow: 0 5px 15px rgba(239, 68, 68, 0.3);
        }
        
        @media (max-width: 600px) {
            .action-grid {
                grid-template-columns: 1fr;
            }
            
            .header h1 {
                font-size: 24px;
            }
            
            .content {
                padding: 25px;
            }
            
            .actions {
                padding: 25px;
            }
        }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <h1>Gestion de Session</h1>
        <p>Gérez vos informations de session facilement</p>
    </div>
    
    <div class="content">
        <div class="info-box">
            <h3>📊 Informations de Session</h3>
            <div class="info-item">
                <strong>Nom d'utilisateur</strong>
                <span><%= username != null ? username : "Non défini" %></span>
            </div>
            <div class="info-item">
                <strong>Compteur de visites</strong>
                <span><%= counter != null ? counter : 0 %></span>
            </div>
        </div>
        
        <form method="post" action="/sprint1/session/save">
            <div class="form-section">
                <div class="form-group">
                    <label for="username">Nom d'utilisateur</label>
                    <input type="text" id="username" name="username" 
                           value="<%= username != null ? username : "" %>" 
                           placeholder="Entrez votre nom" required>
                </div>
                <button type="submit">Sauvegarder en Session</button>
            </div>
        </form>
    </div>
    
    <div class="actions">
        <h3>⚡ Actions Rapides</h3>
        <div class="action-grid">
            <a href="/sprint1/session/view">Voir les variables</a>
            <a href="/sprint1/session/increment">Incrémenter</a>
            <a href="/sprint1/session/remove" class="danger">Supprimer nom</a>
            <a href="/sprint1/session/remove" class="danger">Remove stock</a>
            <a href="/sprint1/session/clear" class="danger">Effacer session</a>
        </div>
    </div>
</div>
</body>
</html>