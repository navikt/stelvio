package no.nav.java;

import no.nav.util.LoggerUtil2;

import com.ibm.websphere.sca.ServiceManager;
import commonj.sdo.DataObject;

public class EchoService2Impl {
	/**
	 * Default constructor.
	 */
	public EchoService2Impl() {
		super();
	}

	/**
	 * Return a reference to the component service instance for this
	 * implementation class. This method should be used when passing this
	 * service to a partner reference or if you want to invoke this component
	 * service asynchronously.
	 * 
	 * @generated (com.ibm.wbit.java)
	 */
	@SuppressWarnings("unused")
	private Object getMyService() {
		return (Object) ServiceManager.INSTANCE.locateService("self");
	}

	/**
	 * Method generated to support implemention of operation "echo" defined for
	 * WSDL port type named "EchoService2".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a
	 * parameter type conveys that its a complex type. Please refer to the WSDL
	 * Definition for more information on the type of input, output and
	 * fault(s).
	 */
	public DataObject echo(DataObject input) {
		LoggerUtil2.log(input);
		return input;
	}
}