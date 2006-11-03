package no.stelvio.common.service;

/**
 * LocalService is an interface that all classes representing a local service should implement.
 * 
 * @author person7553f5959484
 * @version $Revision: 40 $ $Author: psa2920 $ $Date: 2004-04-26 16:24:19 +0200 (Mon, 26 Apr 2004) $
 */
public interface LocalService extends Service {

	/**
	 * Perform the logic implemented by the local service.
	 * 
	 * @param	request the <i>ServiceRequest</i> object sent from
	 * 			the client as input to the service.
	 * 
	 * @return	the <i>ServiceResponse</i> object sent to the client
	 * 			as output from the service.
	 * 
	 * @throws 	ServiceFailedException if service execution fails.
	 */
	ServiceResponse execute(ServiceRequest request) throws ServiceFailedException;

}
