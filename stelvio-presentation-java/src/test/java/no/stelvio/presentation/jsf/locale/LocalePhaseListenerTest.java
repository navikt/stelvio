package no.stelvio.presentation.jsf.locale;

import java.util.Locale;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.context.i18n.LocaleContextHolder;

import no.stelvio.presentation.security.page.AbstractPhaselistenerTestCase;

/**
 * Test class for the LocalePhaseListener, which sets the locale from
 * faces context in Spring LocaleContextHolder.
 *  
 * @version $Id$
 */
public class LocalePhaseListenerTest extends AbstractPhaselistenerTestCase {
	private LocalePhaseListener localePhaseListener;
	private Locale defaultLocale;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onSetUp() {		
		localePhaseListener = new LocalePhaseListener();
		defaultLocale = Locale.FRANCE;
		facesContext.getApplication().setDefaultLocale(defaultLocale);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onTearDown() {
	}
	
	/**
	 * Test that the default locale from faces context is set in the Spring LocaleContextHolder object.
	 */
	@Test
	public void testBefore() {
		assertTrue(LocaleContextHolder.getLocale() != defaultLocale);
		PhaseEvent event = new PhaseEvent(facesContext, PhaseId.INVOKE_APPLICATION, lifecycle);
		localePhaseListener.beforePhase(event);
		
		assertTrue(LocaleContextHolder.getLocale() == defaultLocale);
	}

}
