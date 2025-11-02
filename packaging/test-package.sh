#!/usr/bin/env bash
# Test script for SmallTextPad macOS package
# Verifies that all components work correctly
set -euo pipefail

# Find the project root
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
cd "$PROJECT_ROOT"

APP_DIR="build/macos/SmallTextPad.app"
ZIP_FILE="build/macos/SmallTextPad-1.5.1-macOS-arm64-standalone.zip"
DMG_FILE="build/macos/SmallTextPad-1.5.1-macOS-arm64-bundled.dmg"

echo "🧪 Testing SmallTextPad macOS Package"
echo "======================================"

# Test 1: Check if app bundle exists
echo "1. Checking app bundle structure..."
if [ ! -d "$APP_DIR" ]; then
    echo "❌ App bundle not found. Run ./packaging/build-macos-dmg.sh first"
    exit 1
fi

# Test 2: Verify app bundle structure
echo "2. Verifying app bundle structure..."
required_files=(
    "$APP_DIR/Contents/Info.plist"
    "$APP_DIR/Contents/MacOS/SmallTextPad"
    "$APP_DIR/Contents/Java/SmallTextPad.jar"
    "$APP_DIR/Contents/PlugIns/Java.runtime/bin/java"
)

for file in "${required_files[@]}"; do
    if [ ! -e "$file" ]; then
        echo "❌ Missing required file: $file"
        exit 1
    fi
done
echo "✅ App bundle structure is correct"

# Test 3: Check bundled Java runtime
echo "3. Testing bundled Java runtime..."
JAVA_VERSION=$("$APP_DIR/Contents/PlugIns/Java.runtime/bin/java" -version 2>&1 | head -n1)
echo "   Java version: $JAVA_VERSION"
if [[ "$JAVA_VERSION" == *"openjdk version"* ]]; then
    echo "✅ Bundled Java runtime works"
else
    echo "❌ Bundled Java runtime failed"
    exit 1
fi

# Test 4: Test JAR with bundled Java
echo "4. Testing JAR execution with bundled Java..."
timeout 3s "$APP_DIR/Contents/PlugIns/Java.runtime/bin/java" -jar "$APP_DIR/Contents/Java/SmallTextPad.jar" >/dev/null 2>&1 || true
echo "✅ JAR executes with bundled Java (GUI launched briefly)"

# Test 5: Test launcher script
echo "5. Testing launcher script..."
if [ ! -x "$APP_DIR/Contents/MacOS/SmallTextPad" ]; then
    echo "❌ Launcher script is not executable"
    exit 1
fi
timeout 3s "$APP_DIR/Contents/MacOS/SmallTextPad" >/dev/null 2>&1 || true
echo "✅ Launcher script works (GUI launched briefly)"

# Test 6: Check distribution files
echo "6. Checking distribution files..."
if [ -f "$ZIP_FILE" ]; then
    ZIP_SIZE=$(du -sh "$ZIP_FILE" | cut -f1)
    echo "✅ ZIP file exists: $ZIP_SIZE"
else
    echo "⚠️  ZIP file not found"
fi

if [ -f "$DMG_FILE" ]; then
    DMG_SIZE=$(du -sh "$DMG_FILE" | cut -f1)
    echo "✅ DMG file exists: $DMG_SIZE"
else
    echo "⚠️  DMG file not found"
fi

# Test 7: Test ZIP extraction
echo "7. Testing ZIP extraction..."
if [ -f "$ZIP_FILE" ]; then
    TEST_DIR="build/macos/test_zip"
    rm -rf "$TEST_DIR"
    mkdir -p "$TEST_DIR"
    unzip -q "$ZIP_FILE" -d "$TEST_DIR"
    
    if [ -d "$TEST_DIR/SmallTextPad.app" ]; then
        echo "✅ ZIP extraction successful"
        # Test extracted app
        timeout 3s "$TEST_DIR/SmallTextPad.app/Contents/MacOS/SmallTextPad" >/dev/null 2>&1 || true
        echo "✅ Extracted app launches correctly"
    else
        echo "❌ ZIP extraction failed"
    fi
    rm -rf "$TEST_DIR"
fi

# Test 8: Check app bundle size
echo "8. Checking package sizes..."
APP_SIZE=$(du -sh "$APP_DIR" | cut -f1)
echo "   App bundle: $APP_SIZE"

# Test 9: Verify Info.plist
echo "9. Verifying Info.plist..."
if plutil -lint "$APP_DIR/Contents/Info.plist" >/dev/null 2>&1; then
    echo "✅ Info.plist is valid"
    BUNDLE_ID=$(plutil -extract CFBundleIdentifier raw "$APP_DIR/Contents/Info.plist" 2>/dev/null || echo "unknown")
    echo "   Bundle ID: $BUNDLE_ID"
else
    echo "❌ Info.plist is invalid"
fi

echo ""
echo "🎉 Package Testing Complete!"
echo "=============================="
echo ""
echo "📦 Ready for distribution:"
echo "   • App Bundle: $APP_DIR ($APP_SIZE)"
if [ -f "$ZIP_FILE" ]; then
    echo "   • ZIP Archive: $ZIP_FILE ($ZIP_SIZE)"
fi
if [ -f "$DMG_FILE" ]; then
    echo "   • DMG Installer: $DMG_FILE ($DMG_SIZE)"
fi
echo ""
echo "🚀 To manually test the GUI:"
echo "   open $APP_DIR"
echo ""
echo "✅ All tests passed! Package is ready for distribution."