package no.stelvio.common.framework.service;

import no.stelvio.common.framework.service.LocalService;
import no.stelvio.common.framework.service.ServiceFailedException;
import no.stelvio.common.framework.service.ServiceRequest;
import no.stelvio.common.framework.service.ServiceResponse;

/**
 * LocalService for testing LocalServiceLocator.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 1043 $ $Author: psa2920 $ $Date: 2004-08-16 17:11:51 +0200 (Mon, 16 Aug 2004) $
 */
public class TestLocalService implements LocalService {

	public ServiceResponse execute(ServiceRequest request) throws ServiceFailedException {
		return new ServiceResponse("key", request.getData("key"));
	}

}
