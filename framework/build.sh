#!/bin/bash

# Créer dossier bin
mkdir -p bin

# Compilation avec servlet-api.jar et jakarta.ws.rs-api.jar
javac -d bin \
  -cp "/home/olivia/Documents/apache-tomcat-10.1.28/lib/servlet-api.jar:/home/olivia/Documents/lib/jakarta.ws.rs-api-3.1.0.jar" \
  src/com/sprint1/FrontServlet.java \
  src/com/sprint1/PathAnnotation.java \
  src/com/sprint1/Controller.java \
  src/com/sprint1/ModelView.java

# Création du JAR
mkdir -p dist
jar cvf dist/sprint1.jar -C bin .

echo "Framework compilé dans dist/sprint1.jar"
