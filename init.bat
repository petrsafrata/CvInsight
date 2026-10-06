@echo off
setlocal enableextensions

set "ROOT=%~dp0"
set "GITIGNORE=%ROOT%.gitignore"

if not exist "%GITIGNORE%" (
  >"%GITIGNORE%" echo.
)

set "SECTION=### ENV ###"
findstr /x /c:"%SECTION%" "%GITIGNORE%" >nul || (
  >>"%GITIGNORE%" echo.
  >>"%GITIGNORE%" echo %SECTION%
)

call :addIgnore ".env"
call :addIgnore ".env.*"
call :addIgnore "!.env.example"
call :addIgnore "!**/.env.example"

echo Hotovo. Aktualizovan %GITIGNORE%.
echo Skript se nyni smaze.

timeout /t 1 /nobreak >nul
del /f /q "%~f0"
exit /b 0

:addIgnore
set "LINE=%~1"
findstr /x /c:"%LINE%" "%GITIGNORE%" >nul || >>"%GITIGNORE%" echo %LINE%
exit /b 0
