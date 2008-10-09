package no.nav.bpchelper.actions;

import java.util.ArrayList;
import java.util.Collection;

import no.nav.bpchelper.writers.ReportWriter;

import com.ibm.bpe.api.ProcessInstanceData;
import com.ibm.bpe.api.QueryResultSet;
import com.ibm.bpe.clientmodel.bean.ProcessInstanceBean;

public abstract class AbstractReportAction extends AbstractAction {
	protected static final Collection<ReportColumnSpec<ProcessInstanceData>> DATA_COLUMNS;

	private final Collection<ReportColumnSpec<ProcessInstanceData>> reportColumns;

	static {
		DATA_COLUMNS = new ArrayList<ReportColumnSpec<ProcessInstanceData>>();
		DATA_COLUMNS.add(new ProcessInstancePropertyAccessor(ProcessInstanceBean.NAME_PROPERTY));
		DATA_COLUMNS.add(new ProcessInstancePropertyAccessor(ProcessInstanceBean.PROCESSTEMPLATENAME_PROPERTY));
		DATA_COLUMNS.add(new ProcessInstancePropertyAccessor(ProcessInstanceBean.EXECUTIONSTATE_PROPERTY));
		DATA_COLUMNS.add(new ProcessInstancePropertyAccessor(ProcessInstanceBean.STARTTIME_PROPERTY));
	}

	public AbstractReportAction() {
		this.reportColumns = getReportColumns();
	}

	protected abstract Collection<ReportColumnSpec<ProcessInstanceData>> getReportColumns();

	public int execute() {
		ReportWriter writer = new ReportWriter(getReportFile());
		writer.writeln(buildHeader());

		QueryResultSet queryResultSet = executeQuery();
		int stoppedProcessCount = queryResultSet.size();
		logger.info("{} qualifying processes", stoppedProcessCount);

		while (queryResultSet.next()) {
			ProcessInstanceData processInstance = new ProcessInstanceBean(queryResultSet, getBFMConnection().getAdaptee());
			if (logger.isDebugEnabled()) {
				logger.debug("Processing process with id=<{}>.", processInstance.getID());
			}
			writer.writeln(buildRow(processInstance));
		}
		writer.close();

		return stoppedProcessCount;
	}

	protected QueryResultSet executeQuery() {
		// TODO: Can we use constants for any of the clause fragments?
		String selectClause = "DISTINCT PROCESS_INSTANCE.PIID,PROCESS_INSTANCE.NAME,PROCESS_INSTANCE.TEMPLATE_NAME,PROCESS_INSTANCE.STATE,PROCESS_INSTANCE.STARTED";
		String whereClause = getCriteria().toSqlString();

		return getBFMConnection().getBusinessFlowManagerService().queryAll(selectClause, whereClause, null, null);
	}

	private Collection<String> buildHeader() {
		Collection<String> header = new ArrayList<String>();
		for (ReportColumnSpec<ProcessInstanceData> reportColumn : reportColumns) {
			header.add(reportColumn.getLabel());
		}
		return header;
	}

	private Collection<String> buildRow(ProcessInstanceData processInstance) {
		Collection<String> row = new ArrayList<String>();
		for (ReportColumnSpec<ProcessInstanceData> reportColumn : reportColumns) {
			row.add(reportColumn.getValue(processInstance));
		}
		return row;
	}
}