package wagemaker.co.uk.display;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import wagemaker.co.uk.utility.ConfigDir;
import wagemaker.co.uk.utility.ConfigFile;
import wagemaker.co.uk.utility.ConfigRead;

public class LastPath {
	
	private static String lastPath;

	public static String getLastPath() {
		File config = new File(ConfigFile.getFile());
		if (config.exists()) {
			if (ConfigRead.getReadConfig("LastP") == null) {
				lastPath = ConfigDir.getHomeDir();
			} else {
				lastPath = ConfigRead.getReadConfig("LastP");
			}
		} else {
			lastPath = ConfigDir.getHomeDir();
		}
		return lastPath;
	}

	public static void setLastPath(String lastPath) {
		
		try {
			FileInputStream in = new FileInputStream(ConfigFile.getFile());
			Properties props = new Properties();
			props.load(in);
			in.close();
			
			FileOutputStream out = new FileOutputStream(ConfigFile.getFile());
			props.setProperty("LastP", lastPath);
			props.store(out, null);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
