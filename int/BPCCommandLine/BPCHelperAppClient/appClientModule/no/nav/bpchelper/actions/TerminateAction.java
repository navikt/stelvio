package no.nav.bpchelper.actions;

import java.util.Collection;
import java.util.Properties;

import com.ibm.bpe.api.BusinessFlowManagerService;
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
			BusinessFlowManagerService businessFlowManagerService = bfmConnection.getBusinessFlowManagerService().getAdaptee();
			if (logger.isDebugEnabled()) {
				logger.debug("Attempting to terminate process instance with id=<" + id + ">");
			}
			businessFlowManagerService.forceTerminate(id);
			if (logger.isDebugEnabled()) {
				logger.debug("Successfully terminated process instance with id=<" + id + ">");
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Attempting to delete process instance with id=<" + id + ">");
			}
			businessFlowManagerService.delete(id);
			if (logger.isDebugEnabled()) {
				logger.debug("Successfully deleted process instance with id=<" + id + ">");
			}
			result = "OK";
		} catch (Exception e) {
			logger.warn("Error terminating/deleting process instance with id=<" + id + ">", e);
			result = e.getMessage();
		}
		row.add(result);
		return row;
	}
}
