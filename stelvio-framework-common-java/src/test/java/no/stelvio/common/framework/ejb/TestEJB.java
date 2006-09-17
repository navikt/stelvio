package no.stelvio.common.framework.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBHome;
import javax.ejb.EJBObject;
import javax.ejb.Handle;
import javax.ejb.RemoveException;

import no.stelvio.common.framework.ejb.RemoteService;
import no.stelvio.common.framework.service.ServiceFailedException;
import no.stelvio.common.framework.service.ServiceRequest;
import no.stelvio.common.framework.service.ServiceResponse;

/**
 * Test class used for testing JNDI lookups.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 1129 $ $Author: psa2920 $ $Date: 2004-08-19 17:25:55 +0200 (Thu, 19 Aug 2004) $
 */
public class TestEJB implements RemoteService {
	public ServiceResponse execute(ServiceRequest request) throws ServiceFailedException, RemoteException {
		return new ServiceResponse("key", request.getData("key"));
	}
	public EJBHome getEJBHome() throws RemoteException {
		return null;
	}
	public Object getPrimaryKey() throws RemoteException {
		return null;
	}
	public void remove() throws RemoteException, RemoveException {
	}
	public Handle getHandle() throws RemoteException {
		return null;
	}
	public boolean isIdentical(EJBObject arg0) throws RemoteException {
		return false;
	}
}
