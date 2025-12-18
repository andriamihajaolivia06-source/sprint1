<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Formulaire Élève</title>
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
            max-width: 550px;
            width: 100%;
            background: white;
            padding: 40px;
            border-radius: 16px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
        }

        h1 {
            color: #2d3748;
            text-align: center;
            margin-bottom: 30px;
            font-size: 28px;
            font-weight: 600;
        }

        h3 {
            color: #4a5568;
            margin: 25px 0 15px 0;
            font-size: 18px;
            font-weight: 600;
            padding-bottom: 10px;
            border-bottom: 2px solid #e2e8f0;
        }

        .method-select {
            margin-bottom: 30px;
            padding: 20px;
            background: #f7fafc;
            border-radius: 8px;
            border-left: 4px solid #667eea;
        }

        .method-select label {
            color: #2d3748;
            font-weight: 600;
            margin-bottom: 10px;
        }

        .form-group {
            margin-bottom: 20px;
        }

        label {
            display: block;
            margin-bottom: 8px;
            color: #4a5568;
            font-weight: 500;
            font-size: 14px;
        }

        input[type="text"],
        input[type="number"],
        select {
            width: 100%;
            padding: 12px 16px;
            border: 2px solid #e2e8f0;
            border-radius: 8px;
            font-size: 15px;
            transition: all 0.3s ease;
            background: white;
        }

        input[type="text"]:focus,
        input[type="number"]:focus,
        select:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }

        input::placeholder {
            color: #a0aec0;
        }

        select {
            cursor: pointer;
            appearance: none;
            background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='12' height='12' viewBox='0 0 12 12'%3E%3Cpath fill='%234a5568' d='M6 9L1 4h10z'/%3E%3C/svg%3E");
            background-repeat: no-repeat;
            background-position: right 16px center;
            padding-right: 40px;
        }

        button {
            width: 100%;
            padding: 14px 20px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            margin-top: 10px;
            box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
        }

        button:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(102, 126, 234, 0.6);
        }

        button:active {
            transform: translateY(0);
        }

        .link-container {
            text-align: center;
            margin-top: 25px;
            padding-top: 25px;
            border-top: 1px solid #e2e8f0;
        }

        .link-container a {
            color: #667eea;
            text-decoration: none;
            font-weight: 500;
            transition: all 0.3s ease;
            display: inline-flex;
            align-items: center;
            gap: 5px;
        }

        .link-container a:hover {
            color: #764ba2;
            gap: 8px;
        }

        .link-container a::after {
            content: '→';
            transition: transform 0.3s ease;
        }

        .link-container a:hover::after {
            transform: translateX(3px);
        }

        @media (max-width: 600px) {
            .container {
                padding: 30px 20px;
            }

            h1 {
                font-size: 24px;
            }
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Formulaire Élève</h1>
    
    <div class="method-select">
        <label>Méthode :</label>
        <select id="method">
            <option value="1">Méthode 1: Objet Eleve</option>
            <option value="2">Méthode 2: RequestParam</option>
        </select>
    </div>
    
    <form id="eleveForm" method="post">
        <div class="form-group">
            <label>Nom :</label>
            <input type="text" name="nom" required>
        </div>
        
        <div class="form-group">
            <label>Âge :</label>
            <input type="number" name="age" min="10" max="20" required>
        </div>
        
        <h3>Note</h3>
        <div class="form-group">
            <label>Matière :</label>
            <input type="text" name="note.matiere" placeholder="Mathématiques">
        </div>
        
        <div class="form-group">
            <label>Note (sur 20) :</label>
            <input type="number" name="note.valeur" step="0.5" min="0" max="20" placeholder="15.5">
        </div>
        
        <button type="submit">Enregistrer</button>
    </form>
    
    <div class="link-container">
        <a href="/sprint1/eleve/demo">Voir la démo</a>
    </div>
</div>

<script>
    document.getElementById('eleveForm').addEventListener('submit', function(e) {
        const method = document.getElementById('method').value;
        
        // Toujours créer des champs pour les deux méthodes
        const matiereValue = document.querySelector('[name="note.matiere"]').value;
        const valeurValue = document.querySelector('[name="note.valeur"]').value;
        
        // Créer des champs cachés pour la méthode 2
        if (method === '2') {
            let matiereInput = document.createElement('input');
            matiereInput.type = 'hidden';
            matiereInput.name = 'matiere';
            matiereInput.value = matiereValue;
            this.appendChild(matiereInput);
            
            let valeurInput = document.createElement('input');
            valeurInput.type = 'hidden';
            valeurInput.name = 'valeur';
            valeurInput.value = valeurValue;
            this.appendChild(valeurInput);
            
            this.action = '/sprint1/eleve/save2';
        } else {
            this.action = '/sprint1/eleve/save';
        }
    });
</script>
</body>
</html>