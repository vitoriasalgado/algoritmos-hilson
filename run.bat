@echo off
REM Executa os dois lados do trabalho. Sem Maven, sem Gradle, sem pip install.
cd /d "%~dp0"

echo === PYTHON - problemas 1-4, 11-12, 15-24 ===
python python\main.py
if errorlevel 1 exit /b 1

echo.
echo === JAVA - problemas 5-10, 13-14 ===
dir /s /b java\src\*.java > "%TEMP%\fontes_ed.txt"
javac -d java\out "@%TEMP%\fontes_ed.txt"
if errorlevel 1 exit /b 1
java -cp java\out ed.app.Main
if errorlevel 1 exit /b 1

echo.
echo Saidas gravadas em saidas\