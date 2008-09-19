package no.nav.bpchelper.actions;

import java.util.Collection;

import com.ibm.bpe.api.PIID;
import com.ibm.bpe.clientmodel.bean.ProcessInstanceBean;

public class TerminateAction extends ReportAction {
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
	if (logger.isDebugEnabled()) {
	    logger.debug("Attempting to terminate process instance with id=<" + id + ">");
	}
	String result;
	try {
	    bfmConnection.getBusinessFlowManagerService().getAdaptee().forceTerminate(id);
	    if (logger.isDebugEnabled()) {
		logger.debug("Successfully terminated process instance with id=<" + id + ">");
	    }
	    result = "OK";
	} catch (Exception e) {
	    logger.warn("Error terminating process instance with id=<" + id + ">", e);
	    result = e.getMessage();
	}	
	row.add(result);
	return row;
    }

}
