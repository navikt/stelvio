package no.stelvio.common.event.support;

import org.springframework.context.ApplicationEvent;

import no.stelvio.common.event.ApplicationEventListener;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 * @todo is this a god name? Should call IBMs Common Event Infrastructure
 * @todo will be implemented when we know more about how to use CEI.
 */
public class CeiApplicationEventListener implements ApplicationEventListener {
    public void onApplicationEvent(ApplicationEvent event) {
    }
}
