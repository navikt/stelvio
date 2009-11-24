package no.nav.bpchelper.actions;

import java.util.ArrayList;
import java.util.Collection;

import no.nav.appclient.adapter.ServiceException;

import com.ibm.bpe.api.PIID;
import com.ibm.bpe.api.ProcessInstanceData;
import com.ibm.bpe.clientmodel.bean.ProcessInstanceBean;

public class DeleteAction extends AbstractReportAction {
	@Override
	public String getName() {
		return "delete";
	}

	@Override
	protected Collection<ReportColumnSpec<ProcessInstanceBean>> getReportColumns() {
		Collection<ReportColumnSpec<ProcessInstanceBean>> reportColumns = new ArrayList<ReportColumnSpec<ProcessInstanceBean>>(
				DATA_COLUMNS);
		reportColumns.add(new ReportColumnSpec<ProcessInstanceBean>() {
			public String getLabel() {
				return "Result";
			}

			public String getValue(ProcessInstanceBean processInstance) {
				PIID piid = processInstance.getID();
				try {
					getBFMConnection().getBusinessFlowManagerService().delete(piid, !(processInstance.getExecutionState() == ProcessInstanceData.STATE_TERMINATED));
					return "OK";
				} catch (ServiceException e) {
					logger.warn("Error deleting process instance with id=<" + piid + ">", e);
					return e.getMessage();
				}
			}

		});
		return reportColumns;
	}
}
