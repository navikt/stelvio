package no.nav.datapower.util;

import java.io.*;
import java.util.*;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.*;

import org.apache.commons.io.filefilter.IOFileFilter;

public class DPZipUtils {
	
	public static void compress(File source, File destination) throws IOException {
		Map files = new HashMap();
		final String basedir = source.getName();
		addFiles(source, files, "", basedir);
		compress(files, destination);
		
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
			byte[] buffer = new byte[10240];
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
	
	public static void extractExclude(File source, File dest, IOFileFilter filter) {
		
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
				byte[] buffer = new byte[10240];
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
	
	public static void writeZipEntryToOutputStream(ZipFile zipFile, ZipEntry zipEntry, OutputStream outStream) throws IOException {
		InputStream inStream = zipFile.getInputStream(zipEntry);
		writeInputStreamToOutputStream(inStream, outStream);
		outStream.close();
		inStream.close();
	}
	
	public static void extract2(File inputFile, File outDir) throws ZipException, IOException{
		ZipFile zipFile = new ZipFile(inputFile);
		Enumeration enumeration = zipFile.entries();
		/*while (enumeration.hasMoreElements()) {
			ZipEntry zipEntry = (ZipEntry)enumeration.nextElement();
			String zipFileName = zipEntry.getName();
			if (zipEntry.isDirectory()) new File(outDir + "/" + zipFileName).mkdirs();
			else{
				InputStream inputStream = zipFile.getInputStream(zipEntry);
				OutputStream out = new FileOutputStream(outDir + "/" + zipFileName);
				writeInputStreamToOutputStream(inputStream, out);
				out.close();
				inputStream.close();
			}
		}*/
		while (enumeration.hasMoreElements()) {
			ZipEntry zipEntry = (ZipEntry)enumeration.nextElement();
			File zipFileName = new File(outDir + "/" + zipEntry.getName());
			if (zipEntry.isDirectory()) {
				zipFileName.mkdirs();
			}
			else{
				InputStream inputStream = zipFile.getInputStream(zipEntry);
				OutputStream out = new FileOutputStream(zipFileName);
				writeInputStreamToOutputStream(inputStream, out);
				out.close();
				inputStream.close();
			}
		}
	}
	
	private static void writeInputStreamToOutputStream(InputStream in, OutputStream out) throws IOException{
		byte[] buf = new byte[10240];
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
