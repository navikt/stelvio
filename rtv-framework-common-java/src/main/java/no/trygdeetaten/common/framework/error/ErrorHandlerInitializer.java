package no.trygdeetaten.common.framework.error;

import no.trygdeetaten.common.framework.core.Initializable;

/**
 * Class that initializes the ErrorHandler.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 887 $ $Author: psa2920 $ $Date: 2004-07-07 15:11:26 +0200 (Wed, 07 Jul 2004) $
 */
public class ErrorHandlerInitializer implements Initializable {

	/**
	 * Delegates initialization to ErrorHandler.init().
	 * 
	 * {@inheritDoc}
	 * @see no.trygdeetaten.common.framework.core.Initializable#init()
	 */
	public void init() {
		ErrorHandler.init();
	}
}
