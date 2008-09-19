package no.nav.bpchelper.writers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Locale;

import com.ibm.bpe.clientmodel.bean.ProcessInstanceBean;

public class ReportWriter {
    // TODO: Is default locale ok?
    private Locale locale = Locale.getDefault();

    private Appender appender;

    public ReportWriter() {
	this(createDefaultFile());
    }

    public ReportWriter(File file) {
	this.appender = new Appender(file);
	System.out.println("Writing report to file: " + file.getAbsolutePath());
    }

    private static File createDefaultFile() {
	try {
	    return File.createTempFile("report", ".csv");
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

    public void close() {
	appender.close();
	appender = null;
    }

    public void writeHeader() {
	appender.write(ProcessInstanceBean.getLabel(ProcessInstanceBean.NAME_PROPERTY, locale));
	appender.write(ProcessInstanceBean.getLabel(ProcessInstanceBean.PROCESSTEMPLATENAME_PROPERTY, locale));
	appender.write(ProcessInstanceBean.getLabel(ProcessInstanceBean.EXECUTIONSTATE_PROPERTY, locale));
	appender.write(ProcessInstanceBean.getLabel(ProcessInstanceBean.STARTTIME_PROPERTY, locale));
	appender.newLine();
    }

    public void writeProcessInstance(ProcessInstanceBean processInstanceBean) {
	appender.write(processInstanceBean.getName());
	appender.write(processInstanceBean.getProcessTemplateName());
	appender.write(ProcessInstanceBean.getConverter(ProcessInstanceBean.EXECUTIONSTATE_PROPERTY).getAsString(
		processInstanceBean.getExecutionState(), locale));
	appender.write(ProcessInstanceBean.getConverter(ProcessInstanceBean.STARTTIME_PROPERTY).getAsString(processInstanceBean.getStartTime(),
		locale));
	appender.newLine();
    }

    /**
         * TODO: Escaping
         */
    private static class Appender {
	/**
         * Separator to use in CSV file
         */
	public static final String SEPARATOR = ";";

	private BufferedWriter writer;

	public Appender(File file) {
	    try {
		this.writer = new BufferedWriter(new FileWriter(file));
	    } catch (Exception e) {
		throw new RuntimeException(e);
	    }
	}

	public void close() {
	    try {
		writer.close();
		writer = null;
	    } catch (Exception e) {
		throw new RuntimeException(e);
	    }
	}

	public Appender write(String s) {
	    try {
		writer.write(s);
		writer.write(SEPARATOR);
		return this;
	    } catch (Exception e) {
		throw new RuntimeException(e);
	    }
	}

	public Appender newLine() {
	    try {
		writer.newLine();
		return this;
	    } catch (Exception e) {
		throw new RuntimeException(e);
	    }
	}
    }
}
