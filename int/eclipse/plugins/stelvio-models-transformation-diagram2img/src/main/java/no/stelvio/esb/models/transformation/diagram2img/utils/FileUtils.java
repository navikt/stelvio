package no.stelvio.esb.models.transformation.diagram2img.utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void copyDirectory(File srcDir, File destDir)
			throws IOException {
		if (srcDir == null) {
			throw new NullPointerException("Source must not be null");
		}
		if (destDir == null) {
			throw new NullPointerException("Destination must not be null");
		}
		if (!srcDir.exists()) {
			throw new FileNotFoundException("Source '" + srcDir
					+ "' does not exist");
		}
		if (!srcDir.isDirectory()) {
			throw new IOException("Source '" + srcDir
					+ "' exists but is not a directory");
		}
		if (srcDir.getCanonicalPath().equals(destDir.getCanonicalPath())) {
			throw new IOException("Source '" + srcDir + "' and destination '"
					+ destDir + "' are the same");
		}

		List exclusionList = null;
		if (destDir.getCanonicalPath().startsWith(srcDir.getCanonicalPath())) {
			File[] srcFiles = srcDir.listFiles();
			if ((srcFiles != null) && (srcFiles.length > 0)) {
				exclusionList = new ArrayList(srcFiles.length);
				for (File srcFile : srcFiles) {
					File copiedFile = new File(destDir, srcFile.getName());
					exclusionList.add(copiedFile.getCanonicalPath());
				}
			}
		}
		doCopyDirectory(srcDir, destDir, exclusionList);
	}

	@SuppressWarnings("rawtypes")
	private static void doCopyDirectory(File srcDir, File destDir,
			List exclusionList) throws IOException {
		File[] files = srcDir.listFiles();
		if (files == null) {
			throw new IOException("Failed to list contents of " + srcDir);
		}
		if (destDir.exists()) {
			if (!destDir.isDirectory()) {
				throw new IOException("Destination '" + destDir
						+ "' exists but is not a directory");
			}
		} else if (!destDir.mkdirs()) {
			throw new IOException("Destination '" + destDir
					+ "' directory cannot be created");
		}

		if (!destDir.canWrite()) {
			throw new IOException("Destination '" + destDir
					+ "' cannot be written to");
		}
		for (File file : files) {
			File copiedFile = new File(destDir, file.getName());
			if ((exclusionList == null)
					|| (!exclusionList.contains(file.getCanonicalPath()))) {
				if (file.isDirectory())
					doCopyDirectory(file, copiedFile, exclusionList);
				else {
					doCopyFile(file, copiedFile);
				}
			}

		}

		destDir.setLastModified(srcDir.lastModified());

	}

	private static void doCopyFile(File srcFile, File destFile)
			throws IOException {
		if ((destFile.exists()) && (destFile.isDirectory())) {
			throw new IOException("Destination '" + destFile
					+ "' exists but is a directory");
		}

		FileInputStream fis = null;
		FileOutputStream fos = null;
		FileChannel input = null;
		FileChannel output = null;
		try {
			fis = new FileInputStream(srcFile);
			fos = new FileOutputStream(destFile);
			input = fis.getChannel();
			output = fos.getChannel();
			long size = input.size();
			long pos = 0L;
			long count = 0L;
			while (pos < size) {
				count = size - pos > 52428800L ? 52428800L : size - pos;
				pos += output.transferFrom(input, pos, count);
			}
		} finally {
			closeQuietly(output);
			closeQuietly(fos);
			closeQuietly(input);
			closeQuietly(fis);
		}

		if (srcFile.length() != destFile.length()) {
			throw new IOException("Failed to copy full contents from '"
					+ srcFile + "' to '" + destFile + "'");
		}

		destFile.setLastModified(srcFile.lastModified());
	}

	public static void closeQuietly(Closeable closeable) {
		try {
			if (closeable != null)
				closeable.close();
		} catch (IOException ioe) {
		}
	}

	public static String getFullPath(String filename) {
		if (filename == null) {
			return null;
		}
		int prefix = getPrefixLength(filename);
		if (prefix < 0) {
			return null;
		}
		if (prefix >= filename.length()) {
			return getPrefix(filename);
		}

		int index = indexOfLastSeparator(filename);
		if (index < 0) {
			return filename.substring(0, prefix);
		}
		int end = index + 1;
		if (end == 0) {
			end++;
		}
		return filename.substring(0, end);
	}

	public static String getPrefix(String filename) {
		if (filename == null) {
			return null;
		}
		int len = getPrefixLength(filename);
		if (len < 0) {
			return null;
		}
		if (len > filename.length()) {
			return filename + '/';
		}
		return filename.substring(0, len);
	}

	public static int indexOfLastSeparator(String filename) {
		if (filename == null) {
			return -1;
		}
		int lastUnixPos = filename.lastIndexOf('/');
		int lastWindowsPos = filename.lastIndexOf('\\');
		return Math.max(lastUnixPos, lastWindowsPos);
	}

	public static int getPrefixLength(String filename) {
		if (filename == null) {
			return -1;
		}
		int len = filename.length();
		if (len == 0) {
			return 0;
		}
		char ch0 = filename.charAt(0);
		if (ch0 == ':') {
			return -1;
		}
		if (len == 1) {
			if (ch0 == '~') {
				return 2;
			}
			return isSeparator(ch0) ? 1 : 0;
		}
		if (ch0 == '~') {
			int posUnix = filename.indexOf('/', 1);
			int posWin = filename.indexOf('\\', 1);
			if ((posUnix == -1) && (posWin == -1)) {
				return len + 1;
			}
			posUnix = posUnix == -1 ? posWin : posUnix;
			posWin = posWin == -1 ? posUnix : posWin;
			return Math.min(posUnix, posWin) + 1;
		}
		char ch1 = filename.charAt(1);
		if (ch1 == ':') {
			ch0 = Character.toUpperCase(ch0);
			if ((ch0 >= 'A') && (ch0 <= 'Z')) {
				if ((len == 2) || (!isSeparator(filename.charAt(2)))) {
					return 2;
				}
				return 3;
			}
			return -1;
		}
		if ((isSeparator(ch0)) && (isSeparator(ch1))) {
			int posUnix = filename.indexOf('/', 2);
			int posWin = filename.indexOf('\\', 2);
			if (((posUnix == -1) && (posWin == -1)) || (posUnix == 2)
					|| (posWin == 2)) {
				return -1;
			}
			posUnix = posUnix == -1 ? posWin : posUnix;
			posWin = posWin == -1 ? posUnix : posWin;
			return Math.min(posUnix, posWin) + 1;
		}
		return isSeparator(ch0) ? 1 : 0;
	}

	private static boolean isSeparator(char ch) {
		return (ch == '/') || (ch == '\\');
	}
}
