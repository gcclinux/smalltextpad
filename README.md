# smalltextpad
SmallTextPad with Encryption and multi-language
SmallTextPad is a Simple Java Text Editor and 100% FREE Application, it was written in Java and therefore requires a minimum of JRE 21 installed on your System unless you compile it and package it in snap or flatpak, SmallTextPad has several features already and is still being developed in my spare time.
If Java is not installed it can be downloaded from available opensource online. 
Address: 
<p><strong>3rd Party - License & Software</strong></p>

<ul>
<li>Icons made by <a href="http://www.freepik.com">Freepik</a> from <a href="http://www.flaticon.com/">flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/"><g class="gr_ gr_4 gr-alert gr_spell gr_inline_cards gr_run_anim ContextualSpelling ins-del multiReplace" id="4" data-gr-id="4">CC BY</g> 3.0</a></li>
<li>Java is a trademark or registered trademark of Oracle and/or its affiliates.</li>
<li>Any other names may be trademarks of their respective owners!</li>
<li> Download OpenJDK from <a href="https://adoptium.net/">Adoptium</a></li>
</ul>

# SmallTextPad

SmallTextPad — a small Java text editor with optional file encryption and multi-language support.

## Overview

SmallTextPad is a lightweight text editor written in Java (Swing). It includes basic editing features, undo/redo, printing, and a simple file encryption format (`.sstp`). The project is free and maintained by the original author in spare time.

## Quick facts

- Project: SmallTextPad
- Version: 1.5.0
- Author: Ricardo Wagemaker
- Main class: `wagemaker.co.uk.main.Launcher`
- License: See `LICENSE` and `doc/SmallTextPadLicense.txt`

## Requirements

- Java 21+ (OpenJDK or Oracle JRE)

## Build & run (local)

From the project root:

```bash
javac -d bin -Xlint:deprecation @sources.txt
java -cp bin wagemaker.co.uk.main.Launcher
```

## Notes

- The GUI uses Swing. On WSL/X11 the window manager can sometimes reposition dialogs — the code includes extra centering logic to help, and a debug flag in `src/wagemaker/co/uk/main/Launcher.java` (set `DEBUG = true`) that adds diagnostic output when dialogs are shown.
- A `.gitignore` has been added to ignore compiled artifacts and IDE files. If you want to keep some prebuilt jars in the repository (for releases), add an explicit whitelist to the `.gitignore`.

## Development

- Source is under `src/`
- Version constant: `src/wagemaker/co/uk/utility/Details.java` (field `Version`)
- Encryption extension: `.sstp` (see `Details.encryptionExtention`)

## Troubleshooting

- If the app won't run, ensure `java` and `javac` are installed and available on PATH.
- On WSL, run an X server (for example VcXsrv on Windows) and ensure DISPLAY is configured.

## 3rd-party

- Icons by Freepik / Flaticon — licensed under CC BY 3.0
- Java is a trademark of Oracle and/or its affiliates

## Links

- Project: https://github.com/gcclinux/smalltextpad
- Latest releases: https://github.com/gcclinux/smalltextpad/releases/latest

## Contributing

If you'd like help packaging this as a Snap, Flatpak or providing distribution builds, I can add packaging instructions and CI steps.

# smalltextpad

SmallTextPad with Encryption and multi-language

SmallTextPad is a Simple Java Text Editor and 100% FREE Application, it was written in Java and therefore requires a minimum of JRE 11 installed on your System unless you compile it and package it in snap or flatpak, SmallTextPad has several features already and is still being developed in my spare time.

If Java is not installed it can be downloaded from available opensource online. 
Address: 

<p><strong>3rd Party - License & Software</strong></p>

<ul>
<li>Icons made by <a href="http://www.freepik.com">Freepik</a> from <a href="http://www.flaticon.com/">flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/"><g class="gr_ gr_4 gr-alert gr_spell gr_inline_cards gr_run_anim ContextualSpelling ins-del multiReplace" id="4" data-gr-id="4">CC BY</g> 3.0</a></li>
<li>Java is a trademark or registered trademark of Oracle and/or its affiliates.</li>
<li>Any other names may be trademarks of their respective owners!</li>
<li> Download OpenJDK from <a href="https://adoptium.net/">Adoptium</a></li>
</ul>