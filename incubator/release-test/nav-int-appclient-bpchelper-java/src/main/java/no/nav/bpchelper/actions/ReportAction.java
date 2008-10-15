package no.nav.bpchelper.actions;

import java.util.Collection;

import com.ibm.bpe.api.ProcessInstanceData;

public class ReportAction extends AbstractReportAction {
	@Override
	public String getName() {
		return "report";
	}

	@Override
	protected Collection<ReportColumnSpec<ProcessInstanceData>> getReportColumns() {
		return DATA_COLUMNS;
	}
}
