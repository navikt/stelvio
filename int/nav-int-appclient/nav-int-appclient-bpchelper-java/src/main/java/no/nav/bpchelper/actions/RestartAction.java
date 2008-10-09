package no.nav.bpchelper.actions;

import java.util.ArrayList;
import java.util.Collection;

import no.nav.appclient.adapter.ServiceException;

import com.ibm.bpe.api.PIID;
import com.ibm.bpe.clientmodel.bean.ProcessInstanceBean;

public class RestartAction extends AbstractReportAction {
	@Override
	public String getName() {
		return "restart";
	}

	@Override
	protected Collection<ReportColumnSpec<ProcessInstanceBean>> getReportColumns() {
		Collection<ReportColumnSpec<ProcessInstanceBean>> reportColumns = new ArrayList<ReportColumnSpec<ProcessInstanceBean>>(
				DATA_COLUMNS);
		reportColumns.add(new ReportColumnSpec<ProcessInstanceBean>() {
			public String getLabel() {
				return "Result";
			}

			public String getValue(ProcessInstanceBean instance) {
				PIID piid = instance.getID();
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
