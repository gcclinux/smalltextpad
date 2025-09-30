#!/usr/bin/env bash
# Quick test-run helper for SmallTextPad
# Exits on any error
set -euo pipefail

# Run from the repository root (script lives in repo root)
cd "$(dirname "$0")"

# Ensure required tools exist
command -v find >/dev/null 2>&1 || { echo "find command not found" >&2; exit 1; }
command -v javac >/dev/null 2>&1 || { echo "javac not found in PATH" >&2; exit 1; }
command -v java >/dev/null 2>&1 || { echo "java not found in PATH" >&2; exit 1; }

echo "[test-run] Deleting compiled class files under bin/"
find bin/ -name "*.class" -delete

echo "[test-run] Rebuilding sources.txt from src/"
find src/ -name "*.java" > sources.txt

echo "[test-run] Compiling Java sources to bin/"
javac -d bin @sources.txt

echo "[test-run] Launching SmallTextPad (this will run until you close the GUI)"
java -cp bin wagemaker.co.uk.main.Launcher
