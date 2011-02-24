package no.stelvio.batch.count.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import no.stelvio.batch.count.support.CounterEvent.EventType;

import org.junit.Before;
import org.junit.Test;

import com.jamonapi.MonitorFactory;

/**
 * @author person47c121e3ccb5, BEKK
 *
 */
public class JamonBatchCounterTest {
	private BatchCounter counter;
	private CounterEvent funcEvent = CounterEvent.createCounterEvent(getClass(),
			"testFunc", "test functional event", EventType.FUNCTIONAL);
	private CounterEvent techEvent = CounterEvent.createCounterEvent(getClass(),
			"testTech", "test technincal event", EventType.TECHNICAL);
	
	private final int noOfThreads = 10;
	private CountDownLatch startSignal = new CountDownLatch(1);
	private CountDownLatch doneSignal = new CountDownLatch(noOfThreads);
	
	@Before
	public void setUp() {
		JamonBatchCounter jamonCounter = new JamonBatchCounter(new HashSet<CounterEvent>(Arrays.asList(funcEvent, techEvent)));
		jamonCounter.setJamonEnabled(true);
		jamonCounter.setJamonPrefix("TEST");
		MonitorFactory.reset();
		counter = jamonCounter;
	}
	
	@Test
	public void shouldNotRegisterEventsWithJamonIfDisabled() {
		((JamonBatchCounter) counter).setJamonEnabled(false);
		counter.incrementEvent(funcEvent);
		String report = MonitorFactory.getReport();
		assertEquals("" , report);
	}
	
	@Test
	public void shouldIncrementEventsWithJamon() {
		counter.incrementEvent(funcEvent);
		counter.incrementEvent(techEvent);
		counter.incrementEvent(funcEvent);
		Map<CounterEvent, ? extends EventCounter> events = counter.getEventReport();
		assertEquals(2, events.get(funcEvent).getCount());
		assertEquals(1, events.get(techEvent).getCount());
		String report = MonitorFactory.getReport();
		assertTrue(report.contains("<tr><td>TEST-testFunc, ms.</td><td>2.0</td>"));
		assertTrue(report.contains("<tr><td>TEST-testTech, ms.</td><td>1.0</td>"));
	}
	
	@Test
	public void shouldIncrementEventsAndAddTimeWithJamon() {
		counter.incrementEvent(funcEvent, 1);
		counter.incrementEvent(funcEvent, 3);
		Map<CounterEvent, ? extends EventCounter> events = counter.getEventReport();
		assertEquals(4, events.get(funcEvent).getTime());
		String report = MonitorFactory.getReport();
		assertTrue(report.contains("<tr><td>TEST-testFunc, ms.</td><td>2.0</td><td>2.0</td><td>4.0</td>"));
	}
	
	@Test
	public void shouldAddEventsWithJamon() {
		counter.incrementEvent(funcEvent);
		counter.addEvents(funcEvent, 3);
		Map<CounterEvent, ? extends EventCounter> events = counter.getEventReport();
		assertEquals(4, events.get(funcEvent).getCount());
		String report = MonitorFactory.getReport();
		assertTrue(report.contains("<tr><td>TEST-testFunc, ms.</td><td>4.0</td><td>0.0</td><td>0.0</td>"));
	}	
	
	@Test
	public void shouldAddEventsAndTimeWithJamon() {
		counter.addEvents(funcEvent, 1, 1);
		counter.addEvents(funcEvent, 3, 3);
		Map<CounterEvent, ? extends EventCounter> events = counter.getEventReport();
		assertEquals(4, events.get(funcEvent).getTime());
		assertEquals(4, events.get(funcEvent).getCount());
		String report = MonitorFactory.getReport();
		assertTrue(report.contains("<tr><td>TEST-testFunc, ms.</td><td>4.0</td><td>1.0</td><td>4.0</td>"));
	}	
	
	@Test
	public void shouldRegisterStartAndStopEventsWithJamon() {
		counter.start(funcEvent);
		counter.start(techEvent);
		counter.stop(techEvent);
		counter.stop(funcEvent);
		counter.start(funcEvent);
		counter.start(funcEvent);
		counter.stop(funcEvent);
		counter.stop(funcEvent);		
		Map<CounterEvent, ? extends EventCounter> events = counter.getEventReport();
		assertEquals(3, events.get(funcEvent).getCount());
		assertEquals(1, events.get(techEvent).getCount());
		String report = MonitorFactory.getReport();
		assertTrue(report.contains("<tr><td>TEST-testFunc, ms.</td><td>3.0</td>"));
		assertTrue(report.contains("<tr><td>TEST-testTech, ms.</td><td>1.0</td>"));
	}
	
	@Test
	public void shouldHandleConcurrentIncrements() throws Exception {
		for (int i = 0; i < noOfThreads; i++) {
			new Thread(new EventIncrementer(startSignal, doneSignal)).start();
		}
		startSignal.countDown();
		doneSignal.await();
		
		assertCounters(noOfThreads);
	}
	
	@Test
	public void shouldHandleConcurrentStartAndStop() throws Exception {
		for (int i = 0; i < noOfThreads; i++) {
			new Thread(new EventStarterStopper(startSignal, doneSignal)).start();
		}
		startSignal.countDown();
		doneSignal.await();
		
		assertCounters(noOfThreads);
	}

	private void assertCounters(final int noOfThreads) {
		Map<CounterEvent, ? extends EventCounter> events = counter.getEventReport();
		assertEquals(noOfThreads, events.get(funcEvent).getCount());
		String report = MonitorFactory.getReport();
		assertTrue(report, report.contains("<tr><td>TEST-testFunc, ms.</td><td>" + noOfThreads + ".0</td>"));
	}
	
	private abstract class AbstractEventExecutor implements Runnable {
		protected final CountDownLatch startSignal;
		protected final CountDownLatch doneSignal;
		
		public AbstractEventExecutor(CountDownLatch startSignal, CountDownLatch doneSignal) {
			this.startSignal = startSignal;
			this.doneSignal = doneSignal;
		}
	}
	
	private class EventIncrementer extends AbstractEventExecutor {

		public EventIncrementer(CountDownLatch startSignal, CountDownLatch doneSignal) {
			super(startSignal, doneSignal);
		}

		public void run() {
			try {
				startSignal.await();
				counter.incrementEvent(funcEvent);
				doneSignal.countDown();
			} catch (InterruptedException e) {
			}
		}
	}
	
	private class EventStarterStopper extends AbstractEventExecutor {

		public EventStarterStopper(CountDownLatch startSignal, CountDownLatch doneSignal) {
			super(startSignal, doneSignal);
		}

		public void run() {
			try {
				startSignal.await();
				counter.start(funcEvent);
				Thread.sleep(new Random(this.hashCode()).nextInt(200));
				counter.stop(funcEvent);
				doneSignal.countDown();
			} catch (InterruptedException e) {
			}
		}
	}
	
}
