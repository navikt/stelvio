package no.nav.java;

import no.stelvio.common.bus.util.StelvioContextHelper;

import com.ibm.websphere.sca.Service;
import com.ibm.websphere.sca.ServiceManager;
import com.ibm.websphere.sca.Ticket;
import commonj.sdo.DataObject;

public class DebugWorkAreaImpl {
	/**
	 * Default constructor.
	 */
	public DebugWorkAreaImpl() {
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
	 * named "ContextHandlerVerifierProviderPartner".  This will return an instance of 
	 * {@link com.ibm.websphere.sca.Service}.  This is the dynamic
	 * interface which is used to invoke operations on the reference service
	 * either synchronously or asynchronously.  You will need to pass the operation
	 * name in order to invoke an operation on the service.
	 *
	 * @generated (com.ibm.wbit.java)
	 *
	 * @return Service
	 */
	private Service _ContextHandlerVerifierProviderPartner = null;

	public Service locateService_ContextHandlerVerifierProviderPartner() {
		if (_ContextHandlerVerifierProviderPartner == null) {
			_ContextHandlerVerifierProviderPartner = (Service) ServiceManager.INSTANCE
					.locateService("ContextHandlerVerifierProviderPartner");
		}
		return _ContextHandlerVerifierProviderPartner;
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
		// To get or set attributes for DataObject request, use the APIs as shown below:
		// To set a string attribute in request, use request.setString(stringAttributeName, stringValue)
		// To get a string attribute in request, use request.getString(stringAttributeName)
		// To set a dataObject attribute in request, use request.setDataObject(stringAttributeName, dataObjectValue)
		// To get a dataObject attribute in request, use request.getDataObject(stringAttributeName)
		return null;
	}

	/**
	 * Method generated to support implementation of operation "callWSChain" defined for WSDL port type 
	 * named "ContextHandlerVerifierProvider".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that it is a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject callWSChain(DataObject request) {
		
		DataObject response = ((DataObject) locateService_ContextHandlerVerifierProviderPartner().invoke("callWSChain", request)).getDataObject(0);
	
		StelvioContextHelper stelvioContext = new StelvioContextHelper();
		System.out.println("Provider (callWSChain) workArea: AppId=" +stelvioContext.getApplicationId()+ " ,CorrId="+stelvioContext.getCorrelationId()+" , UserID="+stelvioContext.getUserId());
						
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
	 * for the operation "ContextHandlerVerifierProvider#callWSChain(DataObject request)"
	 * of wsdl interface "ContextHandlerVerifierProvider"	
	 */
	public void onCallWSChainResponse(Ticket __ticket, DataObject returnValue,
			Exception exception) {
		//TODO Needs to be implemented.
	}

}