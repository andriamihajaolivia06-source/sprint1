<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Formulaire</title>

    <style>
        body {
            margin: 0;
            padding: 0;
            font-family: Arial, sans-serif;
            background: #f4f6f8;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .container {
            background: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.08);
            width: 350px;
            text-align: center;
        }

        h2 {
            margin-bottom: 20px;
            color: #333;
            font-weight: 500;
        }

        input[type="text"] {
            width: 100%;
            padding: 12px;
            border: 1px solid #d1d5db;
            border-radius: 6px;
            font-size: 15px;
            outline: none;
            transition: 0.2s;
        }

        input[type="text"]:focus {
            border-color: #4f46e5;
            box-shadow: 0 0 4px rgba(79,70,229,0.3);
        }

        button {
            margin-top: 15px;
            width: 100%;
            background: #4f46e5;
            color: white;
            padding: 12px;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-size: 15px;
            transition: 0.2s;
        }

        button:hover {
            background: #3730a3;
        }
    </style>

</head>
<body>
    <div class="container">
        <h2>Entre ton nom</h2>
        <form action="/sprint1/personne/test2" method="post">
            <input type="text" name="nom" placeholder="Ton nom" required />
            <button type="submit">Envoyer</button>
        </form>
    </div>
</body>
</html>
