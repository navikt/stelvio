package no.stelvio.common.event.support;

import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import no.stelvio.common.error.strategy.support.TestCommonsLoggingFactory;
import no.stelvio.common.event.ApplicationEvent;
import no.stelvio.common.event.ApplicationEventListener;
import no.stelvio.common.event.ApplicationEventListenerTest;

/**
 * Unit test for {@link LoggingApplicationEventListener}.
 * 
 * @author personf8e9850ed756
 */
@RunWith(JMock.class)
public class LoggingApplicationEventListenerTest extends ApplicationEventListenerTest {
	private LoggingApplicationEventListener lael;
	private Mockery context = new JUnit4Mockery();

	/**
	 * Test if event should be logged.
	 * 
	 * @throws IllegalAccessException
	 *             illegal access
	 * @throws NoSuchFieldException
	 *             no field
	 */
	@Test
	public void eventShouldBeLogged() throws IllegalAccessException, NoSuchFieldException {
		final Log log = context.mock(Log.class);
		TestCommonsLoggingFactory.changeLogger(lael, log);

		context.checking(new Expectations() {
			{
				one(log).info(with(an(ApplicationEvent.class)));
			}
		});

		lael.onApplicationEvent(new ApplicationEvent(this) {
			private static final long serialVersionUID = 1L;
		});
	}

	/**
	 * Setup before test.
	 */
	@Before
	public void setupForTest() {
		lael = new LoggingApplicationEventListener();
	}

	/**
	 * Create event listener.
	 * 
	 * @return listener
	 */
	protected ApplicationEventListener createEventListener() {
		return lael;
	}
}
