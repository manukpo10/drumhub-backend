@echo off
set "JAVA_HOME=C:\Program Files\Java\jdk-21"
set "PATH=%JAVA_HOME%\bin;%PATH%"
cd /d "C:\Users\Manu\OneDrive - UTN FRLP\Documentos\DrumHub\drumhub-backend"
mvn test -Dspring.profiles.active=dev --no-transfer-progress
