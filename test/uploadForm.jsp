<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Upload Fichier</title>
    <style>
        body { font-family: Arial; padding: 40px; }
        .container { max-width: 500px; margin: auto; padding: 20px; border: 1px solid #ccc; }
        h1 { color: #333; }
        .form-group { margin: 15px 0; }
        label { display: block; margin-bottom: 5px; }
        input[type="file"] { width: 100%; padding: 8px; }
        button { background: #4CAF50; color: white; padding: 10px 20px; border: none; cursor: pointer; }
        button:hover { background: #45a049; }
    </style>
</head>
<body>
<div class="container">
    <h1>Upload de Fichier</h1>
    <form method="post" enctype="multipart/form-data" action="/sprint1/upload/save">
        <div class="form-group">
            <label>Fichier :</label>
            <input type="file" name="fichier" required>
        </div>
        <button type="submit">Uploader</button>
    </form>
   
</div>
</body>
</html>