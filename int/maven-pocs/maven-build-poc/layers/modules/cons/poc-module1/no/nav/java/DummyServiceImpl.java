package no.nav.java;

import com.ibm.websphere.sca.ServiceManager;

public class DummyServiceImpl {
	/**
	 * Default constructor.
	 */
	public DummyServiceImpl() {
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
	 * Method generated to support implemention of operation "operation1" defined for WSDL port type 
	 * named "DummyIF".
	 * 
	 * Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public void operation1(String input1) {
	}

}