package no.stelvio.batch.count.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import no.stelvio.batch.count.support.CounterEvent.EventType;

import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class BatchCounterTest {
	private BatchCounter counter;
	private CounterEvent funcEvent = CounterEvent.createCounterEvent(getClass(),
			"testFunc", "test functional event", EventType.FUNCTIONAL);
	private CounterEvent techEvent = CounterEvent.createCounterEvent(getClass(),
			"testTech", "test technincal event", EventType.TECHNICAL);

	@Before
	public void setUp() {
		counter = new SimpleBatchCounter(new HashSet<CounterEvent>(Arrays.asList(funcEvent, techEvent)));
	}
	
	@Test
	public void shoudIncrementEvent() {
		counter.incrementEvent(funcEvent);
		counter.incrementEvent(techEvent);
		counter.incrementEvent(techEvent);
		Map<CounterEvent, ? extends EventCounter> events = counter.getEventReport();
		assertEquals(1, events.get(funcEvent).getCount());
		assertEquals(2, events.get(techEvent).getCount());
	}
	
	@Test
	public void shoudIncrementEventAndAddTime() {
		counter.incrementEvent(funcEvent, 2);
		counter.incrementEvent(techEvent, 1);
		counter.incrementEvent(techEvent, 2);
		Map<CounterEvent, ? extends EventCounter> events = counter.getEventReport();
		assertEquals(1, events.get(funcEvent).getCount());
		assertEquals(2, events.get(funcEvent).getTime());
		assertEquals(2, events.get(techEvent).getCount());
		assertEquals(3, events.get(techEvent).getTime());
	}	
	
	@Test
	public void shoudAddEvents() {
		counter.incrementEvent(funcEvent);
		counter.addEvents(funcEvent, 2);
		counter.addEvents(techEvent, 5);
		Map<CounterEvent, ? extends EventCounter> events = counter.getEventReport();
		assertEquals(3, events.get(funcEvent).getCount());
		assertEquals(5, events.get(techEvent).getCount());
	}	

	@Test
	public void shoudAddEventsAndTime() {
		counter.incrementEvent(funcEvent);
		counter.addEvents(funcEvent, 3, 2);
		
		counter.addEvents(techEvent, 5, 10);
		counter.incrementEvent(techEvent, 5);
		counter.addEvents(techEvent, 10, 15);
		Map<CounterEvent, ? extends EventCounter> events = counter.getEventReport();
		assertEquals(4, events.get(funcEvent).getCount());
		assertEquals(2, events.get(funcEvent).getTime());
		assertEquals(16, events.get(techEvent).getCount());
		assertEquals(30, events.get(techEvent).getTime());
	}	
	
	//
	@Test
	public void shouldStartAndStop() throws InterruptedException {
		counter.start(funcEvent);
		Thread.sleep(1);
		counter.stop(funcEvent);
		Map<CounterEvent, ? extends EventCounter> events = counter.getEventReport();
		assertEquals(1, events.get(funcEvent).getCount());
		assertTrue(events.get(funcEvent).getTime() > 0);
	}
	
	//
	@Test
	public void shouldStartAndStopAndAddToTime() throws InterruptedException {
		long time = 123456;
		counter.incrementEvent(funcEvent, time);
		counter.start(funcEvent);
		Thread.sleep(1);
		counter.stop(funcEvent);
		Map<CounterEvent, ? extends EventCounter> events = counter.getEventReport();
		assertEquals(2, events.get(funcEvent).getCount());
		assertTrue(events.get(funcEvent).getTime() > time);
	}	
	
	//
	@Test
	public void shouldStartAndStopAndAddToCount() throws InterruptedException {
		counter.start(funcEvent);
		Thread.sleep(1);
		counter.stop(funcEvent, 11);
		Map<CounterEvent, ? extends EventCounter> events = counter.getEventReport();
		assertEquals(11, events.get(funcEvent).getCount());
		assertTrue(events.get(funcEvent).getTime() > 0);
	}	
}
