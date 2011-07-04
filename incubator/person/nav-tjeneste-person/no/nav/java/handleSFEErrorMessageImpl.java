package no.nav.java;

import no.stelvio.common.bus.util.ErrorHelperUtil;

import com.ibm.websphere.sca.Service;
import com.ibm.websphere.sca.ServiceBusinessException;
import com.ibm.websphere.sca.ServiceManager;
import com.ibm.websphere.sca.ServiceRuntimeException;
import com.ibm.websphere.sca.Ticket;
import com.ibm.websphere.sca.sdo.DataFactory;
import commonj.sdo.DataObject;

public class handleSFEErrorMessageImpl {

	final static String CURRENT_MODULE_NAME = "nav-ent-frg-person";

	final static String FAULT_NAMESPACE = "http://nav-lib-frg-sfe/no/nav/lib/frg/sfe/fault";

	final static String FAULT_PERSON_NOT_FOUND = "FaultSFEPersonIkkeFunnet";

	final static String FAULT_SAMBOER_NOT_FOUND = "FaultSFESamboerIkkeFunnet";

	final static String FAULT_SAMBOER_DEAD = "FaultSFESamboerDod";

	final static String FAULT_SAMBOER_IN_FAMILY = "FaultSFESamboerIFamilie";

	final static String FAULT_SAMBOERSKAP_ALREADY_EXIST = "FaultSFEAlleredeRegistrertSamboerforhold";

	final static String FAULT_STATUS_UTVANDRET_REQUIRED = "FaultSFEStatusIkkeUtvandret";

	final static String FAULT_CHILD_ALREADY_HAVE_MOM = "FaultSFEAlleredeRegistrertMor";

	final static String FAULT_RELATION_TO_IT_SELF = "FaultSFERelasjonTilSegSelv";

	final static String FAULT_BARN_ALREADY_HAVE_FOSTERFAR = "FaultSFEAllerdeRegistrertFosterfar";

	final static String FAULT_BARN_ALREADY_HAVE_FOSTERMOR = "FaultSFEAlleredeRegistrertFostermor";
	
	final static String FAULT_DATES_DOES_NOT_MATCH_WITH_REGISTERED_SAMBOERSKAP = "FaultSFEDatoerStemmerIkkeMedRegistrertSamboerforhold";
	
	final static String FAULT_ALTERED_FNR_NOT_FOUND = "FaultSFEKorrigertPersonIkkeFunnet";
	
	final static String FAULT_HISTORIC_SAMBOERSKAP_NOT_FOUND = "FaultSFESamboerforholdIkkeFunnet";

	final static String ERROR_CODE_FUNCTIONAL = "08";

	final static String ERROR_CODE_TECHNICAL = "12";

	final static String ERROR_MESSAGE_FNR_NOT_FOUND = "Person ikke funnet i tps";

	final static String ERROR_MESSAGE_FNR_NOT_FOUND2 = "Person ikke funnet i TPS.";

	final static String ERROR_MESSAGE_SAMBOER_IN_FAMILY = "Ugyldige samboerskap pga fam-relasjoner.";

	final static String ERROR_MESSAGE_SAMBOER_DEAD = "Ugyldig samboerskap, samboer er d�d.";

	final static String ERROR_MESSAGE_SAMBOERSKAP_ALREADY_EXIST1 = "Aktiv samboer m� opph/annu f�r ny reg.";

	final static String ERROR_MESSAGE_SAMBOERSKAP_ALREADY_EXIST2 = "Fom-dato samboerkap er > enn opph�rs-dato";

	final static String ERROR_MESSAGE_SAMBOERSKAP_NOT_FOUND = "Ingen Aktiv samboer funnet.";

	final static String ERROR_MESSAGECODE_STATUS_UTVANDRET_REQUIRED = "T6300019";

	final static String ERROR_MESSAGECODE_CHILD_ALREADY_HAVE_MOM = "T6300022";

	final static String ERROR_MESSAGECODE_RELATION_TO_IT_SELF = "T6300033";

	final static String ERROR_MESSAGECODE_FOSTERBARN_ALREADY_HAVE_FOSTERFAR = "T6300035";

	final static String ERROR_MESSAGECODE_FOSTERBARN_ALREADY_HAVE_FOSTERMOR = "T6300036";
	
	final static String ERROR_MESSAGECODE_SAMBOERSKAP_ALREADY_EXIST = "T6400012";
	
	final static String ERROR_MESSAGECODE_DATES_DOES_NOT_MATCH_WITH_REGISTERED_SAMBOERSKAP = "T6400017";
	
	final static String ERROR_MESSAGECODE_ALTERED_FNR_NOT_FOUND = "T6400018";
	
	final static String ERROR_MESSAGECODE_HISTORIC_SAMBOERSKAP_NOT_FOUND = "T6400019";

	/**
	 * Default constructor.
	 */
	public handleSFEErrorMessageImpl() {
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
	 * named "SFEPartner".  This will return an instance of 
	 * {@link com.ibm.websphere.sca.Service}.  This is the dynamic
	 * interface which is used to invoke operations on the reference service
	 * either synchronously or asynchronously.  You will need to pass the operation
	 * name in order to invoke an operation on the service.
	 *
	 * @generated (com.ibm.wbit.java)
	 *
	 * @return Service
	 */
	public Service locateService_SFEPartner() {
		return (Service) ServiceManager.INSTANCE.locateService("SFEPartner");
	}

	/**
	 * Method generated to support implementation of operation "endreEpost" defined for WSDL port type 
	 * named "interface.SFE".
	 * 
	 * This WSDL operation has fault(s) defined. The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject endreEpost(DataObject sfeEndreEpostReq)
			throws ServiceBusinessException {

		DataObject sfePersonDataRes = null;
		DataObject faultBo = null;

		sfePersonDataRes = (DataObject) locateService_SFEPartner().invoke(
				"endreEpost", sfeEndreEpostReq);
		if (sfePersonDataRes != null)
			sfePersonDataRes = sfePersonDataRes.getDataObject(0);

		String status = (sfePersonDataRes.getDataObject("sfeTilbakeMelding")
				.getDataObject("svarStatus").getString("returStatus"));
		String utfyllendeMelding = (sfePersonDataRes.getDataObject(
				"sfeTilbakeMelding").getDataObject("svarStatus")
				.getString("utfyllendeMelding"));

		// business error
		if (ERROR_CODE_FUNCTIONAL.equalsIgnoreCase(status)
				&& (ERROR_MESSAGE_FNR_NOT_FOUND
						.equalsIgnoreCase(utfyllendeMelding) || ERROR_MESSAGE_FNR_NOT_FOUND2
						.equalsIgnoreCase(utfyllendeMelding))) {

			faultBo = ErrorHelperUtil.getBusinessFaultBO(CURRENT_MODULE_NAME,
					FAULT_NAMESPACE, FAULT_PERSON_NOT_FOUND, utfyllendeMelding,
					"");
		}

		// runtime error
		else if (ERROR_CODE_TECHNICAL.equalsIgnoreCase(status)) {
			throw new ServiceRuntimeException(utfyllendeMelding);
		}

		// sjekker for feil som ikke er definert i spec'en.
		else if (!(status.equalsIgnoreCase("00") || status
				.equalsIgnoreCase("04"))) {
			throw new ServiceRuntimeException(utfyllendeMelding);
		}

		if (faultBo != null) {
			throw new ServiceBusinessException(faultBo);
		}

		return sfePersonDataRes;
	}

	/**
	 * Method generated to support implementation of operation "endreTelefonnumre" defined for WSDL port type 
	 * named "interface.SFE".
	 * 
	 * This WSDL operation has fault(s) defined. The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject endreTelefonnumre(DataObject sfeEndreTelefonnumreReq)
			throws ServiceBusinessException {

		DataObject sfePersonDataRes = null;
		DataObject faultBo = null;

		sfePersonDataRes = (DataObject) locateService_SFEPartner().invoke(
				"endreTelefonnumre", sfeEndreTelefonnumreReq);
		if (sfePersonDataRes != null)
			sfePersonDataRes = sfePersonDataRes.getDataObject(0);

		String status = (sfePersonDataRes.getDataObject("sfeTilbakeMelding")
				.getDataObject("svarStatus").getString("returStatus"));
		String utfyllendeMelding = (sfePersonDataRes.getDataObject(
				"sfeTilbakeMelding").getDataObject("svarStatus")
				.getString("utfyllendeMelding"));

		// business error
		if (ERROR_CODE_FUNCTIONAL.equalsIgnoreCase(status)
				&& (ERROR_MESSAGE_FNR_NOT_FOUND
						.equalsIgnoreCase(utfyllendeMelding) || ERROR_MESSAGE_FNR_NOT_FOUND2
						.equalsIgnoreCase(utfyllendeMelding))) {

			faultBo = ErrorHelperUtil.getBusinessFaultBO(CURRENT_MODULE_NAME,
					FAULT_NAMESPACE, FAULT_PERSON_NOT_FOUND, utfyllendeMelding,
					"");
		}

		// runtime error
		else if (ERROR_CODE_TECHNICAL.equalsIgnoreCase(status)) {
			throw new ServiceRuntimeException(utfyllendeMelding);
		}

		// sjekker for feil som ikke er definert i spec'en.
		else if (!(status.equalsIgnoreCase("00") || status
				.equalsIgnoreCase("04"))) {
			throw new ServiceRuntimeException(utfyllendeMelding);
		}

		if (faultBo != null) {
			throw new ServiceBusinessException(faultBo);
		}

		return sfePersonDataRes;
	}

	/**
	 * Method generated to support implementation of operation "endreTelefonnumre" defined for WSDL port type 
	 * named "interface.SFE".
	 * 
	 * This WSDL operation has fault(s) defined. The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject endreSprak(DataObject sfeEndreSprakReq)
			throws ServiceBusinessException {

		DataObject sfePersonDataRes = null;
		DataObject faultBo = null;

		sfePersonDataRes = (DataObject) locateService_SFEPartner().invoke(
				"endreSprak", sfeEndreSprakReq);
		if (sfePersonDataRes != null)
			sfePersonDataRes = sfePersonDataRes.getDataObject(0);

		String status = (sfePersonDataRes.getDataObject("sfeTilbakeMelding")
				.getDataObject("svarStatus").getString("returStatus"));
		String utfyllendeMelding = (sfePersonDataRes.getDataObject(
				"sfeTilbakeMelding").getDataObject("svarStatus")
				.getString("utfyllendeMelding"));

		// business error
		if (ERROR_CODE_FUNCTIONAL.equalsIgnoreCase(status)
				&& (ERROR_MESSAGE_FNR_NOT_FOUND
						.equalsIgnoreCase(utfyllendeMelding) || ERROR_MESSAGE_FNR_NOT_FOUND2
						.equalsIgnoreCase(utfyllendeMelding))) {

			faultBo = ErrorHelperUtil.getBusinessFaultBO(CURRENT_MODULE_NAME,
					FAULT_NAMESPACE, FAULT_PERSON_NOT_FOUND, utfyllendeMelding,
					"");
		}

		// runtime error
		else if (ERROR_CODE_TECHNICAL.equalsIgnoreCase(status)) {
			throw new ServiceRuntimeException(utfyllendeMelding);
		}

		// sjekker for feil som ikke er definert i spec'en.
		else if (!(status.equalsIgnoreCase("00") || status
				.equalsIgnoreCase("04"))) {
			throw new ServiceRuntimeException(utfyllendeMelding);
		}

		if (faultBo != null) {
			throw new ServiceBusinessException(faultBo);
		}

		return sfePersonDataRes;
	}

	/**
	 * Method generated to support implementation of operation "endreAdresse" defined for WSDL port type 
	 * named "interface.SFE".
	 * 
	 * This WSDL operation has fault(s) defined. The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject endreAdresse(DataObject sfeEndreAdresseReq)
			throws ServiceBusinessException {
		DataObject sfePersonDataRes = null;
		DataObject faultBo = null;

		sfePersonDataRes = (DataObject) locateService_SFEPartner().invoke(
				"endreAdresse", sfeEndreAdresseReq);
		
		if (sfePersonDataRes != null) {
			sfePersonDataRes = sfePersonDataRes.getDataObject(0);
		}
		String status = (sfePersonDataRes.getDataObject("sfeTilbakeMelding")
				.getDataObject("svarStatus").getString("returStatus"));
		String utfyllendeMelding = (sfePersonDataRes.getDataObject(
				"sfeTilbakeMelding").getDataObject("svarStatus")
				.getString("utfyllendeMelding"));
		// business error
		if (ERROR_CODE_FUNCTIONAL.equalsIgnoreCase(status)
				&& (ERROR_MESSAGE_FNR_NOT_FOUND
						.equalsIgnoreCase(utfyllendeMelding) || ERROR_MESSAGE_FNR_NOT_FOUND2
						.equalsIgnoreCase(utfyllendeMelding))) {
			faultBo = ErrorHelperUtil.getBusinessFaultBO(CURRENT_MODULE_NAME,
					FAULT_NAMESPACE, FAULT_PERSON_NOT_FOUND, utfyllendeMelding,
					"");
		}

		// runtime error
		else if (ERROR_CODE_TECHNICAL.equalsIgnoreCase(status)) {
			throw new ServiceRuntimeException(utfyllendeMelding);
		}

		// sjekker for feil som ikke er definert i spec'en.
		else if (!(status.equalsIgnoreCase("00") || status
				.equalsIgnoreCase("04"))) {
			throw new ServiceRuntimeException(utfyllendeMelding);
		}

		if (faultBo != null) {
			throw new ServiceBusinessException(faultBo);
		}
		return sfePersonDataRes;
	}

	/**
	 * Method generated to support implemention of operation "endreSamboerforhold" defined for WSDL port type 
	 * named "interface.SFE".
	 * 
	 * This WSDL operation has fault(s) defined. The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject endreSamboerforhold(DataObject endreSamboerforholdRequest)
			throws ServiceBusinessException {

		DataObject sfePersonDataRes = null;
		DataObject faultBo = null;

		sfePersonDataRes = (DataObject) locateService_SFEPartner().invoke(
				"endreSamboerforhold", endreSamboerforholdRequest);
		if (sfePersonDataRes != null)
			sfePersonDataRes = sfePersonDataRes.getDataObject(0);

		String status = (sfePersonDataRes.getDataObject("sfeTilbakeMelding")
				.getDataObject("svarStatus").getString("returStatus"));
		String utfyllendeMelding = (sfePersonDataRes.getDataObject(
				"sfeTilbakeMelding").getDataObject("svarStatus")
				.getString("utfyllendeMelding"));

		// business error
		// person not found
		if (ERROR_CODE_FUNCTIONAL.equalsIgnoreCase(status)){
				if (ERROR_MESSAGE_FNR_NOT_FOUND
						.equalsIgnoreCase(utfyllendeMelding) || ERROR_MESSAGE_FNR_NOT_FOUND2
						.equalsIgnoreCase(utfyllendeMelding)) {

						faultBo = ErrorHelperUtil.getBusinessFaultBO(CURRENT_MODULE_NAME,
								FAULT_NAMESPACE, FAULT_PERSON_NOT_FOUND, utfyllendeMelding,
						"");
				}
				// person or samboer dead
				else if (ERROR_MESSAGE_SAMBOER_DEAD
						.equalsIgnoreCase(utfyllendeMelding)) {

							faultBo = ErrorHelperUtil.getBusinessFaultBO(CURRENT_MODULE_NAME,
									FAULT_NAMESPACE, FAULT_SAMBOER_DEAD, utfyllendeMelding, "");
				}

				// person and samboer in family
				else if (ERROR_MESSAGE_SAMBOER_IN_FAMILY
						.equalsIgnoreCase(utfyllendeMelding)) {

						faultBo = ErrorHelperUtil.getBusinessFaultBO(CURRENT_MODULE_NAME,
								FAULT_NAMESPACE, FAULT_SAMBOER_IN_FAMILY,
								utfyllendeMelding, "");
				}

				// person and samboer in family
				else if (ERROR_MESSAGE_SAMBOERSKAP_ALREADY_EXIST1
						.equalsIgnoreCase(utfyllendeMelding) || ERROR_MESSAGE_SAMBOERSKAP_ALREADY_EXIST2
						.equalsIgnoreCase(utfyllendeMelding)) {

						faultBo = ErrorHelperUtil.getBusinessFaultBO(CURRENT_MODULE_NAME,
								FAULT_NAMESPACE, FAULT_SAMBOERSKAP_ALREADY_EXIST,
								utfyllendeMelding, "");
				}
				
				else {
					throw new ServiceRuntimeException(utfyllendeMelding);
				}
		}

		// runtime error
		else if (ERROR_CODE_TECHNICAL.equalsIgnoreCase(status)) {
			throw new ServiceRuntimeException(utfyllendeMelding);
		}

		// sjekker for feil som ikke er definert i spec'en.
		else if (!(status.equalsIgnoreCase("00") || status
				.equalsIgnoreCase("04"))) {
			throw new ServiceRuntimeException(utfyllendeMelding);
		}

		if (faultBo != null) {
			throw new ServiceBusinessException(faultBo);
		}

		return sfePersonDataRes;
	}

	/**
	 * Method generated to support implemention of operation "endreKontoinformasjon" defined for WSDL port type 
	 * named "interface.SFE".
	 * 
	 * This WSDL operation has fault(s) defined. The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject endreKontoinformasjon(
			DataObject endreKontoinformasjonRequest)
			throws ServiceBusinessException {
		DataObject sfePersonDataRes = null;
		DataObject faultBo = null;

		sfePersonDataRes = (DataObject) locateService_SFEPartner().invoke(
				"endreKontoinformasjon", endreKontoinformasjonRequest);
		if (sfePersonDataRes != null)
			sfePersonDataRes = sfePersonDataRes.getDataObject(0);

		String status = (sfePersonDataRes.getDataObject("sfeTilbakeMelding")
				.getDataObject("svarStatus").getString("returStatus"));
		String utfyllendeMelding = (sfePersonDataRes.getDataObject(
				"sfeTilbakeMelding").getDataObject("svarStatus")
				.getString("utfyllendeMelding"));

		// business error
		if (ERROR_CODE_FUNCTIONAL.equalsIgnoreCase(status)
				&& (ERROR_MESSAGE_FNR_NOT_FOUND
						.equalsIgnoreCase(utfyllendeMelding) || ERROR_MESSAGE_FNR_NOT_FOUND2
						.equalsIgnoreCase(utfyllendeMelding))) {

			faultBo = ErrorHelperUtil.getBusinessFaultBO(CURRENT_MODULE_NAME,
					FAULT_NAMESPACE, FAULT_PERSON_NOT_FOUND, utfyllendeMelding,
					"");
		}

		// runtime error
		else if (ERROR_CODE_TECHNICAL.equalsIgnoreCase(status)) {
			throw new ServiceRuntimeException(utfyllendeMelding);
		}

		// sjekker for feil som ikke er definert i spec'en.
		else if (!(status.equalsIgnoreCase("00") || status
				.equalsIgnoreCase("04"))) {
			throw new ServiceRuntimeException(utfyllendeMelding);
		}

		if (faultBo != null) {
			throw new ServiceBusinessException(faultBo);
		}

		return sfePersonDataRes;
	}

	/**
	 * Method generated to support implemention of operation "annullerSamboerforhold" defined for WSDL port type 
	 * named "interface.SFE".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject annullerSamboerforhold(
			DataObject annullerSamboerforholdRequest) {
		DataObject sfePersonDataRes = null;
		DataObject faultBo = null;

		sfePersonDataRes = (DataObject) locateService_SFEPartner().invoke(
				"annullerSamboerforhold", annullerSamboerforholdRequest);
		if (sfePersonDataRes != null)
			sfePersonDataRes = sfePersonDataRes.getDataObject(0);

		String status = (sfePersonDataRes.getDataObject("sfeTilbakeMelding")
				.getDataObject("svarStatus").getString("returStatus"));
		String utfyllendeMelding = (sfePersonDataRes.getDataObject(
				"sfeTilbakeMelding").getDataObject("svarStatus")
				.getString("utfyllendeMelding"));
		// business error, person not found
		if (ERROR_CODE_FUNCTIONAL.equalsIgnoreCase(status)
				&& (ERROR_MESSAGE_FNR_NOT_FOUND
						.equalsIgnoreCase(utfyllendeMelding) || ERROR_MESSAGE_FNR_NOT_FOUND2
						.equalsIgnoreCase(utfyllendeMelding))) {

			faultBo = ErrorHelperUtil.getBusinessFaultBO(CURRENT_MODULE_NAME,
					FAULT_NAMESPACE, FAULT_PERSON_NOT_FOUND, utfyllendeMelding,
					"");
		}

		// business error, samboerforhold not found
		else if (ERROR_CODE_FUNCTIONAL.equalsIgnoreCase(status)
				&& ERROR_MESSAGE_SAMBOERSKAP_NOT_FOUND
						.equalsIgnoreCase(utfyllendeMelding)) {

			faultBo = ErrorHelperUtil.getBusinessFaultBO(CURRENT_MODULE_NAME,
					FAULT_NAMESPACE, FAULT_SAMBOER_NOT_FOUND,
					utfyllendeMelding, "");
		}

		// runtime error
		else if (ERROR_CODE_TECHNICAL.equalsIgnoreCase(status)) {
			throw new ServiceRuntimeException(utfyllendeMelding);
		}

		// sjekker for feil som ikke er definert i spec'en.
		else if (!(status.equalsIgnoreCase("00") || status
				.equalsIgnoreCase("04"))) {
			throw new ServiceRuntimeException(utfyllendeMelding);
		}

		if (faultBo != null) {
			throw new ServiceBusinessException(faultBo);
		}

		return sfePersonDataRes;
	}

	/**
	 * Method generated to support implemention of operation "endreBrukerprofil" defined for WSDL port type 
	 * named "interface.SFE".
	 * 
	 * Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject endreBrukerprofil(DataObject endreBrukerprofilRequest) {
		DataObject sfePersonDataRes = null;
		DataObject faultBo = null;

		sfePersonDataRes = (DataObject) locateService_SFEPartner().invoke(
				"endreBrukerprofil", endreBrukerprofilRequest);
		if (sfePersonDataRes != null)
			sfePersonDataRes = sfePersonDataRes.getDataObject(0);

		String status = (sfePersonDataRes.getDataObject("sfeTilbakeMelding")
				.getDataObject("svarStatus").getString("returStatus"));
		String utfyllendeMelding = (sfePersonDataRes.getDataObject(
				"sfeTilbakeMelding").getDataObject("svarStatus")
				.getString("utfyllendeMelding"));

		// business error
		if (ERROR_CODE_FUNCTIONAL.equalsIgnoreCase(status)
				&& (ERROR_MESSAGE_FNR_NOT_FOUND
						.equalsIgnoreCase(utfyllendeMelding) || ERROR_MESSAGE_FNR_NOT_FOUND2
						.equalsIgnoreCase(utfyllendeMelding))) {

			faultBo = ErrorHelperUtil.getBusinessFaultBO(CURRENT_MODULE_NAME,
					FAULT_NAMESPACE, FAULT_PERSON_NOT_FOUND, utfyllendeMelding,
					"");
		}

		// runtime error
		else if (ERROR_CODE_TECHNICAL.equalsIgnoreCase(status)) {
			throw new ServiceRuntimeException(utfyllendeMelding);
		}

		// sjekker for feil som ikke er definert i spec'en.
		else if (!(status.equalsIgnoreCase("00") || status
				.equalsIgnoreCase("04"))) {
			throw new ServiceRuntimeException(utfyllendeMelding);
		}

		if (faultBo != null) {
			throw new ServiceBusinessException(faultBo);
		}

		return sfePersonDataRes;
	}

	/**
	 * Method generated to support implemention of operation "annullerLinjeAdresse" defined for WSDL port type 
	 * named "interface.SFE".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject annullerLinjeAdresse(
			DataObject annullerLinjeAdresseRequest) {
		DataObject sfePersonDataRes = null;
		DataObject faultBo = null;

		sfePersonDataRes = (DataObject) locateService_SFEPartner().invoke(
				"annullerLinjeAdresse", annullerLinjeAdresseRequest);
		if (sfePersonDataRes != null)
			sfePersonDataRes = sfePersonDataRes.getDataObject(0);

		String status = (sfePersonDataRes.getDataObject("sfeTilbakeMelding")
				.getDataObject("svarStatus").getString("returStatus"));
		String utfyllendeMelding = (sfePersonDataRes.getDataObject(
				"sfeTilbakeMelding").getDataObject("svarStatus")
				.getString("utfyllendeMelding"));

		// business error
		if (ERROR_CODE_FUNCTIONAL.equalsIgnoreCase(status)
				&& (ERROR_MESSAGE_FNR_NOT_FOUND
						.equalsIgnoreCase(utfyllendeMelding) || ERROR_MESSAGE_FNR_NOT_FOUND2
						.equalsIgnoreCase(utfyllendeMelding))) {

			faultBo = ErrorHelperUtil.getBusinessFaultBO(CURRENT_MODULE_NAME,
					FAULT_NAMESPACE, FAULT_PERSON_NOT_FOUND, utfyllendeMelding,
					"");
		}

		// runtime error
		else if (ERROR_CODE_TECHNICAL.equalsIgnoreCase(status)) {
			throw new ServiceRuntimeException(utfyllendeMelding);
		}

		// sjekker for feil som ikke er definert i spec'en.
		else if (!(status.equalsIgnoreCase("00") || status
				.equalsIgnoreCase("04"))) {
			throw new ServiceRuntimeException(utfyllendeMelding);
		}

		if (faultBo != null) {
			throw new ServiceBusinessException(faultBo);
		}

		return sfePersonDataRes;
	}

	/**
	 * Method generated to support implemention of operation "opphorLinjeAdresse" defined for WSDL port type 
	 * named "interface.SFE".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject opphorLinjeAdresse(DataObject opphorLinjeAdresseRequest) {
		DataObject sfePersonDataRes = null;
		DataObject faultBo = null;

		sfePersonDataRes = (DataObject) locateService_SFEPartner().invoke(
				"opphorLinjeAdresse", opphorLinjeAdresseRequest);
		if (sfePersonDataRes != null)
			sfePersonDataRes = sfePersonDataRes.getDataObject(0);

		String status = (sfePersonDataRes.getDataObject("sfeTilbakeMelding")
				.getDataObject("svarStatus").getString("returStatus"));
		String utfyllendeMelding = (sfePersonDataRes.getDataObject(
				"sfeTilbakeMelding").getDataObject("svarStatus")
				.getString("utfyllendeMelding"));

		// business error
		if (ERROR_CODE_FUNCTIONAL.equalsIgnoreCase(status)
				&& (ERROR_MESSAGE_FNR_NOT_FOUND
						.equalsIgnoreCase(utfyllendeMelding) || ERROR_MESSAGE_FNR_NOT_FOUND2
						.equalsIgnoreCase(utfyllendeMelding))) {

			faultBo = ErrorHelperUtil.getBusinessFaultBO(CURRENT_MODULE_NAME,
					FAULT_NAMESPACE, FAULT_PERSON_NOT_FOUND, utfyllendeMelding,
					"");
		}

		// runtime error
		else if (ERROR_CODE_TECHNICAL.equalsIgnoreCase(status)) {
			throw new ServiceRuntimeException(utfyllendeMelding);
		}

		// sjekker for feil som ikke er definert i spec'en.
		else if (!(status.equalsIgnoreCase("00") || status
				.equalsIgnoreCase("04"))) {
			throw new ServiceRuntimeException(utfyllendeMelding);
		}

		if (faultBo != null) {
			throw new ServiceBusinessException(faultBo);
		}

		return sfePersonDataRes;
	}

	/**
	 * Method generated to support implemention of operation "endreStatsborgerskap" defined for WSDL port type 
	 * named "interface.SFE".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject endreStatsborgerskap(
			DataObject endreStatsborgerskapRequest) {
		DataObject sfePersonDataRes = null;
		DataObject faultBo = null;

		sfePersonDataRes = (DataObject) locateService_SFEPartner().invoke(
				"endreStatsborgerskap", endreStatsborgerskapRequest);
		if (sfePersonDataRes != null)
			sfePersonDataRes = sfePersonDataRes.getDataObject(0);

		String status = (sfePersonDataRes.getDataObject("sfeTilbakeMelding")
				.getDataObject("svarStatus").getString("returStatus"));
		String utfyllendeMelding = (sfePersonDataRes.getDataObject(
				"sfeTilbakeMelding").getDataObject("svarStatus")
				.getString("utfyllendeMelding"));

		// business error
		if (ERROR_CODE_FUNCTIONAL.equalsIgnoreCase(status)
				&& (ERROR_MESSAGE_FNR_NOT_FOUND
						.equalsIgnoreCase(utfyllendeMelding) || ERROR_MESSAGE_FNR_NOT_FOUND2
						.equalsIgnoreCase(utfyllendeMelding))) {

			faultBo = ErrorHelperUtil.getBusinessFaultBO(CURRENT_MODULE_NAME,
					FAULT_NAMESPACE, FAULT_PERSON_NOT_FOUND, utfyllendeMelding,
					"");
		}

		// runtime error
		else if (ERROR_CODE_TECHNICAL.equalsIgnoreCase(status)) {
			throw new ServiceRuntimeException(utfyllendeMelding);
		}

		// sjekker for feil som ikke er definert i spec'en.
		else if (!(status.equalsIgnoreCase("00") || status
				.equalsIgnoreCase("04"))) {
			throw new ServiceRuntimeException(utfyllendeMelding);
		}

		if (faultBo != null) {
			throw new ServiceBusinessException(faultBo);
		}

		return sfePersonDataRes;
	}

	/**
	 * Method generated to support implemention of operation "endreDodsdato" defined for WSDL port type 
	 * named "interface.SFE".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject endreDodsdato(DataObject endreDodsdatoRequest) {
		DataObject sfePersonDataRes = null;
		DataObject faultBo = null;

		sfePersonDataRes = (DataObject) locateService_SFEPartner().invoke(
				"endreDodsdato", endreDodsdatoRequest);
		if (sfePersonDataRes != null)
			sfePersonDataRes = sfePersonDataRes.getDataObject(0);

		String status = (sfePersonDataRes.getDataObject("sfeTilbakeMelding")
				.getDataObject("svarStatus").getString("returStatus"));
		String utfyllendeMelding = (sfePersonDataRes.getDataObject(
				"sfeTilbakeMelding").getDataObject("svarStatus")
				.getString("utfyllendeMelding"));

		// business error
		if (ERROR_CODE_FUNCTIONAL.equalsIgnoreCase(status)
				&& (ERROR_MESSAGE_FNR_NOT_FOUND
						.equalsIgnoreCase(utfyllendeMelding) || ERROR_MESSAGE_FNR_NOT_FOUND2
						.equalsIgnoreCase(utfyllendeMelding))) {

			faultBo = ErrorHelperUtil.getBusinessFaultBO(CURRENT_MODULE_NAME,
					FAULT_NAMESPACE, FAULT_PERSON_NOT_FOUND, utfyllendeMelding,
					"");
		}

		// runtime error
		else if (ERROR_CODE_TECHNICAL.equalsIgnoreCase(status)) {
			throw new ServiceRuntimeException(utfyllendeMelding);
		}

		// sjekker for feil som ikke er definert i spec'en.
		else if (!(status.equalsIgnoreCase("00") || status
				.equalsIgnoreCase("04"))) {
			throw new ServiceRuntimeException(utfyllendeMelding);
		}

		if (faultBo != null) {
			throw new ServiceBusinessException(faultBo);
		}

		return sfePersonDataRes;
	}

	/**
	 * Method generated to support implemention of operation "endreSivilstand" defined for WSDL port type 
	 * named "interface.SFE".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject endreSivilstand(DataObject endreSivilstandRequest) {
		DataObject sfePersonDataRes = null;
		DataObject faultBo = null;

		sfePersonDataRes = (DataObject) locateService_SFEPartner().invoke(
				"endreSivilstand", endreSivilstandRequest);
		if (sfePersonDataRes != null)
			sfePersonDataRes = sfePersonDataRes.getDataObject(0);

		String status = (sfePersonDataRes.getDataObject("sfeTilbakeMelding")
				.getDataObject("svarStatus").getString("returStatus"));
		String utfyllendeMelding = (sfePersonDataRes.getDataObject(
				"sfeTilbakeMelding").getDataObject("svarStatus")
				.getString("utfyllendeMelding"));

		// business error
		if (ERROR_CODE_FUNCTIONAL.equalsIgnoreCase(status)
				&& (ERROR_MESSAGE_FNR_NOT_FOUND
						.equalsIgnoreCase(utfyllendeMelding) || ERROR_MESSAGE_FNR_NOT_FOUND2
						.equalsIgnoreCase(utfyllendeMelding))) {

			faultBo = ErrorHelperUtil.getBusinessFaultBO(CURRENT_MODULE_NAME,
					FAULT_NAMESPACE, FAULT_PERSON_NOT_FOUND, utfyllendeMelding,
					"");
		}

		// runtime error
		else if (ERROR_CODE_TECHNICAL.equalsIgnoreCase(status)) {
			throw new ServiceRuntimeException(utfyllendeMelding);
		}

		// sjekker for feil som ikke er definert i spec'en.
		else if (!(status.equalsIgnoreCase("00") || status
				.equalsIgnoreCase("04"))) {
			throw new ServiceRuntimeException(utfyllendeMelding);
		}

		if (faultBo != null) {
			throw new ServiceBusinessException(faultBo);
		}

		return sfePersonDataRes;
	}

	/**
	 * Method generated to support implemention of operation "endreNavn" defined for WSDL port type 
	 * named "interface.SFE".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject endreNavn(DataObject endreNavnRequest) {
		DataObject sfePersonDataRes = null;
		DataObject faultBo = null;

		sfePersonDataRes = (DataObject) locateService_SFEPartner().invoke(
				"endreNavn", endreNavnRequest);
		if (sfePersonDataRes != null)
			sfePersonDataRes = sfePersonDataRes.getDataObject(0);

		String status = (sfePersonDataRes.getDataObject("sfeTilbakeMelding")
				.getDataObject("svarStatus").getString("returStatus"));
		String utfyllendeMelding = (sfePersonDataRes.getDataObject(
				"sfeTilbakeMelding").getDataObject("svarStatus")
				.getString("utfyllendeMelding"));

		// business error
		if (ERROR_CODE_FUNCTIONAL.equalsIgnoreCase(status)
				&& (ERROR_MESSAGE_FNR_NOT_FOUND
						.equalsIgnoreCase(utfyllendeMelding) || ERROR_MESSAGE_FNR_NOT_FOUND2
						.equalsIgnoreCase(utfyllendeMelding))) {

			faultBo = ErrorHelperUtil.getBusinessFaultBO(CURRENT_MODULE_NAME,
					FAULT_NAMESPACE, FAULT_PERSON_NOT_FOUND, utfyllendeMelding,
					"");
		}

		// runtime error
		else if (ERROR_CODE_TECHNICAL.equalsIgnoreCase(status)) {
			throw new ServiceRuntimeException(utfyllendeMelding);
		}

		// sjekker for feil som ikke er definert i spec'en.
		else if (!(status.equalsIgnoreCase("00") || status
				.equalsIgnoreCase("04"))) {
			throw new ServiceRuntimeException(utfyllendeMelding);
		}

		if (faultBo != null) {
			throw new ServiceBusinessException(faultBo);
		}

		return sfePersonDataRes;
	}

	/**
	 * Method generated to support implemention of operation "endreRelasjon" defined for WSDL port type 
	 * named "interface.SFE".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject endreRelasjon(DataObject endreRelasjonRequest) {
		DataObject sfePersonDataRes = null;
		DataObject faultBo = null;

		sfePersonDataRes = (DataObject) locateService_SFEPartner().invoke(
				"endreRelasjon", endreRelasjonRequest);
		if (sfePersonDataRes != null)
			sfePersonDataRes = sfePersonDataRes.getDataObject(0);

		String status = (sfePersonDataRes.getDataObject("sfeTilbakeMelding")
				.getDataObject("svarStatus").getString("returStatus"));
		String utfyllendeMelding = (sfePersonDataRes.getDataObject(
				"sfeTilbakeMelding").getDataObject("svarStatus")
				.getString("utfyllendeMelding"));
		String returMelding = sfePersonDataRes.getDataObject(
				"sfeTilbakeMelding").getDataObject("svarStatus").getString(
				"returMelding");
		// business error
		if (ERROR_CODE_FUNCTIONAL.equalsIgnoreCase(status)) {
			if (ERROR_MESSAGE_FNR_NOT_FOUND.equalsIgnoreCase(utfyllendeMelding)
					|| ERROR_MESSAGE_FNR_NOT_FOUND2
							.equalsIgnoreCase(utfyllendeMelding)) {

				faultBo = ErrorHelperUtil.getBusinessFaultBO(
						CURRENT_MODULE_NAME, FAULT_NAMESPACE,
						FAULT_PERSON_NOT_FOUND, utfyllendeMelding, "");
			}

			else if (ERROR_MESSAGECODE_STATUS_UTVANDRET_REQUIRED
					.equalsIgnoreCase(returMelding)) {
				faultBo = ErrorHelperUtil.getBusinessFaultBO(
						CURRENT_MODULE_NAME, FAULT_NAMESPACE,
						FAULT_STATUS_UTVANDRET_REQUIRED, utfyllendeMelding, "");
			} else if (ERROR_MESSAGECODE_CHILD_ALREADY_HAVE_MOM
					.equalsIgnoreCase(returMelding)) {
				faultBo = ErrorHelperUtil.getBusinessFaultBO(
						CURRENT_MODULE_NAME, FAULT_NAMESPACE,
						FAULT_CHILD_ALREADY_HAVE_MOM, utfyllendeMelding, "");
			} else if (ERROR_MESSAGECODE_RELATION_TO_IT_SELF
					.equalsIgnoreCase(returMelding)) {
				faultBo = ErrorHelperUtil.getBusinessFaultBO(
						CURRENT_MODULE_NAME, FAULT_NAMESPACE,
						FAULT_RELATION_TO_IT_SELF, utfyllendeMelding, "");
			} else {
				//feil som ikke er definert i spec'en.(Runtime error)
				throw new ServiceRuntimeException(utfyllendeMelding);
			}

		}// runtime error
		else if (ERROR_CODE_TECHNICAL.equalsIgnoreCase(status)) {
			throw new ServiceRuntimeException(utfyllendeMelding);
		}

		// sjekker for feil som ikke er definert i spec'en.
		else if (status.equalsIgnoreCase("04")) {
			throw new ServiceRuntimeException(utfyllendeMelding);
		}

		if (faultBo != null) {
			throw new ServiceBusinessException(faultBo);
		}

		return sfePersonDataRes;
	}

	/**
	 * Method generated to support implemention of operation "endreFosterbarn" defined for WSDL port type 
	 * named "interface.SFE".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject endreFosterbarn(DataObject endreFosterbarnRequest) {
		DataObject sfePersonDataRes = null;
		DataObject faultBo = null;

		sfePersonDataRes = (DataObject) locateService_SFEPartner().invoke(
				"endreFosterbarn", endreFosterbarnRequest);
		if (sfePersonDataRes != null)
			sfePersonDataRes = sfePersonDataRes.getDataObject(0);

		String status = (sfePersonDataRes.getDataObject("sfeTilbakeMelding")
				.getDataObject("svarStatus").getString("returStatus"));
		String utfyllendeMelding = (sfePersonDataRes.getDataObject(
				"sfeTilbakeMelding").getDataObject("svarStatus")
				.getString("utfyllendeMelding"));
		String returMelding = sfePersonDataRes.getDataObject(
				"sfeTilbakeMelding").getDataObject("svarStatus").getString(
				"returMelding");
		// business error
		if (ERROR_CODE_FUNCTIONAL.equalsIgnoreCase(status)) {
			if (ERROR_MESSAGE_FNR_NOT_FOUND.equalsIgnoreCase(utfyllendeMelding)
					|| ERROR_MESSAGE_FNR_NOT_FOUND2
							.equalsIgnoreCase(utfyllendeMelding)) {

				faultBo = ErrorHelperUtil.getBusinessFaultBO(
						CURRENT_MODULE_NAME, FAULT_NAMESPACE,
						FAULT_PERSON_NOT_FOUND, utfyllendeMelding, "");
			}

			else if (ERROR_MESSAGECODE_FOSTERBARN_ALREADY_HAVE_FOSTERFAR
					.equalsIgnoreCase(returMelding)) {
				faultBo = ErrorHelperUtil.getBusinessFaultBO(
						CURRENT_MODULE_NAME, FAULT_NAMESPACE,
						FAULT_BARN_ALREADY_HAVE_FOSTERFAR, utfyllendeMelding,
						"");
			} else if (ERROR_MESSAGECODE_FOSTERBARN_ALREADY_HAVE_FOSTERMOR
					.equalsIgnoreCase(returMelding)) {
				faultBo = ErrorHelperUtil.getBusinessFaultBO(
						CURRENT_MODULE_NAME, FAULT_NAMESPACE,
						FAULT_BARN_ALREADY_HAVE_FOSTERMOR, utfyllendeMelding,
						"");
			} else if (ERROR_MESSAGECODE_RELATION_TO_IT_SELF
					.equalsIgnoreCase(returMelding)) {
				faultBo = ErrorHelperUtil.getBusinessFaultBO(
						CURRENT_MODULE_NAME, FAULT_NAMESPACE,
						FAULT_RELATION_TO_IT_SELF, utfyllendeMelding, "");
			} else {
				//feil som ikke er definert i spec'en.(Runtime error)
				throw new ServiceRuntimeException(utfyllendeMelding);
			}
		}// runtime error
		else if (ERROR_CODE_TECHNICAL.equalsIgnoreCase(status)) {
			throw new ServiceRuntimeException(utfyllendeMelding);
		}

		// sjekker for feil som ikke er definert i spec'en.
		else if (status.equalsIgnoreCase("04")) {
			throw new ServiceRuntimeException(utfyllendeMelding);
		}

		if (faultBo != null) {
			throw new ServiceBusinessException(faultBo);
		}

		return sfePersonDataRes;
	}

	/**
	 * Method generated to support implemention of operation "opphorGironrNorsk" defined for WSDL port type 
	 * named "interface.SFE".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject opphorGironrNorsk(DataObject opphorGironrNorskRequest) {
		DataObject sfePersonDataRes = null;
		DataObject faultBo = null;

		sfePersonDataRes = (DataObject) locateService_SFEPartner().invoke(
				"opphorGironrNorsk", opphorGironrNorskRequest);
		if (sfePersonDataRes != null)
			sfePersonDataRes = sfePersonDataRes.getDataObject(0);

		String status = (sfePersonDataRes.getDataObject("sfeTilbakeMelding")
				.getDataObject("svarStatus").getString("returStatus"));
		String utfyllendeMelding = (sfePersonDataRes.getDataObject(
				"sfeTilbakeMelding").getDataObject("svarStatus")
				.getString("utfyllendeMelding"));

		// business error
		if (ERROR_CODE_FUNCTIONAL.equalsIgnoreCase(status)
				&& (ERROR_MESSAGE_FNR_NOT_FOUND
						.equalsIgnoreCase(utfyllendeMelding) || ERROR_MESSAGE_FNR_NOT_FOUND2
						.equalsIgnoreCase(utfyllendeMelding))) {

			faultBo = ErrorHelperUtil.getBusinessFaultBO(CURRENT_MODULE_NAME,
					FAULT_NAMESPACE, FAULT_PERSON_NOT_FOUND, utfyllendeMelding,
					"");
		}

		// runtime error
		else if (ERROR_CODE_TECHNICAL.equalsIgnoreCase(status)) {
			throw new ServiceRuntimeException(utfyllendeMelding);
		}

		// sjekker for feil som ikke er definert i spec'en.
		else if (!(status.equalsIgnoreCase("00") || status
				.equalsIgnoreCase("04"))) {
			throw new ServiceRuntimeException(utfyllendeMelding);
		}

		if (faultBo != null) {
			throw new ServiceBusinessException(faultBo);
		}

		return sfePersonDataRes;
	}

	/**
	 * Method generated to support implemention of operation "opphorGironrUtl" defined for WSDL port type 
	 * named "interface.SFE".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject opphorGironrUtl(DataObject opphorGironrUtlRequest) {
		DataObject sfePersonDataRes = null;
		DataObject faultBo = null;

		sfePersonDataRes = (DataObject) locateService_SFEPartner().invoke(
				"opphorGironrUtl", opphorGironrUtlRequest);
		if (sfePersonDataRes != null)
			sfePersonDataRes = sfePersonDataRes.getDataObject(0);

		String status = (sfePersonDataRes.getDataObject("sfeTilbakeMelding")
				.getDataObject("svarStatus").getString("returStatus"));
		String utfyllendeMelding = (sfePersonDataRes.getDataObject(
				"sfeTilbakeMelding").getDataObject("svarStatus")
				.getString("utfyllendeMelding"));

		// business error
		if (ERROR_CODE_FUNCTIONAL.equalsIgnoreCase(status)
				&& (ERROR_MESSAGE_FNR_NOT_FOUND
						.equalsIgnoreCase(utfyllendeMelding) || ERROR_MESSAGE_FNR_NOT_FOUND2
						.equalsIgnoreCase(utfyllendeMelding))) {

			faultBo = ErrorHelperUtil.getBusinessFaultBO(CURRENT_MODULE_NAME,
					FAULT_NAMESPACE, FAULT_PERSON_NOT_FOUND, utfyllendeMelding,
					"");
		}

		// runtime error
		else if (ERROR_CODE_TECHNICAL.equalsIgnoreCase(status)) {
			throw new ServiceRuntimeException(utfyllendeMelding);
		}

		// sjekker for feil som ikke er definert i spec'en.
		else if (!(status.equalsIgnoreCase("00") || status
				.equalsIgnoreCase("04"))) {
			throw new ServiceRuntimeException(utfyllendeMelding);
		}

		if (faultBo != null) {
			throw new ServiceBusinessException(faultBo);
		}

		return sfePersonDataRes;
	}

	/**
	 * Method generated to support implemention of operation "korreksjonSamboerskap" defined for WSDL port type 
	 * named "SFE".
	 * 
	 * The presence of commonj.sdo.DataObject as the return type and/or as a parameter 
	 * type conveys that its a complex type. Please refer to the WSDL Definition for more information 
	 * on the type of input, output and fault(s).
	 */
	public DataObject korreksjonSamboerskap(
			DataObject korreksjonSamboerskapRequest) {
		DataObject sfePersonDataRes = null;
		DataObject faultBo = null;

		sfePersonDataRes = (DataObject) locateService_SFEPartner().invoke(
				"korreksjonSamboerskap", korreksjonSamboerskapRequest);
		if (sfePersonDataRes != null)
			sfePersonDataRes = sfePersonDataRes.getDataObject(0);
		
		String status = (sfePersonDataRes.getDataObject("sfeTilbakeMelding")
				.getDataObject("svarStatus").getString("returStatus"));
		String returMelding = sfePersonDataRes.getDataObject(
			"sfeTilbakeMelding").getDataObject("svarStatus").getString(
			"returMelding");
		String utfyllendeMelding = (sfePersonDataRes.getDataObject(
				"sfeTilbakeMelding").getDataObject("svarStatus")
				.getString("utfyllendeMelding"));
		
		// business error
		if (ERROR_CODE_FUNCTIONAL.equalsIgnoreCase(status)){
				if (ERROR_MESSAGECODE_DATES_DOES_NOT_MATCH_WITH_REGISTERED_SAMBOERSKAP
						.equalsIgnoreCase(returMelding)) {

						faultBo = ErrorHelperUtil.getBusinessFaultBO(CURRENT_MODULE_NAME,
								FAULT_NAMESPACE, FAULT_DATES_DOES_NOT_MATCH_WITH_REGISTERED_SAMBOERSKAP, utfyllendeMelding,
								"");
				} else if (ERROR_MESSAGECODE_ALTERED_FNR_NOT_FOUND
								.equalsIgnoreCase(returMelding)) {

						faultBo = ErrorHelperUtil.getBusinessFaultBO(CURRENT_MODULE_NAME,
							FAULT_NAMESPACE, FAULT_ALTERED_FNR_NOT_FOUND, utfyllendeMelding,
							"");
				} else if (ERROR_MESSAGECODE_HISTORIC_SAMBOERSKAP_NOT_FOUND
						.equalsIgnoreCase(returMelding)) {

						faultBo = ErrorHelperUtil.getBusinessFaultBO(CURRENT_MODULE_NAME,
							FAULT_NAMESPACE, FAULT_HISTORIC_SAMBOERSKAP_NOT_FOUND, utfyllendeMelding,
							"");
				} else if (ERROR_MESSAGECODE_SAMBOERSKAP_ALREADY_EXIST
						.equalsIgnoreCase(returMelding)) {

					faultBo = ErrorHelperUtil.getBusinessFaultBO(CURRENT_MODULE_NAME,
						FAULT_NAMESPACE, FAULT_SAMBOERSKAP_ALREADY_EXIST, utfyllendeMelding,
						"");
				} else {
					//feil som ikke er definert i spec'en.(Runtime error)
					throw new ServiceRuntimeException(utfyllendeMelding);
				}
		}
		// runtime error
		else if (ERROR_CODE_TECHNICAL.equalsIgnoreCase(status)) {
			throw new ServiceRuntimeException(utfyllendeMelding);
		}

		// sjekker for feil som ikke er definert i spec'en.
		else if (!(status.equalsIgnoreCase("00") || status
				.equalsIgnoreCase("04"))) {
			throw new ServiceRuntimeException(utfyllendeMelding);
		}

		if (faultBo != null) {
			throw new ServiceBusinessException(faultBo);
		}

		return sfePersonDataRes;
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation "interface.SFE#hentSFEData(DataObject hentSFEDataReq)"
	 * of wsdl interface "interface.SFE"	
	 */
	public void onEndreEpostResponse(Ticket __ticket, DataObject returnValue,
			Exception exception) {
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation "interface.SFE#hentSFEData(DataObject hentSFEDataReq)"
	 * of wsdl interface "interface.SFE"	
	 */
	public void onEndreTelefonnumreResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation "interface.SFE#hentSFEData(DataObject hentSFEDataReq)"
	 * of wsdl interface "interface.SFE"	
	 */
	public void onEndreSprakResponse(Ticket __ticket, DataObject returnValue,
			Exception exception) {
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation "interface.SFE#endreAdresse(DataObject sfeEndreAdresseReq)"
	 * of wsdl interface "interface.SFE"	
	 */
	public void onEndreAdresseResponse(Ticket __ticket, DataObject returnValue,
			Exception exception) {
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation "interface.SFE#endreSamboerforhold(DataObject endreSamboerforholdRequest)"
	 * of wsdl interface "interface.SFE"	
	 */
	public void onEndreSamboerforholdResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation "interface.SFE#endreKontoinformasjon(DataObject endreKontoinformasjonRequest)"
	 * of wsdl interface "interface.SFE"	
	 */
	public void onEndreKontoinformasjonResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation "interface.SFE#annullerSamboerforhold(DataObject annullerSamboerforholdRequest)"
	 * of wsdl interface "interface.SFE"	
	 */
	public void onAnnullerSamboerforholdResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation "interface.SFE#endreBrukerprofil(DataObject endreBrukerprofilRequest)"
	 * of wsdl interface "interface.SFE"	
	 */
	public void onEndreBrukerprofilResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation "interface.SFE#annullerLinjeAdresse(DataObject annullerLinjeAdresseRequest)"
	 * of wsdl interface "interface.SFE"	
	 */
	public void onAnnullerLinjeAdresseResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation "interface.SFE#opphorLinjeAdresse(DataObject opphorLinjeAdresseRequest)"
	 * of wsdl interface "interface.SFE"	
	 */
	public void onOpphorLinjeAdresseResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation "interface.SFE#endreStatsborgerskap(DataObject endreStatsborgerskapRequest)"
	 * of wsdl interface "interface.SFE"	
	 */
	public void onEndreStatsborgerskapResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation "interface.SFE#endreDodsdato(DataObject endreDodsdatoRequest)"
	 * of wsdl interface "interface.SFE"	
	 */
	public void onEndreDodsdatoResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation "interface.SFE#endreSivilstand(DataObject endreSivilstandRequest)"
	 * of wsdl interface "interface.SFE"	
	 */
	public void onEndreSivilstandResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation "interface.SFE#endreNavn(DataObject endreNavnRequest)"
	 * of wsdl interface "interface.SFE"	
	 */
	public void onEndreNavnResponse(Ticket __ticket, DataObject returnValue,
			Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation "interface.SFE#endreRelasjon(DataObject endreRelasjonRequest)"
	 * of wsdl interface "interface.SFE"	
	 */
	public void onEndreRelasjonResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation "interface.SFE#endreFosterbarn(DataObject endreFosterbarnRequest)"
	 * of wsdl interface "interface.SFE"	
	 */
	public void onEndreFosterbarnResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation "interface.SFE#opphorGironrNorsk(DataObject opphorGironrNorskRequest)"
	 * of wsdl interface "interface.SFE"	
	 */
	public void onOpphorGironrNorskResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation "interface.SFE#opphorGironrUtl(DataObject opphorGironrUtlRequest)"
	 * of wsdl interface "interface.SFE"	
	 */
	public void onOpphorGironrUtlResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation "SFE#korreksjonSamboerskap(DataObject korreksjonSamboerskapRequest)"
	 * of wsdl interface "SFE"	
	 */
	public void onKorreksjonSamboerskapResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
		//TODO Needs to be implemented.
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation "interface.SFE#endreNorskGironr(DataObject endreNorskGironrRequest)"
	 * of wsdl interface "interface.SFE"	
	 */
	public void onEndreNorskGironrResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
	}

	/**
	 * Method generated to support the async implementation using callback
	 * for the operation "interface.SFE#endreUtenlanskGironr(DataObject endreUtenlandskGironrRequest)"
	 * of wsdl interface "interface.SFE"	
	 */
	public void onEndreUtenlanskGironrResponse(Ticket __ticket,
			DataObject returnValue, Exception exception) {
	}

	/**
	 * Method for preparing the SBE when an error has been recognized from the status message
	 */
	public DataObject prepareFaultBo(DataObject responseStatus) {
		DataObject faultBo = DataFactory.INSTANCE.create(
				"http://nav-lib-frg-sfe/no/nav/lib/frg/asbo", "sfeFault");

		String timestamp = ErrorHelperUtil.getTimestamp();
		String scaContext = ErrorHelperUtil.getSCAContext(CURRENT_MODULE_NAME);

		if (responseStatus != null
				&& responseStatus.getString("utfyllendeMelding") != null
				&& responseStatus.getString("utfyllendeMelding") != "") {
			faultBo.setString("errorCode", "SBE"
					+ responseStatus.getString("returStatus"));
			faultBo.setString("rootCause", "SFE / SFEMessage: " +
			//responseStatus.getString("returStatus") + " - " + 
					//responseStatus.getString("returMelding") + " - " + 
					responseStatus.getString("utfyllendeMelding"));
		} else {
			faultBo.setString("errorCode", "SBE");
			faultBo.setString("rootCause",
					"Message: NOE GIKK GALT I TJENESTEKALLET");
		}

		faultBo.setString("errorMessage",
				"Producer Business Exception - <msg tbd>");
		faultBo.setString("errorSource", scaContext);
		faultBo.setString("errorType", "Business");
		faultBo.setString("dateTimeStamp", timestamp);

		return faultBo;
	}

}