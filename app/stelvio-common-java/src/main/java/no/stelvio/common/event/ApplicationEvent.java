package no.stelvio.common.event;

/**
 * Class to be extended by all application events. Abstract as it doesn't make
 * sense for generic events to be published directly.
 * 
 * @author personf8e9850ed756
 * @deprecated This class does not add functionality and will be removed in next release
 */
public abstract class ApplicationEvent extends org.springframework.context.ApplicationEvent {
	private static final long serialVersionUID = 6886536415805349662L;

	/**
	 * Constructs a new ApplicationEvent.
	 * 
	 * @param source the source
	 */
	public ApplicationEvent(Object source) {
		super(source);
	}
}
