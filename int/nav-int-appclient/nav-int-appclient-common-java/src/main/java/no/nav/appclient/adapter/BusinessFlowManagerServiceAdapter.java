package no.nav.appclient.adapter;

import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bpe.api.PIID;
import com.ibm.bpe.api.ProcessInstanceData;
import com.ibm.bpe.api.QueryResultSet;

public class BusinessFlowManagerServiceAdapter {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private com.ibm.bpe.api.BusinessFlowManagerService adaptee;

	public BusinessFlowManagerServiceAdapter(com.ibm.bpe.api.BusinessFlowManagerService adaptee) {
		this.adaptee = adaptee;
	}

	public com.ibm.bpe.api.BusinessFlowManagerService getAdaptee() {
		return adaptee;
	}

	public QueryResultSet query(String selectClause, String whereClause, String orderByClause, Integer threshold) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("Executing query({},{},{},{})",
						new Object[] { selectClause, whereClause, orderByClause, threshold });
			}
			return adaptee.query(selectClause, whereClause, orderByClause, threshold, TimeZone.getDefault());
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public QueryResultSet queryAll(String selectClause, String whereClause, String orderByClause, Integer threshold) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("Executing queryAll({},{},{},{})", new Object[] { selectClause, whereClause, orderByClause,
						threshold });
			}
			return adaptee.queryAll(selectClause, whereClause, orderByClause, null, threshold, TimeZone.getDefault());
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public PIID getProcessInstanceID(String activityID) {
		String selectClause = "ACTIVITY.PIID";
		StringBuilder whereClause = new StringBuilder("ACTIVITY.AIID=");
		whereClause.append("ID('").append(activityID).append("')");
		QueryResultSet rs = queryAll(selectClause, whereClause.toString(), null, null);
		if (rs.next()) {
			return (PIID) rs.getOID(1);
		} else {
			throw new ServiceException("Activity with id=<" + activityID
					+ "> not found. Process already deleted? Nothing to terminate.");
		}
	}

	public ProcessInstanceData getProcessInstance(PIID piid) {
		try {
			return adaptee.getProcessInstance(piid);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public void forceTerminate(PIID piid) {
		forceTerminate(piid, true);
	}

	public void forceTerminate(PIID piid, boolean delete) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("Attempting to terminate process instance with id=<{}>", piid);
			}
			adaptee.forceTerminate(piid);
			if (logger.isDebugEnabled()) {
				logger.debug("Successfully terminated process instance with id=<{}>", piid);
			}
			if (delete) {
				if (logger.isDebugEnabled()) {
					logger.debug("Attempting to delete process instance with id=<{}>", piid);
				}
				adaptee.delete(piid);
				if (logger.isDebugEnabled()) {
					logger.debug("Successfully deleted process instance with id=<{}>", piid);
				}
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public void forceTerminateFromActivity(String aiid) {
		PIID piid = getProcessInstanceID(aiid);
		forceTerminate(piid);
	}

	public void restart(PIID piid) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("Attempting to restart process instance with id=<{}>", piid);
			}
			adaptee.restart(piid);
			if (logger.isDebugEnabled()) {
				logger.debug("Successfully restarted process instance with id=<{}>", piid);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
}
