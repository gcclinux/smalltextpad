package wagemaker.co.uk.utility;

import java.io.File;
import java.io.IOException;

public class ConfigFile {
	
	private static String configFile;

	public static String getFile() {
		File checkConfig = new File(ConfigDir.getDirectory()+"smaltextpad.cnf");
		
		if(!checkConfig.exists()) {
			try {
				checkConfig.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		configFile = checkConfig.toString();
		return configFile;
	}

}
