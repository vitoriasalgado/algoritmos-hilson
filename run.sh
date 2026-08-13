#!/usr/bin/env bash
# Executa os dois lados do trabalho. Sem Maven, sem Gradle, sem pip install.
# Testado em macOS e Linux.
set -euo pipefail
cd "$(dirname "$0")"

echo "=== PYTHON — problemas 1-4, 11-12, 15-24 ==="
python3 python/main.py

echo
echo "=== JAVA — problemas 5-10, 13-14 ==="
mkdir -p java/out
# -print0/-xargs -0 para nao quebrar em caminhos com espaco
find java/src -name "*.java" -print0 | xargs -0 javac -d java/out
java -cp java/out ed.app.Main

echo
echo "Saidas gravadas em saidas/"