package no.nav.bpchelper.actions;

import java.io.File;

import no.nav.bpchelper.adapters.BFMConnectionAdapter;
import no.nav.bpchelper.query.Criteria;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAction implements Action {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected final BFMConnectionAdapter bfmConnection;
    
    private Criteria criteria;
    private File reportFile;
    
    public abstract String getName();

    public AbstractAction() {
	bfmConnection = BFMConnectionAdapter.getInstance();
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
}