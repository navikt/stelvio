package no.stelvio.common.event;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsSame.sameInstance;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link ApplicationEvent}.
 * 
 * @author personf8e9850ed756
 * @deprecated 
 */
public abstract class ApplicationEventTest {
	private ApplicationEvent event;

	/**
	 * Test constructorArgumentAreMandatory.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void constructorArgumentAreMandatory() {
		new ApplicationEvent(null) {
			private static final long serialVersionUID = 1L;
		};
	}

	/**
	 * Test objectOnWhichTheEventInitiallyOccurredIsSaved.
	 */
	@Test
	public void objectOnWhichTheEventInitiallyOccurredIsSaved() {
		assertThat((ApplicationEventTest) event.getSource(), is(sameInstance(this)));
	}

	/**
	 * Test timeStampIsSetOnCreatedEvent.
	 */
	@Test
	public void timeStampIsSetOnCreatedEvent() {
		assertThat(event.getTimestamp(), is(not(equalTo(0L))));
	}

	/**
	 * Create application event.
	 * 
	 * @return event
	 */
	protected abstract ApplicationEvent createApplicationEvent();

	/**
	 * Setup before test.
	 * 
	 * @throws Exception exception
	 */
	@Before
	public void setupApplicationEvent() throws Exception {
		event = createApplicationEvent();
	}

	/**
	 * Get event.
	 * 
	 * @return event
	 */
	protected ApplicationEvent getEvent() {
		return event;
	}
}
