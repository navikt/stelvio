package utils;
/**
 * This class holds predefined queries to use with the FEM toolkit.
 *
 * @author Andreas Røe
 * @author persona2c5e3b49756 Schnell
 */
public class Queries {
	
	/** Public constant to represent the query to get the current number of failes events */
	public static final String QUERY_COUNT_EVENTS = "getFailedEventCount";
	
	/** Public constant to represent the query to all failed events from the failure event manager (FEM) */
	public static final String QUERY_ALL_EVENTS = "getAllFailedEvents";
	
	/** Public constant to represent the query to all failed events from the failure event manager (FEM)in detail */
	public static final String QUERY_EVENT_WITH_PARAMETERS = "getFailedEventWithParameters";

	/** Public constant to represent the query to all failed events from the failure event manager (FEM)in detail */
	public static final String QUERY_EVENT_WITH_TIMEPERIOD = "getFailedEventsForTimePeriod";

	/** Public constant to represent the query to all failed events from the failure event manager (FEM)in detail */
	public static final String QUERY_EVENT_WITH_SESSIONID = "getFailedEventsBySessionId";
	
	/** Public constant to represent the discard of failed events from the failure event manager (FEM) */
	public static final String QUERY_DISCARD_FAILED_EVENTS = "discardFailedEvents";
	
	/** Public constant to represent the resubmit of failed events from the failure event manager (FEM) */
	public static final String QUERY_RESUBMIT_FAILED_EVENTS = "discardFailedEvents";
	
	/** Public constant to represent the discard of all failed events from the failure event manager (FEM) */
	public static final String QUERY_DISCARD_ALL_FAILED_EVENTS = "discardAll";
}
