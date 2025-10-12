Name:           smalltextpad
Version:        1.5.1
Release:        3%{?dist}
Summary:        A lightweight Java text editor with encryption and multi-language support

License:        Custom
URL:            https://gcclinux.github.io/smalltextpad/
Source0:        %{name}-%{version}.tar.gz

BuildArch:      noarch
BuildRequires:  java-21-openjdk-devel
Requires:       java-21-openjdk-headless
Requires:       hicolor-icon-theme

%description
SmallTextPad is a lightweight text editor written in Java (Swing). It includes 
basic editing features, undo/redo, printing, and a simple file encryption format 
(.sstp). The application supports multiple languages and provides a clean, 
simple interface for text editing tasks.

Features:
- Basic text editing with undo/redo
- File encryption with .sstp format
- Multi-language support
- Print functionality
- Lightweight and fast
- Cross-platform Java application

%prep
%setup -q

%build
# Compile Java sources
find src/ -name "*.java" > sources.txt
mkdir -p bin
javac -d bin -Xlint:deprecation @sources.txt

# Copy resource bundles (properties files) to bin
mkdir -p bin/wagemaker/co/uk/lang
cp src/wagemaker/co/uk/lang/*.properties bin/wagemaker/co/uk/lang/

# Copy image resources into bin root so getResource("/name.png") works
cp -r res/* bin/

# Copy dictionaries to bin root
cp -r dic bin/

# Create JAR file with all resources included
mkdir -p classes/artifacts
jar cfm classes/artifacts/SmallTextPad.jar src/META-INF/MANIFEST.MF -C bin .

%install
# Create directory structure
install -d %{buildroot}%{_datadir}/%{name}
install -d %{buildroot}%{_bindir}
install -d %{buildroot}%{_datadir}/applications
install -d %{buildroot}%{_datadir}/icons/hicolor/128x128/apps
install -d %{buildroot}%{_datadir}/icons/hicolor/256x256/apps
install -d %{buildroot}%{_datadir}/icons/hicolor/1024x1024/apps
install -d %{buildroot}%{_docdir}/%{name}

# Install JAR file
install -m 644 classes/artifacts/SmallTextPad.jar %{buildroot}%{_datadir}/%{name}/

# Install resources and dictionaries
cp -r res %{buildroot}%{_datadir}/%{name}/
cp -r dic %{buildroot}%{_datadir}/%{name}/

# Install icons
install -m 644 res/smalltextpad_128x128.png %{buildroot}%{_datadir}/icons/hicolor/128x128/apps/%{name}.png
install -m 644 res/smalltextpad_256x256.png %{buildroot}%{_datadir}/icons/hicolor/256x256/apps/%{name}.png
install -m 644 res/smalltextpad_1024x1024.png %{buildroot}%{_datadir}/icons/hicolor/1024x1024/apps/%{name}.png

# Create launcher script
cat > %{buildroot}%{_bindir}/%{name} << 'EOF'
#!/bin/bash
# SmallTextPad launcher script
exec java -jar %{_datadir}/%{name}/SmallTextPad.jar "$@"
EOF
chmod 755 %{buildroot}%{_bindir}/%{name}

# Create desktop file
cat > %{buildroot}%{_datadir}/applications/%{name}.desktop << 'EOF'
[Desktop Entry]
Version=1.0
Type=Application
Name=SmallTextPad
Comment=A lightweight Java text editor with encryption and multi-language support
Comment[es]=Un editor de texto ligero en Java con cifrado y soporte multi-idioma
Comment[fr]=Un éditeur de texte léger en Java avec chiffrement et support multi-langues
Comment[de]=Ein leichtgewichtiger Java-Texteditor mit Verschlüsselung und Mehrsprachunterstützung
Exec=smalltextpad %F
Icon=smalltextpad
Terminal=false
StartupNotify=true
MimeType=text/plain;text/x-java;application/x-smalltextpad-encrypted;
Categories=Office;TextEditor;Development;
Keywords=text;editor;java;encryption;
StartupWMClass=SmallTextPad
EOF

# Install documentation
install -m 644 README.md %{buildroot}%{_docdir}/%{name}/
install -m 644 LICENSE %{buildroot}%{_docdir}/%{name}/
if [ -f doc/SmallTextPadLicense.txt ]; then
    install -m 644 doc/SmallTextPadLicense.txt %{buildroot}%{_docdir}/%{name}/
fi

%post
# Update desktop database and icon cache
/bin/touch --no-create %{_datadir}/icons/hicolor &>/dev/null || :
/usr/bin/update-desktop-database &> /dev/null || :

%postun
if [ $1 -eq 0 ] ; then
    /bin/touch --no-create %{_datadir}/icons/hicolor &>/dev/null
    /usr/bin/gtk-update-icon-cache %{_datadir}/icons/hicolor &>/dev/null || :
fi
/usr/bin/update-desktop-database &> /dev/null || :

%posttrans
/usr/bin/gtk-update-icon-cache %{_datadir}/icons/hicolor &>/dev/null || :

%files
%doc %{_docdir}/%{name}/README.md
%license %{_docdir}/%{name}/LICENSE
%doc %{_docdir}/%{name}/SmallTextPadLicense.txt
%{_bindir}/%{name}
%{_datadir}/%{name}/
%{_datadir}/applications/%{name}.desktop
%{_datadir}/icons/hicolor/128x128/apps/%{name}.png
%{_datadir}/icons/hicolor/256x256/apps/%{name}.png
%{_datadir}/icons/hicolor/1024x1024/apps/%{name}.png

%changelog
* Sun Oct 12 2025 Ricardo Wagemaker <wagemra@gmail.com> - 1.5.1-3
- Fixed opening encrypted files by EasyEdit App
* Fri Jan 10 2025 Ricardo Wagemaker <wagemra@gmail.com> - 1.5.0-1
- Initial RPM package for SmallTextPad
- Lightweight Java text editor with encryption support
- Multi-language support included
- Desktop integration with proper icons and MIME types
- Support for .sstp encrypted file format