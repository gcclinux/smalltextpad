#!/usr/bin/env bash
# Quick test-run helper for SmallTextPad
# Can be run from anywhere: ./packaging/build-jar.sh or from packaging/ directory
# Exits on any error
set -euo pipefail

# Find the project root (script lives in packaging/ subdirectory)
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
cd "$PROJECT_ROOT"

echo "[build-jar] Working from project root: $PROJECT_ROOT"

# Ensure required tools exist
command -v find >/dev/null 2>&1 || { echo "find command not found" >&2; exit 1; }
command -v javac >/dev/null 2>&1 || { echo "javac not found in PATH" >&2; exit 1; }
command -v java >/dev/null 2>&1 || { echo "java not found in PATH" >&2; exit 1; }
command -v jar >/dev/null 2>&1 || { echo "jar command not found in PATH" >&2; exit 1; }

echo "[build-jar] Deleting compiled class files under bin/"
find bin/ -name "*.class" -delete 2>/dev/null || true

echo "[build-jar] Rebuilding sources.txt from src/"
find src/ -name "*.java" > sources.txt

echo "[build-jar] Compiling Java sources to bin/"
mkdir -p bin
javac -d bin -Xlint:deprecation @sources.txt

echo "[build-jar] Copying resource bundles (properties files) to bin/"
mkdir -p bin/wagemaker/co/uk/lang
cp src/wagemaker/co/uk/lang/*.properties bin/wagemaker/co/uk/lang/

echo "[build-jar] Copying image resources to bin root/"
cp -r res/* bin/

echo "[build-jar] Copying dictionaries to bin/"
cp -r dic bin/

echo "[build-jar] Creating SmallTextPad.jar with all resources included"
mkdir -p classes/artifacts
jar cfm classes/artifacts/SmallTextPad.jar src/META-INF/MANIFEST.MF -C bin .

echo "[build-jar] Build complete! JAR location: classes/artifacts/SmallTextPad.jar"
echo "[build-jar] Now testing newly created SmallTextPad.jar..."
java -jar classes/artifacts/SmallTextPad.jar
