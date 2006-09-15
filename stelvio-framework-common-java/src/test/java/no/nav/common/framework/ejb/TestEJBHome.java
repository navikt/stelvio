package no.nav.common.framework.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.EJBMetaData;
import javax.ejb.Handle;
import javax.ejb.HomeHandle;
import javax.ejb.RemoveException;

/**
 * Test class used for testing JNDI lookups.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 1129 $ $Author: psa2920 $ $Date: 2004-08-19 17:25:55 +0200 (Thu, 19 Aug 2004) $
 */
public class TestEJBHome implements EJBHome {

	public TestEJB create() throws CreateException, RemoteException {
		return new TestEJB();
	}
	public void remove(Handle arg0) throws RemoteException, RemoveException {
	}
	public void remove(Object arg0) throws RemoteException, RemoveException {
	}
	public EJBMetaData getEJBMetaData() throws RemoteException {
		return null;
	}
	public HomeHandle getHomeHandle() throws RemoteException {
		return null;
	}
}
