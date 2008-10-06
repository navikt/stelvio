package no.nav.appclient.adapter;

import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	public void forceTerminate(String identifier) {
		forceTerminate(identifier, true);
	}

	public void forceTerminate(String identifier, boolean delete) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("Attempting to terminate process instance with id=<{}>", identifier);
			}
			adaptee.forceTerminate(identifier);
			if (logger.isDebugEnabled()) {
				logger.debug("Successfully terminated process instance with id=<{}>", identifier);
			}
			if (delete) {
				if (logger.isDebugEnabled()) {
					logger.debug("Attempting to delete process instance with id=<{}>", identifier);
				}
				adaptee.delete(identifier);
				if (logger.isDebugEnabled()) {
					logger.debug("Successfully deleted process instance with id=<{}>", identifier);
				}
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public void restart(String identifier) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("Attempting to restart process instance with id=<{}>", identifier);
			}
			adaptee.restart(identifier);
			if (logger.isDebugEnabled()) {
				logger.debug("Successfully restarted process instance with id=<{}>", identifier);
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
}
