package no.nav.bpchelper.actions;

import java.util.Collection;

import com.ibm.bpe.clientmodel.bean.ProcessInstanceBean;

public class ReportAction extends AbstractReportAction {
	@Override
	public String getName() {
		return "report";
	}

	@Override
	protected Collection<ReportColumnSpec<ProcessInstanceBean>> getReportColumns() {
		return DATA_COLUMNS;
	}
}
