package wagemaker.co.uk.utility;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

public class Details {
	
	public static String Separate = " - ";
	public static String Name = "FREE Software";
	public static String Developer = "Ricardo Wagemaker";
	public static String Header = "SmallTextPad";

	public static String Version = "1.5.0";
	public static String Title = "SmallTextPad";
	public static String versionFile = "https://github.com/gcclinux/smalltextpad/releases/latest";
	public static String remoteLicense = "https://github.com/gcclinux/smalltextpad";
	
	public static String encryptionExtention = "sstp";
	
	public static Color ORANGE = new Color(255, 105, 23);
	public static Color BLACK = new Color(0, 0, 0);
	public static Color WHITE = new Color(255, 255, 255);
	public static Color DARKGREY = new Color(94, 94, 94);
	public static int SizeW = 1000;
	public static int SizeH = 640;
	
	public static String osName = System.getProperty("os.name").toLowerCase();
	public static String history = "smalltextpad.html";
	static File historyFile = null;
	static String homeDir = System.getProperty("user.home");
	static String path = new File(homeDir).toString();
	

	public static File historyFile() throws IOException {
		if (path.indexOf("/snap/") >= 0) {
			historyFile = new File(homeDir + "/" + "/" + history);
		} else if (osName.indexOf("nux") >= 0 || osName.indexOf("nix") >= 0) {
			historyFile = new File(ConfigDir.getDirectory() + history);
		} else if (osName.equals("windows xp")) {
			historyFile = new File(homeDir + "\\Application Data\\" + history);
		} else if (osName.equals("windows 7")) {
			historyFile = new File(homeDir + "\\Application Data\\" + history);
		} else if (osName.equals("windows 8")) {
			historyFile = new File(homeDir + "\\AppData\\" + "Roaming\\" + history);
		} else if (osName.equals("windows 8.1")) {
			historyFile = new File(homeDir + "\\AppData\\" + "Roaming\\" + history);
		} else if (osName.equals("windows 10")) {
			historyFile = new File(homeDir + "\\AppData\\" + "Roaming\\" + history);
		} else if (osName.indexOf("mac") >= 0) {
			historyFile = new File(homeDir + "/Library/" + history);
		} else {
			historyFile = new File(homeDir + "\\" + history);
		}
		
		return historyFile;
	}
}
