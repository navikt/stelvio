package no.nav.maven.plugins.common.utils;



import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
	
	public static List<File> listFiles(File root, boolean recursive, String namePattern){
		List<File> paths = new ArrayList<File>();
		if(recursive){
			File[] tmp = root.listFiles();
			for(File f : tmp){
				if(f.isDirectory()) paths.addAll(listFiles(f, true));
				else paths.add(f);
			}
		}else{
			paths.addAll(toList(root.listFiles()));
		}
		
		return filterList(paths, namePattern);
	}
	
	public static List<File> listFiles(File root, boolean recursive){
		List<File> paths = new ArrayList<File>();
		if(recursive){
			File[] tmp = root.listFiles();
			for(File f : tmp){
				if(f.isDirectory()) paths.addAll(listFiles(f, true));
				else paths.add(f);
			}
		}else{
			paths.addAll(toList(root.listFiles()));
		}
		
		return paths;
	}
	
	/**
	 * Deletes a path structure recursively
	 * 
	 * @param delMe the path of the config-folder to delete.
	 * 
	 * @return true if path was successfully deleted, otherwise false
	 */
	public static boolean recursiveDelete(File path) {
		if (path.isDirectory()) {
			File[] children = path.listFiles();
			for (int i = 0; i < children.length; i++) {
				System.gc();
				boolean success = recursiveDelete(children[i]);
				if (!success) {
					return false;
				}
			}
		}
		System.gc();
		return path.delete();

	}
	
	public static void copyFile(File src, File dest) throws IOException {
		FileChannel in = new FileInputStream(src).getChannel();
		FileChannel out = new FileOutputStream(dest).getChannel();

		long size = in.size();
		MappedByteBuffer buf = in.map(FileChannel.MapMode.READ_ONLY, 0, size);
		out.write(buf);
	}
	
	private static List<File> toList(File[] paths){
		List<File> ret = new ArrayList<File>(paths.length);
		for(File p : paths){
			ret.add(p);
		}
		return ret;
	}
	
	private static List<File> filterList(List<File> files, String namePattern){
		List<File> localFiles = new ArrayList<File>();
		for(File f : files){
			if(f.getName().contains(namePattern)) localFiles.add(f);
		}
		return localFiles;
	}
}
