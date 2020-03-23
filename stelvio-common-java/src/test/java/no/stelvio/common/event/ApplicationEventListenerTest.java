package no.stelvio.common.event;

import org.junit.Test;

/**
 * Unit test for {@link ApplicationEventListener}.
 *
 */
public abstract class ApplicationEventListenerTest {
	/**
	 * Test that application argument are mandatory.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void applicationEventAreMandatory() {
		createEventListener().onApplicationEvent(null);
	}

	/**
     * Create event listener.
     * 
     * @return listener
     */
	protected abstract ApplicationEventListener createEventListener();
}
