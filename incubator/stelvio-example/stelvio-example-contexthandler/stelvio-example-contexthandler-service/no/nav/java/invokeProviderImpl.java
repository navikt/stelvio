package no.nav.java;

import no.stelvio.common.bus.util.StelvioContextHelper;

import com.ibm.websphere.sca.Service;
import com.ibm.websphere.sca.Ticket;
import commonj.sdo.DataObject;
import com.ibm.websphere.sca.ServiceManager;

public class invokeProviderImpl {
	/**
	 * Default constructor.
	 */
	public invokeProviderImpl() {
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
	 * named "ContextHandlerVerifierProviderPartnerWS".  This will return an instance of 
	 * {@link com.ibm.websphere.sca.Service}.  This is the dynamic
	 * interface which is used to invoke operations on the reference service
	 * either synchronously or asynchronously.  You will need to pass the operation
	 * name in order to invoke an operation on the service.
	 *
	 * @generated (com.ibm.wbit.java)
	 *
	 * @return Service
	 */
	private Service _ContextHandlerVerifierProviderPartnerWS = null;

	public Service locateService_ContextHandlerVerifierProviderPartnerWS() {
		if (_ContextHandlerVerifierProviderPartnerWS == null) {
			_ContextHandlerVerifierProviderPartnerWS = (Service) ServiceManager.INSTANCE
					.locateService("ContextHandlerVerifierProviderPartnerWS");
		}
		return _ContextHandlerVerifierProviderPartnerWS;
	}

	/**
	 * Method generated to support implementation of operation "callNoWSChain" defined for WSDL port type 
	 * named "ContextHandlerVerifierProvider".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that it is a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject callNoWSChain(DataObject request) {
		// returning the request
		return request;
	}

	/**
	 * Method generated to support implementation of operation "callWSChain" defined for WSDL port type 
	 * named "ContextHandlerVerifierProvider".
	 * 
	 * Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject callWSChain(DataObject request) {
		DataObject response = ((DataObject) locateService_ContextHandlerVerifierProviderPartnerWS()
				.invoke("callWSChain", request)).getDataObject(0);
		
		StelvioContextHelper stelvioContext = new StelvioContextHelper();
		
		System.out.println("Service (callWSChain) workArea: AppId=" +stelvioContext.getApplicationId()+ " ,CorrId="+stelvioContext.getCorrelationId()+" , UserID="+stelvioContext.getUserId());
		
		return response;
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation "ContextHandlerVerifierProvider#callNoWSChain(DataObject request)"
	 * of wsdl interface "ContextHandlerVerifierProvider"	
	 */
	public void onCallNoWSChainResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation "ContextHandlerVerifierProvider#callWSChain(String request)"
	 * of wsdl interface "ContextHandlerVerifierProvider"	
	 */
	public void onCallWSChainResponse(Ticket __ticket, DataObject returnValue,
			Exception exception) {
		//TODO Needs to be implemented.
	}

}