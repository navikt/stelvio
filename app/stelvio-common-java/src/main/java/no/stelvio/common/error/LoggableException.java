package no.stelvio.common.error;

/**
 * LoggableException is an interface that must be implemented by all 
 * application and system exceptions in order to take advantage of the advanced 
 * exception logging features.
 * 
 * @author person7553f5959484
 * @version $Revision: 917 $ $Author: psa2920 $ $Date: 2004-07-14 13:38:28 +0200 (Wed, 14 Jul 2004) $
 * @todo have loggable, but not all these props, have more interfaces for these 
 */
public interface LoggableException {

	/**
	  * Checks if the exception already has been logged.
	  *
	  * @return true if logged, false otherwise
	  */
	boolean isLogged();

	/**
	 * Mark the exception as logged.
	 */
	void setLogged();

}
