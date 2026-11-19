@echo off
title EcoTechTrack - Servidor Spring Boot 3
chcp 65001 > nul
cls

echo =====================================================================
echo               ECOTECHTRACK - SISTEMA DE TRAZABILIDAD RAEE            
echo                  Avance 1 - Desarrollo Web Integrado (UTP)           
echo =====================================================================
echo.

:: Establecer ruta de JDK 21 instalada en el equipo
set "JAVA_HOME=C:\Program Files\Android\Android Studio\jbr"
set "PATH=%JAVA_HOME%\bin;%PATH%"

:: Verificar si Java 21 existe
if not exist "%JAVA_HOME%\bin\java.exe" (
    echo [ERROR] No se encontro el JDK en: %JAVA_HOME%
    echo Por favor verifica la ruta de instalacion de Java 17 o 21.
    pause
    exit /b 1
)

echo [*] Configurado JAVA_HOME: %JAVA_HOME%
echo [*] Directorio del proyecto: %~dp0ecotechtrack
echo.

:: Abrir el navegador automaticamente luego de 6 segundos en segundo plano
start "" powershell -NoProfile -Command "Start-Sleep -Seconds 6; Start-Process 'http://localhost:8080/'"

echo [*] Iniciando servidor Spring Boot en http://localhost:8080/ ...
echo [*] La aplicacion se abrira automaticamente en tu navegador.
echo.
echo Presiona [Ctrl + C] en cualquier momento para detener el servidor.
echo =====================================================================
echo.

cd /d "%~dp0ecotechtrack"
"..\apache-maven-3.9.9\bin\mvn.cmd" spring-boot:run

pause
