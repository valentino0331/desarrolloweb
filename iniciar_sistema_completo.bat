@echo off
title EcoTechTrack - Sistema Completo (Spring Boot + Angular)
chcp 65001 > nul
cls

echo =====================================================================
echo           ECOTECHTRACK - SISTEMA DE TRAZABILIDAD RAEE (UTP)           
echo           Integración Completa: Spring Boot (8080) + Angular (4200)   
echo =====================================================================
echo.

set "JAVA_HOME=C:\Program Files\Android\Android Studio\jbr"
set "PATH=%JAVA_HOME%\bin;%PATH%"

if not exist "%JAVA_HOME%\bin\java.exe" (
    echo [ERROR] No se encontró el JDK en: %JAVA_HOME%
    pause
    exit /b 1
)

echo [*] Configurado JAVA_HOME: %JAVA_HOME%
echo.

:: 1. Iniciar Backend Spring Boot en ventana separada
echo [*] Iniciando Backend Spring Boot 3 en http://localhost:8080/ ...
start "EcoTechTrack Backend (Spring Boot)" cmd /k "cd /d "%~dp0ecotechtrack" && set "JAVA_HOME=%JAVA_HOME%" && set "PATH=%JAVA_HOME%\bin;%PATH%" && mvnw.cmd spring-boot:run"

:: 2. Iniciar Frontend Angular en ventana separada
echo [*] Iniciando Frontend Angular 18 en http://localhost:4200/ ...
start "EcoTechTrack Frontend (Angular)" cmd /k "cd /d "%~dp0ecotechtrack-frontend" && npm start"

:: 3. Esperar 8 segundos y abrir navegador en Angular
echo [*] Esperando compilación para abrir el navegador...
powershell -NoProfile -Command "Start-Sleep -Seconds 8; Start-Process 'http://localhost:4200/'"

echo.
echo =====================================================================
echo  ¡SISTEMA INICIADO EXITOSAMENTE!
echo  - Frontend Angular:  http://localhost:4200/
echo  - Backend API REST:  http://localhost:8080/api/
echo  - Consola H2:        http://localhost:8080/h2-console
echo.
echo  Credenciales de acceso rápido:
echo  - Administrador:     admin     / Admin123!
echo  - Operador Logístico: operador / Operador123!
echo  - Auditor / Lector:  usuario   / User123!
echo =====================================================================
echo.
pause
