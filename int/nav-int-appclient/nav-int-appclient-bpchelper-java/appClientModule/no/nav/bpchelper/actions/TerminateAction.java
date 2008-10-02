package no.nav.bpchelper.actions;

import java.util.Collection;
import java.util.Properties;

import no.nav.appclient.adapter.ServiceException;

import com.ibm.bpe.api.PIID;
import com.ibm.bpe.clientmodel.bean.ProcessInstanceBean;

public class TerminateAction extends ReportAction {
	public TerminateAction(Properties properties) {
		super(properties);
	}

	@Override
	public String getName() {
		return "terminate";
	}

	@Override
	protected Collection<String> getHeader() {
		Collection<String> header = super.getHeader();
		header.add("Result");
		return header;
	}

	@Override
	protected Collection<String> getRow(ProcessInstanceBean processInstanceBean) {
		Collection<String> row = super.getRow(processInstanceBean);
		PIID id = processInstanceBean.getID();
		String result;
		try {
			bfmConnection.getBusinessFlowManagerService().forceTerminate(id.toString());
			result = "OK";
		} catch (ServiceException e) {
			logger.warn("Error terminating/deleting process instance with id=<" + id + ">", e);
			result = e.getMessage();
		}
		row.add(result);
		return row;
	}
}
