package no.nav.java;

import oko.lib.nav.no.nav.lib.oko.inf.oppdrag.online.OppdragOnline;

import com.ibm.websphere.sca.ServiceBusinessException;
import com.ibm.websphere.sca.ServiceManager;
import com.ibm.websphere.sca.ServiceRuntimeException;
import com.ibm.websphere.sca.Ticket;
import commonj.sdo.DataObject;

public class OppdragOnlineFaultToSREImpl {
	/**
	 * Default constructor.
	 */
	public OppdragOnlineFaultToSREImpl() {
		super();
	}

	/**
	 * Return a reference to the component service instance for this implementation
	 * class.  This method should be used when passing this service to a partner reference
	 * or if you want to invoke this component service asynchronously.    
	 *
	 * @generated (com.ibm.wbit.java)
	 */
	@SuppressWarnings("unused")
	private Object getMyService() {
		return (Object) ServiceManager.INSTANCE.locateService("self");
	}

	/**
	 * This method is used to locate the service for the reference
	 * named "OppdragOnlinePartner".  This will return an instance of
	 * {@link OppdragOnline}.  If you would like to use this service 
	 * asynchronously then you will need to cast the result
	 * to {@link OppdragOnlineAsync}.
	 *
	 * @generated (com.ibm.wbit.java)
	 *
	 * @return OppdragOnline
	 */
	public OppdragOnline locateService_OppdragOnlinePartner() {
		return (OppdragOnline) ServiceManager.INSTANCE
				.locateService("OppdragOnlinePartner");
	}

	/**
	 * Method generated to support implemention of operation "hentOppdragsimulering" defined for WSDL port type 
	 * named "OppdragOnline".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject hentOppdragsimulering(
			DataObject hentOppdragsimuleringRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "sendAsynkronOppdragsmeldingOnline" defined for WSDL port type 
	 * named "OppdragOnline".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public void sendAsynkronOppdragsmeldingOnline(
			DataObject sendAsynkronOppdragsmeldingOnlineRequest) {
		try {
			locateService_OppdragOnlinePartner()
					.sendAsynkronOppdragsmeldingOnline(
							sendAsynkronOppdragsmeldingOnlineRequest);
		} catch (ServiceBusinessException sbe) {
			throw new ServiceRuntimeException(sbe);
		}
	}

	/**
	 * Method generated to support implemention of operation "sendAsynkronOppdragsmeldingProsessOnline" defined for WSDL port type 
	 * named "OppdragOnline".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public void sendAsynkronOppdragsmeldingProsessOnline(
			DataObject sendAsynkronOppdragsmeldingProsessOnlineRequest) {
		try {
			locateService_OppdragOnlinePartner()
					.sendAsynkronOppdragsmeldingProsessOnline(
							sendAsynkronOppdragsmeldingProsessOnlineRequest);
		} catch (ServiceBusinessException sbe) {
			throw new ServiceRuntimeException(sbe);
		}
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link oko.lib.nav.no.nav.lib.oko.inf.oppdrag.online.OppdragOnline#hentOppdragsimulering(DataObject aDataObject))
	 * of java interface (@link oko.lib.nav.no.nav.lib.oko.inf.oppdrag.online.OppdragOnline)	
	 * @see oko.lib.nav.no.nav.lib.oko.inf.oppdrag.online.OppdragOnline#hentOppdragsimulering(DataObject aDataObject)	
	 */
	public void onHentOppdragsimuleringResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

}