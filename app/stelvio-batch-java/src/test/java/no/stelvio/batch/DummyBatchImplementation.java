package no.stelvio.batch;

import no.stelvio.batch.domain.BatchDO;
import no.stelvio.common.error.SystemUnrecoverableException;
import no.stelvio.domain.time.ChangeStamp;

/**
 * A dummy batch implementation.
 * 
 * @author MA
 *
 */
public class DummyBatchImplementation extends AbstractBatch {

	private boolean testException = false;

	/**
	 * Creates a new instance of DummyBatchImplementation.
	 *
	 */
	protected DummyBatchImplementation() {
		super();
	}

	/**
	 * NB!! The way this batch reports status isn't valid. The correct way would be to conform to ints in BatchStatus
	 * 
	 * @param slice not used
	 * @return 0 if everything is ok
	 * @throws SystemUnrecoverableException system unrecoverable exception
	 */
	@Override
	public int executeBatch(int slice) throws SystemUnrecoverableException {

		BatchDO batchDO = new BatchDO();
		batchDO.setChangeStamp(new ChangeStamp("xxx9999"));
		System.out.println("dummybatch");
		batchDO.setBatchname("IBTesting");

		int status = 2;

		if (testException) {
			throw new BatchTestException("Testing exceptions");
		}

		for (int i = 0; i < 100; i++) {
			if (i == 33) {
				status--;
			}
			if (i == 66) {
				status--;
			}
			if (i == 99) {
				status--;
			}
		}

		return 0;
	}

	/**
	 * Set test exception status to true.
	 * 
	 */
	public void setTestException() {
		this.testException = true;
	}

	/**
	 * Flush updates, doing nothing.
	 */
	@Override
	protected void flushUpdates() {
		// Empty in this test
	}
}
