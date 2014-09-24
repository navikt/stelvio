package no.nav.bpchelper.actions;

import java.util.ArrayList;
import java.util.Collection;

import no.nav.appclient.adapter.ServiceException;
import no.nav.bpchelper.query.Criteria;
import no.nav.bpchelper.query.Restrictions;

import com.ibm.bpe.api.PIID;
import com.ibm.bpe.clientmodel.bean.ProcessInstanceBean;

public class ResumeAction extends AbstractReportAction {
	
	@Override
	public String getName() {
		return "resume";
	}
	
	@Override
	public void setCriteria(Criteria criteria) {
		this.criteria = criteria;
		//This adds a criteria to the query when RESUME is used, which ensures that only suspended process instances are returned
		this.criteria.add(Restrictions.eq("PROCESS_INSTANCE.STATE", ProcessInstanceBean.STATE_SUSPENDED));
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
					getBFMConnection().getBusinessFlowManagerService().resume(piid);
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