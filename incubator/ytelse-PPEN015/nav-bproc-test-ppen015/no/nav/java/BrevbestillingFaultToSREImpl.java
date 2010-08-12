package no.nav.java;

import com.ibm.websphere.sca.ServiceBusinessException;
import com.ibm.websphere.sca.ServiceRuntimeException;
import com.ibm.websphere.sca.Ticket;
import commonj.sdo.DataObject;
import sah.lib.nav.no.nav.lib.sah.inf.brevbestilling.Brevbestilling;
import com.ibm.websphere.sca.ServiceManager;

public class BrevbestillingFaultToSREImpl {
	/**
	 * Default constructor.
	 */
	private static final String FAULT_ADRESSE_IKKE_REGISTRERT = "FaultAdresseIkkeRegistrert";
	
	public BrevbestillingFaultToSREImpl() {
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
	 * {@link Brevbestilling}.  If you would like to use this service 
	 * asynchronously then you will need to cast the result
	 * to {@link sah.lib.nav.no.nav.lib.sah.inf.brevbestilling.BrevbestillingAsync}.
	 *
	 * @generated (com.ibm.wbit.java)
	 *
	 * @return Brevbestilling
	 */
	public Brevbestilling locateService_BrevbestillingPartner() {
		return (Brevbestilling) ServiceManager.INSTANCE
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
		try{
			return locateService_BrevbestillingPartner().bestillBrev(bestillBrevRequest);
		}catch (ServiceBusinessException sbe){
			String faultName = ((DataObject) sbe.getData()).getType().getName();
			
			if(FAULT_ADRESSE_IKKE_REGISTRERT.equalsIgnoreCase(faultName))
				throw sbe;
			else
				throw new ServiceRuntimeException(sbe);
		}
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