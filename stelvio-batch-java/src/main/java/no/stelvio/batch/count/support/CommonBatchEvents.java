package no.stelvio.batch.count.support;

import no.stelvio.batch.count.support.CounterEvent.EventType;


/**
 * Contains common batch events, see {@link CounterEvent}.
 * 
 *
 */
public final class CommonBatchEvents {
	
	private CommonBatchEvents() {
	}

	/**	Batch run */
	public static final CounterEvent JOB_EVENT = 
		CounterEvent.createCounterEvent(CommonBatchEvents.class, "job", "Kjører batch.", EventType.TECHNICAL);

}
