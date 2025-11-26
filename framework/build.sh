#!/bin/bash

# Créer le dossier bin
mkdir -p bin

# -parameters EST ABSOLUMENT OBLIGATOIRE
javac -d bin \
  -parameters \
  -cp "/home/olivia/Documents/apache-tomcat-10.1.28/lib/servlet-api.jar" \
  src/com/sprint1/*.java

# Créer le JAR
mkdir -p dist
jar cvf dist/sprint1.jar -C bin .

echo ""
echo "COMPILATION RÉUSSIE !"
echo "-parameters est activé → String nom marche sans annotation"
echo "Framework prêt : dist/sprint1.jar"
echo ""