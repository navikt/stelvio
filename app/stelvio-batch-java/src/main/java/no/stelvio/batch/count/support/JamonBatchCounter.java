package no.stelvio.batch.count.support;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.jamonapi.MonKey;
import com.jamonapi.MonKeyImp;
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
	private ThreadLocal<Map<CounterEvent, Monitor>> timeMonitorsForThread = new ThreadLocal<Map<CounterEvent,Monitor>>() {
		/** {@inheritDoc} */
		@Override
		protected Map<CounterEvent, Monitor> initialValue() {
			return new HashMap<CounterEvent, Monitor>();
		}
	};

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
			synchronized (event) {
				Monitor monitor = MonitorFactory.start(getMonKey(event));
				timeMonitorsForThread.get().put(event, monitor);
			}
		}
		super.start(event);
	}

	@Override
	public void stop(CounterEvent event) {
		if (isJamonEnabled) {
			synchronized (event) {
				Monitor monitor = timeMonitorsForThread.get().get(event);
				monitor.stop();
			}
		}
		super.stop(event);
	}


	@Override
	public void stop(CounterEvent event, int count) {
		if (isJamonEnabled) {
			synchronized (event) {
				Monitor monitor = timeMonitorsForThread.get().get(event);
				monitor.stop();
				monitor.setHits(monitor.getHits() + count - 1); //monitor.stop() adds 1
			}
		}
		super.stop(event, count);
	}

	@Override
	public void incrementEvent(CounterEvent event) {
		if (isJamonEnabled) {
			Monitor monitor = MonitorFactory.getMonitor(getMonKey(event));
			synchronized (event) {
				monitor.setHits(monitor.getHits() + 1);
			}
		}
		super.incrementEvent(event);
	}

	@Override
	public void incrementEvent(CounterEvent event, long ms) {
		if (isJamonEnabled) {
			Monitor monitor = MonitorFactory.getMonitor(getMonKey(event));
			synchronized (event) {
				monitor.add(ms);
			}
		}
		super.incrementEvent(event, ms);
	}

	@Override
	public void addEvents(CounterEvent event, long count) {
		if (isJamonEnabled) {
			Monitor monitor = MonitorFactory.getMonitor(getMonKey(event));
			synchronized (event) {
				monitor.setHits(monitor.getHits() + count);
			}
		}
		super.addEvents(event, count);
	}

	@Override
	public void addEvents(CounterEvent event, long count, long ms) {
		if (isJamonEnabled) {
			Monitor monitor = MonitorFactory.getMonitor(getMonKey(event));
			synchronized (event) {
				monitor.add(ms);
				monitor.setHits(monitor.getHits() + count - 1); //monitor.add adds 1
			}
		}
		super.addEvents(event, count, ms);
	}
	
	
	public Monitor getMonitor(CounterEvent event) {
		return MonitorFactory.getTimeMonitor(getMonKey(event));
	}
	
	private MonKey getMonKey(CounterEvent event) {
		return new MonKeyImp(getMonitorLabel(event), UNITS);
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
