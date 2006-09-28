package no.stelvio.integration.service;

import no.stelvio.common.service.LocalService;
import no.stelvio.common.service.ServiceFailedException;
import no.stelvio.common.service.ServiceRequest;
import no.stelvio.common.service.ServiceResponse;

/**
 * LocalService for testing LocalServiceLocator.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 1098 $ $Author: psa2920 $ $Date: 2004-08-18 15:50:27 +0200 (Wed, 18 Aug 2004) $
 */
public class TestLocalService implements LocalService {

	public ServiceResponse execute(ServiceRequest request) throws ServiceFailedException {
		return new ServiceResponse("key", request.getData("key"));
	}

}
