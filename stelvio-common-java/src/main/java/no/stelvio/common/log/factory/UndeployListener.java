package no.stelvio.common.log.factory;

import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

/**
 * This implementation of ApplicationListener performs cleanup of references to the application classloader in commons-logging
 * when the application is stopped.
 * 
 * Note that we use getClass().getClassLoader() to identify the classloader, so this class MUST be deployed in the application
 * container!
 * 
 *
 */
public class UndeployListener implements ApplicationListener {
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof ContextClosedEvent) {
			LogFactory.release(getClass().getClassLoader());
		}
	}
}
