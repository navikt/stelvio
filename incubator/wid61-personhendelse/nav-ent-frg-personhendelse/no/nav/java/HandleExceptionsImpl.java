package no.nav.java;

import com.ibm.websphere.sca.Service;
import commonj.sdo.DataObject;
import com.ibm.websphere.sca.ServiceManager;
import com.ibm.websphere.sca.ServiceRuntimeException;
import com.ibm.websphere.sca.ServiceBusinessException;

public class HandleExceptionsImpl {
	/**
	 * Default constructor.
	 */
	public HandleExceptionsImpl() {
		super();
	}

	/**
	 * Return a reference to the component service instance for this implementation
	 * class.  This method should be used when passing this service to a partner reference
	 * or if you want to invoke this component service asynchronously.    
	 *
	 * @generated (com.ibm.wbit.java)
	 */
	private Object getMyService() {
		// test test test
		return (Object) ServiceManager.INSTANCE.locateService("self");
	}

	/**
	 * This method is used to locate the service for the reference
	 * named "VedlikeholdServicePartner".  This will return an instance of 
	 * {@link com.ibm.websphere.sca.Service}.  This is the dynamic
	 * interface which is used to invoke operations on the reference service
	 * either synchronously or asynchronously.  You will need to pass the operation
	 * name in order to invoke an operation on the service.
	 *
	 * @generated (com.ibm.wbit.java)
	 *
	 * @return Service
	 */
	public Service locateService_VedlikeholdServicePartner() {
		return (Service) ServiceManager.INSTANCE
				.locateService("VedlikeholdServicePartner");
	}

	/**
	 * Method generated to support implemention of operation "oppdaterFodselsnummer" defined for WSDL port type 
	 * named "interface.VedlikeholdService".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public void oppdaterFodselsnummer(DataObject oppdaterfodselsnummerrequestdto) {
			
		try {
			locateService_VedlikeholdServicePartner().invoke("oppdaterFodselsnummer", oppdaterfodselsnummerrequestdto);
			
		} catch (ServiceBusinessException sbe) {
			ServiceRuntimeException sre = new ServiceRuntimeException(sbe.getMessage());
			throw sre;
		}
	}

}