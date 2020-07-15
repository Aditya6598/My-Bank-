cd .\mybankjerseyservice\customer
start "customer service" cmd.exe /k "mvn clean compile exec:java"
cd ..\account
start "account service" cmd.exe /k "mvn clean compile exec:java"
cd ..\moneytransfer
start "moneytransfer service" cmd.exe /k "mvn clean compile exec:java"
cd ..\eventsync
start "eventsync service" cmd.exe /k "mvn clean compile exec:java"
