<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>
<%
    Map<String, Object> sessionMap = (Map<String, Object>) request.getAttribute("session");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Toutes les Variables de Session</title>
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
        }
        
        .container { 
            max-width: 900px;
            margin: 0 auto;
            background: white;
            border-radius: 16px;
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
        
        .header {
            background: black;
            color: white;
            padding: 35px;
            text-align: center;
        }
        
        .header h1 { 
            font-size: 28px;
            font-weight: 600;
            margin-bottom: 8px;
        }
        
        .header p {
            opacity: 0.95;
            font-size: 15px;
        }
        
        .content {
            padding: 35px;
        }
        
        .summary { 
            background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
            padding: 25px;
            border-radius: 12px;
            margin-bottom: 30px;
            border-left: 4px solid #667eea;
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
        }
        
        .summary-item {
            display: flex;
            flex-direction: column;
            gap: 8px;
        }
        
        .summary-item strong {
            color: #555;
            font-size: 13px;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            font-weight: 600;
        }
        
        .summary-item span {
            color: #667eea;
            font-size: 20px;
            font-weight: 700;
        }
        
        .summary-item .session-id {
            font-size: 14px;
            font-family: monospace;
            background: white;
            padding: 8px 12px;
            border-radius: 6px;
            word-break: break-all;
        }
        
        .table-container {
            overflow-x: auto;
            margin-bottom: 30px;
            border-radius: 12px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.08);
        }
        
        .session-table { 
            width: 100%;
            border-collapse: collapse;
            background: white;
        }
        
        .session-table thead {
            background: black;
            color: white;
        }
        
        .session-table th {
            padding: 16px;
            text-align: left;
            font-weight: 600;
            font-size: 14px;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }
        
        .session-table td { 
            padding: 16px;
            border-bottom: 1px solid #e5e7eb;
            vertical-align: middle;
        }
        
        .session-table tbody tr {
            transition: all 0.2s ease;
        }
        
        .session-table tbody tr:hover { 
            background: #f9fafb;
            transform: scale(1.01);
            box-shadow: 0 2px 8px rgba(0,0,0,0.05);
        }
        
        .session-table tbody tr:last-child td {
            border-bottom: none;
        }
        
        .key-cell {
            font-weight: 600;
            color: #374151;
            font-size: 15px;
        }
        
        .value-cell {
            color: #6b7280;
            max-width: 300px;
            word-wrap: break-word;
        }
        
        .type-badge { 
            display: inline-block;
            padding: 6px 12px;
            background: black;
            color: white;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }
        
        .empty { 
            text-align: center;
            padding: 60px 20px;
            background: #f9fafb;
            border-radius: 12px;
            margin-bottom: 30px;
        }
        
        .empty-icon {
            font-size: 64px;
            margin-bottom: 20px;
            opacity: 0.6;
        }
        
        .empty h3 {
            color: #374151;
            font-size: 20px;
            margin-bottom: 10px;
        }
        
        .empty p {
            color: #6b7280;
            font-size: 15px;
            line-height: 1.6;
        }
        
        .actions {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 12px;
            padding-bottom: 10px;
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
        
        .actions a.danger {
            border-color: #ef4444;
            color: #ef4444;
        }
        
        .actions a.danger:hover {
            background: #ef4444;
            color: white;
            box-shadow: 0 8px 20px rgba(239, 68, 68, 0.3);
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
        
        @media (max-width: 768px) {
            .header {
                padding: 30px 20px;
            }
            
            .header h1 {
                font-size: 24px;
            }
            
            .content {
                padding: 25px 20px;
            }
            
            .summary {
                grid-template-columns: 1fr;
            }
            
            .session-table th,
            .session-table td {
                padding: 12px;
                font-size: 13px;
            }
            
            .actions {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <h1>Variables de Session</h1>
        <p>Visualisez toutes les données stockées dans votre session</p>
    </div>
    
    <div class="content">
        <div class="summary">
            <div class="summary-item">
                <strong>Nombre de variables</strong>
                <span><%= sessionMap != null ? sessionMap.size() : 0 %></span>
            </div>
            <div class="summary-item">
                <strong>ID de session</strong>
                <span class="session-id"><%= session != null ? session.getId() : "N/A" %></span>
            </div>
        </div>
        
        <% if (sessionMap != null && !sessionMap.isEmpty()) { %>
            <div class="table-container">
                <table class="session-table">
                    <thead>
                        <tr>
                            <th>Clé</th>
                            <th>Valeur</th>
                            <th>Type</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Map.Entry<String, Object> entry : sessionMap.entrySet()) { %>
                        <tr>
                            <td class="key-cell"><%= entry.getKey() %></td>
                            <td class="value-cell"><%= entry.getValue() %></td>
                            <td>
                                <span class="type-badge">
                                    <%= entry.getValue() != null ? entry.getValue().getClass().getSimpleName() : "null" %>
                                </span>
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        <% } else { %>
            <div class="empty">
                <div class="empty-icon"></div>
                <h3>Aucune variable de session</h3>
                <p>Votre session est actuellement vide.<br>Utilisez le formulaire pour ajouter des variables à la session.</p>
            </div>
        <% } %>
        
        <div class="actions">
            <a href="/sprint1/session/form" class="primary">Retour au formulaire</a>
            <a href="/sprint1/session/clear" class="danger">Effacer la session</a>
            <a href="/sprint1/upload/form">Aller à l'upload</a>
        </div>
    </div>
</div>
</body>
</html>