#!/bin/bash

TOMCAT_WEBAPPS_PATH="/home/olivia/Documents/apache-tomcat-10.1.28/webapps"
WAR_NAME="sprint1"

# Nettoyage précédent
rm -rf $TOMCAT_WEBAPPS_PATH/$WAR_NAME
rm -f $TOMCAT_WEBAPPS_PATH/$WAR_NAME.war

# Création du .war
cd test
cp ../framework/dist/sprint1.jar WEB-INF/lib/
jar cvf ../$WAR_NAME.war *

# Déplacement du .war dans Tomcat
mv ../$WAR_NAME.war $TOMCAT_WEBAPPS_PATH/

echo "Application déployée dans Tomcat à l'adresse : http://localhost:8080/$WAR_NAME"
