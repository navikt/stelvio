package no.nav.maven.plugins.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipUtils {
	private static final int BUF_SIZE = 2048;
	
	public static void compress(File source, File destination) throws IOException {
		Map files = new HashMap();
		final String basedir = source.getName();
		addFiles(source, files, "", basedir);
		compress(files, destination);

	}
	
	/*public static void compress(File source, File destination) throws IOException {
		de.schlichtherle.io.File zip = new de.schlichtherle.io.File(destination);
		zip.copyAllFrom(source,ArchiveDetector.ALL);
		zip.umount();
	}*/
	
	
	
	private static String streamToString(InputStream stream) throws IOException{
		byte[] content = new byte[stream.available()];
		stream.read(content, 0, content.length);
		return new String(content);
	}
	
	private static void addFiles(File source, Map files, String path, String basedir) throws IOException {
		if (source.isDirectory()) {
			File[] subfiles = source.listFiles();
			for (int i = 0; i < subfiles.length; i++) {
				if (source.getName().equals(basedir)) {
					addFiles(subfiles[i], files, path, basedir);
				} else {
					if (path.equals("")) {
						addFiles(subfiles[i], files, source.getName(), basedir);
					} else {
						addFiles(subfiles[i], files, path + "/" + source.getName(), basedir);
					}
				}
			}
		} else {
			if (path.equals("")) {
				files.put(source.getName(), source);
			} else {
				files.put(path + "/" + source.getName(), source);
			}
		}
	}

	/**
	 * 
	 * Zips up the source files provided and creates an archive using the given
	 * destination.
	 * 
	 */
	private static void compress(Map sourceFiles, File destination) throws IOException {
		ZipOutputStream output = null;

		output = getArchiveOutputStream(destination);

		Set keys = sourceFiles.keySet();
		for (Iterator iter = keys.iterator(); iter.hasNext();) {
			String path = (String) iter.next();
			File file = (File) sourceFiles.get(path);
			final String filename = path;
			ZipEntry entry = new ZipEntry(filename);
			output.putNextEntry(entry);
			byte[] buffer = new byte[BUF_SIZE];
			int bytes = 0;
			FileInputStream str = new FileInputStream(file);
			while ((bytes = str.read(buffer, 0, buffer.length)) > 0) {
				for (int i = 0; i < bytes; i++) {
					output.write((byte) buffer[i]);
				}
			}
		}

		output.close();

	}
	
	public static void extract(File inputFile, File outDir) throws ZipException, IOException{
		ZipFile zipFile = new ZipFile(inputFile);
		Enumeration enumeration = zipFile.entries();
		while (enumeration.hasMoreElements()) {
			ZipEntry zipEntry = (ZipEntry)enumeration.nextElement();
			String zipFileName = zipEntry.getName();
			if(zipEntry.isDirectory()){
				new File(outDir + "/" + zipEntry.getName()).mkdirs();
			}else{
				//check for complex path entry
				if(zipFileName.lastIndexOf("/") >= 0){
					String path = zipFileName.substring(0, zipFileName.lastIndexOf("/"));
					new File(outDir + "/" + path).mkdirs();
				}
				
				InputStream inputStream = zipFile.getInputStream(zipEntry);
				OutputStream out = new FileOutputStream(outDir + "/" + zipFileName);
				writeInputStreamToOutputStream(inputStream, out);
				out.close();
				inputStream.close();
			}
		}
	}
	
	private static void writeInputStreamToOutputStream(InputStream in, OutputStream out) throws IOException{
		byte[] buf = new byte[BUF_SIZE];
		int len;
		while((len=in.read(buf))>0){
			out.write(buf,0,len);
		}
	}

	/**
	 * 
	 * Determines what type of archive file (zip, jar, etc.) the file is and
	 * returns the appropriate ZipFile instance.
	 * 
	 */
	private static ZipFile getArchiveFile(File file) throws IOException {
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
	private static ZipOutputStream getArchiveOutputStream(File file) throws IOException {
		//
		// check the extension to see what kind of archive it is
		//
		String path = file.getAbsolutePath();

		FileOutputStream output = null;
		ZipOutputStream archive = null;

		//
		// we want to append to the archive if it already exists
		//
		output = new FileOutputStream(file);

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
