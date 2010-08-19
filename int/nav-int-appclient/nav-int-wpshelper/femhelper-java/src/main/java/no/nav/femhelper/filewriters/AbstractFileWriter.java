package no.nav.femhelper.filewriters;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AbstractFileWriter {
	/**
	 * Logger instance
	 */
	private Logger LOGGER = Logger.getLogger(AbstractFileWriter.class.getName());

	/**
	 * Private constant for this class to organize the content properly in the
	 * output file
	 */
	protected static final String TABULATOR = "\t\t";
	/**
	 * Separator to use in CSV file
	 */
	protected static final String separator = ";";
	/**
	 * TODO AR Replace with a Apache Commons StringUtils.EMPTY
	 */
	protected static final String EMPTY = " ";
	/**
	 * Used to do all the printing. Declared as BufferedWriter and not just
	 * Writer to inherit the <code>newLine()</code> method
	 */
	protected BufferedWriter writer;

	/**
	 * Close the EventWriterFile and flush all
	 */
	public void close() {
		try {
			if (null != writer) {
				writer.close();
			}
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Error closing writer", e);
		}
	}
}
