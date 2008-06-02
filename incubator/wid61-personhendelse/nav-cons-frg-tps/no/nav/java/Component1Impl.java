package no.nav.java;

import commonj.sdo.DataObject;
import com.ibm.websphere.sca.ServiceManager;

public class Component1Impl {
	/**
	 * Default constructor.
	 */
	public Component1Impl() {
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
	 * Method generated to support implemention of operation "fodselsnummerOppdatert" defined for WSDL port type 
	 * named "interface.TPSPersonHendelse".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public void fodselsnummerOppdatert(DataObject distribusjonsMelding) {
		System.out.println("Jepp");
	}

}