package no.nav.appclient.adapter;

import java.rmi.RemoteException;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bpe.api.EngineNotAuthorizedException;
import com.ibm.bpe.api.EngineWrongKindException;
import com.ibm.bpe.api.EngineWrongStateException;
import com.ibm.bpe.api.IdWrongFormatException;
import com.ibm.bpe.api.ObjectDoesNotExistException;
import com.ibm.bpe.api.PIID;
import com.ibm.bpe.api.ProcessInstanceData;
import com.ibm.bpe.api.QueryResultSet;
import com.ibm.bpe.api.UnexpectedFailureException;
import com.ibm.bpe.api.ArchiveUnsupportedOperationException;

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

	public PIID getProcessInstanceIdByActivityId(String activityId) {
		String selectClause = "ACTIVITY.PIID";
		StringBuilder whereClause = new StringBuilder("ACTIVITY.AIID=");
		whereClause.append("ID('").append(activityId).append("')");
		QueryResultSet rs = queryAll(selectClause, whereClause.toString(), null, null);
		if (rs.next()) {
			return (PIID) rs.getOID(1);
		} else {
			throw new ServiceException("Activity with id=<" + activityId + "> not found.");
		}
	}

	public ProcessInstanceData getProcessInstance(PIID piid) {
		try {
			return adaptee.getProcessInstance(piid);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public void delete(PIID piid, boolean terminateBeforeDelete) {
		try {
			if (terminateBeforeDelete) {
				forceTerminate(piid);
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Attempting to delete process instance with id=<{}>", piid);
			}
			adaptee.delete(piid);
			if (logger.isDebugEnabled()) {
				logger.debug("Successfully deleted process instance with id=<{}>", piid);
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public void deleteProcessInstanceByActivityId(String activityId) {
		PIID piid = getProcessInstanceIdByActivityId(activityId);
		delete(piid, false);
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

	private void forceTerminate(PIID piid) throws EngineNotAuthorizedException, EngineWrongKindException,
			IdWrongFormatException, ObjectDoesNotExistException, UnexpectedFailureException, RemoteException,
			ArchiveUnsupportedOperationException {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("Attempting to force terminate process instance with id=<{}>", piid);
			}
			adaptee.forceTerminate(piid);
			if (logger.isDebugEnabled()) {
				logger.debug("Successfully force terminated process instance with id=<{}>", piid);
			}
		} catch (EngineWrongStateException e) {
			// Catching wrong state exception - the processes is probably
			// already terminated
			if (logger.isDebugEnabled()) {
				logger.debug("Caught wrong state exception when attempting to force terminate process instance with id=<"
						+ piid + ">; ignoring", e);
			}
		}
	}

}
