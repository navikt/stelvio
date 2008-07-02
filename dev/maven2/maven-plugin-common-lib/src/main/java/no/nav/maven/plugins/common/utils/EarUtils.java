package no.nav.maven.plugins.common.utils;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipException;

public class EarUtils {
	public static final String EJB_SUBPATH = "/ejbjar";
	
	private static Map<String, String> flattenLog;

	private static Map<String, String> tagLog;
	
	/**
	 * Extracts an ear file to a target directory appended with the ear file
	 * name, and searches for an inner EJB module and extracts that as well
	 * 
	 * @param earFile,
	 *            ear file to extract
	 * @param targetDir,
	 *            folder to put the extracted content
	 * @throws IOException
	 * @throws ZipException
	 */
	public static void extractEarAndInnerModule(File earFile, File targetDir)
			throws ZipException, IOException {
		File earExtractDir, ejbDir = null;

		earExtractDir = new File(targetDir.getAbsolutePath() + "/"
				+ earFile.getName().replaceAll(".ear", ""));
		earExtractDir.delete();
		earExtractDir.mkdirs();
		ZipUtils.extract(earFile, earExtractDir);

		// searching for inner EJB module
		File[] ejb = earExtractDir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith("EJB.jar");
			}
		});

		if (ejb != null && ejb.length == 1) {
			ejbDir = new File(earExtractDir.getAbsolutePath() + EJB_SUBPATH);
			ejbDir.delete();
			ejbDir.mkdirs();
			ZipUtils.extract(ejb[0], ejbDir);
			ejb[0].delete();
		}
	}

	/**
	 * compresses and ear directory back to an ear archive (supports inner
	 * extracted EJB modules)
	 * 
	 * @param extractDir,
	 *            folder containing the extracted ear
	 * @param outputFile,
	 *            ear archive to create
	 * @throws IOException
	 */
	public static void compressEarAndInnerModule(File extractDir,
			File outputFile) throws IOException {
		// searching for inner EJB module
		File ejbFolder = new File(extractDir.getAbsolutePath() + EJB_SUBPATH);
		if (ejbFolder.exists()) {
			File ejbModule = new File(extractDir + "/" + extractDir.getName()
					+ "EJB.jar");
			ejbModule.delete();
			
			
			ZipUtils.compress(ejbFolder, ejbModule);
			
			FileUtils.recursiveDelete(ejbFolder);
		}
		
		ZipUtils.compress(extractDir, outputFile);
	}

	public static void flattenEarStructure(File earFolder, File targetFolder)
			throws IOException {
		flattenLog = new HashMap<String, String>();
		tagLog = new HashMap<String, String>();

		
		List<File> earFiles = FileUtils.listFiles(earFolder, true,".ear");
		for (File f : earFiles) {
			File targetEar = new File(targetFolder.getAbsolutePath() + "/"
					+ f.getName());
			tagLog.put(f.getName(), f.getParentFile().getName());
			flattenLog.put(targetEar.getAbsolutePath(), f.getAbsolutePath());
			copyFile(f, targetEar);
		}
	}

	public static void restoreEarTagStructure() throws IOException{
		File srcFile, destFile;
		for(String fileName : flattenLog.keySet()){
			srcFile = new File(fileName);
			destFile = new File(flattenLog.get(fileName));
			
			if(!srcFile.renameTo(destFile)){
				throw new IOException("Error moving '" + srcFile.getAbsolutePath() + "' to '" + destFile.getAbsolutePath() + "'");
			}
		}
	}

	private static void copyFile(File src, File dest) throws IOException {
		FileChannel in = new FileInputStream(src).getChannel();
		FileChannel out = new FileOutputStream(dest).getChannel();

		long size = in.size();
		MappedByteBuffer buf = in.map(FileChannel.MapMode.READ_ONLY, 0, size);
		out.write(buf);
	}

	public static Map<String, String> getTagLog() {
		return tagLog;
	}

	public static void setTagLog(Map<String, String> tagLog) {
		EarUtils.tagLog = tagLog;
	}
}
