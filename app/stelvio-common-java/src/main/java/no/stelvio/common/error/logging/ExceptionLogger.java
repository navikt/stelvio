package no.stelvio.common.error.logging;

import org.apache.commons.logging.Log;


/**
 * 
 * Interface for components used to log Exceptions in Stelvio.
 * 
 * @author person983601e0e117 (Accenture)
 *
 */
public interface ExceptionLogger {
	
	/**
	 * Get logger.
	 * 
	 * @return logger
	 */
	Log getLog();
	
	/**
	 * Set logger to be used when logging.
	 * 
	 * @param log Log
	 */
	void setLog(Log log);
		
	/**
	 * Method that is called to log an exception/throwable. The exception
	 * will be logged as either functional or technical depending on
	 * the exception type passed in to this method
	 * @param t The throwable to log
	 */
	void log(Throwable t);

	/**
	 * Method that is called to log an exception/throwable. The exception
	 * will be logged as either functional or technical depending on
	 * the exception type passed in to this method
	 * 
	 * @param message An explanatory text
	 * @param t The throwable to log
	 */
	void log(String message, Throwable t);
	
	
	/**
	 * Method used to log functional errors. As a rule, the {@link #log(Throwable)}
	 * method should be favored over {@link #logFunctional(Throwable)} and 
	 * {@link #logTechnical(Throwable)}. The latter two should only be used
	 * in exceptional cases where an error that is specified as technical should
	 * be logged as functional or vice versa.
	 * 
	 * @param t the exception to log
	 * 
	 * @see #log(Throwable)
	 */
	void logFunctional(Throwable t);
	
	/**
	 * Method used to log functional errors. As a rule, the {@link #log(Throwable)}
	 * method should be favored over {@link #logFunctional(Throwable)} and 
	 * {@link #logTechnical(Throwable)}. The latter two should only be used
	 * in exceptional cases where an error that is specified as technical should
	 * be logged as functional or vice versa.
	 *
	 * @param message An explanatory text
	 * @param t The exception to log
	 * 
	 * @see #log(Throwable)
	 */
	void logFunctional(String message, Throwable t);
	
	/**
	 * Method used to log technical errors. As a rule, the {@link #log(Throwable)}
	 * method should be favored over {@link #logFunctional(Throwable)} and 
	 * {@link #logTechnical(Throwable)}. The latter two should only be used
	 * in exceptional cases where an error that is specified as technical should
	 * be logged as functional or vice versa.
	 *
	 * @param t the exception to log
	 * 
	 * @see #log(Throwable)
	 */	
	void logTechnical(Throwable t);

	/**
	 * Method used to log technical errors. As a rule, the {@link #log(Throwable)}
	 * method should be favored over {@link #logFunctional(Throwable)} and 
	 * {@link #logTechnical(Throwable)}. The latter two should only be used
	 * in exceptional cases where an error that is specified as technical should
	 * be logged as functional or vice versa.
	 *
	 * @param message An explanatory text
	 * @param t the exception to log
	 * 
	 * @see #log(Throwable)
	 */	
	void logTechnical(String message, Throwable t);
	
}
