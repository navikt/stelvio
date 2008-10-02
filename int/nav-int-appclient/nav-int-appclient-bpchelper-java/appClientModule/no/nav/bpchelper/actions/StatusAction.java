package no.nav.bpchelper.actions;

import java.util.Properties;

import com.ibm.bpe.api.QueryResultSet;

public class StatusAction extends AbstractAction {
    
	public StatusAction(Properties properties) {
    	super(properties);
	}

	@Override
    public String getName() {
	return "status";
    }
    
    public void process() {
	// TODO: Can we use constants for any of the clause fragments?
	String selectClause = "COUNT(DISTINCT PROCESS_INSTANCE.PIID)";
	String whereClause = getCriteria().toSqlString();

	QueryResultSet rs = bfmConnection.getBusinessFlowManagerService().queryAll(selectClause, whereClause, null, null);
	
	rs.next();
	logger.info("Number of stopped process instances: " + rs.getLong(1));
    }
}
