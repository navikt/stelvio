package no.nav.java;

import com.ibm.websphere.sca.ServiceManager;
import com.ibm.websphere.sca.ServiceRuntimeException;
import commonj.sdo.DataObject;

public class DummyReturn {
	/**
	 * Default constructor.
	 */
	public DummyReturn() {
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
	 * Method generated to support implementation of operation "endreSFEData" defined for WSDL port type 
	 * named "SFENoNS".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that it is a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject endreSFEData(DataObject sfeEndreDataReq) {
		//This should not happen, this module should only be used with Stelvio System Availability configured
		throw new ServiceRuntimeException("Stubbing was not activated in the stub module. Make sure to configure Stelvio System Availability correctly.");
	}

}