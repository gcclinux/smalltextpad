package wagemaker.co.uk.utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigRead {

	private static String readConfig;
	
	public static String getReadConfig(String variable) {
		Properties prop = new Properties();
		InputStream input = null;
		
			try {
				input = new FileInputStream(ConfigFile.getFile());
				prop.load(input);
				readConfig = prop.getProperty(variable);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (input != null) {
					try {
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		
		return readConfig;
	}

}
