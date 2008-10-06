package no.nav.bpchelper.actions;

import java.util.ArrayList;
import java.util.Collection;

import no.nav.bpchelper.writers.ReportWriter;

import com.ibm.bpe.api.QueryResultSet;
import com.ibm.bpe.clientmodel.bean.ProcessInstanceBean;

public abstract class AbstractReportAction extends AbstractAction {
	protected static final Collection<ReportColumnSpec<ProcessInstanceBean>> DATA_COLUMNS;

	private final Collection<ReportColumnSpec<ProcessInstanceBean>> reportColumns;

	static {
		DATA_COLUMNS = new ArrayList<ReportColumnSpec<ProcessInstanceBean>>();
		DATA_COLUMNS.add(new ProcessInstancePropertyAccessor(ProcessInstanceBean.NAME_PROPERTY));
		DATA_COLUMNS.add(new ProcessInstancePropertyAccessor(ProcessInstanceBean.PROCESSTEMPLATENAME_PROPERTY));
		DATA_COLUMNS.add(new ProcessInstancePropertyAccessor(ProcessInstanceBean.EXECUTIONSTATE_PROPERTY));
		DATA_COLUMNS.add(new ProcessInstancePropertyAccessor(ProcessInstanceBean.STARTTIME_PROPERTY));
	}

	public AbstractReportAction() {
		this.reportColumns = getReportColumns();
	}

	protected abstract Collection<ReportColumnSpec<ProcessInstanceBean>> getReportColumns();

	public int execute() {
		ReportWriter writer = new ReportWriter(getReportFile());
		writer.writeln(buildHeader());

		QueryResultSet queryResultSet = executeQuery();
		int stoppedProcessCount = queryResultSet.size();
		logger.info("{} qualifying processes", new Object[] { stoppedProcessCount });

		while (queryResultSet.next()) {
			ProcessInstanceBean processInstanceBean = new ProcessInstanceBean(queryResultSet, getBFMConnection().getAdaptee());
			writer.writeln(buildRow(processInstanceBean));
		}
		writer.close();

		return stoppedProcessCount;
	}

	protected Collection<String> buildHeader() {
		Collection<String> header = new ArrayList<String>();
		for (ReportColumnSpec<ProcessInstanceBean> reportColumn : reportColumns) {
			header.add(reportColumn.getLabel());
		}
		return header;
	}

	protected Collection<String> buildRow(ProcessInstanceBean processInstanceBean) {
		Collection<String> row = new ArrayList<String>();
		for (ReportColumnSpec<ProcessInstanceBean> reportColumn : reportColumns) {
			row.add(reportColumn.getValue(processInstanceBean));
		}
		return row;
	}

	protected QueryResultSet executeQuery() {
		// TODO: Can we use constants for any of the clause fragments?
		String selectClause = "DISTINCT PROCESS_INSTANCE.PIID,PROCESS_INSTANCE.NAME,PROCESS_INSTANCE.TEMPLATE_NAME,PROCESS_INSTANCE.STATE,PROCESS_INSTANCE.STARTED";
		String whereClause = getCriteria().toSqlString();

		return getBFMConnection().getBusinessFlowManagerService().queryAll(selectClause, whereClause, null, null);
	}
}