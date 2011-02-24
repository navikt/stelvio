package no.stelvio.batch.count.support;


/**
 * Represents a counter to be used by a {@link BatchCounter}.
 * @author person47c121e3ccb5, BEKK
 *
 */
public interface EventCounter {

	/**
	 * Number of times a {@link CounterEvent} has occured.
	 * @return the count
	 */
	long getCount();


	/**
	 * Total time of execution for a {@link CounterEvent}.
	 * @return the total time in ms.
	 */
	long getTime();
	
	/**
	 * Average time of execution for a {@link CounterEvent}.
	 * @return the average time in ms
	 */
	long getAvg();
}
