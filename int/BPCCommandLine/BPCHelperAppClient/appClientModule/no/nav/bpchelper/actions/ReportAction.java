package no.nav.bpchelper.actions;

import java.util.ArrayList;
import java.util.Collection;

import no.nav.bpchelper.adapters.BFMConnectionAdapter;
import no.nav.bpchelper.writers.ReportWriter;

import com.ibm.bpe.api.QueryResultSet;
import com.ibm.bpe.clientmodel.bean.ProcessInstanceBean;

public class ReportAction extends AbstractAction {
    public void process() {
	BFMConnectionAdapter bfmConnection = BFMConnectionAdapter.getInstance();

	Collection<ProcessInstancePropertyAccessor> propertyAccessors = new ArrayList<ProcessInstancePropertyAccessor>();
	propertyAccessors.add(new ProcessInstancePropertyAccessor(ProcessInstanceBean.NAME_PROPERTY));
	propertyAccessors.add(new ProcessInstancePropertyAccessor(ProcessInstanceBean.PROCESSTEMPLATENAME_PROPERTY));
	propertyAccessors.add(new ProcessInstancePropertyAccessor(ProcessInstanceBean.EXECUTIONSTATE_PROPERTY));
	propertyAccessors.add(new ProcessInstancePropertyAccessor(ProcessInstanceBean.STARTTIME_PROPERTY));

	// TODO: Can we use constants for any of the clause fragments?
	String selectClause = "DISTINCT PROCESS_INSTANCE.PIID,PROCESS_INSTANCE.NAME,PROCESS_INSTANCE.TEMPLATE_NAME,PROCESS_INSTANCE.STATE,PROCESS_INSTANCE.STARTED";
	String whereClause = getCriteria().toSqlString();

	// TODO: Is query the correct service to use, or should queryAll be used?
	QueryResultSet rs = bfmConnection.getBusinessFlowManagerService().query(selectClause, whereClause, null, null);

	Collection<String> temp = new ArrayList<String>(propertyAccessors.size());

	ReportWriter writer = new ReportWriter();
	for (ProcessInstancePropertyAccessor propertyAccessor : propertyAccessors) {
	    temp.add(propertyAccessor.getLabel());
	}
	writer.writeln(temp);

	while (rs.next()) {
	    temp.clear();
	    ProcessInstanceBean processInstanceBean = new ProcessInstanceBean(rs, bfmConnection.getAdaptee());
	    for (ProcessInstancePropertyAccessor propertyAccessor : propertyAccessors) {
		temp.add(propertyAccessor.getValue(processInstanceBean));
	    }
	    writer.writeln(temp);
	}
	
	writer.close();
    }
}
