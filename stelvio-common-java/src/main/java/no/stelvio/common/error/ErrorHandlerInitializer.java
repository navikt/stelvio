package no.stelvio.common.error;

import no.stelvio.common.core.Initializable;
import no.stelvio.common.error.old.ErrorHandler;


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
	 * @see no.stelvio.common.core.Initializable#init()
	 */
	public void init() {
		ErrorHandler.init();
	}
}
