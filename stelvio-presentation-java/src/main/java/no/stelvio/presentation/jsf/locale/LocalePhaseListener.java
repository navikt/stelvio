/**
 * 
 */
package no.stelvio.presentation.jsf.locale;

import java.util.Locale;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Phaselistener for setting the current locale from FacesContext in the Spring LocaleContextHolder. This phaselistener
 * is invoked before the <code>INVOKE_APPLICATION</code> phase.
 * 
 * @version $Id$
 */
public class LocalePhaseListener implements PhaseListener {
	private static final long serialVersionUID = -3817462721376494041L;

	/**
	 * Nothing is done after this phase.
	 * 
	 * @param event The phase event that has occured
	 */
	public void afterPhase(PhaseEvent event) {
		// nothing to do
	}


	/**
	 * Before the 'invoke application' phase is entered the current locale in the Spring LocaleContextHolder is updated
	 * with the locale set in the application. Retrieves the current locale from the current view root and sets it in
	 * the Spring LocaleContextHolder.
	 * 
	 * @param event The phase event that has occured.
	 */
	public void beforePhase(PhaseEvent event) {
		Locale currentLocale = event.getFacesContext().getViewRoot().getLocale(); 
		LocaleContextHolder.setLocale(currentLocale, true);
	}

	/**
	 * Returns the phase id of the phase this phase listener is listening on, this phaselistener is listening on the
	 * <code>INVOKE_APPLICATION</code> phase.
	 * 
	 * @return the phase id of the <code>INVOKE_APPLICATION</code> phase.
	 */
	public PhaseId getPhaseId() {
		return PhaseId.INVOKE_APPLICATION;
	}

}
