package no.nav.appclient.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.ibm.ws.security.util.PasswordUtil;

/**
 * This class encrypts password listed in the FEM & BPC Helpers property file.
 * 
 * FileNotFoundException is not handled. This shall be validated before
 * delegating work to this class.
 * 
 * @author Andreas Roe
 */
public class PasswordEncodeDelegate {
	/**
	 * This method manipulate the property file with an xor encoded password
	 * 
	 * @param propertiesFile
	 */
	public void encodePassword(File propertiesFile) {
		try {
			FileInputStream in = new FileInputStream(propertiesFile);
			Properties properties = new Properties();
			properties.load(in);
			in.close();
			// Check is password is defined
			if (!StringUtils.isEmpty(properties.getProperty(Constants.password))) {
				// Check if password allready is encrypted
				String password = properties.getProperty(Constants.password);
				if (!password.startsWith("{xor}")) {
					// Write encoded password back to the property file
					String encodedPassword = PasswordUtil.encode(password);
					properties.setProperty(Constants.password, encodedPassword);
					FileOutputStream out = new FileOutputStream(propertiesFile);
					properties.store(out, new Date() + ": encoded password");
					out.close();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
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
		try {
			if (password != null && password.startsWith("{xor}")) {
				return PasswordUtil.decode(password);
			} else {
				return password;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
