package wagemaker.co.uk.lang;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import wagemaker.co.uk.utility.ConfigFile;
import wagemaker.co.uk.utility.ConfigRead;

public class LangS {

	private static String languageS;
	
	public static String getLanguage() {
		File config = new File(ConfigFile.getFile());
		if (config.exists()) {
			if (ConfigRead.getReadConfig("Language") == null) {
				languageS = "en";
			} else {
				languageS = ConfigRead.getReadConfig("Language");
			}
		} else {
			languageS = "en";
		}
		return languageS;
	}
	
	
	public static void setLanguage(String languageS) {
		
		try {
			FileInputStream in = new FileInputStream(ConfigFile.getFile());
			Properties props = new Properties();
			props.load(in);
			in.close();
			
			FileOutputStream out = new FileOutputStream(ConfigFile.getFile());
			props.setProperty("Language", languageS);
			props.store(out, null);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
