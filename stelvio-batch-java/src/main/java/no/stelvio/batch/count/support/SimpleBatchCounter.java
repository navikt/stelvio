package no.stelvio.batch.count.support;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.util.Assert;

/**
 * Simple map based implementation of {@link BatchCounter}.
 * 
 */
public class SimpleBatchCounter implements BatchCounter {
	private ConcurrentMap<CounterEvent, SimpleEventCounter> events = new ConcurrentHashMap<>();

	/**
	 * Used by framework for AOP proxy creation
	 */
	SimpleBatchCounter() {
	}

	/**
	 * Creates a new counter and register all events in order to include events
	 * that are not incremented/used in a run in the report.
	 * 
	 * @param events
	 *            A list of classes that contains {@link CounterEvent}
	 *            constants.
	 */
	public SimpleBatchCounter(Class<?>... events) {
		for (Class<?> eventHolder : events) {
			try {
				Constructor<?> c = eventHolder.getDeclaredConstructor();
				c.setAccessible(true);
				c.newInstance();
			} catch (Exception e) {
				throw new RuntimeException("Failed to load counter events.", e);
			}
			mergeEvents(CounterEvent.getRegisteredEvents().get(eventHolder));
		}
	}

	/**
	 * Creates a new batch counter.
	 * 
	 * @param registeredEvents
	 *            Events to count.
	 */
	public SimpleBatchCounter(Set<CounterEvent> registeredEvents) {
		for (CounterEvent event : registeredEvents) {
			events.put(event, new SimpleEventCounter(0, 0));
		}
	}

	/**
	 * Merges events into this counter instance. (Adds events that does not
	 * already exists in the event map)
	 * 
	 * @param registeredEvents
	 *            A set of events.
	 */
	private void mergeEvents(Set<CounterEvent> registeredEvents) {
		for (CounterEvent event : registeredEvents) {
			getEventOrPutIfAbsent(event);
		}
	}

	/**
	 * Skal h�ndtere at en eventklasse ikke er registrert
	 * 
	 * @param event
	 * @return
	 */
	private SimpleEventCounter getEventOrPutIfAbsent(CounterEvent event) {
		Assert.notNull(event, "CounterEvent can not be null.");
		SimpleEventCounter eventCounter = events.putIfAbsent(event, new SimpleEventCounter(0, 0));
		return eventCounter != null ? eventCounter : events.get(event);
	}

	@Override
	public void resetCounter() {
		for (Entry<CounterEvent, SimpleEventCounter> eventEntry : events.entrySet()) {
			eventEntry.setValue(new SimpleEventCounter(0, 0));
		}
	}

	@Override
	public void addEvents(CounterEvent event, long count) {
		SimpleEventCounter counter = getEventOrPutIfAbsent(event);
		counter.addCount(count);
	}

	@Override
	public void addEvents(CounterEvent event, long count, long ms) {
		SimpleEventCounter counter = getEventOrPutIfAbsent(event);
		counter.addCount(count);
		counter.addTime(ms);
	}

	public Map<CounterEvent, ? extends EventCounter> getEventReport() {
		return Collections.unmodifiableMap(events);
	}

	@Override
	public void incrementEvent(CounterEvent event) {
		SimpleEventCounter counter = getEventOrPutIfAbsent(event);
		counter.addCount(1);
	}

	@Override
	public void incrementEvent(CounterEvent event, long ms) {
		SimpleEventCounter counter = getEventOrPutIfAbsent(event);
		counter.addCount(1);
		counter.addTime(ms);
	}

	@Override
	public void start(CounterEvent event) {
		SimpleEventCounter e = getEventOrPutIfAbsent(event);
		e.setStartTime(System.currentTimeMillis());
	}

	@Override
	public void stop(CounterEvent event) {
		SimpleEventCounter e = addTime(event);
		e.addCount(1);
	}

	@Override
	public void stop(CounterEvent event, int count) {
		SimpleEventCounter e = addTime(event);
		e.addCount(count);
	}

	private SimpleEventCounter addTime(CounterEvent event) {
		SimpleEventCounter e = getEventOrPutIfAbsent(event);
		e.addTime(System.currentTimeMillis() - e.getStartTime());
		return e;
	}
}
