package no.stelvio.common.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Utility for exporting error codes from java class files,
 * for easy synchronization with persistent storage.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 2570 $ $Author: psa2920 $ $Date: 2005-10-19 18:07:03 +0200 (Wed, 19 Oct 2005) $
 */
public final class ErrorCodesUtil {

	/**
	 * Private Constructor to limit instantiations outside of this class.
	 */
	private ErrorCodesUtil() {
		super();
	}

	/**
	 * Export all error codes in specified java file to specified report file.
	 * 
	 * @param javaFileName complete file path to java file to investigate.
	 * @param reportFileName complete file path to report file to write to
	 */
	public static void export(String javaFileName, String reportFileName) {

		BufferedReader reader = null;
		FileWriter writer = null;
		try {
			reader = new BufferedReader(new FileReader(javaFileName));
			writer = new FileWriter(reportFileName, false);

			String className = javaFileName.substring(javaFileName.lastIndexOf("/") + 1, javaFileName.lastIndexOf("."));

			StringBuffer report = new StringBuffer();
			int constantCount = 0;

			String line = reader.readLine();
			while (line != null) {
				String javadoc = getJavadoc(line);
				line = reader.readLine();
				if (null != javadoc) {
					int start = line.lastIndexOf(className) + className.length() + 1;
					int end = line.lastIndexOf(")");
					String code = line.substring(start, end);
					//System.out.println(code + "\t4\t" + javadoc);
					report.append(code).append("\t4\t").append(javadoc).append("\n");
					constantCount++;
				}

			}
			writer.write(report.toString());

			System.out.println("Found " + constantCount + " error codes in " + className);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Extract the javadoc comment from the specified line.
	 * 
	 * @param line the line of text containing javadoc comment
	 * @return the javadoc comment (no slashes or stars)
	 */
	private static String getJavadoc(String line) {
		if (null == line) {
			return null;
		} else {
			int start = line.indexOf("/**");
			if (start == -1) {
				return null;
			} else {
				start += 3;
				int end = line.indexOf("*/");
				if (end == -1) {
					return null;
				} else {
					return line.substring(start, end).trim();
				}
			}
		}
	}

	/**
	 * Run this utility. Example usage:
	 * 
	 * <p>How to export <b>ErrorCode.java</b></p>
	 * 
	 * <pre>
	 * java -cp CLASSPATH no.stelvio.common.framework util.ErrorCodesUtil \
	 *    E:/data/workspace/head/stelvio-framework-common-java/src/main/java/no/stelvio/common/framework/error/ErrorCode.java \
	 *    E:/data/workspace/head/ErrorCode.java.report
	 * </pre>
	 * 
	 * <p>How to export <b>FrameworkError.java</b></p>
	 * 
	 * <pre>
	 * java -cp CLASSPATH no.stelvio.common.framework util.ErrorCodesUtil \
	 *    E:/data/workspace/head/stelvio-framework-common-java/src/main/java/no/stelvio/common/framework/FrameworkError.java \
	 *    E:/data/workspace/head/FrameworkError.java.report
	 * </pre>
	 * 
	 * <p>How to export <b>ApplicationError.java</b></p>
	 * 
	 * <pre>
	 * java -cp CLASSPATH no.stelvio.common.framework util.ErrorCodesUtil \
	 *    E:/data/workspace/head/rtv-bidrag-common-java/src/main/java/no/stelvio/common/ApplicationError.java \
	 *    E:/data/workspace/head/ApplicationError.java.report
	 * </pre>
	 * 
	 * @param args the array of arguments, length should be 2, where args[0] is javaFilename and args[1] is reportFileName.
	 */
	public static void main(String[] args) {

		if (args.length == 2) {
			ErrorCodesUtil.export(args[0], args[1]);
		} else {
			System.err.println(
				"Usage: java -cp CLASSPATH no.stelvio.common.framework util.ErrorCodesUtil javaFilename reportFileName");
			System.err.println(
				"Example: java -cp CLASSPATH no.stelvio.common.framework util.ErrorCodesUtil "
					+ "E:/data/workspace/head/stelvio-framework-common-java/src/main/java/"
					+ "no/stelvio/common/framework/FrameworkError.java "
					+ "E:/data/workspace/head/FrameworkError.java.report");
			System.exit(1);
		}
	}

}
