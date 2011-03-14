/**
 * 
 */
package no.stelvio.batch.count.support;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import no.stelvio.batch.count.support.CounterEvent.EventType;

import org.junit.Before;
import org.junit.Test;

/**
 * @author person47c121e3ccb5, BEKK
 *
 */
public class ConcurrentBatchCounterTest {
	private BatchCounter counter;
	private CounterEvent concurrentEvent = CounterEvent.createCounterEvent(getClass(),
			"testConc", "test concurrent event", EventType.FUNCTIONAL);
	private int numberofThreads = 4;
	private int numberOfCountsPerThread = 100000;
	private int eventTime = 3;
	
	@Before
	public void setUp() {
		counter = new SimpleBatchCounter(new HashSet<CounterEvent>(Arrays.asList(concurrentEvent)));
	}
	
	@Test
	public void shoudIncrementEventsConcurrently() throws InterruptedException {
		Runnable action = new Runnable() {
			public void run() {
				counter.incrementEvent(concurrentEvent);
			}
		};
		ConcurrentExecutor.execute(numberofThreads, numberOfCountsPerThread, action);
		assertCount(counter.getEventReport());
		
	}

	@Test
	public void shoudIncrementEventsAndAddTimeConcurrently() throws InterruptedException {
		Runnable action = new Runnable() {
			public void run() {
				counter.incrementEvent(concurrentEvent, eventTime);
			}
		};
		ConcurrentExecutor.execute(numberofThreads, numberOfCountsPerThread, action);		
		Map<CounterEvent, ? extends EventCounter> events = counter.getEventReport();
		assertCount(events);
		assertTime(events);
	}	
	

	private void assertTime(Map<CounterEvent, ? extends EventCounter> events) {
		assertEquals(numberofThreads * numberOfCountsPerThread * eventTime, events.get(concurrentEvent).getTime());
	}

	private void assertCount(Map<CounterEvent, ? extends EventCounter> events) {
		assertEquals(numberofThreads * numberOfCountsPerThread, events.get(concurrentEvent).getCount());
	}	
}
