package no.nav.bpchelper.actions;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import no.nav.bpchelper.cmdoptions.ActionOptionValues;
import no.nav.bpchelper.cmdoptions.OptionOpts;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.FastDateFormat;

public class ActionFactory {
    private static final DateFormat REPORT_FILENAME_DATEFORMAT = new SimpleDateFormat("yyyyddMMHHmmssSSS");
    
    public static Action getAction(CommandLine commandLine) {
	String actionValue = commandLine.getOptionValue(OptionOpts.ACTION);
	AbstractAction action = ActionOptionValues.valueOf(actionValue).getAction();
	
	action.setCriteria(CriteriaBuilder.build(commandLine));
	
	File reportFile;
	String reportFilename = commandLine.getOptionValue(OptionOpts.REPORT_FILENAME);
	if (reportFilename == null) {
	    StringBuilder sb = new StringBuilder();
	    sb.append(action.getName());
	    sb.append('_').append(REPORT_FILENAME_DATEFORMAT.format(System.currentTimeMillis()));
	    sb.append(".csv");
	    reportFilename = sb.toString();
	}
	String reportDirectory = commandLine.getOptionValue(OptionOpts.REPORT_DIR);
	if (reportDirectory != null) {
	    reportFile = new File(reportDirectory, reportFilename);
	} else {
	    reportFile = new File(reportFilename);
	}
	action.setReportFile(reportFile);
	return action;
    }
}
