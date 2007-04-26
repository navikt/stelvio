package no.nav.maven.plugins;

import java.io.*;
import java.util.Enumeration;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.*;

public class ZipUtils {

	public static void compress(File source, File destination) throws IOException {
		//
		// if the source is a directory, simply add all of its files
		//
		if (source.isDirectory()) {
			//
			// we need to create an array that has the files AND the
			// directory, otherwise we won't be able to accurately
			// extract the files
			//
			File[] files = source.listFiles();
			File[] toBeArchived = new File[files.length + 1];

			//
			// the containing directory goes before its files
			//
			toBeArchived[0] = destination;
			System.arraycopy(files, 0, toBeArchived, 1, files.length);

			compress(toBeArchived, destination);
		}

		//
		// compressing one file - make a File[] with the one item
		//
		else
			compress(new File[] { source }, destination);
	}

	/**
	 * 
	 * Zips up the source files provided and creates an archive using the given
	 * destination.
	 * 
	 */
	public static void compress(File[] sourceFiles, File destination) throws IOException {
		ZipOutputStream output = null;

		try {
			output = getArchiveOutputStream(destination);

			for (int n = 0; n < sourceFiles.length; ++n) {
				ZipEntry entry = new ZipEntry(sourceFiles[n].getAbsolutePath());
				output.putNextEntry(entry);
			}
		}

		finally {
			if (output != null)
				output.close();
		}
	}

	/**
	 * 
	 * Decompresses an archive's contents into the given destination.
	 * 
	 */
	public static void extract(File source, File destination) throws IOException {
		String destinationPath = destination.getAbsolutePath();
		ZipFile archive = getArchiveFile(source);

		Enumeration zipFiles = archive.entries();

		//
		// for each file in the archive, copy it onto disk
		//
		while (zipFiles.hasMoreElements()) {
			//
			// create the path for the new file/directory
			//
			ZipEntry entry = (ZipEntry) zipFiles.nextElement();
			File entryFile = new File(destinationPath, entry.getName());

			//
			// if it's a directory, simply create an empty directory - it
			// will be filled later by other entries
			//
			new File(entryFile.getParent()).mkdirs();
			InputStream input = null;
			FileOutputStream output = null;

			try {
				input = archive.getInputStream(entry);
				output = new FileOutputStream(entryFile);

				//
				// we can't reliably get the uncompressed size of
				// a zip entry, so just use a moderately-sized
				// buffer to do the copying
				//
				byte[] buffer = new byte[2048];
				int bytesRead = input.read(buffer);

				//
				// make the copy!
				//
				while (bytesRead >= 0) {
					output.write(buffer, 0, bytesRead);
					output.flush();

					bytesRead = input.read(buffer);
				}
			}

			finally {
				if (input != null)
					input.close();

				if (output != null)
					output.close();
			}
		}
	}

	/**
	 * 
	 * Determines what type of archive file (zip, jar, etc.) the file is and
	 * returns the appropriate ZipFile instance.
	 * 
	 */
	public static ZipFile getArchiveFile(File file) throws IOException {
		String path = file.getAbsolutePath();
		ZipFile archive = null;

		//
		// we can support either jar or zip files. if the extension is
		// not .jar, we will try to open it as a zip file
		//

		if (path.endsWith(".jar"))
			archive = new JarFile(path);

		else
			archive = new ZipFile(path);

		return archive;
	}

	/**
	 * 
	 * Determines what type of archive file (zip, jar, etc.) the file is and
	 * returns the appropriate ZipOutputStream for writing to it. If the archive
	 * already exists, the stream appends new entries to the end of the file.
	 * 
	 */
	public static ZipOutputStream getArchiveOutputStream(File file) throws IOException {
		//
		// check the extension to see what kind of archive it is
		//
		String path = file.getAbsolutePath();

		FileOutputStream output = null;
		ZipOutputStream archive = null;

		//
		// we want to append to the archive if it already exists
		//
		output = new FileOutputStream(file, true);

		//
		// we can support either jar or zip files. if the extension is
		// not .jar, we will try to open it as a zip file
		//

		if (path.endsWith("jar"))
			archive = new JarOutputStream(output);

		else
			archive = new ZipOutputStream(output);

		return archive;
	}

}
