package no.nav.bpchelper.service;

import com.ibm.bpe.api.QueryResultSet;


public interface BusinessFlowManagerService {
	public QueryResultSet query(String selectClause, String whereClause, String orderByClause, Integer threshold);
	
	public QueryResultSet queryAll(String selectClause, String whereClause, String orderByClause, Integer threshold);
}
