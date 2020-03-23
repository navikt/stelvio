package no.stelvio.presentation.security.page;

import no.stelvio.common.security.SecurityException;

/**
 * Always thrown from the SecurityPhaseListener whenever a page requires authentication. It is normal for this exception to be
 * rethrown within the PhaseListener, and out into the PhaseListenerManager so that any PhaseListeners scheduled to run after
 * the SecurityPhaseListener will be aborted.
 * 
 */
public class PageAuthenticationRequiredException extends SecurityException {

	private static final long serialVersionUID = 2645594313572000103L;

	private PageAuthenticationRequest authenticationRequest;

	/**
	 * Constructs a <code>PageAuthenticationRequiredException</code> with page authentication request and message.
	 * 
	 * @param authenticationRequest -
	 *            the page authentication request.
	 * @param message -
	 *            the exception message.
	 */
	public PageAuthenticationRequiredException(PageAuthenticationRequest authenticationRequest, String message) {
		super(message);
		this.authenticationRequest = authenticationRequest;
	}

	/**
	 * Constructs a <code>PageAuthenticationRequiredException</code> with message and cause.
	 * 
	 * @param message -
	 *            the exception message.
	 * @param cause -
	 *            the throwable that caused the exception to be raised.
	 */
	public PageAuthenticationRequiredException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a <code>PageAuthenticationRequiredException</code> with message.
	 * 
	 * @param message -
	 *            the exception message.
	 */
	public PageAuthenticationRequiredException(String message) {
		super(message);
	}

	/**
	 * Get authenticationRequest.
	 * 
	 * @return authenticationRequest
	 */
	public PageAuthenticationRequest getAuthenticationRequest() {
		return authenticationRequest;
	}

}