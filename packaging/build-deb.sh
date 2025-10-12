#!/usr/bin/env bash

# Change to project root
cd "$(dirname "$0")/.."

# Build JAR if it doesn't exist
JAR_FILE="classes/artifacts/SmallTextPad_jar/SmallTextPad.jar"
if [ ! -f "$JAR_FILE" ]; then
    echo "JAR file not found. Building from source..."
    javac -d bin -Xlint:deprecation @sources.txt
    mkdir -p classes/artifacts/SmallTextPad_jar
    jar cfm "$JAR_FILE" src/META-INF/MANIFEST.MF -C bin . -C . res -C . dic
    echo "JAR file created: $JAR_FILE"
fi

# SmallTextPad DEB Package Builder
APP_NAME="smalltextpad"
VERSION="1.5.1"
ARCH="amd64"
MAINTAINER="Ricardo Wagemaker <wagemra@gmail.com>"
DESCRIPTION="SmallTextPad - Simple Java Text Editor with Encryption"
HOMEPAGE="https://gcclinux.github.io/smalltextpad/"

# Create package structure
DEB_DIR="${APP_NAME}_${VERSION}_${ARCH}"
mkdir -p "$DEB_DIR/DEBIAN"
mkdir -p "$DEB_DIR/usr/share/applications"
mkdir -p "$DEB_DIR/usr/share/pixmaps"
mkdir -p "$DEB_DIR/usr/share/${APP_NAME}"
mkdir -p "$DEB_DIR/usr/bin"

# Create control file
cat > "$DEB_DIR/DEBIAN/control" << EOF
Package: $APP_NAME
Version: $VERSION
Section: editors
Priority: optional
Architecture: $ARCH
Depends: openjdk-21-jre
Maintainer: $MAINTAINER
Description: $DESCRIPTION
 SmallTextPad is a lightweight Java text editor with optional file encryption
 and multi-language support. Features include basic editing, undo/redo,
 printing, and encrypted file format (.sstp).
Homepage: $HOMEPAGE
EOF

# Copy application files
cp classes/artifacts/SmallTextPad_jar/SmallTextPad.jar "$DEB_DIR/usr/share/${APP_NAME}/"
cp -r res "$DEB_DIR/usr/share/${APP_NAME}/"
cp -r dic "$DEB_DIR/usr/share/${APP_NAME}/"

# Copy icon
cp res/smalltextpad_128x128.png "$DEB_DIR/usr/share/pixmaps/${APP_NAME}.png"

# Create launcher script
cat > "$DEB_DIR/usr/bin/${APP_NAME}" << EOF
#!/bin/bash
cd /usr/share/${APP_NAME}
exec java -jar SmallTextPad.jar "\$@"
EOF
chmod +x "$DEB_DIR/usr/bin/${APP_NAME}"

# Create desktop file
cat > "$DEB_DIR/usr/share/applications/${APP_NAME}.desktop" << EOF
[Desktop Entry]
Name=SmallTextPad
Comment=Simple Java Text Editor with Encryption
Exec=${APP_NAME} %F
Icon=${APP_NAME}
Terminal=false
Type=Application
Categories=Office;TextEditor;
MimeType=text/plain;text/x-java;text/x-c;text/x-c++;text/x-python;text/x-sh;
StartupNotify=true
StartupWMClass=SmallTextPad
EOF

# Build package
dpkg-deb --build "$DEB_DIR" "classes/artifacts/${DEB_DIR}.deb"
echo "DEB package created: classes/artifacts/${DEB_DIR}.deb"

# Cleanup
rm -rf "$DEB_DIR"