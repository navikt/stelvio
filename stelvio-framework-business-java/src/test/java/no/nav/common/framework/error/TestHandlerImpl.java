package no.nav.common.framework.error;

import no.nav.common.framework.error.Handler;

/**
 * Handler for testing ErrorHandler.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 1266 $ $Author: tkc2920 $ $Date: 2004-09-09 09:33:53 +0200 (Thu, 09 Sep 2004) $
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
