package no.nav.bpchelper.service;

import com.ibm.bpe.api.QueryResultSet;

public class BusinessFlowManagerServiceAdapter implements BusinessFlowManagerService {
	private com.ibm.bpe.api.BusinessFlowManagerService adaptee;

	public BusinessFlowManagerServiceAdapter(com.ibm.bpe.api.BusinessFlowManagerService adaptee) {
		this.adaptee = adaptee;
	}
	
	public com.ibm.bpe.api.BusinessFlowManagerService getAdaptee() {
		return adaptee;
	}
	
	public QueryResultSet query(String selectClause, String whereClause, String orderByClause, Integer threshold) {
		try {
			return adaptee.query(selectClause, whereClause, orderByClause, threshold, null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public QueryResultSet queryAll(String selectClause, String whereClause, String orderByClause, Integer threshold) {
		try {
			return adaptee.queryAll(selectClause, whereClause, orderByClause, null, threshold, null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
}
