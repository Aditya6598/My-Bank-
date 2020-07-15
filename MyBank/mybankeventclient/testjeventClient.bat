title jeventClient 
echo off 
cls
echo "COMPILING"
start "MVN COMPILE JEVENTCLIENT" cmd.exe /k "mvn clean compile"

timeout 10 > NUL

echo "CREATING jeventClient jar" 

cd .\target\classes
jar cvf jevent.jar jevent\*.class 
cd ..
cd ..
copy .\target\classes\jevent.jar

echo "INSTALLING mysql-connector-java-8.0.18 jar files maven repository .. "

start "MVN INSTALLING mysql-connector-java-8.0.18 jar files maven repository" cmd.exe /k "mvn install:install-file -Dfile=D:\Projects\Laptop_Project\MyBank\mybankdatabase\mysql-connector-java-8.0.18.jar -DgroupId=mysql -DartifactId=mysql-connector-java -Dversion=8.0.18 -Dpackaging=jar -DgeneratePom=true"

echo "INSTALLING json-simple-1.1.1 jar files maven repository .. "
 
start "MVN INSTALLING json-simple-1.1.1 jar files maven repository .. " cmd.exe /k "mvn install:install-file -Dfile=D:\Projects\Laptop_Project\MyBank\mybankdatabase\json-simple-1.1.1.jar -DgroupId=com.googlecode.json-simple -DartifactId=json-simple -Dversion=1.1.1 -Dpackaging=jar -DgeneratePom=true"

echo "INSTALLING jeventClient jar files maven repository .. "

start "MVN INSTALLING jeventClient jar files maven repository .. " cmd.exe /k "mvn install:install-file -Dfile=D:\Projects\Laptop_Project\MyBank\mybankeventclient\jevent.jar -DgroupId=myevent -DartifactId=jevent -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true"

echo "TESTING jeventClient connection .."
echo
mvn exec:java 

