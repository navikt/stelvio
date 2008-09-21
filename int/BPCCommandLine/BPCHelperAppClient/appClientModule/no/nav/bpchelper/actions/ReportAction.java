package no.nav.bpchelper.actions;

import java.util.ArrayList;
import java.util.Collection;

import no.nav.bpchelper.writers.ReportWriter;

import com.ibm.bpe.api.QueryResultSet;
import com.ibm.bpe.clientmodel.bean.ProcessInstanceBean;

public class ReportAction extends AbstractAction {
    private final Collection<ProcessInstancePropertyAccessor> propertyAccessors;

    public ReportAction() {
	propertyAccessors = new ArrayList<ProcessInstancePropertyAccessor>();
	propertyAccessors.add(new ProcessInstancePropertyAccessor(ProcessInstanceBean.NAME_PROPERTY));
	propertyAccessors.add(new ProcessInstancePropertyAccessor(ProcessInstanceBean.PROCESSTEMPLATENAME_PROPERTY));
	propertyAccessors.add(new ProcessInstancePropertyAccessor(ProcessInstanceBean.EXECUTIONSTATE_PROPERTY));
	propertyAccessors.add(new ProcessInstancePropertyAccessor(ProcessInstanceBean.STARTTIME_PROPERTY));
    }

    @Override
    public String getName() {
	return "report";
    }
    
    public void process() {
	ReportWriter writer = new ReportWriter(getReportFile());

	writer.writeln(getHeader());

	QueryResultSet queryResultSet = executeQuery();
	while (queryResultSet.next()) {
	    ProcessInstanceBean processInstanceBean = new ProcessInstanceBean(queryResultSet, bfmConnection.getAdaptee());
	    writer.writeln(getRow(processInstanceBean));
	}

	writer.close();
    }

    protected Collection<String> getRow(ProcessInstanceBean processInstanceBean) {
	Collection<String> row = new ArrayList<String>();
	for (ProcessInstancePropertyAccessor propertyAccessor : propertyAccessors) {
	    row.add(propertyAccessor.getValue(processInstanceBean));
	}
	return row;
    }

    protected Collection<String> getHeader() {
	Collection<String> header = new ArrayList<String>();
	for (ProcessInstancePropertyAccessor propertyAccessor : propertyAccessors) {
	    header.add(propertyAccessor.getLabel());
	}
	return header;
    }

    protected QueryResultSet executeQuery() {
	// TODO: Can we use constants for any of the clause fragments?
	String selectClause = "DISTINCT PROCESS_INSTANCE.PIID,PROCESS_INSTANCE.NAME,PROCESS_INSTANCE.TEMPLATE_NAME,PROCESS_INSTANCE.STATE,PROCESS_INSTANCE.STARTED";
	String whereClause = getCriteria().toSqlString();

	// TODO: Is query the correct service to use, or should queryAll be used?
	return bfmConnection.getBusinessFlowManagerService().query(selectClause, whereClause, null, null);
    }
}
