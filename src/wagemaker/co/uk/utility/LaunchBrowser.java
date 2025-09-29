package wagemaker.co.uk.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;

public class LaunchBrowser {

	@SuppressWarnings("unused")
	public static void launcher(String address) {
		String url = address;
		String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);

		try {
			// Detect WSL (Windows Subsystem for Linux) by /proc/version contents or env
			boolean isWsl = false;
			try {
				File f = new File("/proc/version");
				if (f.exists() && f.canRead()) {
					try (BufferedReader br = new BufferedReader(new FileReader(f))) {
						String v = br.readLine();
						if (v != null && v.toLowerCase(Locale.ROOT).contains("microsoft")) {
							isWsl = true;
						}
					}
				}
			} catch (IOException ign) {
				// ignore
			}

			if (isWsl) {
				// When running under WSL, use Windows' cmd.exe to open the URL on the host
				new ProcessBuilder("cmd.exe", "/c", "start", "", url).start();
				return;
			}

			if (os.contains("win")) {
				new ProcessBuilder("rundll32", "url.dll,FileProtocolHandler", url).start();
				return;
			}

			if (os.contains("mac")) {
				new ProcessBuilder("open", url).start();
				return;
			}

			if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
				String path = new File(getMyOS.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).toString();
				File xdg = new File("/usr/bin/xdg-open");

				if (path.indexOf("/snap/") >= 0) {
					new ProcessBuilder("xdg-open", url).start();
					return;
				} else {
					if (getMyOS.getOsName().equals("ubuntu")) {
						if (xdg.canExecute()) {
							new ProcessBuilder(xdg.getAbsolutePath(), url).start();
							return;
						} else {
							// fallback: try common browsers via sh -c
							String[] browsers = {"epiphany", "firefox", "mozilla", "konqueror", "netscape", "opera", "links", "lynx", "google-chrome", "chromium-browser", "chromium"};
							StringBuilder cmd = new StringBuilder();
							for (int i = 0; i < browsers.length; i++) {
								if (i > 0) cmd.append(" || ");
								cmd.append(browsers[i]).append(" \"").append(url).append("\"");
							}
							new ProcessBuilder("sh", "-c", cmd.toString()).start();
							return;
						}
					}
				}
			}

		} catch (Exception e) {
			// swallow - we don't want to crash the app for failing to open a browser
		}
	}
}