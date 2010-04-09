
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.ibm.websphere.sca.ServiceManager;

public class ErrorComponentImpl {
	private Logger logger = LogManager.getLogManager().getLogger(getClass().getName());
	
	/**
	 * Default constructor.
	 */
	public ErrorComponentImpl() {
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
	 * Method generated to support implementation of operation "operation1" defined for WSDL port type 
	 * named "ErrorIF".
	 * 
	 * Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public String operation1(String input1) {
		RuntimeException runtimeException = new RuntimeException("This is the error message");
		logger.severe(runtimeException.getMessage());
		throw runtimeException;
	}
}