package no.stelvio.common.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import no.stelvio.common.error.SystemException;
import no.stelvio.common.service.Service;
import no.stelvio.common.service.ServiceFailedException;
import no.stelvio.common.service.ServiceRequest;
import no.stelvio.common.service.ServiceResponse;

/**
 * RemoteService is an interface that all classes representing a remote service should implement.
 * 
 * @author person7553f5959484
 * @version $Revision: 2372 $ $Author: skb2930 $ $Date: 2005-06-23 16:53:27 +0200 (Thu, 23 Jun 2005) $
 */
public interface RemoteService extends Service, EJBObject {

	/**
	 * Perform the logic implemented by the remote service.
	 * 
	 * @param	request the <i>ServiceRequest</i> object sent from
	 * 			the client as input to the service.
	 * 
	 * @return	the <i>ServiceResponse</i> object sent to the client
	 * 			as output from the service.
	 * 
	 * @throws 	ServiceFailedException if service execution fails.
	 * @throws 	SystemException if service execution fails.
	 * @throws	RemoteException if communication with remote host fails.
	 */
	ServiceResponse execute(ServiceRequest request) throws ServiceFailedException, SystemException, RemoteException;
}