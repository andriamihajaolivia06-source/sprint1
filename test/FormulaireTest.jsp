<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Formulaire Complet - Test Framework</title>
    <style>
        /* Reset et styles de base */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }
        
        .container {
            max-width: 700px;
            width: 100%;
            background: white;
            padding: 40px;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.15);
            position: relative;
            overflow: hidden;
        }
        
        .container::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 5px;
            background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
        }
        
        h1 {
            color: #2d3748;
            text-align: center;
            margin-bottom: 30px;
            font-size: 2.2rem;
            position: relative;
            padding-bottom: 15px;
        }
        
        h1::after {
            content: '';
            position: absolute;
            bottom: 0;
            left: 50%;
            transform: translateX(-50%);
            width: 80px;
            height: 3px;
            background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
            border-radius: 2px;
        }
        
        /* Styles des champs de formulaire */
        .form-group {
            margin-bottom: 25px;
        }
        
        label {
            display: block;
            margin-bottom: 8px;
            font-weight: 600;
            color: #4a5568;
            font-size: 0.95rem;
            transition: color 0.3s ease;
        }
        
        input[type=text],
        input[type=number],
        select {
            width: 100%;
            padding: 14px 16px;
            border: 2px solid #e2e8f0;
            border-radius: 10px;
            font-size: 16px;
            transition: all 0.3s ease;
            background: #f8fafc;
        }
        
        input[type=text]:focus,
        input[type=number]:focus,
        select:focus {
            outline: none;
            border-color: #667eea;
            background: white;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }
        
        /* Styles des checkboxes */
        .checkbox-group {
            background: #f8fafc;
            padding: 20px;
            border-radius: 10px;
            border: 2px solid #e2e8f0;
            margin: 15px 0;
        }
        
        .checkbox-group label {
            display: inline-flex;
            align-items: center;
            margin: 10px 25px 10px 0;
            font-weight: 500;
            color: #2d3748;
            cursor: pointer;
            padding: 8px 12px;
            border-radius: 6px;
            transition: all 0.3s ease;
        }
        
        .checkbox-group label:hover {
            background: rgba(102, 126, 234, 0.1);
            transform: translateY(-1px);
        }
        
        .checkbox-group input[type="checkbox"] {
            margin-right: 10px;
            width: 18px;
            height: 18px;
            accent-color: #667eea;
            cursor: pointer;
        }
        
        /* Bouton de soumission */
        .submit-btn {
            text-align: center;
            margin-top: 30px;
        }
        
        input[type=submit] {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 16px 40px;
            border: none;
            border-radius: 10px;
            font-size: 18px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            box-shadow: 0 10px 20px rgba(102, 126, 234, 0.3);
            letter-spacing: 0.5px;
        }
        
        input[type=submit]:hover {
            transform: translateY(-2px);
            box-shadow: 0 15px 30px rgba(102, 126, 234, 0.4);
        }
        
        input[type=submit]:active {
            transform: translateY(0);
        }
        
        /* Message d'erreur */
        .error {
            color: #e53e3e;
            font-weight: 600;
            background: #fed7d7;
            padding: 15px;
            border-radius: 10px;
            margin: 20px 0;
            border-left: 4px solid #e53e3e;
            animation: slideIn 0.3s ease;
        }
        
        /* Lien */
        .link-container {
            text-align: center;
            margin-top: 30px;
            padding-top: 20px;
            border-top: 2px solid #e2e8f0;
        }
        
        .link-container a {
            color: #667eea;
            text-decoration: none;
            font-weight: 600;
            padding: 10px 20px;
            border-radius: 6px;
            transition: all 0.3s ease;
            display: inline-block;
        }
        
        .link-container a:hover {
            background: rgba(102, 126, 234, 0.1);
            transform: translateY(-1px);
        }
        
        /* Placeholder stylisé */
        ::placeholder {
            color: #a0aec0;
            opacity: 1;
        }
        
        /* Animation */
        @keyframes slideIn {
            from {
                opacity: 0;
                transform: translateY(-10px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }
        
        /* Responsive */
        @media (max-width: 768px) {
            .container {
                padding: 25px;
                margin: 10px;
            }
            
            h1 {
                font-size: 1.8rem;
            }
            
            .checkbox-group label {
                margin: 8px 15px 8px 0;
                display: block;
            }
            
            input[type=submit] {
                width: 100%;
            }
        }
        
        /* Animation au focus */
        .form-group:focus-within label {
            color: #667eea;
        }
        
        /* Style pour select personnalisé */
        select {
            appearance: none;
            background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='16' height='16' fill='%234a5568' viewBox='0 0 16 16'%3E%3Cpath d='M8 11L3 6h10l-5 5z'/%3E%3C/svg%3E");
            background-repeat: no-repeat;
            background-position: right 16px center;
            padding-right: 40px;
            cursor: pointer;
        }
    </style>
</head>
<body>

    

<div class="container">
    <h1>Test du Framework Ultime</h1>
    <h1>ETU003243</h1>


    <!-- Affichage des erreurs si renvoyées -->
    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>

    <form action="/sprint1/personne/savePersonne" method="post">

        <div class="form-group">
            <label for="nom">Nom complet :</label>
            <input type="text" id="nom" name="nom" value="${nom}" required placeholder="Entrez votre nom complet" />
        </div>

        <div class="form-group">
            <label for="age">Age :</label>
            <input type="number" id="age" name="age" value="${age}" min="1" max="150" placeholder="Votre âge" />
        </div>

        <div class="form-group">
            <label for="sexe">Sexe :</label>
            <select id="sexe" name="sexe">
                <option value="M">Homme</option>
                <option value="F" selected>Femme</option>
                <option value="A">Autre</option>
            </select>
        </div>

        <div class="form-group">
            <label>Loisirs preferes (plusieurs choix possibles) :</label>
            <div class="checkbox-group">
                <label><input type="checkbox" name="loisir" value="Foot"> Football</label>
                <label><input type="checkbox" name="loisir" value="Lecture"> Lecture</label>
                <label><input type="checkbox" name="loisir" value="Jeux"> Jeux video</label>
                <label><input type="checkbox" name="loisir" value="Musique"> Musique</label>
                <label><input type="checkbox" name="loisir" value="Voyage"> Voyage</label>
            </div>
        </div>

        <div class="form-group">
            <label for="commentaire">Commentaire :</label>
            <input type="text" id="commentaire" name="commentaire" value="${commentaire}" placeholder="Ajoutez un commentaire optionnel..." />
        </div>

        <div class="submit-btn">
            <input type="submit" value="Sauvegarder la personne" />
        </div>
    </form>

    <div class="link-container">
        <a href="/sprint1/personne/ftest">Recharger le formulaire</a>
    </div>
</div>

</body>
</html>