
import com.ibm.websphere.sca.Service;
import com.ibm.websphere.sca.Ticket;
import commonj.sdo.DataObject;
import com.ibm.websphere.sca.ServiceManager;
import com.ibm.websphere.sca.sdo.DataFactory;

public class ExampleOrchestrationImpl {
	/**
	 * Default constructor.
	 */
	public ExampleOrchestrationImpl() {
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
	 * This method is used to locate the service for the reference
	 * named "ContextHandlerVerifierProviderPartner1".  This will return an instance of 
	 * {@link com.ibm.websphere.sca.Service}.  This is the dynamic
	 * interface which is used to invoke operations on the reference service
	 * either synchronously or asynchronously.  You will need to pass the operation
	 * name in order to invoke an operation on the service.
	 *
	 * @generated (com.ibm.wbit.java)
	 *
	 * @return Service
	 */
	private Service _ContextHandlerVerifierProviderPartner1 = null;

	public Service locateService_ContextHandlerVerifierProviderPartner1() {
		if (_ContextHandlerVerifierProviderPartner1 == null) {
			_ContextHandlerVerifierProviderPartner1 = (Service) ServiceManager.INSTANCE
					.locateService("ContextHandlerVerifierProviderPartner1");
		}
		return _ContextHandlerVerifierProviderPartner1;
	}

	/**
	 * Method generated to support implementation of operation "startExampleProcessWR" defined for WSDL port type 
	 * named "StartExampleProcess".
	 * 
	 * Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public String startExampleProcessWR(String inputString) {
		System.out.println("inputString is: " + inputString);
		
		// First WS call
		DataObject requestObject1 = createRequestObject("request no. 1");
		locateService_ContextHandlerVerifierProviderPartner().invoke("callWSChain", requestObject1);
		
		// Second WS call
		DataObject requestObject2 = createRequestObject("request no. 2");
		locateService_ContextHandlerVerifierProviderPartner().invoke("callWSChain", requestObject2);
		
		String outputString = "Input was: '" + inputString + "'";
		return outputString;
	}
	
	private static DataObject createRequestObject(String requestString){
		DataObject requestObject = DataFactory.INSTANCE.create("http://stelvio-example-contexthandler-provider-lib/no/nav/dataobject", "requestObject");
		requestObject.setString("request", requestString);
		return requestObject;
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