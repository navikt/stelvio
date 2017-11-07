package no.stelvio.presentation.security.page;

/**
 * A wrapper exception ment to be thrown by a PhaseListener in order to prevent any PhaseListeners scheduled to run after the
 * current PhaseListener to be ignored by the PhaseListenerManager. The PhaseListenerManager will according to the JSF 1.2 spec.
 * always catch and log (swallow) exceptions thrown in the afterPhase() and beforePhase() methods of registered PhaseListeners,
 * and ignore any subsequent phaselisteners until the next phase. For the SecurityPhaseListener this is particulary important as
 * many situations requires the user to be authenticated before any subsequent phaselisteners are executed.
 * 
 * @see JeeSecurityPhaseListener
 * @see PhaseListenerManager
 * @author persondab2f89862d3, Accenture
 */
public class IgnoreSubsequentPhaselisteners extends RuntimeException {

	private static final long serialVersionUID = -6220893097700279765L;

	/**
	 * Creates a new instance of IgnoreSubsequentPhaselisteners.
	 * 
	 * @param cause
	 *            cause
	 */
	public IgnoreSubsequentPhaselisteners(Throwable cause) {
		super(cause);
	}
}
