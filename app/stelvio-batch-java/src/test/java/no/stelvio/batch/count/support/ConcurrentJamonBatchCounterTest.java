/**
 * 
 */
package no.stelvio.batch.count.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;

import no.stelvio.batch.count.support.CounterEvent.EventType;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

/**
 * @author person47c121e3ccb5, BEKK
 *
 */
public class ConcurrentJamonBatchCounterTest {
	private JamonBatchCounter counter;
	private CounterEvent concurrentEvent = CounterEvent.createCounterEvent(getClass(),
			"testConc", "test concurrent event", EventType.TECHNICAL);
	private int noOfThreads = 4;
	private int numberOfExecutionsPerThread = 100000;
	
	@Before
	public void setUp() {
		counter = new JamonBatchCounter(new HashSet<CounterEvent>(Arrays.asList(concurrentEvent)));
		counter.setJamonEnabled(true);
		counter.setJamonPrefix("TEST");
		MonitorFactory.reset();
	}
	
	@Test
	@Ignore
	public void shouldHandleStartAndStopConcurrently() throws Exception {
		final int sleeptime = 100;
		ConcurrentCounterTestUtil.executeConcurrentStartAndStop(counter, concurrentEvent, sleeptime);
		Monitor monitor = (Monitor) counter.getMonitor(concurrentEvent);
		assertTrue(monitor.getTotal() > sleeptime);
	}
	
	@Test
	public void shouldSampleRunningTimeBetweenStartAndStop() throws Exception {
		final int sleeptime = 100;
		Runnable action = new Runnable() {
			public void run() {
				counter.start(concurrentEvent);
				try {
					Thread.sleep(sleeptime);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				counter.stop(concurrentEvent);
			}
		};
		int numberOfTimeSamplesPerThread = numberOfExecutionsPerThread / 10000;
		ConcurrentExecutor.execute(noOfThreads, numberOfTimeSamplesPerThread, action);	
		
		Monitor monitor = (Monitor) counter.getMonitor(concurrentEvent);
		assertEquals(noOfThreads * numberOfTimeSamplesPerThread * sleeptime, monitor.getTotal(), 1000);
	}	
	
	@Test
	public void shouldHandleConcurrentStartAndStop() throws Exception {
		Runnable action = new Runnable() {
			public void run() {
				counter.start(concurrentEvent);
				counter.stop(concurrentEvent);
			}
		};
		ConcurrentExecutor.execute(noOfThreads, numberOfExecutionsPerThread, action);	
		
		assertHits(noOfThreads * numberOfExecutionsPerThread);
	}		

	@Test
	public void shouldHandleConcurrentStartAndStopWithCounts() throws Exception {
		final int count = 2;
		Runnable action = new Runnable() {
			public void run() {
				counter.start(concurrentEvent);
				counter.stop(concurrentEvent, count);
			}
		};
		ConcurrentExecutor.execute(noOfThreads, numberOfExecutionsPerThread, action);	
		
		assertHits(noOfThreads * numberOfExecutionsPerThread * count);
	}
	
	@Test
	public void shouldHandleConcurrentIncrements() throws Exception {
		Runnable action = new Runnable() {
			public void run() {
				counter.incrementEvent(concurrentEvent);
			}
		};
		ConcurrentExecutor.execute(noOfThreads, numberOfExecutionsPerThread, action);		
		assertHits(noOfThreads * numberOfExecutionsPerThread);
	}	
	
	@Test
	public void shouldHandleConcurrentIncrementsWithTime() throws Exception {
		final int time = 2;
		Runnable action = new Runnable() {
			public void run() {
				counter.incrementEvent(concurrentEvent, time);
			}
		};
		ConcurrentExecutor.execute(noOfThreads, numberOfExecutionsPerThread, action);	
		assertHits(noOfThreads * numberOfExecutionsPerThread);
		assertTotalTime(noOfThreads * numberOfExecutionsPerThread * time);
	}	
	

	@Test
	public void shouldHandleConcurrentAddEvents() throws Exception {
		final int count = 2;
		Runnable action = new Runnable() {
			public void run() {
				counter.addEvents(concurrentEvent, count);
			}
		};
		ConcurrentExecutor.execute(noOfThreads, numberOfExecutionsPerThread, action);		
		assertHits(noOfThreads * numberOfExecutionsPerThread * count);
	}	
	
	@Test
	public void shouldHandleConcurrentAddEventsWithTime() throws Exception {
		final int count = 2;
		final int time = 3;
		Runnable action = new Runnable() {
			public void run() {
				counter.addEvents(concurrentEvent, count, time);
			}
		};
		ConcurrentExecutor.execute(noOfThreads, numberOfExecutionsPerThread, action);		
		assertHits(noOfThreads * numberOfExecutionsPerThread * count);
		assertTotalTime(noOfThreads * numberOfExecutionsPerThread * time);
	}	
	
	@Test
	public void shouldHandleConcurrentIncrementAndAddEvents() throws Exception {
		final int count = 2;
		final int time = 3;
		Runnable action = new Runnable() {
			public void run() {
				counter.incrementEvent(concurrentEvent, time);
				counter.addEvents(concurrentEvent, count, time);
			}
		};
		ConcurrentExecutor.execute(noOfThreads, numberOfExecutionsPerThread, action);		
		assertHits(noOfThreads * numberOfExecutionsPerThread * (count + 1));
		assertTotalTime(noOfThreads * numberOfExecutionsPerThread * 2 * time);
	}	
	
	private void assertHits(int hits) {
		assertEquals(hits, ((Monitor) counter.getMonitor(concurrentEvent)).getHits(), 0);
	}
	
	private void assertTotalTime(int time) {
		assertEquals(time, ((Monitor) counter.getMonitor(concurrentEvent)).getTotal(), 0);
	}	
}
