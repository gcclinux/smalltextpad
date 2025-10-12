#!/usr/bin/env bash
# Flatpak build script for SmallTextPad
# This script builds the Flatpak package using flatpak-builder

set -euo pipefail

# Configuration
APP_ID="io.github.gcclinux.smalltextpad"
MANIFEST_FILE="$APP_ID.yml"

# Get script directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

echo "Building Flatpak package for $APP_ID"

# Check if we're in the right directory
if [[ ! -f "$PROJECT_ROOT/README.md" ]] || [[ ! -f "$PROJECT_ROOT/src/wagemaker/co/uk/main/Launcher.java" ]]; then
    echo "Error: This script must be run from the packaging directory of the SmallTextPad project"
    exit 1
fi

# Check for required tools
command -v flatpak-builder >/dev/null 2>&1 || { 
    echo "Error: flatpak-builder not found. Please install flatpak-builder:"
    echo "  Fedora/RHEL: sudo dnf install flatpak-builder"
    echo "  Ubuntu/Debian: sudo apt install flatpak-builder"
    echo "  openSUSE: sudo zypper install flatpak-builder"
    exit 1
}

command -v flatpak >/dev/null 2>&1 || {
    echo "Error: flatpak not found. Please install flatpak first"
    exit 1
}

# Check if manifest exists
if [[ ! -f "$PROJECT_ROOT/$MANIFEST_FILE" ]]; then
    echo "Error: Manifest file $MANIFEST_FILE not found in project root"
    echo "Make sure the manifest file exists: $PROJECT_ROOT/$MANIFEST_FILE"
    exit 1
fi

# Check if required runtime is installed
echo "Checking if required runtime is installed..."
if ! flatpak list --runtime | grep -q "org.freedesktop.Platform.*23.08"; then
    echo "Installing required runtime org.freedesktop.Platform//23.08..."
    flatpak install -y flathub org.freedesktop.Platform//23.08
fi

if ! flatpak list --runtime | grep -q "org.freedesktop.Sdk.*23.08"; then
    echo "Installing required SDK org.freedesktop.Sdk//23.08..."
    flatpak install -y flathub org.freedesktop.Sdk//23.08
fi

if ! flatpak list --runtime | grep -q "org.freedesktop.Sdk.Extension.openjdk21"; then
    echo "Installing OpenJDK 21 extension..."
    flatpak install -y flathub org.freedesktop.Sdk.Extension.openjdk21//23.08
fi

# Create build directory
BUILD_DIR="$PROJECT_ROOT/flatpak-build"
REPO_DIR="$PROJECT_ROOT/flatpak-repo"

echo "Creating build directories..."
mkdir -p "$BUILD_DIR"
mkdir -p "$REPO_DIR"

# Build the Flatpak
echo "Building Flatpak package..."
cd "$PROJECT_ROOT"

flatpak-builder --force-clean --repo="$REPO_DIR" "$BUILD_DIR" "$MANIFEST_FILE"

# Create bundle
echo "Creating Flatpak bundle..."
mkdir -p classes/artifacts
flatpak build-bundle "$REPO_DIR" "classes/artifacts/$APP_ID.flatpak" "$APP_ID"

echo ""
echo "Build completed successfully!"
echo "Flatpak bundle is available at:"
echo "  classes/artifacts/$APP_ID.flatpak"
echo ""
echo "To install the Flatpak:"
echo "  flatpak install --user classes/artifacts/$APP_ID.flatpak"
echo ""
echo "To run the application:"
echo "  flatpak run $APP_ID"
echo ""
echo "To test locally without installing:"
echo "  flatpak-builder --run $BUILD_DIR $MANIFEST_FILE smalltextpad"