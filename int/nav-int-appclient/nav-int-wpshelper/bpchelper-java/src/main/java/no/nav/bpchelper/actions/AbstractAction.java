package no.nav.bpchelper.actions;

import java.io.File;
import java.util.Properties;

import no.nav.appclient.adapter.BFMConnectionAdapter;
import no.nav.bpchelper.query.Criteria;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAction implements Action {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private Properties properties;
	private Criteria criteria;
	private File reportFile;
	private File inputFile;
	private boolean interactiveMode = true;

	private BFMConnectionAdapter bfmConnection;

	public abstract String getName();

	protected BFMConnectionAdapter getBFMConnection() {
		if (bfmConnection == null) {
			bfmConnection = BFMConnectionAdapter.getInstance(getProperties());
		}
		return bfmConnection;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public Criteria getCriteria() {
		return criteria;
	}

	public void setCriteria(Criteria criteria) {
		this.criteria = criteria;
	}

	public File getReportFile() {
		return reportFile;
	}

	public void setReportFile(File reportFile) {
		this.reportFile = reportFile;
	}
	
	public File getInputFile(){
		return inputFile;
	}
	
	public void setInputFile(File inputFile){
		this.inputFile = inputFile;
	}

	public boolean isInteractiveMode() {
		return interactiveMode;
	}

	public void setInteractiveMode(boolean interactiveMode) {
		this.interactiveMode = interactiveMode;
	}
}