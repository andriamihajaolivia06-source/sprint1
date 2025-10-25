#!/bin/bash

# Nettoyer les anciens .class
rm -rf test/com

# Compiler Employer.java et Main.java dans test/com/sprint1/
javac -d test \
  -cp "framework/dist/sprint1.jar" \
  test/Employer.java test/Main.java

echo "Employer et Main compilés → test/com/sprint1/"
