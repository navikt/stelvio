package no.nav.bpchelper.actions;

import java.util.ArrayList;
import java.util.Collection;

import no.nav.appclient.adapter.ServiceException;

import com.ibm.bpe.api.PIID;
import com.ibm.bpe.api.ProcessInstanceData;

public class RestartAction extends AbstractReportAction {
	@Override
	public String getName() {
		return "restart";
	}

	@Override
	protected Collection<ReportColumnSpec<ProcessInstanceData>> getReportColumns() {
		Collection<ReportColumnSpec<ProcessInstanceData>> reportColumns = new ArrayList<ReportColumnSpec<ProcessInstanceData>>(
				DATA_COLUMNS);
		reportColumns.add(new ReportColumnSpec<ProcessInstanceData>() {
			public String getLabel() {
				return "Result";
			}

			public String getValue(ProcessInstanceData processInstance) {
				PIID piid = processInstance.getID();
				try {
					getBFMConnection().getBusinessFlowManagerService().restart(piid);
					return "OK";
				} catch (ServiceException e) {
					logger.warn("Error terminating/deleting process instance with id=<" + piid + ">", e);
					return e.getMessage();
				}
			}

		});
		return reportColumns;
	}
}
