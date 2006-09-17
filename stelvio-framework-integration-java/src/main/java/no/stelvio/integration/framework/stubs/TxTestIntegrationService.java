package no.stelvio.integration.framework.stubs;

import no.stelvio.integration.framework.service.IntegrationService;
import no.stelvio.common.framework.error.ErrorCode;
import no.stelvio.common.framework.error.SystemException;
import no.stelvio.common.framework.service.ServiceFailedException;
import no.stelvio.common.framework.service.ServiceRequest;
import no.stelvio.common.framework.service.ServiceResponse;


/**
 * IntegrationService for testing transaction handling.
 * 
 * @author person7553f5959484, Accenture
 * @version $Id: TxTestIntegrationService.java 2298 2005-05-31 10:06:52Z psa2920 $
 */
public class TxTestIntegrationService extends IntegrationService {

	/** 
	 * {@inheritDoc}
	 * @see no.stelvio.integration.framework.service.IntegrationService#doExecute(no.stelvio.common.framework.service.ServiceRequest)
	 */
	protected ServiceResponse doExecute(ServiceRequest request) throws ServiceFailedException {

		if (null != request.getData("SystemException")) {
			throw new SystemException(ErrorCode.UNSPECIFIED_ERROR, "Legacy system not available!");
		} else if (null != request.getData("ServiceFailedException")) {
			throw new ServiceFailedException(ErrorCode.UNSPECIFIED_ERROR, "Legacy system failed to service the request!");
		} else {
			return new ServiceResponse();
		}
	}
}
