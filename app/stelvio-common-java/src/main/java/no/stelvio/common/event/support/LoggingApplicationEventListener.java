package no.stelvio.common.event.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationEvent;

import no.stelvio.common.event.ApplicationEventListener;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 * @todo should we do any filtering on type of ApplicationEvent?
 * @todo do we need a better log resolver? Can share with error logging
 */
public class LoggingApplicationEventListener implements ApplicationEventListener {
    private Log log = LogFactory.getLog(LoggingApplicationEventListener.class);

    public void onApplicationEvent(ApplicationEvent event) {
        if (null == event) {
            throw new IllegalArgumentException("event cannot be null");
        }

        log.info(event);
    }
}
