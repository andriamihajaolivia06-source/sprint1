<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Formulaire</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f4f6f9;
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
            margin: 0;
        }

        .container {
            background: white;
            padding: 30px 40px;
            border-radius: 12px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            text-align: center;
            width: 320px;
        }

        h2 {
            margin-bottom: 20px;
            color: #333;
        }

        input[type="text"] {
            width: 100%;
            padding: 12px;
            margin-bottom: 20px;
            border: 1px solid #ccc;
            border-radius: 8px;
            font-size: 15px;
            transition: 0.3s;
        }

        input[type="text"]:focus {
            border-color: #0066ff;
            box-shadow: 0 0 6px rgba(0, 102, 255, 0.3);
            outline: none;
        }

        button {
            background: #0066ff;
            color: white;
            border: none;
            padding: 12px 25px;
            border-radius: 8px;
            font-size: 15px;
            cursor: pointer;
            transition: 0.3s;
            width: 100%;
        }

        button:hover {
            background: #004ecc;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Entre ton nom</h2>
        <form action="/sprint1/personne/saluer" method="post">
            <input type="text" name="prenom" placeholder="Ton nom" required />
            <button type="submit">Envoyer</button>
        </form>
    </div>
</body>
</html>
