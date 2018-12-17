package no.stelvio.batch.count.support;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A batch event that can be counted by a {@link BatchCounter}.
 * 
 * @author person47c121e3ccb5, BEKK
 * 
 */
public final class CounterEvent {
	private static ConcurrentHashMap<Class<?>, Set<CounterEvent>> registeredEvents = 
		new ConcurrentHashMap<>();

	/**
	 * Defines different types of {@link CounterEvent}.
	 * 
	 * @author person47c121e3ccb5, BEKK
	 * 
	 */
	public enum EventType {
		/** */
		FUNCTIONAL,
		/** */
		TECHNICAL
	}

	private final String name;
	private final String description;
	private final EventType type;

	private CounterEvent(String name, String description, EventType type) {
		this.name = name;
		this.description = description;
		this.type = type;
	}

	/**
	 * Creates a new event and registrates it.
	 * 
	 * @param eventClass
	 *            Class that defines the event
	 * @param name
	 *            Name of event
	 * @param description
	 *            Description of event
	 * @param type
	 *            Type of event
	 * @return The newly created event
	 */
	public static CounterEvent createCounterEvent(Class<?> eventClass, String name, String description, EventType type) {
		CounterEvent event = new CounterEvent(name, description, type);
		Set<CounterEvent> events = registeredEvents.get(eventClass);
		if (events == null) {
			Set<CounterEvent> newEventSet = new HashSet<>();
			events = registeredEvents.putIfAbsent(eventClass, newEventSet);
			if (events == null) {
				events = newEventSet;
			}
		}
		events.add(event);
		return event;
	}

	/**
	 * @return the registeredEvents
	 */
	public static Map<Class<?>, Set<CounterEvent>> getRegisteredEvents() {
		return registeredEvents;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof CounterEvent)) {
			return false;
		}
		CounterEvent event = (CounterEvent) o;
		return new EqualsBuilder().append(name, event.name).append(description, event.description).append(type, event.type)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(7, 13).append(name).append(description).append(type).toHashCode();
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the type
	 */
	public EventType getType() {
		return type;
	}

	@Override
	public String toString() {
		return "name=" + name + ", description=" + description + ", type=" + type;
	}

}
