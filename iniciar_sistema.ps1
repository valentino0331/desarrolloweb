Write-Host "=====================================================================" -ForegroundColor Green
Write-Host "          ECOTECHTRACK - SISTEMA DE TRAZABILIDAD RAEE (UTP)          " -ForegroundColor Green
Write-Host "          Integraci�n Completa: Spring Boot (8080) + Angular (4200)  " -ForegroundColor Green
Write-Host "=====================================================================" -ForegroundColor Green

$root = $PSScriptRoot

Start-Process cmd -ArgumentList "/c `"$root\ecotechtrack\run-backend.bat`""
Start-Process cmd -ArgumentList "/c `"$root\ecotechtrack-frontend\run-frontend.bat`""

Write-Host "`n[*] Esperando inicializacion de servicios..." -ForegroundColor Yellow
Start-Sleep -Seconds 10
Start-Process "http://localhost:4200/"

Write-Host "`n[OK] Sistema en ejecucion:" -ForegroundColor Green
Write-Host " - Frontend: http://localhost:4200/"
Write-Host " - Backend:  http://localhost:8080/"
Write-Host " - H2 DB:    http://localhost:8080/h2-console"
Write-Host "`nCredenciales de acceso: admin / Admin123!`n" -ForegroundColor White
