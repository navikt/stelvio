package no.nav.datapower.util;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.lang.Validate;

public class DPFileUtils {
		
	private static class FileLister extends RecursiveLister {
		
		@Override
		protected void handleFile(File file, int depth, Collection results) {
			results.add(file);
		}
	}

	private static class FileFinder extends RecursiveLister {
		
		private FileFilter filter;
		public FileFinder(String filterString) {
			this.filter = new WildcardPathFilter(filterString);
		}
		
		@Override
		protected void handleFile(File file, int depth, Collection results) {
			if (filter.accept(file))
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
	
		public List<File> list(File root) throws IOException {
			List<File> list = DPCollectionUtils.newArrayList();
			walk(root, list);
			return list;
		}
	}
	
	private static class DirectoryFinder extends DirectoryWalker {
		private final String directoryName;
		public DirectoryFinder(final String directoryName) {
			//super(FileFilterUtils.nameFileFilter(directoryName),-1);
			this.directoryName = directoryName;
//			super(new FileFilter() {
//
//				public boolean accept(File pathname) {
//					return pathname.isDirectory() && pathname.getName().equals(directoryName);
//				}				
//			}, FileFilterUtils.falseFileFilter(),-1);
		}
		@Override
		protected boolean handleDirectory(File directory, int depth, Collection results) {
			if (directory.getName().equals(directoryName)) {
//				System.out.println("DirectoryFinder.handleDirectory(), dir = " + directory);
				results.add(directory);
				return false;
			}
			return true;
		}
		
		public File getDirectory(File root) throws IOException {
			List<File> list = DPCollectionUtils.newArrayList();
			walk(root, list);
			if (list.isEmpty())
				throw new IllegalArgumentException("The File specified as root is not a directory");
			return list.get(0);
		}
	}

	private DPFileUtils() {}


	public static boolean containsFiles(File root) {
		return root.listFiles((FileFilter) FileFilterUtils.fileFileFilter()).length > 0;
	}

	public static boolean containsDirectories(File root) {
		return root.listFiles((FileFilter) FileFilterUtils.directoryFileFilter()).length > 0;
	}

	public static List<File> listFiles(File root) {
		return Arrays.asList(root.listFiles((FileFilter) FileFilterUtils.fileFileFilter()));
	}
	
	public static List<File> listDirectories(File root) {
		return Arrays.asList(root.listFiles((FileFilter) FileFilterUtils.directoryFileFilter()));
	}

	public static void deleteFilesFromDirectory(File root) {
		for (File file : listFiles(root)) {
			file.delete();
		}
	}
	
//	public static <T> File getResource(Class<T> clazz, String resource) {
	public static File getResource(Class clazz, String resource) {
		URL url = clazz.getResource(resource);
		File file = FileUtils.toFile(url);
//		Validate.notNull(file, "Resource '" + resource + "' was not found using ClassLoader for class '" + clazz.getName() + "'");
		Validate.notNull(file, "Resource '" + resource + "' was not found in '" + clazz.getResource("/") + "' using ClassLoader for class '" + clazz.getName() + "'");
		return file;
	}
	
	public static List<File> createSubDirectories(File rootDir, String... dirs) {
		List<File> subDirs = DPCollectionUtils.newArrayList();
		rootDir.mkdirs();
		for (String dir : dirs) {
			File subDir = append(rootDir, dir);
			subDir.mkdir();
			subDirs.add(subDir);
		}
		return subDirs;
	}
	
	public static File append(File directory, String path) {
		return new File(directory.getAbsolutePath() + File.separator + path);
	}
	
	public static void copyFilesToDirectory(List<File> files, File targetDir) throws IOException {
		for (File file : files) {
			FileUtils.copyFileToDirectory(file, targetDir);
		}
	}

	public static File mkdirs(File directory, String path) {
		File newDir = append(directory, path);
		newDir.mkdirs();
		return newDir;
	}
	
	public static File getRelativePath(File file, File relativeTo) {
		//return new File(DPFilenameUtils.getRelativePath(file.getPath(), relativeTo.getPath()));
		if (!relativeTo.isDirectory())
			throw new IllegalArgumentException("The File specified as relativeTo is not a directory");
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

	public static void extractArchiveFiltered(File inputFile, File outDir, IOFileFilter filter) throws IOException {
		outDir.mkdirs();
		ZipFile zipFile = new ZipFile(inputFile);
		Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
		while (enumeration.hasMoreElements()) {
			ZipEntry zipEntry = enumeration.nextElement();
			String zipEntryName = zipEntry.getName();
			File zipFileName = new File(outDir + "/" + zipEntryName.substring(zipEntryName.indexOf('/')));
			if (filter.accept(new File(zipEntryName))) {
				zipFileName.getParentFile().mkdirs();
				InputStream inputStream = zipFile.getInputStream(zipEntry);
				OutputStream out = new FileOutputStream(zipFileName);
				DPStreamUtils.pump(inputStream, out);
				out.close();
				inputStream.close();
			}
		}
	}
	
	public static String replaceSeparator(File file, char newSeparator) {
		return file.getPath().replace(File.separatorChar, newSeparator);
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
	
	public static File findDirectory(File rootDir, String dirToFind) throws IOException {
		if(!rootDir.isDirectory())
			throw new IllegalArgumentException("Specified path is not a directory");
		return new DirectoryFinder(dirToFind).getDirectory(rootDir);
	}
	
	public static List<File> findFilesRecursively(File rootDir, String filterString) throws IOException {
		if(!rootDir.isDirectory())
			throw new IllegalArgumentException("Specified path is not a directory");
		return new FileFinder(filterString).list(rootDir);
	}

	
	public static List<File> getFileListFiltered(File dir, FileFilter filter) {
		return Arrays.asList(dir.listFiles(filter));
	}

}
