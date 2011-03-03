package no.nav.femhelper.common;
/**
 * This class holds predefined queries to use with the FEM toolkit.
 *
 * @author Andreas Røe
 * @author persona2c5e3b49756 Schnell
 */
public class Queries {
	
	/** Public constant to represent the query to get the current number of failed events */
	public static final String QUERY_COUNT_EVENTS = "getFailedEventCount";
	
	/** Public constant to represent the query to all failed events from the failure event manager (FEM) */
	public static final String QUERY_ALL_EVENTS = "getAllFailedEvents";
	
	/** Public constant to represent the query to all failed events from the failure event manager (FEM)in detail */
	public static final String QUERY_EVENT_WITH_PARAMETERS = "getFailedEventWithParameters";

	/** Public constant to represent the query to all failed events from the failure event manager (FEM)in detail */
	public static final String QUERY_EVENT_WITH_SESSIONID = "getFailedEventsBySessionId";
	
	/** Public constant to represent the query to all failed events from the failure event manager (FEM)in detail */
	public static final String QUERY_EVENT_WITH_MESSAGEID = "getFailedEventsByMessageId";
	
	/** Public constant to represent the discard of failed events from the failure event manager (FEM) */
	public static final String QUERY_DISCARD_FAILED_EVENTS = "discardFailedEvents";
	
	/** Public constant to represent the resubmit of failed events from the failure event manager (FEM) */
	public static final String QUERY_RESUBMIT_FAILED_EVENTS = "resubmitFailedEvents";
	
	/** Public constant to represent the discard of all failed events from the failure event manager (FEM) */
	public static final String QUERY_DISCARD_ALL_FAILED_EVENTS = "discardAll";
	
	/** Public constant to represent the query to all failed events from the failure event manager (FEM)
	 *  Replaces the deprecated methods getFailedEventForDestination and getFailedEventsForTimePeriod in WPS6.1*/
	public static final String QUERY_FAILED_EVENTS = "queryFailedEvents";	
	
	/** Public constant to represent the query to get details about SCA events from the failure event manager (FEM)
	 *  Replaces the deprecated methods getFailedEventWithParameters in WPS6.1*/
	public static final String QUERY_EVENT_DETAIL_FOR_SCA = "getEventDetailForSCA";
	
	/** Public constant to represent the query to get details about BPC events from the failure event manager (FEM)
	 *  Replaces the deprecated methods getFailedEventWithParameters in WPS6.1*/
	public static final String QUERY_EVENT_DETAIL_FOR_BPC = "getEventDetailForBPC";
	
	/** Public constant to represent the query to get details about JMS events from the failure event manager (FEM)
	 *  Replaces the deprecated methods getFailedEventWithParameters in WPS6.1*/
	public static final String QUERY_EVENT_DETAIL_FOR_JMS = "getEventDetailForJMS";
	
	/** Public constant to represent the query to get details about JMS events from the failure event manager (FEM)
	 *  Replaces the deprecated methods getFailedEventWithParameters in WPS6.1*/
	public static final String QUERY_EVENT_DETAIL_FOR_MQ = "getEventDetailForMQ";
}
