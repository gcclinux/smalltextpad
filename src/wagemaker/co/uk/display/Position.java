package wagemaker.co.uk.display;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import wagemaker.co.uk.utility.ConfigFile;
import wagemaker.co.uk.utility.ConfigRead;

public class Position {

	private static int posX = (int) ((int) Size.getScreenHeight() / (2.3));
	private static int posY = ((int) Size.getScreenWidth() / 7);
	private static int checkX;
	private static int checkY;
	
	public static int getPosX() {
		File config = new File(ConfigFile.getFile());
		if (config.exists()) {
			if (ConfigRead.getReadConfig("PosX") == null) {
				checkX = posX;
			} else {
				checkX = Integer.parseInt(ConfigRead.getReadConfig("PosX"));
			}
		} else {
			checkX = posX;
		}
		return checkX;
	}
	
	public static int getPosY() {
		File config = new File(ConfigFile.getFile());
		if (config.exists()) {
			if (ConfigRead.getReadConfig("PosY") == null) {
				checkY = posY;
			} else {
				checkY = Integer.parseInt(ConfigRead.getReadConfig("PosY"));
			}
		} else {
			checkY = posY;
		}
		return checkY;
	}
	
	public static void setPosX(int posX) {
		
		try {
			FileInputStream in = new FileInputStream(ConfigFile.getFile());
			Properties props = new Properties();
			props.load(in);
			in.close();
			
			FileOutputStream out = new FileOutputStream(ConfigFile.getFile());
			props.setProperty("PosX", Integer.toString(posX));
			props.store(out, null);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void setPosY(int posY) {
		
		try {
			FileInputStream in = new FileInputStream(ConfigFile.getFile());
			Properties props = new Properties();
			props.load(in);
			in.close();
			
			FileOutputStream out = new FileOutputStream(ConfigFile.getFile());
			props.setProperty("PosY", Integer.toString(posY));
			props.store(out, null);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
