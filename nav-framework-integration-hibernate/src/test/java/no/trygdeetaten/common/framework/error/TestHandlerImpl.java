package no.trygdeetaten.common.framework.error;

import no.trygdeetaten.common.framework.error.Handler;

/**
 * Handler for testing ErrorHandler.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 1141 $ $Author: psa2920 $ $Date: 2004-08-24 10:33:12 +0200 (Tue, 24 Aug 2004) $
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
