package no.stelvio.business.service;

import no.stelvio.common.service.LocalService;
import no.stelvio.common.service.ServiceFailedException;
import no.stelvio.common.service.ServiceRequest;
import no.stelvio.common.service.ServiceResponse;

/**
 * LocalService for testing LocalServiceLocator.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 1070 $ $Author: psa2920 $ $Date: 2004-08-17 18:08:35 +0200 (Tue, 17 Aug 2004) $
 */
public class TestLocalService implements LocalService {

	public ServiceResponse execute(ServiceRequest request) throws ServiceFailedException {
		return new ServiceResponse("key", request.getData("key"));
	}

}
