package no.nav.datapower.util;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.DirectoryWalker;

public class DPFileUtils {
	
	private static class FileLister extends RecursiveLister {
		
		@Override
		protected void handleFile(File file, int depth, Collection results) {
			results.add(file);
		}
	}
	
	private static class FolderLister extends RecursiveLister {
				
		@Override
		protected boolean handleDirectory(File directory, int depth, Collection results) {
			results.add(directory);
			return true;
		}
	}
	private static abstract class RecursiveLister extends DirectoryWalker {
	
		public List<File> list(File dir) throws IOException {
			List<File> list = DPCollectionUtils.newArrayList();
			walk(dir, list);
			return list;
		}
	}

	
	private DPFileUtils() {}
	
	public static File getRelativePath(File file, File relativeTo) {
		//return new File(DPFilenameUtils.getRelativePath(file.getPath(), relativeTo.getPath()));
		if (!relativeTo.isDirectory())
			throw new IllegalArgumentException("the File specified as relativeTo is not a directory");
		String fileAbsPath = file.getAbsolutePath();
		String relativeToAbsPath = relativeTo.getAbsolutePath();
		return new File(fileAbsPath.substring(relativeToAbsPath.length()+1));
	}
	
	public static List<File> getFileList(File root) throws IOException {
		return new FileLister().list(root);
	}
	
	public static List<File> getFolderList(File root) throws IOException {
		return new FolderLister().list(root);
	}

	public static List<File> getFolderListExcludeRoot(File root) throws IOException {
		List<File> list =  new FolderLister().list(root);
		if (list.contains(root)) {
			list.remove(root);
		}
		return list;
	}
	
	public static List<File> getFolderListRelativeTo(File root, File relativeTo) throws IOException {
		return getRelativePathList(getFolderList(root), relativeTo);
	}
	
	public static List<File> getRelativePathList(List<File> filePaths, File relativeTo) {
		List<File> relPaths = DPCollectionUtils.newArrayList();
		for (File filePath : filePaths) {
			relPaths.add(getRelativePath(filePath, relativeTo));
		}
		return relPaths;
	}
		
	public static byte[] createZipArchive(byte[] data, String entryName) throws IOException {
		ByteArrayOutputStream zipData = new ByteArrayOutputStream();
		ZipOutputStream output = new ZipOutputStream(zipData);
		output.putNextEntry(new ZipEntry(entryName));
		output.write(data);
		output.flush();
		output.close();
		zipData.flush();
		return zipData.toByteArray();
	}
	
	public static void extractArchive(File archive, File destinationFolder) throws IOException {
		destinationFolder.mkdirs();
		DPZipUtils.extract2(archive, destinationFolder);
	}


	public static String replaceSeparator(File file, char newSeparator) {
		return file.getPath().replace(file.separatorChar, newSeparator);
	}
		
	public static void scanFolders(File parent, List<File> folders) {
		File[] children = parent.listFiles();
		if (children != null) {
			for (File child : children) {
				if (child.isDirectory()) {
					folders.add(child);
					scanFolders(child, folders);
				}
			}
		}
	}

	public static List<File> getRelativeFolderPaths(File root, File relativeTo) throws IOException {
		List<File> relativePaths = DPCollectionUtils.newArrayList();
		for (File folder : getFolderListExcludeRoot(root)) {
			relativePaths.add(DPFileUtils.getRelativePath(folder, relativeTo));
		}
		return relativePaths;
	}

	public static List<File> getRelativeFolderPaths(File root) throws IOException {
		return getRelativeFolderPaths(root, root);
	}

	public static String base64EncodeFile(File file) throws IOException {
		DataInput input = new DataInputStream(new FileInputStream(file));
		int length = new Long(file.length()).intValue();
		byte[] buf = new byte[length];
		input.readFully(buf, 0, length);
		return new String(Base64.encodeBase64(buf));
	}
	
	public static File findDirectory(File rootDir, String dirToFind) {
		if(!rootDir.isDirectory())
			throw new IllegalArgumentException("Specified path is not a directory");
		File[] children = rootDir.listFiles();
		for (File child : children) {
			if (child.isDirectory() && child.getName().equals(dirToFind))
				return child;
		}
		for (File child : children) {
			if (child.isDirectory())
				return findDirectory(rootDir, dirToFind);
		}
		throw new IllegalArgumentException("Specified directory tree does not contain a '" + dirToFind + "' directory");
	}
}
