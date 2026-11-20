@echo off
title EcoTechTrack - Sistema Completo
cd /d "%~dp0"

echo =====================================================================
echo           ECOTECHTRACK - SISTEMA DE TRAZABILIDAD RAEE (UTP)           
echo           Integracion Completa: Spring Boot (8080) + Angular (4200)   
echo =====================================================================
echo.

start "EcoTechTrack Backend" cmd /c "%~dp0ecotechtrack\run-backend.bat"
start "EcoTechTrack Frontend" cmd /c "%~dp0ecotechtrack-frontend\run-frontend.bat"

echo [*] Esperando inicializacion de servicios...
powershell -NoProfile -Command "Start-Sleep -Seconds 10; Start-Process 'http://localhost:4200/'"

echo.
echo =====================================================================
echo  Sistema iniciado con exito!
echo  - Frontend: http://localhost:4200/
echo  - Backend:  http://localhost:8080/
echo  - H2 DB:    http://localhost:8080/h2-console
echo  Credenciales: admin / Admin123!
echo =====================================================================
pause
