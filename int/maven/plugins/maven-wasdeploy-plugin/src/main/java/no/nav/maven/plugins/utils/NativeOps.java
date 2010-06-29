package no.nav.maven.plugins.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Handles the native operations of the deployment
 * 
 * @author test@example.com
 */
public class NativeOps {

	public static void copy(File sourceFile, File destFile) throws IOException {

		if (!destFile.exists())
			destFile.createNewFile();

		FileChannel source = null;
		FileChannel destination = null;

		try {
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();
			destination.transferFrom(source, 0, source.size());
		}

		finally {
			if (source != null)
				source.close();
			
			if (destination != null)
				destination.close();
		}
		
		System.out.println("[INFO] ### FILE COPY ### " + sourceFile + " => " + destFile);
	}
}
