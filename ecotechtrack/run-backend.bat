@echo off
title EcoTechTrack - Backend Spring Boot (8080)
cd /d "%~dp0"
set "JAVA_HOME=C:\Program Files\Android\Android Studio\jbr"
set "PATH=%JAVA_HOME%\bin;%PATH%"
echo =======================================================
echo  Iniciando Backend Spring Boot 3 en puerto 8080...
echo =======================================================
call mvnw.cmd spring-boot:run
pause
