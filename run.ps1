# Quick test-run helper for SmallTextPad (Windows PowerShell)
# Exits on any error
$ErrorActionPreference = "Stop"

# Run from the repository root
Set-Location $PSScriptRoot

# Ensure required tools exist
if (-not (Get-Command javac -ErrorAction SilentlyContinue)) { Write-Error "javac not found in PATH"; exit 1 }
if (-not (Get-Command java -ErrorAction SilentlyContinue)) { Write-Error "java not found in PATH"; exit 1 }

Write-Host "[run] Deleting compiled class files under bin/"
Get-ChildItem -Path bin -Filter *.class -Recurse | Remove-Item

Write-Host "[run] Rebuilding sources.txt from src/"
Get-ChildItem -Path src -Filter *.java -Recurse | Select-Object -ExpandProperty FullName | Set-Content sources.txt

Write-Host "[run] Compiling Java sources to bin/"
javac -d bin @(Get-Content sources.txt)

Write-Host "[run] Copying resource files to bin/"
# Ensure language properties are available in the classpath
$propDest = "bin/wagemaker/co/uk/lang"
if (-not (Test-Path $propDest)) { New-Item -ItemType Directory -Path $propDest -Force | Out-Null }
Copy-Item -Path src/wagemaker/co/uk/lang/*.properties -Destination $propDest -Force

# Copy image resources into bin root so getResource("/name.png") works
if (Test-Path res) {
	Copy-Item -Path (Join-Path $PSScriptRoot 'res\*') -Destination (Join-Path $PSScriptRoot 'bin') -Recurse -Force
}

Write-Host "[run] Launching SmallTextPad (this will run until you close the GUI)"
java -cp bin wagemaker.co.uk.main.Launcher
