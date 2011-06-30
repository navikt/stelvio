package no.nav.maven.plugin.configurewasdev.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;

/**
 * @author B123034
 *
 */
public class Utils {

	public static void downloadURL(URL sourceURL, File destination) {
		
		
		try {
			System.out.println("Opening connection to " + sourceURL + "...");
			URLConnection urlC = sourceURL.openConnection();
			InputStream is = sourceURL.openStream();
			FileOutputStream fos = null;

			String localFile = null;
			
			StringTokenizer st = new StringTokenizer(sourceURL.getFile(), "/");
			while (st.hasMoreTokens()) {
				localFile = st.nextToken();
			}
			System.out.println("Copying resource "+localFile+" (type: " + urlC.getContentType() +") to " + destination.getAbsolutePath()+"/"+localFile);
			if (destination.isDirectory()) {
				fos = new FileOutputStream(destination.getAbsolutePath()+"/"+localFile);
			} else {
				fos = new FileOutputStream(localFile);
			}

			int oneChar, count = 0;
			
			while ((oneChar=is.read()) != -1 ){
				fos.write(oneChar);
				count++;
				if (count != 0 && count % 8192 == 0) {		
					System.out.print("\r" + count + "/" + urlC.getContentLength() + " byte(s) copied");
				}
			}
			is.close();
			fos.close();
			System.out.print("\r" + count + "/" + urlC.getContentLength() + " byte(s) copied\n");
			
		} catch (MalformedURLException e) {
			System.err.println(e.toString());
		} catch (IOException e) {
			System.err.println(e.toString());
		}
	}
	
}
