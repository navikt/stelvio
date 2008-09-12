package no.nav.bpchelper.service;

import com.ibm.bpe.api.BusinessFlowManager;
import com.ibm.bpe.api.QueryResultSet;

public class BusinessFlowManagerServiceImpl implements BusinessFlowManagerService {
	private BusinessFlowManager businessFlowManager;

	public BusinessFlowManagerServiceImpl(BusinessFlowManager businessFlowManager) {
		this.businessFlowManager = businessFlowManager;
	}

	public QueryResultSet query(String selectClause, String whereClause, String orderByClause, Integer threshold) {
		try {
			return businessFlowManager.query(selectClause, whereClause, orderByClause, threshold, null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
