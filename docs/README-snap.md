---
layout: default
title: Snap Build Guide - SmallTextPad
---

[← Back to Home](index.html)

# Snap Packaging for SmallTextPad

This guide covers building Snap packages for SmallTextPad, a lightweight Java text editor with encryption and multi-language support.

## Files

- `../snap/snapcraft.yaml` - Snap package definition and configuration
- `../snap/gui/smalltextpad.desktop` - Desktop integration file
- `build-snap.sh` - Helper script to build the Snap package
- `README-snap.md` - This documentation file

## Prerequisites

### System Requirements
- Linux system (Ubuntu, Fedora, Debian, etc.)
- Snapcraft tool installed
- Java 21+ JDK for building the JAR
- Snapd for testing (optional)

### Installing Build Dependencies

**Ubuntu/Debian:**
```bash
sudo apt update
sudo apt install snapcraft openjdk-21-jdk
```

**Fedora/RHEL:**
```bash
sudo dnf install snapd openjdk-21-jdk
sudo ln -s /var/lib/snapd/snap /snap  # Enable classic snap support
sudo systemctl enable --now snapd.socket
# Install snapcraft
sudo snap install snapcraft --classic
```

**Other Distributions:**
```bash
# Install snapd first (varies by distribution)
# Then install snapcraft
sudo snap install snapcraft --classic
```

## Building the Snap Package

### Standard Build:
```bash
cd packaging
./build-snap.sh
```

### Build on Systems Without LXD (e.g., Raspberry Pi):
```bash
snapcraft pack --destructive-mode
```

The script will:
1. Clean previous build artifacts
2. Rebuild the source list
3. Compile Java sources
4. Create the JAR file
5. Package everything as a Snap with `snapcraft pack`

## Package Details

### Package Information
- **Name:** smalltextpad
- **Version:** 1.5.0
- **Base:** core22 (Ubuntu 22.04 LTS)
- **Architecture:** Currently configured for arm64, but can be built for other architectures
- **Confinement:** strict (sandboxed for security)
- **Grade:** stable

### Snap Configuration
The snap includes:
- **Java Runtime:** OpenJDK 17 JRE (bundled)
- **Application JAR:** Pre-compiled SmallTextPad.jar
- **Resources:** Images, dictionaries, and language files
- **Desktop Integration:** Application menu entry and icon

### Permissions (Plugs)
The snap requests the following permissions:
- `home` - Access to user's home directory
- `desktop` - Desktop environment integration
- `desktop-legacy` - Legacy desktop support
- `wayland` - Wayland display server
- `x11` - X11 display server
- `unity7` - Unity desktop support
- `opengl` - OpenGL graphics
- `removable-media` - Access to USB drives and external storage

## Build Output

After successful build:
- **Snap package:** `classes/artifacts/smalltextpad_1.5.0_amd64.snap` (or arm64)
- **JAR file:** `classes/artifacts/SmallTextPad.jar` (intermediate build artifact)

## Installing the Snap

### Install Locally (Development):
```bash
sudo snap install --dangerous classes/artifacts/smalltextpad_1.5.0_*.snap
```

The `--dangerous` flag is required for local snaps that aren't signed by the Snap Store.

### Install from Snap Store (When Published):
```bash
sudo snap install smalltextpad
```

## Using the Application

Once installed, you can:

1. **Launch from application menu:** Look for "SmallTextPad" in your applications
2. **Launch from command line:** `smalltextpad`
3. **Open files:** `smalltextpad /path/to/file.txt`

## Snap-Specific Features

### Confinement
The snap runs in strict confinement mode, which means:
- It has limited access to system resources
- File access is restricted to home directory and removable media
- More secure than classic confinement

### Auto-Updates
When installed from the Snap Store, the application will:
- Update automatically in the background
- Always run the latest stable version
- Preserve user data during updates

### Snap Commands

**Check snap info:**
```bash
snap info smalltextpad
```

**View snap connections:**
```bash
snap connections smalltextpad
```

**Remove the snap:**
```bash
sudo snap remove smalltextpad
```

**Connect additional interfaces (if needed):**
```bash
sudo snap connect smalltextpad:removable-media
```

## Publishing to Snap Store

To publish SmallTextPad to the Snap Store:

1. **Register the snap name:**
   ```bash
   snapcraft register smalltextpad
   ```

2. **Build and upload:**
   ```bash
   snapcraft
   snapcraft upload --release=stable smalltextpad_1.5.0_amd64.snap
   ```

3. **Automated publishing:**
   - The project can use GitHub Actions or other CI/CD
   - Upload to Snap Store happens automatically on release

## Multi-Architecture Support

### Building for Different Architectures:

**For amd64 (x86_64):**
```yaml
architectures:
  - build-on: amd64
```

**For arm64 (ARM64/aarch64):**
```yaml
architectures:
  - build-on: arm64
```

**For multiple architectures:**
```yaml
architectures:
  - build-on: amd64
  - build-on: arm64
  - build-on: armhf
```

Edit `snap/snapcraft.yaml` to modify the architecture settings.

## Troubleshooting

### Snapcraft Not Found:
Install snapcraft:
```bash
sudo snap install snapcraft --classic
```

### LXD/Multipass Issues:
Use destructive mode (builds directly on host):
```bash
snapcraft pack --destructive-mode --output=classes/artifacts/smalltextpad_1.5.0_amd64.snap
```

### Permission Denied Errors:
Ensure the build script is executable:
```bash
chmod +x packaging/build-snap.sh
```

### JAR Not Found:
The snap build expects a pre-built JAR. Build it first:
```bash
./packaging/build-jar.sh
```

### Desktop File Not Showing:
Check that the desktop file exists:
```bash
cat snap/gui/smalltextpad.desktop
```

### Application Won't Start:
Check snap logs:
```bash
snap logs smalltextpad -f
```

## Snap Package Structure

Inside the snap:
```
/snap/smalltextpad/current/
├── SmallTextPad.jar          # Application JAR
├── res/                       # Image resources
├── dic/                       # Dictionary files
├── bin/
│   └── smalltextpad          # Wrapper script
└── usr/
    ├── lib/jvm/              # Bundled Java runtime
    └── share/
        ├── applications/      # Desktop file
        └── pixmaps/          # Application icon
```

## Development Notes

- The snap uses a wrapper script that:
  - Locates the bundled Java runtime
  - Sets the DISPLAY environment variable
  - Executes the JAR with proper arguments

- The `override-build` section in snapcraft.yaml:
  - Creates the wrapper script
  - Copies the pre-built JAR and resources
  - Installs desktop integration files

- The snap bundles OpenJDK 17 JRE (from Ubuntu 22.04 repos)
  - Users don't need Java installed
  - Application is truly self-contained

## Testing

Before publishing, test the snap thoroughly:

```bash
# Install locally
sudo snap install --dangerous classes/artifacts/smalltextpad_*.snap

# Test launching
smalltextpad

# Test with file
smalltextpad test.txt

# Test file associations
xdg-mime query default text/plain

# Check permissions
snap connections smalltextpad

# View logs
snap logs smalltextpad
```

## See Also

- [JAR Build Guide](README-jar.html) - Building standalone JAR files
- [RPM Build Guide](README-rpm.html) - Building RPM packages for Fedora/RHEL
- [Windows Build Guide](README-windows.html) - Building Windows installers
- [Back to Home](index.html) - Main documentation and downloads
- [Snapcraft Documentation](https://snapcraft.io/docs) - Official Snapcraft documentation
- [Publishing to Snap Store](https://snapcraft.io/docs/releasing-your-app) - Publishing guide
