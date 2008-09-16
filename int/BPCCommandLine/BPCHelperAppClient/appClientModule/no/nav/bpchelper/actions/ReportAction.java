package no.nav.bpchelper.actions;

import java.util.Calendar;
import java.util.Date;

import no.nav.bpchelper.adapters.BFMConnectionAdapter;
import no.nav.bpchelper.query.MinStartedFilter;
import no.nav.bpchelper.writers.ReportWriter;

import com.ibm.bpe.api.QueryResultSet;
import com.ibm.bpe.clientmodel.bean.ProcessInstanceBean;


public class ReportAction extends AbstractAction {
	public void process() {
		BFMConnectionAdapter bfmConnection = BFMConnectionAdapter.getInstance();
		
		// TODO: Can we use constants for any of the clause fragments?
		String selectClause = "DISTINCT PROCESS_INSTANCE.PIID,PROCESS_INSTANCE.NAME,PROCESS_INSTANCE.TEMPLATE_NAME,PROCESS_INSTANCE.STATE,PROCESS_INSTANCE.STARTED";
		
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2008, Calendar.SEPTEMBER, 12, 16, 0, 0);
		MinStartedFilter filter = new MinStartedFilter(cal.getTime());
		String whereClause = filter.getWhereClause();
		
		// TODO: Is query the correct service to use, or should queryAll be used?
		QueryResultSet rs = bfmConnection.getBusinessFlowManagerService().query(selectClause, whereClause, null, null);
		
		ReportWriter writer = new ReportWriter();
		writer.writeHeader();
		while(rs.next()) {
			writer.writeProcessInstance(new ProcessInstanceBean(rs, bfmConnection.getAdaptee()));
		}
		writer.close();
	}
}
