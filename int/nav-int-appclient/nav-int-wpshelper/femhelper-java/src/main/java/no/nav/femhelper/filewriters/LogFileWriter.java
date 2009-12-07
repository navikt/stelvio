package no.nav.femhelper.filewriters;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import no.nav.femhelper.common.Constants;

import org.apache.commons.lang.StringUtils;

public class LogFileWriter extends AbstractFileWriter {

	/**
	 * Logger instance
	 */
	private Logger LOGGER = Logger.getLogger(LogFileWriter.class.getName());

	/**
	 * Default parameterized constructor
	 * 
	 * @param path
	 *            path
	 * @param filename
	 *            filename
	 * @throws IOException
	 */
	public LogFileWriter(String path, String filename) throws IOException {
		if (StringUtils.isEmpty(path)) {
			String tempFolderProperty = "java.io.tmpdir";
			String tempFolder = System.getProperty(tempFolderProperty);
			path = tempFolder;
		}

		String completePath = path + File.separatorChar + filename;
		LOGGER.log(Level.FINE, "Creating instance of BufferedWriter with path: " + completePath);
		writer = new BufferedWriter(new FileWriter(completePath, true));
	}

	public void log(String string) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT_MILLS);
		String date = sdf.format(Calendar.getInstance().getTime());
		writer.write(date + TABULATOR + string);
		writer.newLine();
		writer.flush();
	}
}
