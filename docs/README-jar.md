---
layout: default
title: JAR Build Guide - SmallTextPad
---

[← Back to Home](index.html)

# JAR Build for SmallTextPad

This guide covers building the standalone JAR file for SmallTextPad, a lightweight Java text editor with encryption and multi-language support.

## Files

- `build-jar.sh` - Script to compile sources and build the standalone JAR file
- `README-jar.md` - This documentation file

## Prerequisites

### System Requirements
- Java 21+ JDK installed
- Standard Unix tools (bash, find)

### Installing Build Dependencies

**Fedora/RHEL/CentOS:**
```bash
sudo dnf install java-21-openjdk-devel
```

**Ubuntu/Debian:**
```bash
sudo apt install openjdk-21-jdk
```

**openSUSE:**
```bash
sudo zypper install java-21-openjdk-devel
```

**macOS:**
```bash
brew install openjdk@21
```

## Building the JAR File

The build script can be run from anywhere in the project:

### From Project Root:
```bash
./packaging/build-jar.sh
```

### From Packaging Directory:
```bash
cd packaging
./build-jar.sh
```

The script will:
1. Navigate to the project root automatically
2. Delete any existing compiled class files
3. Generate a list of all Java source files
4. Compile all Java sources to the `bin/` directory
5. Copy resource bundles (`.properties` files) to the correct package structure
6. Copy image resources to the JAR root for classpath access
7. Copy dictionary files for spell-checking
8. Create the JAR file with proper manifest
9. Test launch the application

## Build Output

After successful build:
- **JAR file:** `classes/artifacts/SmallTextPad.jar`
- **Compiled classes:** `bin/` directory
- **Source list:** `sources.txt` (generated during build)

## Running the JAR

### Direct Execution:
```bash
java -jar classes/artifacts/SmallTextPad.jar
```

### With File Argument:
```bash
java -jar classes/artifacts/SmallTextPad.jar /path/to/file.txt
```

### Using Desktop Environment:
On systems with desktop integration, you can double-click the JAR file if `.jar` files are associated with Java.

## JAR Contents

The JAR file includes:
- **Compiled classes:** All Java classes in proper package structure
- **Resource bundles:** Language files (English, Dutch, Polish, Portuguese)
  - `wagemaker/co/uk/lang/LabelsBundle_*.properties`
- **Images:** Application icons and toolbar images at JAR root
  - `about.png`, `copy.png`, `cut.png`, `paste.png`, etc.
- **Dictionaries:** Spell-check dictionaries
  - `dic/Dictionary_en.txt`
  - `dic/Dictionary_nl.txt`
- **Manifest:** Proper Main-Class definition for executable JAR

## Distribution

The generated JAR is a fully self-contained executable that can be:
- Copied to any system with Java 21+ installed
- Distributed via download links
- Included in other packaging formats (RPM, DEB, Snap, etc.)
- Run without installation

## Troubleshooting

### Build Fails - javac not found:
Ensure Java JDK is installed and `javac` is in your PATH:
```bash
which javac
javac -version
```

### Application Doesn't Launch:
Verify Java runtime is installed:
```bash
java -version
```

### Missing Resources Error:
If you see `MissingResourceException` or `NullPointerException` for resources:
1. Clean and rebuild:
   ```bash
   rm -rf bin/ classes/artifacts/
   ./packaging/build-jar.sh
   ```
2. Verify JAR contents:
   ```bash
   jar tf classes/artifacts/SmallTextPad.jar | grep -E "(\.properties$|\.png$)"
   ```

### Permission Denied:
Make the script executable:
```bash
chmod +x packaging/build-jar.sh
```

## Development Workflow

For rapid development and testing:

1. **Edit source files** in `src/` directory
2. **Run build script** to compile and test:
   ```bash
   ./packaging/build-jar.sh
   ```
3. **Application launches automatically** for testing
4. **Close application** and repeat as needed

The script automatically cleans previous builds, so you always get a fresh compile.

## Notes

- The build script uses `set -euo pipefail` to exit immediately on any error
- Deprecation warnings during compilation are enabled with `-Xlint:deprecation`
- The script supports being run from any directory - it automatically finds the project root
- The JAR manifest specifies `wagemaker.co.uk.main.Launcher` as the Main-Class

## See Also

- [RPM Build Guide](README-rpm.html) - Building RPM packages for Fedora/RHEL/openSUSE
- [Snap Build Guide](README-snap.html) - Building Snap packages for universal Linux distribution
- [Windows Build Guide](README-windows.html) - Building Windows installers and portable packages
- [Back to Home](index.html) - Main documentation and downloads
