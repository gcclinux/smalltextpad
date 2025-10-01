# Windows PowerShell packaging script for SmallTextPad
$ErrorActionPreference = "Stop"

# Run from the repository root
Set-Location $PSScriptRoot

# Clean previous build artifacts
Write-Host "[package] Cleaning previous build artifacts..."
Remove-Item -Recurse -Force bin, dist -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Path bin | Out-Null
New-Item -ItemType Directory -Path dist | Out-Null

# Compile Java sources
Write-Host "[package] Compiling Java sources..."
Get-ChildItem -Path src -Filter *.java -Recurse | Select-Object -ExpandProperty FullName | Set-Content sources.txt
javac -d bin @(Get-Content sources.txt)

# Copy resource files (.properties)
Write-Host "[package] Copying resource files..."
$propDest = "bin/wagemaker/co/uk/lang"
if (-not (Test-Path $propDest)) { New-Item -ItemType Directory -Path $propDest -Force | Out-Null }
Copy-Item -Path src/wagemaker/co/uk/lang/*.properties -Destination $propDest -Force

# Copy images and other resources
Write-Host "[package] Copying image resources..."
# Copy the contents of res/ into bin root so resources are accessible as '/printer.png' etc.
if (Test-Path res) {
	Copy-Item -Path (Join-Path $PSScriptRoot 'res\*') -Destination (Join-Path $PSScriptRoot 'bin') -Recurse -Force
}

# Create JAR file
Write-Host "[package] Creating JAR file..."
$metaDest = "bin/META-INF"
if (-not (Test-Path $metaDest)) { New-Item -ItemType Directory -Path $metaDest -Force | Out-Null }
Copy-Item -Path src/META-INF/* -Destination $metaDest -Force
$manifest = "bin/META-INF/MANIFEST.MF"
$jarPath = "dist/SmallTextPad.jar"
jar cfm $jarPath $manifest -C bin .

Write-Host "[package] Packaging complete. JAR created at $jarPath"