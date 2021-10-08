package wagemaker.co.uk.display;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import wagemaker.co.uk.utility.ConfigFile;
import wagemaker.co.uk.utility.ConfigRead;

public class Size {

	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private static int height = (int) screenSize.getHeight();
	private static int width = (int) screenSize.getWidth();
	private static int frameWidth;
	private static int frameHeight;
	
	//Get Frame width
	public static int getFrameWidth() {
		File config = new File(ConfigFile.getFile());
		if (config.exists()) {
			if (ConfigRead.getReadConfig("FrameW") == null) {
				frameWidth = (getScreenWidth()/2);
			} else {
				frameWidth = Integer.parseInt(ConfigRead.getReadConfig("FrameW"));
			}
		} else {
			frameWidth = (getScreenWidth()/2);
		}	
		return frameWidth;
	}
	// set frame width
	public static void setFrameWidth(int frameWidth) {
		try {
			FileInputStream in = new FileInputStream(ConfigFile.getFile());
			Properties props = new Properties();
			props.load(in);
			in.close();
			
			FileOutputStream out = new FileOutputStream(ConfigFile.getFile());
			props.setProperty("FrameW", Integer.toString(frameWidth));
			props.store(out, null);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public static int getFrameHeight() {
		File config = new File(ConfigFile.getFile());
		if (config.exists()) {
			if (ConfigRead.getReadConfig("FrameH") == null) {
				frameHeight = (getScreenHeight()/2);
			} else {
				frameHeight = Integer.parseInt(ConfigRead.getReadConfig("FrameH"));
			}
		} else {
			frameHeight = (getScreenHeight()/2);
		}	
		return frameHeight;
	}
	public static void setFrameHeight(int frameHeight) {
		try {
			FileInputStream in = new FileInputStream(ConfigFile.getFile());
			Properties props = new Properties();
			props.load(in);
			in.close();
			
			FileOutputStream out = new FileOutputStream(ConfigFile.getFile());
			props.setProperty("FrameH", Integer.toString(frameHeight));
			props.store(out, null);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	
	public static Dimension getScreenSize() {
		return screenSize;
	}
	public static int getScreenHeight() {
		return height;
	}
	public static int getScreenWidth() {
		return width;
	}
	
}
