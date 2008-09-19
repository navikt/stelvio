package no.nav.bpchelper.actions;

import no.nav.bpchelper.adapters.BFMConnectionAdapter;
import no.nav.bpchelper.writers.ReportWriter;

import com.ibm.bpe.api.QueryResultSet;
import com.ibm.bpe.clientmodel.bean.ProcessInstanceBean;

public class ReportAction extends AbstractAction {
    public void process() {
	BFMConnectionAdapter bfmConnection = BFMConnectionAdapter.getInstance();

	// TODO: Can we use constants for any of the clause fragments?
	String selectClause = "DISTINCT PROCESS_INSTANCE.PIID,PROCESS_INSTANCE.NAME,PROCESS_INSTANCE.TEMPLATE_NAME,PROCESS_INSTANCE.STATE,PROCESS_INSTANCE.STARTED";
	String whereClause = getCriteria().toSqlString();

	// TODO: Is query the correct service to use, or should queryAll be used?
	QueryResultSet rs = bfmConnection.getBusinessFlowManagerService().query(selectClause, whereClause, null, null);

	ReportWriter writer = new ReportWriter();
	writer.writeHeader();
	while (rs.next()) {
	    writer.writeProcessInstance(new ProcessInstanceBean(rs, bfmConnection.getAdaptee()));
	}
	writer.close();
    }
}
