#!/usr/bin/env bash
# Quick test-run helper for SmallTextPad
# On RaspberryPI5 you may need to install snapd first and use it without lxd
# $ snapcraft pack --destructive-mode
# Exits on any error
set -euo pipefail

# Run from the repository root (script lives in repo root)
cd "$(dirname "$0")"

# Ensure required tools exist
command -v find >/dev/null 2>&1 || { echo "find command not found" >&2; exit 1; }
command -v javac >/dev/null 2>&1 || { echo "javac not found in PATH" >&2; exit 1; }
command -v java >/dev/null 2>&1 || { echo "java not found in PATH" >&2; exit 1; }

echo "[run] Deleting compiled class files under bin/"
find bin/ -name "*.class" -delete

echo "[run] Rebuilding sources.txt from src/"
find src/ -name "*.java" > sources.txt

echo "[run] Compiling Java sources to bin/"
javac -d bin @sources.txt

echo "[run] Now creating SmallTextPad.jar in the current directory"
jar cfm classes/artifacts/SmallTextPad.jar src/META-INF/MANIFEST.MF -C bin . -C . res -C . dic

echo "[run] Now creating smalltextpad_<version>_amd64.snap in the current directory"
snapcraft pack --output=classes/artifacts/smalltextpad_$(grep '^version:' snap/snapcraft.yaml | awk '{print $2}' | tr -d "'")_amd64.snap




