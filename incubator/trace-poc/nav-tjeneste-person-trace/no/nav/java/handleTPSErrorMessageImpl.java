package no.nav.java;

import no.stelvio.common.bus.util.ErrorHelperUtil;

import com.ibm.websphere.sca.Service;
import com.ibm.websphere.sca.ServiceBusinessException;
import com.ibm.websphere.sca.ServiceManager;
import com.ibm.websphere.sca.ServiceRuntimeException;
import com.ibm.websphere.sca.Ticket;
import commonj.sdo.DataObject;

public class handleTPSErrorMessageImpl {

	final static String CURRENT_MODULE_NAME = "nav-ent-frg-person";
	
	final static String FAULT_NAMESPACE = "http://nav-lib-frg-tps/no/nav/lib/frg/tps/fault";
	final static String FAULT_PERSON_NOT_FOUND = "FaultTPSPersonIkkeFunnet";
	final static String FAULT_FINNPERSON = "FaultTPSFinnPerson";
	
	final static String WARNING_CODE = "04";
	final static String ERROR_CODE_FUNCTIONAL = "08";
	final static String ERROR_CODE_TECHNICAL = "12";
	
	final static String ERROR_MESSAGE_FNRNOTCORRECT				= "FØDSELSNUMMER INNGITT ER UGYLDIG";
	final static String ERROR_MESSAGE_PERSONNOTFOUND			= "PERSON IKKE FUNNET";
	final static String ERROR_MESSAGE_NOPERSONSFOUND			= "INGEN PERSONER FUNNET";
	final static String ERROR_MESSAGE_SEARCHCRITERIANOTFOUND	= "SPØRREVERDI IKKE FUNNET";
	final static String ERROR_CODE_NOSEARCHRESULT				= "S030008F"; //INGEN FOREKOMSTER FUNNET
	final static String ERROR_MESSAGE_SEARCHCRITERIANOTFOUND2	= "SPØRREVERDI FINNES IKKE."; 
	

	/**
	 * Default constructor.
	 */
	public handleTPSErrorMessageImpl() {
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
	 * named "TPSPartner".  This will return an instance of 
	 * {@link com.ibm.websphere.sca.Service}.  This is the dynamic
	 * interface which is used to invoke operations on the reference service
	 * either synchronously or asynchronously.  You will need to pass the operation
	 * name in order to invoke an operation on the service.
	 *
	 * @generated (com.ibm.wbit.java)
	 *
	 * @return Service
	 */
	public Service locateService_TPSPartner() {
		return (Service) ServiceManager.INSTANCE.locateService("TPSPartner");
	}

	/**
	 * Method generated to support implemention of operation "A0" defined for WSDL port type 
	 * named "interface.TPS".
	 * 
	 * This WSDL operation has fault(s) defined. The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject hentTPSData(DataObject tpsPersonDataReq)
			throws ServiceBusinessException {
		
		DataObject tpsPersonDataRes = null;
		DataObject faultBo = null;

		tpsPersonDataRes = (DataObject) locateService_TPSPartner().invoke(
				"hentTPSData", tpsPersonDataReq);
		if (tpsPersonDataRes != null)
			tpsPersonDataRes = tpsPersonDataRes.getDataObject(0);

		String status = (tpsPersonDataRes.getDataObject("tpsSvar")
				.getDataObject("svarStatus").getString("returStatus"));
		String melding = (tpsPersonDataRes.getDataObject("tpsSvar")
				.getDataObject("svarStatus").getString("returMelding"));
		String utfyllendeMelding = (tpsPersonDataRes.getDataObject("tpsSvar")
				.getDataObject("svarStatus").getString("utfyllendeMelding"));
		
		// Håndtering for finnAdresseListe
		String rutine = (tpsPersonDataRes.getDataObject("tpsServiceRutine")
			.getString("serviceRutinenavn"));
		
		if ((melding.equalsIgnoreCase("S103002F") || melding.equalsIgnoreCase("S103001F") || 
			 melding.equalsIgnoreCase("S103007I")) && rutine.equalsIgnoreCase("FS03-FDNUMMER-ADRESSER-M")) {
			return tpsPersonDataRes;
		}
		
		// Håndtering for hentIdentHistorikkBolk
		if ((melding.equalsIgnoreCase("S011011F") || melding.equalsIgnoreCase("S211001F") || melding.equalsIgnoreCase("SX00003F") || //den siste er ny
			 melding.equalsIgnoreCase("S011007I") || melding.equalsIgnoreCase("S011006F"))  && rutine.equalsIgnoreCase("FS03-FDNUMMER-FNRHISTO-M")) {
			return tpsPersonDataRes;
		}
		
		// Håndtering for hentPersonUtlandHistorikkListe
		for (int i = 2; i < 6 &&
				"FS03-FDNUMMER-SOAIHIST-O".equalsIgnoreCase(rutine) && 
				"S016009I".equalsIgnoreCase(melding) && 
				WARNING_CODE.equalsIgnoreCase(status); i++) {
			
			tpsPersonDataRes = null;
			faultBo = null;
	
			tpsPersonDataReq.getDataObject("tpsServiceRutine").setString("buffNr", Integer.toString(i));
			tpsPersonDataRes = (DataObject) locateService_TPSPartner().invoke(
					"hentTPSData", tpsPersonDataReq);
			if (tpsPersonDataRes != null)
				tpsPersonDataRes = tpsPersonDataRes.getDataObject(0);
	
			status = (tpsPersonDataRes.getDataObject("tpsSvar")
					.getDataObject("svarStatus").getString("returStatus"));
			melding = (tpsPersonDataRes.getDataObject("tpsSvar")
					.getDataObject("svarStatus").getString("returMelding"));
			utfyllendeMelding = (tpsPersonDataRes.getDataObject("tpsSvar")
					.getDataObject("svarStatus").getString("utfyllendeMelding"));
		}
		
		// business error
		if (ERROR_CODE_FUNCTIONAL.equalsIgnoreCase(status) && 
				ERROR_MESSAGE_FNRNOTCORRECT.equals(utfyllendeMelding) || utfyllendeMelding.startsWith(ERROR_MESSAGE_PERSONNOTFOUND)) {
			
			//String returmelding = (tpsPersonDataRes.getDataObject("tpsSvar")
			//	.getDataObject("svarStatus").getString("returMelding"));
			// TODO Jonny: Må finne en oversikt over returMeldinger her og se om det kan komme andre som ikke skal være person ikke funnet
			faultBo = ErrorHelperUtil.getBusinessFaultBO(CURRENT_MODULE_NAME, FAULT_NAMESPACE, FAULT_PERSON_NOT_FOUND, utfyllendeMelding, "");
		}
		
		// runtime error
		else if (ERROR_CODE_TECHNICAL.equalsIgnoreCase(status)) {
			// throw SBE instead of SRE to get around limitation in mediation flow+POJO which effectively removes root cause 
			throw new ServiceBusinessException(utfyllendeMelding);
		}
		
		// feil i finnPerson som skal fanges, men ikke returneres til konsument
		else if (ERROR_CODE_FUNCTIONAL.equalsIgnoreCase(status) && 
				(ERROR_MESSAGE_NOPERSONSFOUND.equals(utfyllendeMelding) || ERROR_MESSAGE_SEARCHCRITERIANOTFOUND.equals(utfyllendeMelding)
				|| ERROR_MESSAGE_SEARCHCRITERIANOTFOUND2.equals(utfyllendeMelding))) {
			faultBo = ErrorHelperUtil.getBusinessFaultBO(CURRENT_MODULE_NAME, FAULT_NAMESPACE, FAULT_FINNPERSON, utfyllendeMelding, "");
		}
		
		else if (ERROR_CODE_FUNCTIONAL.equalsIgnoreCase(status) && ERROR_CODE_NOSEARCHRESULT.equals(melding))
		{ /*do nothing, empty result*/ }
		
		// sjekker for feil som egentlig ikke skal oppstå.
		else if (!("00".equalsIgnoreCase(status) || "04".equalsIgnoreCase(status))) {
			// throw SBE instead of SRE to get around limitation in mediation flow+POJO which effectively removes root cause 
			throw new ServiceBusinessException(utfyllendeMelding);	
			//faultBo = ErrorHelperUtil.getBusinessFaultBO(CURRENT_MODULE_NAME, FAULT_NAMESPACE, FAULT_PERSON_NOT_FOUND, utfyllendeMelding, "");
		}

		if (faultBo != null) {
			throw new ServiceBusinessException(faultBo);
		}
		
		return tpsPersonDataRes;
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation "interface.TPS#A0(DataObject tpsPersonDataReq)"
	 * of wsdl interface "interface.TPS"	
	 */
	public void onHentTPSDataResponse(Ticket __ticket, DataObject returnValue,
			Exception exception) {
	}

}