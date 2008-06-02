package no.nav.java;

import com.ibm.websphere.sca.Service;
import commonj.sdo.DataObject;
import com.ibm.websphere.sca.ServiceManager;

public class RouteCallImpl {
	/**
	 * Default constructor.
	 */
	public RouteCallImpl() {
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
	 * named "PersonHendelsePartner".  This will return an instance of 
	 * {@link com.ibm.websphere.sca.Service}.  This is the dynamic
	 * interface which is used to invoke operations on the reference service
	 * either synchronously or asynchronously.  You will need to pass the operation
	 * name in order to invoke an operation on the service.
	 *
	 * @generated (com.ibm.wbit.java)
	 *
	 * @return Service
	 */
	public Service locateService_PersonHendelsePartner() {
		return (Service) ServiceManager.INSTANCE
				.locateService("PersonHendelsePartner");
	}

	/**
	 * Method generated to support implemention of operation "fodselsnummerOppdatert" defined for WSDL port type 
	 * named "interface.PersonHendelse".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public void fodselsnummerOppdatert(DataObject fodselsnummerOppdatertRequest) {
		String meldingsKode = fodselsnummerOppdatertRequest.getString("meldingsKode");
		if("linkingDnrFnr".equals(meldingsKode) ||
				"linkingDnrDnr".equals(meldingsKode) ||
				"linkingFnrFnr".equals(meldingsKode) ){
			locateService_PersonHendelsePartner().invoke("fodselsnummerOppdatert", fodselsnummerOppdatertRequest);
		}
		else if("tildelingEndringDnr".equals(meldingsKode) || "tildelingFnr".equals(meldingsKode) ){
			fodselsnummerOpprettet(fodselsnummerOppdatertRequest);
		}
		/* 29.10.07: removed in branch because adresseEndring is not part of rekrutten
		 * TODO: må fjernes i trunk (kjempen)
		else if("adresseEndring".equals(meldingsKode) ||
				"adresseEndringPostUtl".equals(meldingsKode) ||
				"adresseEndringPostNor".equals(meldingsKode) ||
				"adresseEndringTilNor".equals(meldingsKode) ||
				"adresseEndringUtlAdr".equals(meldingsKode) ){
			adresseOppdatert(fodselsnummerOppdatertRequest);
		}*/
	}

	/**
	 * Method generated to support implemention of operation "adresseOppdatert" defined for WSDL port type 
	 * named "interface.PersonHendelse".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public void adresseOppdatert(DataObject adresseOppdatertRequest) {
		locateService_PersonHendelsePartner().invoke("adresseOppdatert", adresseOppdatertRequest);
	}

	/**
	 * Method generated to support implemention of operation "fodselsnummerOpprettet" defined for WSDL port type 
	 * named "interface.PersonHendelse".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public void fodselsnummerOpprettet(DataObject fodselsnummerOpprettetRequest) {
		locateService_PersonHendelsePartner().invoke("fodselsnummerOpprettet", fodselsnummerOpprettetRequest);
	}

}