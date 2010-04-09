
import com.ibm.websphere.sca.Service;
import com.ibm.websphere.sca.Ticket;
import com.ibm.websphere.sca.ServiceManager;

public class CatchRuntimeExceptionImpl {
	/**
	 * Default constructor.
	 */
	public CatchRuntimeExceptionImpl() {
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
	 * named "ErrorIFPartner".  This will return an instance of 
	 * {@link com.ibm.websphere.sca.Service}.  This is the dynamic
	 * interface which is used to invoke operations on the reference service
	 * either synchronously or asynchronously.  You will need to pass the operation
	 * name in order to invoke an operation on the service.
	 *
	 * @generated (com.ibm.wbit.java)
	 *
	 * @return Service
	 */
	public Service locateService_ErrorIFPartner() {
		return (Service) ServiceManager.INSTANCE
				.locateService("ErrorIFPartner");
	}

	/**
	 * Method generated to support implementation of operation "operation1" defined for WSDL port type 
	 * named "ErrorIF".
	 * 
	 * Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public String operation1(String input1) {
		try {
			return (String) locateService_ErrorIFPartner().invoke("operation1",
					input1);
		} catch (RuntimeException e) {
			throw SOAPFaultExceptionFactory.createSoapFaultException(e);
		}
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation "ErrorIF#operation1(String input1)"
	 * of wsdl interface "ErrorIF"	
	 */
	public void onOperation1Response(Ticket __ticket, String returnValue,
			Exception exception) {
		//TODO Needs to be implemented.
	}

}