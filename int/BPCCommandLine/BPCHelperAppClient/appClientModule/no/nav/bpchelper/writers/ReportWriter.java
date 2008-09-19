package no.nav.bpchelper.writers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Ostermiller.util.CSVPrinter;
import com.ibm.bpe.clientmodel.bean.ProcessInstanceBean;

public class ReportWriter {
    /**
         * Locale used to format labels
         */
    private static final Locale LOCALE = Locale.getDefault();

    /**
         * Delimiter to use in CSV file
         */
    private static final char DELIMITER = ';';

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private CSVPrinter csvPrinter;

    public ReportWriter() {
	this(createDefaultFile());
    }

    public ReportWriter(File file) {
	try {
	    this.csvPrinter = new CSVPrinter(new BufferedWriter(new FileWriter(file)));
	    this.csvPrinter.changeDelimiter(DELIMITER);
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
	logger.info("Writing report to file: " + file.getAbsolutePath());
    }

    private static File createDefaultFile() {
	try {
	    return File.createTempFile("report", ".csv");
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

    public void close() {
	try {
	    csvPrinter.close();
	} catch (Exception e) {
	    logger.warn("Error closing writer", e);
	}
	csvPrinter = null;
    }

    public void writeHeader() {
	Collection<String> line = new ArrayList<String>();
	line.add(ProcessInstanceBean.getLabel(ProcessInstanceBean.NAME_PROPERTY, LOCALE));
	line.add(ProcessInstanceBean.getLabel(ProcessInstanceBean.PROCESSTEMPLATENAME_PROPERTY, LOCALE));
	line.add(ProcessInstanceBean.getLabel(ProcessInstanceBean.EXECUTIONSTATE_PROPERTY, LOCALE));
	line.add(ProcessInstanceBean.getLabel(ProcessInstanceBean.STARTTIME_PROPERTY, LOCALE));
	csvPrinter.println(line.toArray(new String[line.size()]));
    }

    public void writeProcessInstance(ProcessInstanceBean processInstanceBean) {
	Collection<String> line = new ArrayList<String>();
	line.add(processInstanceBean.getName());
	line.add(processInstanceBean.getProcessTemplateName());
	line.add(ProcessInstanceBean.getConverter(ProcessInstanceBean.EXECUTIONSTATE_PROPERTY).getAsString(processInstanceBean.getExecutionState(),
		LOCALE));
	line.add(ProcessInstanceBean.getConverter(ProcessInstanceBean.STARTTIME_PROPERTY).getAsString(processInstanceBean.getStartTime(), LOCALE));
	csvPrinter.println(line.toArray(new String[line.size()]));
    }
}
