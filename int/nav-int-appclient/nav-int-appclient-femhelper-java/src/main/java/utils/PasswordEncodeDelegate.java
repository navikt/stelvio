package utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import no.nav.appclient.util.Constants;

import org.apache.commons.lang.StringUtils;

import com.ibm.ws.security.util.PasswordUtil;

/**
 * This class encrypts password listed in the FEM & BPC Helpers
 * property file.
 * 
 * FileNotFoundException is not handled. This shall be validated
 * before delegating work to this class.
 * 
 * @author Andreas Roe
 */
public class PasswordEncodeDelegate {

	private static Logger LOGGER = Logger.getLogger(PasswordEncodeDelegate.class.getName());
	
	
	/**
	 * This method manipulate the property file with an xor encoded password
	 * 
	 * @param filename
	 * @throws IOException 
	 */
	public void encode(String filename) throws IOException {
		
		FileInputStream in = new FileInputStream(filename);
		Properties properties = new Properties();
		properties.load(in);
		in.close();
		
		// Check is password is defined
		if (!StringUtils.isEmpty(properties.getProperty(Constants.password))) {
			
			// Check if password allready is encrypted
			String password = properties.getProperty(Constants.password);
			if (!password.startsWith("{xor}")) {
				
				try {
					// Write encoded password back to the property file
					String encoded = PasswordUtil.encode(password);
					properties.setProperty(Constants.password, encoded);
					FileOutputStream out = new FileOutputStream(filename);
					properties.store(out, "Encoded password");
					out.close();
					
				} catch (Exception e) {
					LOGGER.warning("Password not encrypted due to " + e.getClass());
				}
			}
		}
	}
	
	/**
	 * This method returns a decoded password. If the password don't start with 
	 * '{xor}' (not encoded) will the original password be returned
	 * 
	 * @param password
	 * @return decoded password
	 */
	public String getDecryptedPassword(String password) {
		
		if (null != password && password.startsWith("{xor}")) {
			try {
				return PasswordUtil.decode(password);
			} catch (Exception e) {
				LOGGER.warning("Password not encrypted due to " + e.getClass());
			}
		}
	
		return "";
	}
	
}
