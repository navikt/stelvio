package no.stelvio.batch.count.support;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.util.Assert;

/**
 * Simple map based implementation of {@link BatchCounter}.
 * 
 * @author person47c121e3ccb5, BEKK
 * @author person95f6f76be33a, Sirius IT
 */
public class SimpleBatchCounter implements BatchCounter {

	private ConcurrentMap<CounterEvent, SimpleEventCounter> events = new ConcurrentHashMap<CounterEvent, SimpleEventCounter>();

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
	 * Skal håndtere at en eventklasse ikke er registrert
	 * 
	 * @param event
	 * @return
	 */
	private SimpleEventCounter getEventOrPutIfAbsent(CounterEvent event) {
		Assert.notNull(event, "CounterEvent can not be null.");
		SimpleEventCounter eventCounter = events.putIfAbsent(event, new SimpleEventCounter(0, 0));
		return eventCounter != null ? eventCounter : events.get(event);
	}

	/** {@inheritDoc} */
	public void resetCounter() {
		for (Entry<CounterEvent, SimpleEventCounter> eventEntry : events.entrySet()) {
			eventEntry.setValue(new SimpleEventCounter(0, 0));
		}
	}

	/** {@inheritDoc} */
	public void addEvents(CounterEvent event, long count) {
		SimpleEventCounter counter = getEventOrPutIfAbsent(event);
		counter.count += count;
	}

	/** {@inheritDoc} */
	public void addEvents(CounterEvent event, long count, long ms) {
		SimpleEventCounter counter = getEventOrPutIfAbsent(event);
		counter.count += count;
		counter.time += ms;
	}

	public Map<CounterEvent, ? extends EventCounter> getEventReport() {
		return Collections.unmodifiableMap(events);
	}

	/** {@inheritDoc} */
	public void incrementEvent(CounterEvent event) {
		SimpleEventCounter counter = getEventOrPutIfAbsent(event);
		counter.count++;
	}

	/** {@inheritDoc} */
	public void incrementEvent(CounterEvent event, long ms) {
		SimpleEventCounter counter = getEventOrPutIfAbsent(event);
		counter.count++;
		counter.time += ms;
	}

	/** {@inheritDoc} */
	public void start(CounterEvent event) {
		SimpleEventCounter e = getEventOrPutIfAbsent(event);
		e.startTime = System.currentTimeMillis();
	}

	/** {@inheritDoc} */
	public void stop(CounterEvent event) {
		SimpleEventCounter e = addTime(event);
		e.count++;
	}

	/** {@inheritDoc} */
	public void stop(CounterEvent event, int count) {
		SimpleEventCounter e = addTime(event);
		e.count += count;
	}

	private SimpleEventCounter addTime(CounterEvent event) {
		SimpleEventCounter e = getEventOrPutIfAbsent(event);
		e.time += System.currentTimeMillis() - e.startTime;
		return e;
	}

	/**
	 * @author person47c121e3ccb5, BEKK
	 * 
	 */
	static class SimpleEventCounter implements EventCounter {
		private long count;
		private long time;
		private long startTime;

		/**
		 * Creates a new counter
		 * 
		 * @param count
		 *            initial count value
		 * @param time
		 *            initial time value
		 */
		public SimpleEventCounter(long count, long time) {
			this.count = count;
			this.time = time;
		}

		/** {@inheritDoc} */
		public long getCount() {
			return count;
		}

		/** {@inheritDoc} */
		public long getTime() {
			return time;
		}

		/** {@inheritDoc} */
		public long getAvg() {
			if (count == 0) {
				return 0;
			}
			return time / count;
		}

		/** {@inheritDoc} */
		@Override
		public String toString() {
			return "count=" + count + ", time=" + time + ", avg=" + getAvg();
		}

	}

}
