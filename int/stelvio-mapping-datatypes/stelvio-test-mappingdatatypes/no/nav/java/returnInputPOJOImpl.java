package no.nav.java;

import commonj.sdo.DataObject;
import com.ibm.websphere.sca.ServiceManager;

public class returnInputPOJOImpl {
	/**
	 * Default constructor.
	 */
	public returnInputPOJOImpl() {
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
	 * Method generated to support implementation of operation "testMappingInternal" defined for WSDL port type 
	 * named "TESTMappingDatatypesInternal".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that it is a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject testMappingInternal(DataObject testMappingInternalRequest) {
		//mapping all the elements through java to check if the transformation between xml and java changes behavior
		testMappingInternalRequest.set("string", testMappingInternalRequest.get("string"));
		testMappingInternalRequest.set("boolean", testMappingInternalRequest.get("boolean"));
		testMappingInternalRequest.set("date", testMappingInternalRequest.get("date"));
		testMappingInternalRequest.set("int", testMappingInternalRequest.get("int"));
		testMappingInternalRequest.set("double", testMappingInternalRequest.get("double"));
		testMappingInternalRequest.set("complex1", testMappingInternalRequest.get("complex1"));
		testMappingInternalRequest.set("complex2", testMappingInternalRequest.get("complex2"));
		testMappingInternalRequest.set("string2", testMappingInternalRequest.get("string2"));
		testMappingInternalRequest.set("byte", testMappingInternalRequest.get("byte"));
		testMappingInternalRequest.set("float", testMappingInternalRequest.get("float"));
		testMappingInternalRequest.set("long", testMappingInternalRequest.get("long"));
		testMappingInternalRequest.set("short", testMappingInternalRequest.get("short"));
		testMappingInternalRequest.set("booleanNillable", testMappingInternalRequest.get("booleanNillable"));
		return testMappingInternalRequest;
	}

}