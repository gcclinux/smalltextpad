#!/bin/bash
# RPM build script for SmallTextPad
# This script prepares the source tarball and builds the RPM package

set -euo pipefail

# Configuration
PACKAGE_NAME="smalltextpad"
VERSION="1.5.0"
SPEC_FILE="smalltextpad.spec"

# Get script directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

echo "Building RPM package for $PACKAGE_NAME version $VERSION"

# Check if we're in the right directory
if [[ ! -f "$PROJECT_ROOT/README.md" ]] || [[ ! -f "$PROJECT_ROOT/src/wagemaker/co/uk/main/Launcher.java" ]]; then
    echo "Error: This script must be run from the packaging directory of the SmallTextPad project"
    exit 1
fi

# Check for required tools
command -v rpmbuild >/dev/null 2>&1 || { 
    echo "Error: rpmbuild not found. Please install rpm-build package:"
    echo "  Fedora/RHEL/CentOS: sudo dnf install rpm-build rpmdevtools"
    echo "  openSUSE: sudo zypper install rpm-build"
    exit 1
}

command -v java >/dev/null 2>&1 || {
    echo "Error: java not found. Please install Java 21+ JDK:"
    echo "  Fedora/RHEL: sudo dnf install java-21-openjdk-devel"
    echo "  openSUSE: sudo zypper install java-21-openjdk-devel"
    exit 1
}

command -v javac >/dev/null 2>&1 || {
    echo "Error: javac not found. Please install Java 21+ JDK (see above)"
    exit 1
}

# Setup RPM build environment
echo "Setting up RPM build environment..."
rpmdev-setuptree 2>/dev/null || mkdir -p ~/rpmbuild/{BUILD,RPMS,SOURCES,SPECS,SRPMS}

# Create source tarball
echo "Creating source tarball..."
cd "$PROJECT_ROOT"
TEMP_DIR=$(mktemp -d)
SOURCE_DIR="$TEMP_DIR/$PACKAGE_NAME-$VERSION"

# Copy source files
mkdir -p "$SOURCE_DIR"
cp -r src dic res packaging snap doc "$SOURCE_DIR/" 2>/dev/null || true
cp README.md LICENSE package.sh run.sh sources.txt "$SOURCE_DIR/" 2>/dev/null || true

# Create MANIFEST.MF if it doesn't exist
if [[ ! -f "$SOURCE_DIR/src/META-INF/MANIFEST.MF" ]]; then
    mkdir -p "$SOURCE_DIR/src/META-INF"
    cat > "$SOURCE_DIR/src/META-INF/MANIFEST.MF" << 'EOF'
Manifest-Version: 1.0
Main-Class: wagemaker.co.uk.main.Launcher
Class-Path: .
EOF
fi

# Create tarball
cd "$TEMP_DIR"
tar czf "$PACKAGE_NAME-$VERSION.tar.gz" "$PACKAGE_NAME-$VERSION"

# Copy to RPM SOURCES
cp "$PACKAGE_NAME-$VERSION.tar.gz" ~/rpmbuild/SOURCES/

# Copy spec file
cp "$SCRIPT_DIR/$SPEC_FILE" ~/rpmbuild/SPECS/

# Clean up temp directory
rm -rf "$TEMP_DIR"

# Build RPM
echo "Building RPM package..."
cd ~/rpmbuild/SPECS
rpmbuild -ba "$SPEC_FILE"

echo ""
echo "Build completed successfully!"
echo "RPM packages are available in:"
echo "  Binary RPM: ~/rpmbuild/RPMS/noarch/$PACKAGE_NAME-$VERSION-1.*.noarch.rpm"
echo "  Source RPM: ~/rpmbuild/SRPMS/$PACKAGE_NAME-$VERSION-1.*.src.rpm"
echo ""
echo "To install the package:"
echo "  sudo rpm -ivh ~/rpmbuild/RPMS/noarch/$PACKAGE_NAME-$VERSION-1.*.noarch.rpm"
echo ""
echo "Or on Fedora/RHEL systems:"
echo "  sudo dnf install ~/rpmbuild/RPMS/noarch/$PACKAGE_NAME-$VERSION-1.*.noarch.rpm"