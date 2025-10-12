---
layout: default
title: Flatpak Build Instructions
description: How to build SmallTextPad as a Flatpak package from source
---

# SmallTextPad Flatpak Build Instructions

This guide explains how to build SmallTextPad as a Flatpak package from source.

## Prerequisites

### Install pre-requisite flatpakrepo & flatpak-builder

```bash
$ flatpak remote-add --if-not-exists --user flathub https://dl.flathub.org/repo/flathub.flatpakrepo
```

```bash
$ flatpak install org.flatpak.Builder
```

## Download the Flatpak Manifest

#### Download the io.github.gcclinux.smalltextpad.yml

[https://raw.githubusercontent.com/gcclinux/smalltextpad/refs/heads/main/io.github.gcclinux.smalltextpad.yml](https://raw.githubusercontent.com/gcclinux/smalltextpad/refs/heads/main/io.github.gcclinux.smalltextpad.yml)

```bash
$ curl -O https://raw.githubusercontent.com/gcclinux/smalltextpad/refs/heads/main/io.github.gcclinux.smalltextpad.yml
```

## Build Process

### Build the Flatpak

Use the flatpak-builder command to build the package:

```bash
$ flatpak-builder --force-clean --user --install-deps-from=flathub --repo=repo builddir io.github.gcclinux.smalltextpad.yml
```

### Export the Flatpak Package

After the build is complete, export the package to a file:

```bash
$ flatpak build-export repo builddir
```

### Create the Flatpak File

Use the flatpak build-bundle command to create the .flatpak file:

```bash
$ flatpak build-bundle repo SmallTextPad-1.5.1.flatpak io.github.gcclinux.smalltextpad
```

### Install the Created File

```bash
$ flatpak install --user SmallTextPad-$(arch)-1.5.1.flatpak
```

## Running SmallTextPad

After installation, you can run SmallTextPad from your application menu or via command line:

```bash
$ flatpak run io.github.gcclinux.smalltextpad
```

## Notes

- The Flatpak includes all necessary Java runtime dependencies
- SmallTextPad will have access to your home directory, documents, desktop, and downloads folders
- The application supports both X11 and Wayland display servers
- All encryption, multi-language support, and core features are included in the Flatpak build