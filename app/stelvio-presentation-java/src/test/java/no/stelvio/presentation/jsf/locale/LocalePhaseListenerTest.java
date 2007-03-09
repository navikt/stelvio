package no.stelvio.presentation.jsf.locale;

import java.util.Locale;
import static org.junit.Assert.*;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;

import no.stelvio.presentation.security.page.AbstractPhaselistenerTestCase;

import org.junit.Test;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Test class for the LocalePhaseListener, which sets the locale from
 * faces context in Spring LocaleContextHolder 
 * @author person6045563b8dec
 * @version $Id$
 *
 */
public class LocalePhaseListenerTest extends AbstractPhaselistenerTestCase {

	LocalePhaseListener localePhaseListener;
	Locale defaultLocale;
	
	/**
	 * Set up mock objects necessary to run the test
	 * @throws Exception if an error occurs during set up
	 */
	@Override
	protected void onSetUp() throws Exception {		
		localePhaseListener = new LocalePhaseListener();
		defaultLocale = Locale.FRANCE;
		facesContext.getApplication().setDefaultLocale(defaultLocale);
	}
	/**
	 * Tear down mock objects used in the test
	 * 
	 * @throws Exception if an error occurs during tear down
	 */
	
	protected void onTearDown() throws Exception {
		
	}
	
	/**
	 * Test that the default locale from faces context is set in the
	 * Spring LocaleContextHolder object.
	 *
	 */
	@Test
	public void testBefore() {
		assertTrue(LocaleContextHolder.getLocale() != defaultLocale);
		PhaseEvent event = new PhaseEvent(facesContext, PhaseId.INVOKE_APPLICATION, lifecycle);
		localePhaseListener.beforePhase(event);
		assertTrue(LocaleContextHolder.getLocale() == defaultLocale);
	}

}
