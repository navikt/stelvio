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
	
	/**
	 * Creates an exact copy of the instance 
	 * 
	 * @return a copy of this instance.
	 */
	Object copy();

    /**
	 * Get the arguments identifying details about the error.
	 * 
	 * @return The arguments detailing this exception.
	 */
	Object [] getArguments();
	
	/**
	 * Get the unique id of this exception.
	 *
	 * @return The unique id of this exception
	 */
	String getErrorId();

	/**
	 * Get the id of the user or system that executed
	 * the code when the exception occured.
	 * 
	 * @return The id of the current user/system
	 */
	String getUserId();
	
	/**
	 * Get the id of the screen that started the process 
	 * that executed the code that failed.
	 * 
	 * @return The id of the current screen, null if not user initiated
	 */
	String getScreenId();

	/**
	 * Get the id of the process that was executing
	 * the code when the exception occured.
	 * 
	 * @return The id of the current process
	 */
	String getProcessId();

	/**
	 * Get the unique id of the transaction that was executing
	 * the code when the exception occured.
	 * 
	 * @return The unique id of the current transaction
	 */
	String getTransactionId();
}
