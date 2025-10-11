# Create a Windows installer EXE using jpackage
# Usage: .\jpackage.ps1
# Requires a JDK 14+ with jpackage on PATH
# Requires choco install wixtoolset -y

$ErrorActionPreference = 'Stop'
Set-Location $PSScriptRoot

# Ensure jpackage exists
$jp = Get-Command jpackage -ErrorAction SilentlyContinue
if (-not $jp) { Write-Error 'jpackage not found on PATH. Please install a JDK with jpackage (JDK 14+).' ; exit 1 }

# Build the jar first
Write-Host '[jpackage] Building jar using package.ps1'
try { & "$PSScriptRoot\..\package.ps1" } catch { Write-Error "package.ps1 failed: $_"; exit 1 }

# Read metadata from Details.java
$detailsFile = Join-Path $PSScriptRoot '..\src\wagemaker\co\uk\utility\Details.java'
if (-not (Test-Path $detailsFile)) { Write-Error 'Details.java not found'; exit 1 }
$content = Get-Content $detailsFile -Raw
$version = ([regex]::Match($content, 'public\s+static\s+String\s+Version\s*=\s*"([^\"]+)"')).Groups[1].Value
$appName = ([regex]::Match($content, 'public\s+static\s+String\s+Title\s*=\s*"([^\"]+)"')).Groups[1].Value
$vendor = ([regex]::Match($content, 'public\s+static\s+String\s+Developer\s*=\s*"([^\"]+)"')).Groups[1].Value
$homepage = ([regex]::Match($content, 'public\s+static\s+String\s+remoteLicense\s*=\s*"([^\"]+)"')).Groups[1].Value

$jarPath = Join-Path $PSScriptRoot '..\dist\SmallTextPad.jar'
if (-not (Test-Path $jarPath)) { Write-Error "Jar not found at $jarPath"; exit 1 }

# Prefer packaging/SmallTextPad.ico
$iconIco = Join-Path $PSScriptRoot 'SmallTextPad.ico'
if (Test-Path $iconIco) { Write-Host "[jpackage] Using icon: $iconIco" } else { Write-Host '[jpackage] packaging/SmallTextPad.ico not found; continuing without custom icon' ; $iconIco = $null }

# Destination folders
$installerOut = Join-Path $PSScriptRoot '..\dist\installer'
$appImageOut = Join-Path $PSScriptRoot '..\dist\appimage'
if (-not (Test-Path $installerOut)) { New-Item -ItemType Directory -Path $installerOut | Out-Null }
if (-not (Test-Path $appImageOut)) { New-Item -ItemType Directory -Path $appImageOut | Out-Null }

# Detect WiX (needed for exe/msi packaging on Windows)
$wixFound = $false
foreach ($tool in @('candle.exe','light.exe','wix.exe')) {
    if (Get-Command $tool -ErrorAction SilentlyContinue) { $wixFound = $true; break }
}

if ($wixFound) {
    Write-Host '[jpackage] WiX toolset detected — creating Windows installer (exe)'
    $pkgType = 'exe'
} else {
    Write-Host '[jpackage] WiX not detected — will create app-image (no installer). To produce an EXE installer install WiX and re-run this script.'
    $pkgType = 'app-image'
}

# Build jpackage arguments depending on chosen type
$inputDir = (Join-Path $PSScriptRoot '..\dist')
$jpackageCmd = (Get-Command jpackage -ErrorAction SilentlyContinue).Source

try {
    if ($pkgType -eq 'exe') {
        $jpackArgs = @(
            '--type','exe',
            '--input', $inputDir,
            '--name', $appName,
            '--main-jar','SmallTextPad.jar',
            '--main-class','wagemaker.co.uk.main.Launcher',
            '--app-version',$version,
            '--vendor',$vendor,
            '--dest',$installerOut,
            '--win-menu','--win-shortcut'
        )
        if ($iconIco) { $jpackArgs += @('--icon',$iconIco) }
        if ($homepage) { $jpackArgs += @('--copyright',$homepage) }
        Write-Host "[jpackage] Running jpackage (installer)"
        & $jpackageCmd @jpackArgs
        Write-Host '[jpackage] Installer created. Contents of installer folder:'
        Get-ChildItem -Path $installerOut -Recurse | Select-Object FullName
    } else {
        $jpackArgs = @(
            '--type','app-image',
            '--input', $inputDir,
            '--name', $appName,
            '--main-jar','SmallTextPad.jar',
            '--main-class','wagemaker.co.uk.main.Launcher',
            '--app-version',$version,
            '--vendor',$vendor,
            '--dest',$appImageOut
        )
        if ($iconIco) { $jpackArgs += @('--icon',$iconIco) }
        if ($homepage) { $jpackArgs += @('--copyright',$homepage) }
        Write-Host "[jpackage] Running jpackage (app-image)"
        & $jpackageCmd @jpackArgs

        # Zip the produced app-image for distribution
        $producedFolder = Join-Path $appImageOut $appName
        if (Test-Path $producedFolder) {
            $zipPath = Join-Path (Join-Path $PSScriptRoot '..\dist') "$appName-windows-appimage.zip"
            if (Test-Path $zipPath) { Remove-Item $zipPath -Force }
            Write-Host "[jpackage] Zipping app-image to $zipPath"
            Compress-Archive -Path (Join-Path $producedFolder '*') -DestinationPath $zipPath -Force
            Write-Host "[jpackage] App-image zip created: $zipPath"
        } else {
            Write-Warning "Expected app-image folder not found: $producedFolder"
        }
    }
} catch {
    Write-Error "jpackage failed: $_"
    exit 1
}

Write-Host '[jpackage] Packaging finished.'