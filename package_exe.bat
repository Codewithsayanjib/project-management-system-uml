@echo off
REM ============================================================================
REM  Build a native Windows .exe installer for Acme PMS using jpackage.
REM  Requirements (on Windows):
REM    - JDK 17+ (jpackage ships with the JDK; `jpackage --version` must work)
REM    - WiX Toolset 3.x on PATH (needed by jpackage for .exe/.msi installers)
REM  Run this file on a Windows machine from the project root.
REM ============================================================================

echo ==^> Compiling...
if exist out rmdir /s /q out
mkdir out
dir /s /b src\*.java > sources.txt
javac -d out @sources.txt

echo ==^> Packaging JAR...
if not exist dist mkdir dist
echo Main-Class: com.pms.ui.MainFrame> manifest.txt
jar cfm dist\AcmePMS.jar manifest.txt -C out .

echo ==^> Building Windows .exe (this needs WiX Toolset installed)...
jpackage ^
  --type exe ^
  --name "AcmePMS" ^
  --app-version 1.0 ^
  --vendor "Acme Software" ^
  --description "Project Management System - ME UML Lab" ^
  --input dist ^
  --main-jar AcmePMS.jar ^
  --main-class com.pms.ui.MainFrame ^
  --win-shortcut ^
  --win-menu ^
  --dest dist\installer

echo ==^> Done. Installer is in dist\installer\AcmePMS-1.0.exe
pause
