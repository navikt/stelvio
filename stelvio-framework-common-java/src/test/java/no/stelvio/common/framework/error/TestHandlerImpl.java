package no.stelvio.common.framework.error;

import no.stelvio.common.framework.error.Handler;

/**
 * Handler for testing ErrorHandler.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 1037 $ $Author: psa2920 $ $Date: 2004-08-16 15:25:10 +0200 (Mon, 16 Aug 2004) $
 */
public class TestHandlerImpl implements Handler {

	public void init() {
	}
	public Throwable handleError(Throwable t) {
		return t;
	}
	public String getMessage(Throwable t) {
		return t.getLocalizedMessage();
	}
}
