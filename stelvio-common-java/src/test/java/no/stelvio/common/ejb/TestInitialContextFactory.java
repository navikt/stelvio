package no.stelvio.common.ejb;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

/**
 * InitialContectFactory implemented to return a TestInitialContext that won't cause unit tests to delay..
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 1129 $ $Author: psa2920 $ $Date: 2004-08-19 17:25:55 +0200 (Thu, 19 Aug 2004) $
 */
public class TestInitialContextFactory implements InitialContextFactory {

	public TestInitialContextFactory() {
		System.out.println(this.getClass().getName() + " created");
	}

	public Context getInitialContext(Hashtable environment) throws NamingException {
		return new TestContext();
	}

}
