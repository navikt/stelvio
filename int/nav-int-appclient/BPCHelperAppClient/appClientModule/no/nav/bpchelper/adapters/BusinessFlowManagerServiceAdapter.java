package no.nav.bpchelper.adapters;

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
		logger.debug("Executing query({},{},{},{})", new Object[]{selectClause,whereClause,orderByClause,threshold});
	    }
	    return adaptee.query(selectClause, whereClause, orderByClause, threshold, TimeZone.getDefault());
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

    public QueryResultSet queryAll(String selectClause, String whereClause, String orderByClause, Integer threshold) {
	try {
	    if (logger.isDebugEnabled()) {
		logger.debug("Executing queryAll({},{},{},{})", new Object[]{selectClause,whereClause,orderByClause,threshold});
	    }
	    return adaptee.queryAll(selectClause, whereClause, orderByClause, null, threshold, TimeZone.getDefault());
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

}
