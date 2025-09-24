#!/bin/bash

# Créer dossier bin
mkdir -p bin

# Compilation
javac -d bin -cp "/home/olivia/Documents/apache-tomcat-10.1.28/lib/servlet-api.jar" src/com/sprint1/FrontServlet.java

# Création du JAR
mkdir -p dist
jar cvf dist/sprint1.jar -C bin .

echo "Framework compilé dans dist/sprint1.jar"
