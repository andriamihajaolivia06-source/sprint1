#!/bin/bash

TOMCAT_WEBAPPS_PATH="/home/olivia/Documents/apache-tomcat-10.1.28/webapps"
WAR_NAME="sprint1"

rm -rf $TOMCAT_WEBAPPS_PATH/$WAR_NAME
rm -f $TOMCAT_WEBAPPS_PATH/$WAR_NAME.war

cd test

# 1. Créer WEB-INF/classes
mkdir -p WEB-INF/classes/com/sprint1

# 2. Compiler les classes de test
javac -d WEB-INF/classes \
  -cp "../framework/dist/sprint1.jar:/home/olivia/Documents/apache-tomcat-10.1.28/lib/servlet-api.jar" \
  *.java

# 3. Copier le JAR
cp ../framework/dist/sprint1.jar WEB-INF/lib/

# 4. Créer le .war
jar cvf ../$WAR_NAME.war *

mv ../$WAR_NAME.war $TOMCAT_WEBAPPS_PATH/

echo "DÉPLOYÉ : http://localhost:8080/$WAR_NAME"