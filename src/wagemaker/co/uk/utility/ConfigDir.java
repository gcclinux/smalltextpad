package wagemaker.co.uk.utility;

import java.io.File;

public class ConfigDir {

	private static String Directory = "smalltextpad";
	private static String homeDir = System.getProperty("user.home");
	private static String workingDIR = null;
	
	public static String getHomeDir() {
		return homeDir;
	}
	public static String getDirectory() {
		if (SystemOS.getOS().indexOf("win") >=0 ) {
			workingDIR = homeDir+"\\"+Directory+"\\";
			if (!new File (workingDIR).exists()) {
				new File (workingDIR).mkdir();
			}
		} else if (SystemOS.getOS().indexOf("linux") >=0 ) {
			workingDIR = homeDir+"/"+".config"+"/";
			if (!new File (workingDIR).exists()) {
				new File (workingDIR).mkdir();
			}
		}
		return workingDIR;
	}
}