package no.stelvio.batch.count.support;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

/**
 * Extends the {@link SimpleBatchCounter} by adding Jamon monitoring for
 * timed/counted events.
 * 
 * @author person47c121e3ccb5, BEKK
 * @author person95f6f76be33a, Sirius IT 
 */
public class JamonBatchCounter extends SimpleBatchCounter {
	private String jamonPrefix;
	private boolean isJamonEnabled = false;
	private static final String UNITS = "ms.";
	private ConcurrentMap<String, Monitor> monitors = new ConcurrentHashMap<String, Monitor>();

	/**
	 * Creates a counter and registers events.
	 * 
	 * @param events
	 *            Classes that holds events to be timed/counted
	 */
	public JamonBatchCounter(Class<?>... events) {
		super(events);
	}

	/**
	 * Creates a counter and registers events.
	 * 
	 * @param registeredEvents
	 *            Events to be timed/counted
	 */
	public JamonBatchCounter(Set<CounterEvent> registeredEvents) {
		super(registeredEvents);
	}

	@Override
	public void start(CounterEvent event) {
		if (isJamonEnabled) {
			Monitor monitor = MonitorFactory.start(getMonitorLabel(event));
			monitors.put(getMonitorLabel(event), monitor);
		}
		super.start(event);
	}

	@Override
	public void stop(CounterEvent event) {
		if (isJamonEnabled) {
			Monitor monitor = monitors.get(getMonitorLabel(event));
			monitor.stop();
		}
		super.stop(event);
	}

	@Override
	public void stop(CounterEvent event, int count) {
		if (isJamonEnabled) {
			Monitor monitor = monitors.get(getMonitorLabel(event));
			monitor.setHits(monitor.getHits() + count);
			monitor.stop();
		}
		super.stop(event, count);
	}

	@Override
	public void incrementEvent(CounterEvent event) {
		if (isJamonEnabled) {
			Monitor monitor = MonitorFactory.getMonitor(getMonitorLabel(event), UNITS);
			monitor.setHits(monitor.getHits() + 1);
		}
		super.incrementEvent(event);
	}

	@Override
	public void incrementEvent(CounterEvent event, long ms) {
		if (isJamonEnabled) {
			Monitor monitor = MonitorFactory.getMonitor(getMonitorLabel(event), UNITS);
			monitor.add(ms);
		}
		super.incrementEvent(event, ms);
	}

	@Override
	public void addEvents(CounterEvent event, long count) {
		if (isJamonEnabled) {
			Monitor monitor = MonitorFactory.getMonitor(getMonitorLabel(event), UNITS);
			monitor.setHits(monitor.getHits() + count);
		}
		super.addEvents(event, count);
	}

	@Override
	public void addEvents(CounterEvent event, long count, long ms) {
		if (isJamonEnabled) {
			Monitor monitor = MonitorFactory.getMonitor(getMonitorLabel(event), UNITS);
			monitor.add(ms);
			monitor.setHits(monitor.getHits() + count - 1);
		}
		super.addEvents(event, count, ms);
	}

	private String getMonitorLabel(CounterEvent event) {
		return jamonPrefix + "-" + event.getName();
	}

	public void setJamonPrefix(String jamonPrefix) {
		this.jamonPrefix = jamonPrefix;
	}

	public void setJamonEnabled(boolean isJamonEnabled) {
		this.isJamonEnabled = isJamonEnabled;
	}

}
