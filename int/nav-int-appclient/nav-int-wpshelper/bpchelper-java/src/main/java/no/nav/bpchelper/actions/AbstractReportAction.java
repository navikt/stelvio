package no.nav.bpchelper.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;

import no.nav.appclient.adapter.BusinessFlowManagerServiceAdapter;
import no.nav.bpchelper.writers.ReportWriter;

import com.ibm.bpe.api.PIID;
import com.ibm.bpe.api.ProcessInstanceData;
import com.ibm.bpe.api.QueryResultSet;
import com.ibm.bpe.clientmodel.bean.ProcessInstanceBean;

public abstract class AbstractReportAction extends AbstractAction {
	private static final MessageFormat CONFIRM_MESSAGE_FORMAT = new MessageFormat(
			"Do you want to continue and {0} {1} qualifying process(es) (y/n)?");
	protected static final Collection<ReportColumnSpec<ProcessInstanceBean>> DATA_COLUMNS;

	private final Collection<ReportColumnSpec<ProcessInstanceBean>> reportColumns;

	static {
		DATA_COLUMNS = new ArrayList<ReportColumnSpec<ProcessInstanceBean>>();
		DATA_COLUMNS.add(new ProcessInstancePropertyAccessor(ProcessInstanceBean.ID_PROPERTY));
		DATA_COLUMNS.add(new ProcessInstancePropertyAccessor(ProcessInstanceBean.COMPLETIONTIME_PROPERTY));
		DATA_COLUMNS.add(new ProcessInstancePropertyAccessor(ProcessInstanceBean.CREATIONTIME_PROPERTY));
		DATA_COLUMNS.add(new ProcessInstancePropertyAccessor(ProcessInstanceBean.DESCRIPTION_PROPERTY));
		DATA_COLUMNS.add(new ProcessInstancePropertyAccessor(ProcessInstanceBean.NAME_PROPERTY));
		DATA_COLUMNS.add(new ProcessInstancePropertyAccessor(ProcessInstanceBean.PARENTPROCESSINSTANCENAME_PROPERTY));
		DATA_COLUMNS.add(new ProcessInstancePropertyAccessor(ProcessInstanceBean.STARTTIME_PROPERTY));
		DATA_COLUMNS.add(new ProcessInstancePropertyAccessor(ProcessInstanceBean.STARTER_PROPERTY));
		DATA_COLUMNS.add(new ProcessInstancePropertyAccessor(ProcessInstanceBean.EXECUTIONSTATE_PROPERTY));
		DATA_COLUMNS.add(new ProcessInstancePropertyAccessor(ProcessInstanceBean.PROCESSTEMPLATENAME_PROPERTY));
		DATA_COLUMNS.add(new ProcessInstancePropertyAccessor(ProcessInstanceBean.TOPLEVELPROCESSINSTANCENAME_PROPERTY));
		DATA_COLUMNS.add(new ProcessInstanceCustomPropertiesAccessor());
	}

	public AbstractReportAction() {
		this.reportColumns = getReportColumns();
	}

	protected abstract Collection<ReportColumnSpec<ProcessInstanceBean>> getReportColumns();

	public int execute() {
		Collection<PIID> piidCollection = executeQuery();
		int stoppedProcessCount = piidCollection.size();
		logger.info("{} qualifying process(es)", stoppedProcessCount);

		if (stoppedProcessCount > 0 && isInteractiveMode() && !confirm(stoppedProcessCount)) {
			return stoppedProcessCount;
		}

		ReportWriter writer = new ReportWriter(getReportFile());
		writer.writeln(buildHeader());

		BusinessFlowManagerServiceAdapter businessFlowManagerService = getBFMConnection().getBusinessFlowManagerService();
		for (PIID piid : piidCollection) {
			if (logger.isDebugEnabled()) {
				logger.debug("Processing process with id=<{}>.", piid);
			}
			ProcessInstanceData processInstanceData = businessFlowManagerService.getProcessInstance(piid);
			ProcessInstanceBean processInstance = new ProcessInstanceBean(processInstanceData, getBFMConnection().getAdaptee());
			writer.writeln(buildRow(processInstance));
		}

		writer.close();

		return stoppedProcessCount;
	}

	private boolean confirm(int stoppedProcessCount) {
		StringBuffer question = CONFIRM_MESSAGE_FORMAT.format(new Object[] { getName(), stoppedProcessCount },
				new StringBuffer(), null);

		String answer = null;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			while (!"y".equalsIgnoreCase(answer) && !"n".equalsIgnoreCase(answer)) {
				if (answer != null) {
					System.out.println(answer + " is not a valid answer");
				}
				System.out.print(question + " ");
				answer = in.readLine();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return "y".equalsIgnoreCase(answer);
	}

	protected Collection<PIID> executeQuery() {

		QueryResultSet rs = getBFMConnection().getBusinessFlowManagerService().queryAll(
				"DISTINCT PROCESS_INSTANCE.PIID", 
				getCriteria().toSqlString(), 
				null, 
				getCriteria().getResultRowLimit());

		Collection<PIID> result = new ArrayList<PIID>(rs.size());
		while (rs.next()) {
			result.add((PIID) rs.getOID(1));
		}
		return result;
	}

	private Collection<String> buildHeader() {
		Collection<String> header = new ArrayList<String>();
		for (ReportColumnSpec<ProcessInstanceBean> reportColumn : reportColumns) {
			header.add(reportColumn.getLabel());
		}
		return header;
	}

	private Collection<String> buildRow(ProcessInstanceBean processInstance) {
		Collection<String> row = new ArrayList<String>();
		for (ReportColumnSpec<ProcessInstanceBean> reportColumn : reportColumns) {
			row.add(reportColumn.getValue(processInstance));
		}
		return row;
	}
}