package no.nav.bpchelper.actions;

import com.ibm.bpe.api.QueryResultSet;

public class StatusAction extends AbstractAction {
	@Override
	public String getName() {
		return "status";
	}

	public int execute() {
		String selectClause = "COUNT(DISTINCT PROCESS_INSTANCE.PIID)";
		String whereClause = getCriteria().toSqlString();

		QueryResultSet rs = getBFMConnection().getBusinessFlowManagerService().queryAll(selectClause, whereClause, null, null);

		rs.next();
		int stoppedProcessCount = rs.getInteger(1);
		logger.info("{} qualifying process(es)", stoppedProcessCount);
		return stoppedProcessCount;
	}
}
