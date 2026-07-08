#!/usr/bin/env bash
# Compile the whole project and build a double-clickable runnable JAR.
set -e
cd "$(dirname "$0")"

echo "==> Compiling..."
rm -rf out && mkdir -p out dist build
find src -name "*.java" > build/sources.txt
javac -d out @build/sources.txt

echo "==> Packaging JAR..."
printf 'Main-Class: com.pms.ui.MainFrame\n' > build/manifest.txt
jar cfm dist/AcmePMS.jar build/manifest.txt -C out .

echo "==> Done: dist/AcmePMS.jar"
echo "    Run with:  java -jar dist/AcmePMS.jar"
