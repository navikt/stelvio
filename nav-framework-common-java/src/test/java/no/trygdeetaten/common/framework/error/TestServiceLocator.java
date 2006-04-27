package no.trygdeetaten.common.framework.error;

import java.rmi.RemoteException;

import javax.ejb.EJBHome;
import javax.ejb.EJBObject;
import javax.ejb.Handle;
import javax.ejb.RemoveException;

import no.trygdeetaten.common.framework.FrameworkError;
import no.trygdeetaten.common.framework.ejb.RemoteService;
import no.trygdeetaten.common.framework.service.Service;
import no.trygdeetaten.common.framework.service.ServiceFailedException;
import no.trygdeetaten.common.framework.service.ServiceNotFoundException;
import no.trygdeetaten.common.framework.service.ServiceRequest;
import no.trygdeetaten.common.framework.service.ServiceResponse;

/**
 * ServiceLocator for testing ErrorHandler.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 1037 $ $Author: psa2920 $ $Date: 2004-08-16 15:25:10 +0200 (Mon, 16 Aug 2004) $
 */
public class TestServiceLocator {

	public Service lookup(String name) throws ServiceNotFoundException {

		if ("BusinessFacade".equals(name)) {

			return new RemoteService() {

				public ServiceResponse execute(ServiceRequest request) throws ServiceFailedException, RemoteException {
					return new ServiceResponse("The Key", request.getData("The Key"));
				}

				// No need to test these methods
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
			};
		} else {
			throw new ServiceNotFoundException(FrameworkError.SERVICE_NAME_NOT_FOUND);
		}
	}
}