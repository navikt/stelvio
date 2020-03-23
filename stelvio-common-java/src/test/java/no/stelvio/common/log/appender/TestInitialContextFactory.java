package no.stelvio.common.log.appender;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

/**
 * InitialContextFactory implementation for unit testing.
 * 
 * @version $Id: TestInitialContextFactory.java 2190 2005-04-06 07:44:39Z psa2920 $
 */
public class TestInitialContextFactory implements InitialContextFactory {

	private static Context c = new TestInitialContext();
	private static boolean errorOnCreate = false;

	/** 
	 * {@inheritDoc}
	 * @see javax.naming.spi.InitialContextFactory#getInitialContext(java.util.Hashtable)
	 */
	public Context getInitialContext(Hashtable<?, ?> environment) throws NamingException {
		if (errorOnCreate) {
			throw new NamingException("Test error");
		}
		return c;
	}

	/**
	 * Error on create?
	 * 
	 * @return true for error on create, false otherwise.
	 */
	public static boolean isErrorOnCreate() {
		return errorOnCreate;
	}

	/**
	 * Set error on create.
	 * 
	 * @param b true for error on create, false otherwise.
	 */
	public static void setErrorOnCreate(boolean b) {
		errorOnCreate = b;
	}
}