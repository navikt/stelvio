package no.stelvio.common.event;

/**
 * Class to be extended by all application events. Abstract as it doesn't make sense for generic events to be published
 * directly.
 *
 * @author personf8e9850ed756
 * @todo Do we need our own or could we use Spring's version directly?
 */
public abstract class ApplicationEvent extends org.springframework.context.ApplicationEvent {
    public ApplicationEvent(Object source) {
        super(source);
    }
}
