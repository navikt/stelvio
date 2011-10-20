package no.nav.java;

import com.ibm.websphere.sca.Service;
import com.ibm.websphere.sca.Ticket;
import commonj.sdo.DataObject;
import com.ibm.websphere.sca.ServiceManager;

public class MakeInvokeSynchronous {
	/**
	 * Default constructor.
	 */
	public MakeInvokeSynchronous() {
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
	 * named "SFENoNSPartner".  This will return an instance of 
	 * {@link com.ibm.websphere.sca.Service}.  This is the dynamic
	 * interface which is used to invoke operations on the reference service
	 * either synchronously or asynchronously.  You will need to pass the operation
	 * name in order to invoke an operation on the service.
	 *
	 * @generated (com.ibm.wbit.java)
	 *
	 * @return Service
	 */
	private Service _SFENoNSPartner = null;

	public Service locateService_SFENoNSPartner() {
		if (_SFENoNSPartner == null) {
			_SFENoNSPartner = (Service) ServiceManager.INSTANCE
					.locateService("SFENoNSPartner");
		}
		return _SFENoNSPartner;
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
		
		DataObject response = (DataObject) locateService_SFENoNSPartner().invoke("endreSFEData", sfeEndreDataReq);
		
		if (response != null)
			return (DataObject) response.get(0);
		else
			return response;
		
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation "SFENoNS#endreSFEData(DataObject sfeEndreDataReq)"
	 * of wsdl interface "SFENoNS"	
	 */
	public void onEndreSFEDataResponse(Ticket __ticket, DataObject returnValue,
			Exception exception) {
		//TODO Needs to be implemented.
	}

}