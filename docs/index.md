---
layout: default
title: SmallTextPad
---

# SmallTextPad

A lightweight Java text editor with encryption and multi-language support.

## Features

- Simple text editing with undo/redo
- File encryption (.sstp format)
- Multi-language support (English, Dutch, Polish, Portuguese)
- Cross-platform (Java 21+)
- Print support
- File history tracking

## Download

[Latest Release v1.5.0](https://github.com/gcclinux/smalltextpad/releases/latest)

### Available Packages

| Package | Platform | Size | SHA256 |
|---------|----------|------|--------|
| [SmallTextPad.jar](https://github.com/gcclinux/smalltextpad/releases/latest/download/SmallTextPad.jar) | All (Java 21+) | 2.97 MB | `4649f0ea...` |
| [smalltextpad_1.5.0_amd64.snap](https://github.com/gcclinux/smalltextpad/releases/latest/download/smalltextpad_1.5.0_amd64.snap) | Linux (amd64) | 122 MB | `35e876ff...` |
| [smalltextpad_1.5.0_arm64.snap](https://github.com/gcclinux/smalltextpad/releases/latest/download/smalltextpad_1.5.0_arm64.snap) | Linux (arm64) | 130 MB | `d7727f78...` |
| [smalltextpad_1.5.0_amd64.deb](https://github.com/gcclinux/smalltextpad/releases/latest/download/smalltextpad_1.5.0_amd64.deb) | Debian/Ubuntu | 2.92 MB | `43beb170...` |
| [smalltextpad-1.5.0-2.fc42.noarch.rpm](https://github.com/gcclinux/smalltextpad/releases/latest/download/smalltextpad-1.5.0-2.fc42.noarch.rpm) | Fedora/RHEL | 2.93 MB | `7ae8e8f1...` |
| [SmallTextPadSetup.exe](https://github.com/gcclinux/smalltextpad/releases/latest/download/SmallTextPadSetup.exe) | Windows | 34.9 MB | `38955432...` |
| [SmallTextPad-windows-appimage.zip](https://github.com/gcclinux/smalltextpad/releases/latest/download/SmallTextPad-windows-appimage.zip) | Windows (portable) | 47 MB | `da05350f...` |

## Installation

### Requirements
- Java 21 or higher

### Snap (Linux)
```bash
sudo snap install smalltextpad
```

### DEB (Debian/Ubuntu)
```bash
sudo dpkg -i smalltextpad_1.5.0_amd64.deb
```

### RPM (Fedora/RHEL)
```bash
sudo rpm -ivh smalltextpad-1.5.0-1.noarch.rpm
```

### JAR (All platforms)
```bash
java -jar SmallTextPad.jar
```

## Build Documentation

Want to build SmallTextPad from source or create packages for distribution? Check out our comprehensive build guides:

### 📦 Package Build Guides

| Format | Platform | Documentation |
|--------|----------|---------------|
| **JAR** | All Platforms | [📄 JAR Build Guide](README-jar.html) |
| **Snap** | Linux Universal | [📄 Snap Build Guide](README-snap.html) |
| **Flatpak** | Linux Universal | [📄 Flatpak Build Guide](README-flatpak.html) |
| **RPM** | Fedora/RHEL/openSUSE | [📄 RPM Build Guide](README-rpm.html) |
| **Windows** | Windows | [📄 Windows Build Guide](README-windows.html) |

Each guide includes:
- Prerequisites and dependencies
- Step-by-step build instructions
- Installation and testing procedures
- Troubleshooting tips
- Distribution information

## Screenshots

![Language Selector](https://github.com/gcclinux/smalltextpad/blob/main/screenshots/languages.png?raw=true)
![Encrypted File](https://github.com/gcclinux/smalltextpad/blob/main/screenshots/encrypted.png?raw=true)
![History Picker](https://github.com/gcclinux/smalltextpad/blob/main/screenshots/history-picker.png?raw=true)

## Support

[![Discussions](https://img.shields.io/badge/💬_Join_Discussions-GitHub-blue?style=for-the-badge)](https://github.com/gcclinux/smalltextpad/discussions) 
[![Issues](https://img.shields.io/badge/🐛_Report_Issues-GitHub-red?style=for-the-badge)](https://github.com/gcclinux/smalltextpad/issues)   

[![Sponsor](https://img.shields.io/badge/💖_Sponsor-GitHub-pink?style=for-the-badge)](https://github.com/sponsors/gcclinux) 
[![Buy Me A Coffee](https://img.shields.io/badge/☕_Buy_Me_A_Coffee-Support-yellow?style=for-the-badge)](https://www.buymeacoffee.com/gcclinux) 

## License

MIT License - See [LICENSE](https://github.com/gcclinux/smalltextpad/blob/main/LICENSE)