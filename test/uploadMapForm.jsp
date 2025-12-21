<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Upload Multiple avec Map</title>
    <style>
        body { font-family: Arial; padding: 40px; }
        .container { max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ccc; }
        h1 { color: #333; }
        .form-group { margin: 15px 0; }
        label { display: block; margin-bottom: 5px; }
        input[type="file"] { width: 100%; padding: 8px; }
        button { background: #4CAF50; color: white; padding: 10px 20px; border: none; cursor: pointer; }
        button:hover { background: #45a049; }
        .file-input { margin: 10px 0; }
        .add-file-btn { background: #2196F3; margin: 10px 0; }
    </style>
    <script>
        let fileCount = 1;
        
        function addFileInput() {
            fileCount++;
            const container = document.getElementById('fileInputs');
            const div = document.createElement('div');
            div.className = 'file-input';
            div.innerHTML = `
                <label>Fichier ${fileCount} :</label>
                <input type="file" name="fichier${fileCount}" accept="*/*">
            `;
            container.appendChild(div);
        }
    </script>
</head>
<body>
<div class="container">
    <h1>Upload Multiple (Map&lt;String, byte[]&gt;)</h1>
    
    <form method="post" enctype="multipart/form-data" action="/sprint1/upload/save-multiple">
        <div id="fileInputs">
            <div class="file-input">
                <label>Fichier 1 :</label>
                <input type="file" name="fichier1" accept="*/*" required>
            </div>
        </div>
        
        <button type="button" class="add-file-btn" onclick="addFileInput()">+ Ajouter un autre fichier</button>
        
        <div style="margin-top: 20px;">
            <button type="submit">Uploader tous les fichiers</button>
        </div>
    </form>
    
    <p><a href="/sprint1/upload/form">← Upload simple</a></p>
</div>
</body>
</html>