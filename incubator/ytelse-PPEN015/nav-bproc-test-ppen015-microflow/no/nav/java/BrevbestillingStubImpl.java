package no.nav.java;

import com.ibm.websphere.sca.Service;
import com.ibm.websphere.sca.Ticket;
import commonj.sdo.DataObject;
import com.ibm.websphere.sca.ServiceManager;

public class BrevbestillingStubImpl {
	/**
	 * Default constructor.
	 */
	public BrevbestillingStubImpl() {
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
	 * named "BrevbestillingPartner".  This will return an instance of 
	 * {@link com.ibm.websphere.sca.Service}.  This is the dynamic
	 * interface which is used to invoke operations on the reference service
	 * either synchronously or asynchronously.  You will need to pass the operation
	 * name in order to invoke an operation on the service.
	 *
	 * @generated (com.ibm.wbit.java)
	 *
	 * @return Service
	 */
	public Service locateService_BrevbestillingPartner() {
		return (Service) ServiceManager.INSTANCE
				.locateService("BrevbestillingPartner");
	}

	/**
	 * Method generated to support implemention of operation "bestillBrev" defined for WSDL port type 
	 * named "Brevbestilling".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public String bestillBrev(DataObject bestillBrevRequest) {
		return "journalpostId";
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation (@link sah.lib.nav.no.nav.lib.sah.inf.brevbestilling.Brevbestilling#bestillBrev(DataObject aDataObject))
	 * of java interface (@link sah.lib.nav.no.nav.lib.sah.inf.brevbestilling.Brevbestilling)	
	 * @see sah.lib.nav.no.nav.lib.sah.inf.brevbestilling.Brevbestilling#bestillBrev(DataObject aDataObject)	
	 */
	public void onBestillBrevResponse(Ticket __ticket, String returnValue,
			Exception exception) {
		//TODO Needs to be implemented.
	}

}