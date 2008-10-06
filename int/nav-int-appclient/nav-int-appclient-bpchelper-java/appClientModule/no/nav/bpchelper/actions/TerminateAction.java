package no.nav.bpchelper.actions;

import java.util.Collection;

import no.nav.appclient.adapter.ServiceException;

import com.ibm.bpe.api.PIID;
import com.ibm.bpe.clientmodel.bean.ProcessInstanceBean;

public class TerminateAction extends ReportAction {
	@Override
	public String getName() {
		return "terminate";
	}

	@Override
	protected Collection<String> buildHeader() {
		Collection<String> header = super.buildHeader();
		header.add("Result");
		return header;
	}

	@Override
	protected Collection<String> buildRow(ProcessInstanceBean processInstanceBean) {
		Collection<String> row = super.buildRow(processInstanceBean);
		PIID id = processInstanceBean.getID();
		String result;
		try {
			getBFMConnection().getBusinessFlowManagerService().forceTerminate(id.toString());
			result = "OK";
		} catch (ServiceException e) {
			logger.warn("Error terminating/deleting process instance with id=<" + id + ">", e);
			result = e.getMessage();
		}
		row.add(result);
		return row;
	}
}
