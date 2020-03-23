package no.stelvio.common.log.appender;

import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.varia.FallbackErrorHandler;

/**
 * FallbackErrorHandler implementation for unit testing.
 * 
 * @version $Id: TestErrorhandler.java 2193 2005-04-06 08:01:35Z psa2920 $
 */
public class TestErrorhandler extends FallbackErrorHandler {

	private int numErrors = 0;
	private String testErrorString = null;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.apache.log4j.spi.ErrorHandler#error(java.lang.String, java.lang.Exception, int,
	 *      org.apache.log4j.spi.LoggingEvent)
	 */
	public void error(String arg0, Exception arg1, int arg2, LoggingEvent arg3) {
		this.testErrorString = arg0;
		super.error(arg0, arg1, arg2, arg3);
		numErrors++;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.apache.log4j.spi.ErrorHandler#error(java.lang.String, java.lang.Exception, int)
	 */
	public void error(String arg0, Exception arg1, int arg2) {
		this.testErrorString = arg0;
		numErrors++;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.apache.log4j.spi.ErrorHandler#error(java.lang.String)
	 */
	public void error(String arg0) {
		this.testErrorString = arg0;
		numErrors++;
	}

	/**
	 * Returns the number of errors.
	 * 
	 * @return number of errors.
	 */
	public synchronized int getNumErrors() {
		return numErrors;
	}

	/**
	 * Sets the number of errors.
	 * 
	 * @param errs
	 *            number of erros.
	 */
	public synchronized void setNumErrors(int errs) {
		numErrors = errs;
	}

	/**
	 * Returns the test error string.
	 * 
	 * @return the test error string.
	 */
	public String getTestErrorString() {
		return testErrorString;
	}

	/**
	 * Sets the test error string.
	 * 
	 * @param string
	 *            the test error string.
	 */
	public void setTestErrorString(String string) {
		testErrorString = string;
	}

}
