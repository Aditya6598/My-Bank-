echo off 

TITLE TEST SERVICE ORIRENTED COMPUTING ARCHITECTURE

echo "compiling common event client object" 
cd mybankdatabase 
start "compile jdatabase common object" cmd.exe /k "testjdatabase"
timeout 10 > NUL
cd ..

echo "compiling common event client object" 
cd mybankeventclient
start "compile jeventclient common object" cmd.exe /k "testjeventclient"
timeout 10 > NUL

cd..


echo "compiling services: eventsync, customer, account .." 
cd mybankjerseyservice
timeout 5 > NUL
cd eventsync
start "compile eventsync service" cmd.exe /k "mvn clean compile"
timeout 5 > NUL
cd ..
cd account
start "compile account service" cmd.exe /k "mvn clean compile"
timeout 5 > NUL
cd ..
cd customer
start "compile customer service" cmd.exe /k "mvn clean compile"
timeout 10 > NUL
cd ..
echo "starting all services: eventsync, customer, account .." 
cd eventsync
start "run eventsync service" cmd.exe /k "mvn exec:java"
timeout 5 > NUL
cd ..
cd account
start "run account service" cmd.exe /k "mvn exec:java"
timeout 5 > NUL
cd ..
cd customer
start "run customer service" cmd.exe /k "mvn exec:java"

