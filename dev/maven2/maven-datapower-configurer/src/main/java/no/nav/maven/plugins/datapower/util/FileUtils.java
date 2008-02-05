package no.nav.maven.plugins.datapower.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import no.nav.maven.plugins.ZipUtils;
import no.nav.maven.plugins.datapower.DeviceFileStore;
import no.nav.maven.plugins.datapower.XMLMgmtRequest;
import no.nav.maven.plugins.datapower.command.CreateDirCommand;
import no.nav.maven.plugins.datapower.command.SetFileCommand;

import org.apache.commons.codec.binary.Base64;


public class FileUtils {
	
	public static String getFileAsString(File file) throws IOException {
		StringBuffer content = new StringBuffer();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		while ((line = reader.readLine()) != null) {
			content.append(line);
			content.append(System.getProperty("line.separator"));
		}
		reader.close();
		return content.toString();
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
		ZipUtils.extract2(archive, destinationFolder);
	}

	public static String getRelativePath(File path, File folder) {
		String strPath = path.getAbsolutePath();
		String strTmpFolder = folder.getAbsolutePath();
		strPath = strPath.replaceAll("\\\\", "/");
		strTmpFolder = strTmpFolder.replaceAll("\\\\", "/");
		return strPath.replaceAll(strTmpFolder, "").substring(1);
	}	
	
	public static void scanFilesAndFolders(File root, File parent, List directories, Map files) throws IOException {
		File[] children = parent.listFiles();
		File child;

		for (int i = 0; i < children.length; i++) {
			child = children[i];
			if (child.isDirectory()) {
				String folderPath = getRelativePath(child, root);
				System.out.println("Folder path = " + folderPath);
				directories.add(folderPath);
				scanFilesAndFolders(root, child, directories, files);
			} else if (child.getName().toLowerCase().compareTo("manifest.mf") != 0) {
				String filePath = getRelativePath(child, root);
				System.out.println("File path = " + filePath);
				files.put(filePath, base64EncodeFile(child));
			}
		}
	}
	public static void scanFilesAndFolders(File root, File parent, XMLMgmtRequest request) throws IOException {
		File[] children = parent.listFiles();
		File child;

		for (int i = 0; i < children.length; i++) {
			child = children[i];
			if (child.isDirectory()) {
				String folderPath = getRelativePath(child, root);
				System.out.println("Folder path = " + folderPath);
				request.addCommand(new CreateDirCommand(DeviceFileStore.LOCAL, folderPath));
				scanFilesAndFolders(root, child, request);
			} else if (child.getName().toLowerCase().compareTo("manifest.mf") != 0) {
				String filePath = getRelativePath(child, root);
				System.out.println("File path = " + filePath);
				request.addCommand(new SetFileCommand(DeviceFileStore.LOCAL, filePath, base64EncodeFile(child)));
			}
		}
	}
	
	public static String base64EncodeFile(File file) throws IOException {
		DataInput input = new DataInputStream(new FileInputStream(file));
		int length = new Long(file.length()).intValue();
		byte[] buf = new byte[length];
		input.readFully(buf, 0, length);
		return new String(Base64.encodeBase64(buf));
	}
}
