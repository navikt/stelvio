package no.stelvio.common.event.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationEvent;

import no.stelvio.common.event.ApplicationEventListener;

/**
 * @author personf8e9850ed756
 */
public class LoggingApplicationEventListener implements ApplicationEventListener {
	private static Log log = LogFactory.getLog(LoggingApplicationEventListener.class);

	/**
	 * Throws an IllegalArgumentException if the event is null, and logs the event in the log.
	 * 
	 * @param event an applicationEvent
	 */
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (null == event) {
			throw new IllegalArgumentException("event cannot be null");
		}

		log.info(event);
	}
}
