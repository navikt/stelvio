package no.stelvio.common.event;

import static org.hamcrest.MatcherAssert.assertThat;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import static org.hamcrest.core.IsSame.same;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link ApplicationEvent}:
 *
 * @author personf8e9850ed756
 */
public abstract class ApplicationEventTest {
    private ApplicationEvent event;

    @Test(expected = IllegalArgumentException.class)
    public void constructorArgumentAreMandatory() {
        new ApplicationEvent(null) {};
    }

    @Test
    public void objectOnWhichTheEventInitiallyOccurredIsSaved() {
        assertThat((ApplicationEventTest) event.getSource(), same(this));
    }

	@Test
	public void timeStampIsSetOnCreatedEvent() {
		assertThat(event.getTimestamp(), IsNot.not(IsEqual.eq(0L)));
	}


	protected abstract ApplicationEvent createApplicationEvent();

    @Before
    public void setupApplicationEvent() throws Exception {
        event = createApplicationEvent();
    }

    protected ApplicationEvent getEvent() {
        return event;
    }
}
