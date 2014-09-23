package no.nav.bpchelper.actions;

import com.ibm.bpe.api.QueryResultSet;

public class StatusAction extends AbstractAction {
	@Override
	public String getName() {
		return "status";
	}

	public int execute() {
		QueryResultSet rs = getBFMConnection().getBusinessFlowManagerService().queryAll(
				"COUNT(DISTINCT PROCESS_INSTANCE.PIID)",
				getCriteria().toSqlString(),
				null,
				getCriteria().getResultRowLimit());

		rs.next();
		int stoppedProcessCount = rs.getInteger(1);
		logger.info("{} qualifying process(es)", stoppedProcessCount);
		return stoppedProcessCount;
	}
}
