# macOS DMG Packaging for SmallTextPad

This directory contains the script to build a native macOS DMG package for SmallTextPad.

## Prerequisites

- Java 21+ installed (OpenJDK or Temurin)
- macOS with Xcode Command Line Tools
- Optional: ImageMagick for icon conversion (`brew install imagemagick`)

## Building the DMG

Run the build script from the project root or packaging directory:

```bash
./packaging/build-macos-dmg.sh
```

## What the script does

1. **Compiles the Java application** - Builds all source files and creates a JAR
2. **Creates minimal JRE** - Uses `jlink` to create a custom, minimal Java runtime (~46MB)
3. **Creates macOS app bundle** - Generates `SmallTextPad.app` with proper structure
4. **Bundles Java runtime** - Embeds the minimal JRE so users don't need Java installed
5. **Generates Info.plist** - Configures app metadata and file associations
6. **Creates launcher script** - Native macOS launcher that uses bundled Java
7. **Converts icon** - Converts ICO to ICNS format (if ImageMagick available)
8. **Packages DMG** - Creates distributable DMG with Applications symlink

## Output

The build creates:
- `build/macos/SmallTextPad.app` - Native macOS application bundle with embedded JRE
- `build/macos/SmallTextPad-1.5.1-macOS-arm64-bundled.dmg` - Self-contained distributable DMG file (~34MB)

## Installation

1. Open the generated DMG file
2. Drag `SmallTextPad.app` to the Applications folder
3. Launch from Applications, Launchpad, or Spotlight

## Features

- **Self-contained** - No Java installation required by end users
- **Minimal JRE** - Custom runtime with only necessary modules (~46MB)
- **Native macOS app bundle** structure
- **Proper file associations** for .txt files
- **High-resolution display** support
- **Applications folder symlink** for easy installation
- **Fallback Java detection** - Uses system Java if bundled JRE fails

## Customization

Edit the script variables at the top to customize:
- `APP_VERSION` - Application version number
- `BUNDLE_ID` - macOS bundle identifier
- Icon and metadata in Info.plist