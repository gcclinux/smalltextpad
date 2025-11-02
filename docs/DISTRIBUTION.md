# SmallTextPad Distribution Guide

The build script creates multiple distribution formats for different use cases:

## 📦 Distribution Files

### 1. ZIP Archive (Recommended) - `SmallTextPad-1.5.1-macOS-arm64-standalone.zip` (32MB)
**Best for**: Direct sharing, GitHub releases, website downloads

**How users install**:
1. Download and double-click the ZIP file
2. Drag `SmallTextPad.app` to Applications folder
3. Launch from Applications or Spotlight

**Advantages**:
- Smallest file size
- No mounting required
- Works with all download methods
- Easy to share via email/cloud storage

### 2. DMG Installer - `SmallTextPad-1.5.1-macOS-arm64-bundled.dmg` (34MB)
**Best for**: Professional distribution, Mac App Store style experience

**How users install**:
1. Download and double-click the DMG file
2. Drag `SmallTextPad.app` to Applications folder shortcut
3. Eject the DMG
4. Launch from Applications

**Advantages**:
- Professional installer experience
- Applications folder shortcut included
- Traditional Mac distribution method

### 3. TAR.GZ Archive - `SmallTextPad-1.5.1-macOS-arm64-standalone.tar.gz` (33MB)
**Best for**: Command-line distribution, package managers

**How users install**:
```bash
tar -xzf SmallTextPad-1.5.1-macOS-arm64-standalone.tar.gz
mv SmallTextPad.app /Applications/
```

## 🔒 Code Signing (Optional)

For enhanced security and to avoid "unidentified developer" warnings:

```bash
# Set your Developer ID before building
export CODESIGN_IDENTITY="Developer ID Application: Your Name (TEAM_ID)"
./packaging/build-macos-dmg.sh
```

## ✅ What's Included

All distribution formats contain:
- **Complete SmallTextPad application**
- **Bundled Java Runtime Environment** (no Java installation required)
- **All resources and dependencies**
- **Native macOS launcher**
- **File associations for .txt files**

## 🚀 Distribution Recommendations

### For GitHub Releases
Upload the ZIP file - it's the most user-friendly format

### For Website Downloads
Offer both ZIP (for simplicity) and DMG (for traditional Mac experience)

### For Enterprise Distribution
Use the DMG format with code signing enabled

### For Package Managers (Homebrew, etc.)
Use the TAR.GZ format

## 📋 File Sizes
- App Bundle: 48MB (uncompressed)
- ZIP Archive: 32MB
- TAR.GZ Archive: 33MB  
- DMG Installer: 34MB

All formats are self-contained and require no additional software installation.