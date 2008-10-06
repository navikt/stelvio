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

	public int execute() {
		ReportWriter writer = new ReportWriter(getReportFile());
		writer.writeln(getHeader());
		
		QueryResultSet queryResultSet = executeQuery();
		int stoppedProcessCount = queryResultSet.size();
		logger.info("{} qualifying processes", new Object[] { stoppedProcessCount });

		while (queryResultSet.next()) {
			ProcessInstanceBean processInstanceBean = new ProcessInstanceBean(queryResultSet, getBFMConnection().getAdaptee());
			writer.writeln(getRow(processInstanceBean));
		}
		writer.close();

		return stoppedProcessCount;
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

		return getBFMConnection().getBusinessFlowManagerService().queryAll(selectClause, whereClause, null, null);
	}
}
