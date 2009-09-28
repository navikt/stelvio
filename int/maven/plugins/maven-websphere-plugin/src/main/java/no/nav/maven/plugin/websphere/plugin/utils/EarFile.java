package no.nav.maven.plugin.websphere.plugin.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/** 
 * @author test@example.com 
 */
public final class EarFile {

	public static void copyFile(final File source, final File dest) {
		FileInputStream in = null;
		FileOutputStream out = null;
		
		try {
			in = new FileInputStream(source);
			out = new FileOutputStream(dest);
		   byte[] buffer = new byte[4096];         
		   int bytes_read;                         

		   while((bytes_read = in.read(buffer)) != -1) { 
			   out.write(buffer, 0, bytes_read);            
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException("An error occured reading source file: " + source.getAbsolutePath(), e);
		} catch (IOException e) {
			throw new RuntimeException("An error occured writing desctination file: " + dest.getAbsolutePath(), e);
		} finally {
			if(in != null) {
				try {
					in.close();
				} catch (IOException e) {}	
			}
			if(out != null) {
				try {
					out.close();
				} catch (IOException e) {}	
			}
		}		
	}
}
