#!/usr/bin/env bash
# macOS DMG packaging script for SmallTextPad
# Creates a native macOS app bundle and packages it into a DMG
# Exits on any error
set -euo pipefail

# Find the project root (script lives in packaging/ subdirectory)
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
cd "$PROJECT_ROOT"

echo "[build-macos-dmg] Working from project root: $PROJECT_ROOT"

# Configuration
APP_NAME="SmallTextPad"
APP_VERSION="1.5.1"
BUNDLE_ID="wagemaker.co.uk.smalltextpad"
DMG_NAME="${APP_NAME}-${APP_VERSION}-macOS-arm64-bundled"
BUILD_DIR="build/macos"
APP_DIR="${BUILD_DIR}/${APP_NAME}.app"
JRE_DIR="${APP_DIR}/Contents/PlugIns/Java.runtime"

# Ensure required tools exist
command -v javac >/dev/null 2>&1 || { echo "javac not found in PATH" >&2; exit 1; }
command -v jar >/dev/null 2>&1 || { echo "jar command not found in PATH" >&2; exit 1; }
command -v hdiutil >/dev/null 2>&1 || { echo "hdiutil not found in PATH" >&2; exit 1; }

echo "[build-macos-dmg] Cleaning previous build..."
rm -rf "$BUILD_DIR"
mkdir -p "$BUILD_DIR"

echo "[build-macos-dmg] Building JAR file..."
# Clean and compile
mkdir -p bin
find bin/ -name "*.class" -delete 2>/dev/null || true
find src/ -name "*.java" > sources.txt
javac -d bin -Xlint:deprecation @sources.txt

# Copy resources
mkdir -p bin/wagemaker/co/uk/lang
cp src/wagemaker/co/uk/lang/*.properties bin/wagemaker/co/uk/lang/
cp -r res/* bin/
cp -r dic bin/

# Create JAR
mkdir -p classes/artifacts
jar cfm classes/artifacts/SmallTextPad.jar src/META-INF/MANIFEST.MF -C bin .

echo "[build-macos-dmg] Creating macOS app bundle structure..."
# Create app bundle structure
mkdir -p "${APP_DIR}/Contents/MacOS"
mkdir -p "${APP_DIR}/Contents/Resources"
mkdir -p "${APP_DIR}/Contents/Java"
mkdir -p "${APP_DIR}/Contents/PlugIns"

# Copy JAR to app bundle
cp classes/artifacts/SmallTextPad.jar "${APP_DIR}/Contents/Java/"

echo "[build-macos-dmg] Bundling Java Runtime Environment..."
# Find current Java installation
JAVA_HOME_PATH=""
if [ -n "${JAVA_HOME:-}" ] && [ -d "$JAVA_HOME" ]; then
    JAVA_HOME_PATH="$JAVA_HOME"
elif [ -d "/opt/homebrew/opt/openjdk@21" ]; then
    JAVA_HOME_PATH="/opt/homebrew/opt/openjdk@21"
elif [ -d "/usr/local/opt/openjdk@21" ]; then
    JAVA_HOME_PATH="/usr/local/opt/openjdk@21"
else
    # Try to find any Java installation
    JAVA_BIN=$(which java 2>/dev/null || true)
    if [ -n "$JAVA_BIN" ]; then
        # Follow symlinks to find actual Java home
        REAL_JAVA=$(readlink -f "$JAVA_BIN" 2>/dev/null || realpath "$JAVA_BIN" 2>/dev/null || echo "$JAVA_BIN")
        JAVA_HOME_PATH=$(dirname "$(dirname "$REAL_JAVA")")
    fi
fi

if [ -z "$JAVA_HOME_PATH" ] || [ ! -d "$JAVA_HOME_PATH" ]; then
    echo "[build-macos-dmg] Error: Could not find Java installation"
    echo "[build-macos-dmg] Please ensure Java is installed and JAVA_HOME is set"
    exit 1
fi

echo "[build-macos-dmg] Found Java at: $JAVA_HOME_PATH"

# Create custom JRE with only necessary modules
echo "[build-macos-dmg] Creating minimal JRE..."
"$JAVA_HOME_PATH/bin/jlink" \
    --add-modules java.base,java.desktop,java.logging,java.management,java.naming,java.security.jgss,java.xml \
    --strip-debug \
    --no-man-pages \
    --no-header-files \
    --compress=2 \
    --output "$JRE_DIR"

echo "[build-macos-dmg] JRE bundled successfully ($(du -sh "$JRE_DIR" | cut -f1))"

# Optional code signing (if developer certificate is available)
if [ -n "${CODESIGN_IDENTITY:-}" ]; then
    echo "[build-macos-dmg] Code signing with identity: $CODESIGN_IDENTITY"
    # Sign the JRE first
    find "$JRE_DIR" -name "*.dylib" -exec codesign --force --sign "$CODESIGN_IDENTITY" {} \;
    codesign --force --sign "$CODESIGN_IDENTITY" "$JRE_DIR/bin/java"
    
    # Sign the main app
    codesign --force --sign "$CODESIGN_IDENTITY" --entitlements <(echo '<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
    <key>com.apple.security.cs.allow-jit</key>
    <true/>
    <key>com.apple.security.cs.allow-unsigned-executable-memory</key>
    <true/>
    <key>com.apple.security.cs.disable-library-validation</key>
    <true/>
</dict>
</plist>') "$APP_DIR"
    
    echo "[build-macos-dmg] Code signing completed"
else
    echo "[build-macos-dmg] Skipping code signing (set CODESIGN_IDENTITY to enable)"
fi

echo "[build-macos-dmg] Creating Info.plist..."
# Create Info.plist
cat > "${APP_DIR}/Contents/Info.plist" << EOF
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
    <key>CFBundleDevelopmentRegion</key>
    <string>en</string>
    <key>CFBundleExecutable</key>
    <string>SmallTextPad</string>
    <key>CFBundleIconFile</key>
    <string>SmallTextPad.icns</string>
    <key>CFBundleIdentifier</key>
    <string>${BUNDLE_ID}</string>
    <key>CFBundleInfoDictionaryVersion</key>
    <string>6.0</string>
    <key>CFBundleName</key>
    <string>${APP_NAME}</string>
    <key>CFBundlePackageType</key>
    <string>APPL</string>
    <key>CFBundleShortVersionString</key>
    <string>${APP_VERSION}</string>
    <key>CFBundleVersion</key>
    <string>${APP_VERSION}</string>
    <key>LSMinimumSystemVersion</key>
    <string>10.15</string>
    <key>NSHighResolutionCapable</key>
    <true/>
    <key>NSSupportsAutomaticGraphicsSwitching</key>
    <true/>
    <key>JVMRuntime</key>
    <string>Java.runtime</string>
    <key>JVMMainClassName</key>
    <string>wagemaker.co.uk.main.Launcher</string>
    <key>CFBundleDocumentTypes</key>
    <array>
        <dict>
            <key>CFBundleTypeExtensions</key>
            <array>
                <string>txt</string>
                <string>text</string>
            </array>
            <key>CFBundleTypeName</key>
            <string>Text Document</string>
            <key>CFBundleTypeRole</key>
            <string>Editor</string>
            <key>LSHandlerRank</key>
            <string>Alternate</string>
        </dict>
    </array>
</dict>
</plist>
EOF

echo "[build-macos-dmg] Creating launcher script..."
# Create launcher script
cat > "${APP_DIR}/Contents/MacOS/SmallTextPad" << 'EOF'
#!/bin/bash
# macOS launcher script for SmallTextPad with bundled JRE

# Get the directory where this script is located (MacOS folder)
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
# Go up one level to Contents folder
CONTENTS_DIR="$(dirname "$SCRIPT_DIR")"

# Set up paths
JAVA_JAR="$CONTENTS_DIR/Java/SmallTextPad.jar"
BUNDLED_JAVA="$CONTENTS_DIR/PlugIns/Java.runtime/bin/java"

# Debug: Print paths for troubleshooting
# echo "CONTENTS_DIR: $CONTENTS_DIR"
# echo "JAVA_JAR: $JAVA_JAR"
# echo "BUNDLED_JAVA: $BUNDLED_JAVA"

# Use bundled Java if available, otherwise fall back to system Java
if [ -x "$BUNDLED_JAVA" ]; then
    JAVA_CMD="$BUNDLED_JAVA"
elif command -v java >/dev/null 2>&1; then
    JAVA_CMD="java"
else
    osascript -e 'display dialog "Java Runtime Environment not found. Please contact the application developer." buttons {"OK"} default button "OK"'
    exit 1
fi

# Launch the application
exec "$JAVA_CMD" -jar "$JAVA_JAR" "$@"
EOF

# Make launcher executable
chmod +x "${APP_DIR}/Contents/MacOS/SmallTextPad"

echo "[build-macos-dmg] Creating app icon..."
# Create a simple icon (you can replace this with a proper .icns file)
if [ -f "packaging/SmallTextPad.ico" ]; then
    # If you have ImageMagick installed, convert ICO to PNG then to ICNS
    if command -v convert >/dev/null 2>&1 && command -v iconutil >/dev/null 2>&1; then
        mkdir -p "${BUILD_DIR}/SmallTextPad.iconset"
        convert "packaging/SmallTextPad.ico" -resize 16x16 "${BUILD_DIR}/SmallTextPad.iconset/icon_16x16.png"
        convert "packaging/SmallTextPad.ico" -resize 32x32 "${BUILD_DIR}/SmallTextPad.iconset/icon_16x16@2x.png"
        convert "packaging/SmallTextPad.ico" -resize 32x32 "${BUILD_DIR}/SmallTextPad.iconset/icon_32x32.png"
        convert "packaging/SmallTextPad.ico" -resize 64x64 "${BUILD_DIR}/SmallTextPad.iconset/icon_32x32@2x.png"
        convert "packaging/SmallTextPad.ico" -resize 128x128 "${BUILD_DIR}/SmallTextPad.iconset/icon_128x128.png"
        convert "packaging/SmallTextPad.ico" -resize 256x256 "${BUILD_DIR}/SmallTextPad.iconset/icon_128x128@2x.png"
        convert "packaging/SmallTextPad.ico" -resize 256x256 "${BUILD_DIR}/SmallTextPad.iconset/icon_256x256.png"
        convert "packaging/SmallTextPad.ico" -resize 512x512 "${BUILD_DIR}/SmallTextPad.iconset/icon_256x256@2x.png"
        convert "packaging/SmallTextPad.ico" -resize 512x512 "${BUILD_DIR}/SmallTextPad.iconset/icon_512x512.png"
        convert "packaging/SmallTextPad.ico" -resize 1024x1024 "${BUILD_DIR}/SmallTextPad.iconset/icon_512x512@2x.png"
        iconutil -c icns "${BUILD_DIR}/SmallTextPad.iconset" -o "${APP_DIR}/Contents/Resources/SmallTextPad.icns"
        rm -rf "${BUILD_DIR}/SmallTextPad.iconset"
    else
        echo "[build-macos-dmg] Warning: ImageMagick or iconutil not found. Skipping icon conversion."
        echo "[build-macos-dmg] Install ImageMagick with: brew install imagemagick"
    fi
fi

echo "[build-macos-dmg] Creating standalone app archive..."
# Create a compressed standalone app for direct distribution
STANDALONE_APP_PATH="${BUILD_DIR}/${APP_NAME}-${APP_VERSION}-macOS-arm64-standalone.tar.gz"
tar -czf "$STANDALONE_APP_PATH" -C "$BUILD_DIR" "${APP_NAME}.app"

echo "[build-macos-dmg] Creating ZIP archive for easy distribution..."
# Create ZIP version (more common for Mac app distribution)
ZIP_PATH="${BUILD_DIR}/${APP_NAME}-${APP_VERSION}-macOS-arm64-standalone.zip"
(cd "$BUILD_DIR" && zip -r -q "$(basename "$ZIP_PATH")" "${APP_NAME}.app")

echo "[build-macos-dmg] Creating DMG..."
# Create temporary DMG directory
DMG_TEMP_DIR="${BUILD_DIR}/dmg_temp"
mkdir -p "$DMG_TEMP_DIR"

# Copy app to DMG temp directory
cp -R "$APP_DIR" "$DMG_TEMP_DIR/"

# Create Applications symlink for easy installation
ln -s /Applications "$DMG_TEMP_DIR/Applications"

# Create DMG
DMG_PATH="${BUILD_DIR}/${DMG_NAME}.dmg"
hdiutil create -volname "$APP_NAME" -srcfolder "$DMG_TEMP_DIR" -ov -format UDZO "$DMG_PATH"

# Clean up temp directory
rm -rf "$DMG_TEMP_DIR"

echo "[build-macos-dmg] ✅ Build complete!"
echo "[build-macos-dmg] "
echo "[build-macos-dmg] 📦 Distribution files created:"
echo "[build-macos-dmg] "
echo "[build-macos-dmg] 1. Complete App Bundle:"
echo "[build-macos-dmg]    $APP_DIR ($(du -sh "$APP_DIR" | cut -f1))"
echo "[build-macos-dmg] "
echo "[build-macos-dmg] 2. ZIP Archive (recommended for direct distribution):"
echo "[build-macos-dmg]    $ZIP_PATH ($(du -sh "$ZIP_PATH" | cut -f1))"
echo "[build-macos-dmg] "
echo "[build-macos-dmg] 3. TAR.GZ Archive:"
echo "[build-macos-dmg]    $STANDALONE_APP_PATH ($(du -sh "$STANDALONE_APP_PATH" | cut -f1))"
echo "[build-macos-dmg] "
echo "[build-macos-dmg] 4. DMG Installer:"
echo "[build-macos-dmg]    $DMG_PATH ($(du -sh "$DMG_PATH" | cut -f1))"
echo "[build-macos-dmg] "
echo "[build-macos-dmg] 🚀 Distribution options:"
echo "[build-macos-dmg] "
echo "[build-macos-dmg] • For direct app sharing: Use the ZIP file"
echo "[build-macos-dmg]   Users extract and drag SmallTextPad.app to Applications"
echo "[build-macos-dmg] "
echo "[build-macos-dmg] • For installer experience: Use the DMG file"
echo "[build-macos-dmg]   Users mount DMG and drag app to Applications folder"
echo "[build-macos-dmg] "
echo "[build-macos-dmg] • For command-line distribution: Use the TAR.GZ file"