package no.stelvio.maven.build.plugin.utils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

/**
 * Util-class that searches recursively through a directory tree and gets all pom.xml files
 * 
 * @author test@example.com
 */
public class PomSearchUtil {
	
	private static ArrayList<File> poms = new ArrayList<File>();
	
	private static void listFiles(File dir){
		File[] entries = dir.listFiles(new MyFileFilter());
		for (int i = 0; i<entries.length;i++){
			if (entries[i].isDirectory()) listFiles(entries[i]);
			else poms.add(entries[i]);
		}
	}
	
	/**
	 * Get list of pom files
	 * @param dir - search root folder
	 * @return list of pom files
	 */
	public static ArrayList<File> SearchForPoms(File dir) {
		listFiles(dir);
		return poms;
	} 
	
	/**
	 * Get list of pom files
	 * @param dir - search root folder
	 * @return list of pom files
	 */
	public static ArrayList<File> SearchForPoms(String dir){
		File f = new File(dir);
		return SearchForPoms(f);
	}
}

/**
 * Implementation of FileFilter interface. Lists directories and pom.xml files. 
 */
class MyFileFilter implements FileFilter{

	@Override
	public boolean accept(File pathname) {
		return pathname.isDirectory() || pathname.toString().endsWith("pom.xml");
	}	
}
