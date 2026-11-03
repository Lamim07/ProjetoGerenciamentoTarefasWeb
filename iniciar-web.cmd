@echo off
cd /d "%~dp0"
powershell.exe -NoProfile -ExecutionPolicy Bypass -File "%~dp0iniciar-web.ps1"
if errorlevel 1 (
    echo.
    echo Nao foi possivel iniciar o sistema web. Veja a mensagem acima.
    pause
)
