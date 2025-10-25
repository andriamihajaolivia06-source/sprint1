# #!/bin/bash

# # Nettoyer les anciens .class
# rm -rf test/com

# # Compiler Employer.java et Main.java dans test/com/sprint1/
# javac -d test \
#   -cp "framework/dist/sprint1.jar" \
#   test/Employer.java test/Main.java

# echo "Employer et Main compilés → test/com/sprint1/"


#!/bin/bash

rm -rf test/com

javac -d test \
  -cp "framework/dist/sprint1.jar" \
  test/Class1.java test/Class2.java test/Class3.java test/Main.java

echo "Test classes compilées → test/com/sprint1/"
