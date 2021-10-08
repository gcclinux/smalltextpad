package wagemaker.co.uk.utility;

/******************************************************************************************************
 * @author by Ricardo Wagemaker (["java"] + "@" + "wagemaker.co.uk") 2010-2018
 * @title SmallTextPad
 * @version 1.3.2
 * @since   2010 - 2018
 * @License MIT
 ******************************************************************************************************/

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class STPFileCrypter {

   static boolean result;

static boolean fileProcessor(int cipherMode,String key,File inputFile,File outputFile){

	 try {
			DESKeySpec dks = new DESKeySpec(key.getBytes());
			SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
			SecretKey desKey = skf.generateSecret(dks);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(cipherMode, desKey);

	       FileInputStream inputStream = new FileInputStream(inputFile);
	       byte[] inputBytes = new byte[(int) inputFile.length()];
	       inputStream.read(inputBytes);

	       byte[] outputBytes = cipher.doFinal(inputBytes);

	       FileOutputStream outputStream = new FileOutputStream(outputFile);
	       outputStream.write(outputBytes);

	       inputStream.close();
	       outputStream.close();
	       
	       result = true;

	    } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | IOException | InvalidKeySpecException e) {
	    	 result = false;
            }
	 
	 return result;
     }
	
    public static boolean main(String filePathTrue, String code, String method) {
    	boolean result = false;
		String key = code;
		File inputFile = new File(filePathTrue);
		File encryptedFile = new File(filePathTrue+"."+Details.encryptionExtention);

	try {
			if (method == "encrypt") {
				     if (STPFileCrypter.fileProcessor(Cipher.ENCRYPT_MODE,key,inputFile,encryptedFile) == true ) {
				    	 result = true;
							if (encryptedFile.exists()){
								System.gc();
								inputFile.delete();			
							} 				
			     } else {
			    	 result = false;
			     }
			} else if (method == "decrypt") {
				File decryptedFile = null;
				
				if (filePathTrue.indexOf(".") > 0) {
					decryptedFile = new File(filePathTrue.substring(0, filePathTrue.lastIndexOf(".")));
				}	

			     if (STPFileCrypter.fileProcessor(Cipher.DECRYPT_MODE,key,inputFile,decryptedFile) == true) {
			    	 result = true;
						if (decryptedFile.exists()){
							System.gc();
							inputFile.delete();			
						} 	
		     } else {
		    	 result = false;
		     }
			} 
	     	
	 } catch (Exception ex) {
             ex.printStackTrace();
	 }
	return result;
     }
}
