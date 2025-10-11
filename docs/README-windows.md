---
layout: default
title: Windows Build Guide - SmallTextPad
---

[← Back to Home](index.html)

# Windows Packaging for SmallTextPad

This guide covers building Windows installers for SmallTextPad.

The `packaging/build-exe.ps1` script will produce either:

- a Windows installer (EXE) if the WiX toolset is installed on the system, or
- an app-image ZIP (no installer) if WiX is not available.

Requirements (to build an EXE installer locally):
- JDK 21+ with `build-exe` on PATH
- WiX toolset (install via Chocolatey: `choco install wixtoolset -y` or download from https://wixtoolset.org)
- PowerShell (run as Administrator for WiX installation)

Build locally:

1. From repo root run (PowerShell):

   .\packaging\build-exe.ps1

2. If WiX is installed the installer will be in `dist/installer/`.
   Otherwise an app-image ZIP will be created at `dist/SmallTextPad-windows-appimage.zip`.

## Continuous Integration

A GitHub Actions workflow `.github/workflows/windows-installer.yml` is provided to build the installer on a Windows runner and upload the produced artifacts automatically.

## See Also

- [JAR Build Guide](README-jar.html) - Building standalone JAR files
- [RPM Build Guide](README-rpm.html) - Building RPM packages for Fedora/RHEL
- [Snap Build Guide](README-snap.html) - Building Snap packages for universal Linux distribution
- [Back to Home](index.html) - Main documentation and downloads
