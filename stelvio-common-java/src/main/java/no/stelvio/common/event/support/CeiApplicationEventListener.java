package no.stelvio.common.event.support;

import org.springframework.context.ApplicationEvent;

import no.stelvio.common.event.ApplicationEventListener;

/**
 * Implemets an application event listener for CEI (IBM Common Event
 * Infrastructure).
 * 
 * Is this a god name? Should call IBMs Common Event Infrastructure. Will be
 * implemented when we know more about how to use CEI.
 * 
 * @author personf8e9850ed756
 */
public class CeiApplicationEventListener implements ApplicationEventListener {

	/**
	 * An event that indicates that a common event infrastructure (CEI)
	 * application event has occured.
	 * 
	 * @param event
	 *            a CEI application event
	 */
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
	}
}
