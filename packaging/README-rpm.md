# RPM Packaging for SmallTextPad

This directory contains the RPM packaging files for SmallTextPad, a lightweight Java text editor with encryption and multi-language support.

## Files

- `smalltextpad.spec` - RPM spec file with complete package definition
- `build-rpm.sh` - Helper script to build the RPM package
- `README-rpm.md` - This documentation file

## Prerequisites

### System Requirements
- Linux distribution with RPM package management (Fedora, RHEL, CentOS, openSUSE, etc.)
- Java 21+ JDK installed
- RPM build tools

### Installing Build Dependencies

**Fedora/RHEL/CentOS:**
```bash
sudo dnf install rpm-build rpmdevtools java-21-openjdk-devel
```

**openSUSE:**
```bash
sudo zypper install rpm-build java-21-openjdk-devel
```

## Building the RPM Package

1. Navigate to the packaging directory:
   ```bash
   cd packaging
   ```

2. Run the build script:
   ```bash
   ./build-rpm.sh
   ```

The script will:
- Set up the RPM build environment
- Create a source tarball
- Build both binary and source RPM packages

## Package Details

### Package Information
- **Name:** smalltextpad
- **Version:** 1.5.0
- **Architecture:** noarch (Java application)
- **License:** Custom (see LICENSE file)
- **URL:** https://github.com/gcclinux/smalltextpad
- **Maintainer:** Ricardo Wagemaker

### Installation Locations
- **Executable:** `/usr/bin/smalltextpad`
- **JAR file:** `/usr/share/smalltextpad/SmallTextPad.jar`
- **Resources:** `/usr/share/smalltextpad/res/`
- **Dictionaries:** `/usr/share/smalltextpad/dic/`
- **Desktop file:** `/usr/share/applications/smalltextpad.desktop`
- **Icons:** `/usr/share/icons/hicolor/{128x128,256x256,1024x1024}/apps/smalltextpad.png`
- **Documentation:** `/usr/share/doc/smalltextpad/`

### Features Included
- Desktop integration with application menu entry
- File associations for text files and .sstp encrypted files
- Multiple icon sizes for different display densities
- Proper MIME type handling
- Multi-language desktop entry descriptions
- Automatic icon cache and desktop database updates

## Installing the Package

After building, install the package with:

```bash
# Using rpm directly
sudo rpm -ivh ~/rpmbuild/RPMS/noarch/smalltextpad-1.5.0-1.*.noarch.rpm

# Or using dnf (Fedora/RHEL)
sudo dnf install ~/rpmbuild/RPMS/noarch/smalltextpad-1.5.0-1.*.noarch.rpm

# Or using zypper (openSUSE)
sudo zypper install ~/rpmbuild/RPMS/noarch/smalltextpad-1.5.0-1.*.noarch.rpm
```

## Using the Application

Once installed, you can:

1. **Launch from application menu:** Look for "SmallTextPad" in the Office or Development category
2. **Launch from command line:** `smalltextpad [filename]`
3. **Open files:** Right-click text files and choose "Open with SmallTextPad"

## Uninstalling

```bash
sudo rpm -e smalltextpad
# or
sudo dnf remove smalltextpad
# or
sudo zypper remove smalltextpad
```

## Customization

### Modifying the Spec File

The `smalltextpad.spec` file can be customized for different requirements:

- **Dependencies:** Modify the `Requires:` lines to change Java version requirements
- **File locations:** Change installation paths in the `%install` section
- **Desktop integration:** Modify the desktop file creation in the `%install` section
- **Package metadata:** Update summary, description, and other metadata

### Building for Different Architectures

While SmallTextPad is a Java application (noarch), you can modify the spec file to build architecture-specific packages if needed by changing:
```spec
BuildArch: noarch
```

## Troubleshooting

### Common Issues

1. **Java not found during build:**
   - Ensure Java 21+ JDK is installed
   - Check that `java` and `javac` are in your PATH

2. **RPM build tools missing:**
   - Install `rpm-build` and `rpmdevtools` packages

3. **Permission errors:**
   - Ensure you have write permissions to `~/rpmbuild/`
   - The build script will create the directory structure if needed

4. **Missing MANIFEST.MF:**
   - The build script automatically creates this file if missing

### Build Logs

Check RPM build logs for detailed error information:
```bash
tail -f ~/rpmbuild/BUILD/smalltextpad-*/build.log
```

## Contributing

To contribute improvements to the RPM packaging:

1. Fork the repository
2. Make changes to the spec file or build scripts
3. Test the build process
4. Submit a pull request

## Support

- **Project Repository:** https://github.com/gcclinux/smalltextpad
- **Issues:** https://github.com/gcclinux/smalltextpad/issues
- **Latest Releases:** https://github.com/gcclinux/smalltextpad/releases/latest