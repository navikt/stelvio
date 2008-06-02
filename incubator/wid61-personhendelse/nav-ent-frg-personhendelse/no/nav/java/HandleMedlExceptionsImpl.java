package no.nav.java;

import com.ibm.websphere.sca.Service;
import com.ibm.websphere.sca.ServiceBusinessException;
import com.ibm.websphere.sca.ServiceRuntimeException;
import com.ibm.websphere.sca.Ticket;
import commonj.sdo.DataObject;
import com.ibm.websphere.sca.ServiceManager;

public class HandleMedlExceptionsImpl {
	/**
	 * Default constructor.
	 */
	public HandleMedlExceptionsImpl() {
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
		return (Object) ServiceManager.INSTANCE.locateService("self");
	}

	/**
	 * This method is used to locate the service for the reference
	 * named "MEDLPartner".  This will return an instance of 
	 * {@link com.ibm.websphere.sca.Service}.  This is the dynamic
	 * interface which is used to invoke operations on the reference service
	 * either synchronously or asynchronously.  You will need to pass the operation
	 * name in order to invoke an operation on the service.
	 *
	 * @generated (com.ibm.wbit.java)
	 *
	 * @return Service
	 */
	public Service locateService_MEDLPartner() {
		return (Service) ServiceManager.INSTANCE.locateService("MEDLPartner");
	}

	/**
	 * Method generated to support implemention of operation "lagreMedlemskapsInfo" defined for WSDL port type 
	 * named "interface.MEDL".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject lagreMedlemskapsInfo(
			DataObject lagreMedlemskapsInfoRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "hentMedlemskapsInfoListe" defined for WSDL port type 
	 * named "interface.MEDL".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject hentMedlemskapsInfoListe(
			DataObject hentMedlemskapsInfoListeRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "hentFnrMedUtenlandskId" defined for WSDL port type 
	 * named "interface.MEDL".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject hentFnrMedUtenlandskId(
			DataObject hentFnrMedUtenlandskIdRequest) {
		//TODO Needs to be implemented.
		return null;
	}

	/**
	 * Method generated to support implemention of operation "oppdaterFodselsnummer" defined for WSDL port type 
	 * named "interface.MEDL".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public void oppdaterFodselsnummer(DataObject oppdaterFodselsnummerRequest) {
		try {
			locateService_MEDLPartner().invoke("oppdaterFodselsnummer", oppdaterFodselsnummerRequest);
			
		} catch (ServiceBusinessException sbe) {
			ServiceRuntimeException sre = new ServiceRuntimeException(sbe.getMessage());
			throw sre;
		}
	}

	/**
	 * Method generated to support implemention of operation "opprettFodselsnummer" defined for WSDL port type 
	 * named "interface.MEDL".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public void opprettFodselsnummer(DataObject opprettFodselsnummerRequest) {
		try {
			locateService_MEDLPartner().invoke("opprettFodselsnummer", opprettFodselsnummerRequest);
			
		} catch (ServiceBusinessException sbe) {
			ServiceRuntimeException sre = new ServiceRuntimeException(sbe.getMessage());
			throw sre;
		}
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation "interface.MEDL#lagreMedlemskapsInfo(DataObject lagreMedlemskapsInfoRequest)"
	 * of wsdl interface "interface.MEDL"	
	 */
	public void onLagreMedlemskapsInfoResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation "interface.MEDL#hentMedlemskapsInfoListe(DataObject hentMedlemskapsInfoListeRequest)"
	 * of wsdl interface "interface.MEDL"	
	 */
	public void onHentMedlemskapsInfoListeResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation "interface.MEDL#hentFnrMedUtenlandskId(DataObject hentFnrMedUtenlandskIdRequest)"
	 * of wsdl interface "interface.MEDL"	
	 */
	public void onHentFnrMedUtenlandskIdResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

}