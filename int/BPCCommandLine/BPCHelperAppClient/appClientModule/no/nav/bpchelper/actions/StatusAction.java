package no.nav.bpchelper.actions;

import no.nav.bpchelper.adapters.BFMConnectionAdapter;

import com.ibm.bpe.api.QueryResultSet;

public class StatusAction extends AbstractAction {
    public void process() {
	BFMConnectionAdapter bfmConnection = BFMConnectionAdapter.getInstance();

	// TODO: Can we use constants for any of the clause fragments?
	String selectClause = "COUNT(DISTINCT PROCESS_INSTANCE.PIID)";
	String whereClause = getCriteria().toSqlString();

	// TODO: Is query the correct service to use, or should queryAll be used?
	QueryResultSet rs = bfmConnection.getBusinessFlowManagerService().query(selectClause, whereClause, null, null);
	rs.next();
	System.out.println("Number of stopped process instances: " + rs.getLong(1));
    }
}
