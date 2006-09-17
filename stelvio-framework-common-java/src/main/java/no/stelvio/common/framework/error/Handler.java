package no.stelvio.common.framework.error;

/**
 * Handler is the interface that all error handlers must implement.
 * 
 * @author person7553f5959484
 * @version $Revision: 744 $ $Author: psa2920 $ $Date: 2004-06-22 13:19:18 +0200 (Tue, 22 Jun 2004) $
 */
interface Handler {

	/**
	 * Initializes the error handler.
	 */
	void init();

	/**
	 * Handle the specified error.
	 * 
	 * @param t the Throwable to handle
	 * @return the handled Throwable
	 */
	Throwable handleError(Throwable t);

	/**
	 * Get the message describing the specified error.
	 * 
	 * @param t the error to describe.
	 * @return the error message
	 */
	String getMessage(Throwable t);
}
