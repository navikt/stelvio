package no.trygdeetaten.integration.framework.externalapi.eldok.dokument;

import java.rmi.RemoteException;

import no.ibm.egov.earkiv.api.EAContext;
import no.ibm.egov.earkiv.api.EAServiceLocatorException;
import no.ibm.egov.earkiv.api.dataobject.Dokbeskriv;
import no.ibm.egov.earkiv.api.dataobject.Dokversjon;
import no.ibm.egov.earkiv.api.dokument.EADokument;
import no.ibm.egov.earkiv.util.exceptions.EArkivException;

import no.trygdeetaten.common.framework.FrameworkError;
import no.trygdeetaten.common.framework.error.SystemException;
import no.trygdeetaten.common.framework.error.system.ELDOKException;
import no.trygdeetaten.common.framework.service.ServiceFailedException;
import no.trygdeetaten.common.framework.service.ServiceRequest;
import no.trygdeetaten.common.framework.service.ServiceResponse;

import no.trygdeetaten.integration.framework.service.IntegrationService;

/**
 * 
 * @author person5b7fd84b3197, Accenture
 * @version $Id: EldokDokumentIntegrationService.java 2738 2006-01-13 15:15:11Z skb2930 $
 */
public class EldokDokumentIntegrationService extends IntegrationService {

	private EADokument dokument = null;

	private EAContext context = null;

	private String username = null;

	private String password = null;

	private static final String FUNCTION = "FUNCTION";

	private static final String RETRIEVE_BESK = "RETRIEVE_BESK";

	private static final String UPDATE = "UPDATE";

	private static final String NEW_DOKBESKRIVELSE = "NEW_DOKBESKRIVELSE";

	private static final String NEW_DOKVERSJON = "NEW_DOKVERSJON";

	private static final String RETRIEVE_VER = "RETRIEVE_VER";

	private static final String VARIANT = "VARIANT";

	private static final String VERSJON = "VERSJON";

	private static final String INCLUDE = "INCLUDE";

	private static final String DOKUMENT_ID = "DOKUMENT_ID";

	private static final String JOURNALPOST_ID = "JOURNALPOST_ID";

	private static final String DOKBESK = "DOKBESK";

	private static final String RETURNVAL = "RETURNVAL";

	/**
	 * Initmethod.
	 * Only called once and will establish connection to eldok
	 * when called.
	 */
	public void init() {
		if (null != dokument) {
			// This should only be created once
			return;
		}

		if (null == context) {
			throw new SystemException(FrameworkError.ELDOK_CONFIG_ERROR, "Context is not set.");
		}

		if (null == username) {
			throw new SystemException(FrameworkError.ELDOK_CONFIG_ERROR, "Username is not set.");
		}

		if (null == password) {
			throw new SystemException(FrameworkError.ELDOK_CONFIG_ERROR, "Password is not set.");
		}

		try {
			context.connect(username, password);
			dokument = new EADokument(context);
		} catch (RemoteException e) {
			throw new ELDOKException(FrameworkError.ELDOK_CONNECTIVITY_ERROR, e);
		} catch (EArkivException e) {
			throw new ELDOKException(FrameworkError.ELDOK_CONNECTIVITY_ERROR, e);
		} catch (EAServiceLocatorException e) {
			throw new ELDOKException(FrameworkError.ELDOK_CONNECTIVITY_ERROR, e);
		} catch (Exception e) {
			throw new ELDOKException(FrameworkError.ELDOK_CONNECTIVITY_ERROR, e);
		}
	}

	/**
	 * This method have several functions available configurable through the ServiceRequest.
	 * <p/>
	 * Search Sak:
	 * 	FUNCTION = RETRIEVE_BESK
	 * 	PARAMETERS -> HashMap 
	 * 		<table>
	 * 			<tr><td>key</td><td>valueType</td></tr>
	 * 			<tr><td>JOURNALPOST_ID</td><td>Long</td></tr>
	 * 			<tr><td>DOKUMENT_ID</td><td>Long</td></tr>
	 * 			<tr><td>INCLUDE</td><td>Integer</td></tr>
	 * 		<table>
	 * RETURNVAL = Dokbeskriv
	 * <p/>
	 * Retrieve Sak:
	 * 	FUNCTION = RETRIEVE_VER
	 * 	PARAMETERS -> HashMap 
	 * 		<table>
	 * 			<tr><td>key</td><td>valueType</td></tr>
	 * 			<tr><td>DOKUMENT_ID</td><td>Long</td></tr>
	 * 			<tr><td>VERSJON</td><td>Long</td></tr>
	 * 			<tr><td>VARIANT</td><td>String</td></tr>
	 * 			<tr><td>INCLUDE</td><td>Integer</td></tr>
	 * 		<table>
	 * RETURNVAL = Dokversjon
	 * <p/>
	 * Update Sak:
	 * 	FUNCTION = UPDATE
	 * 	PARAMETERS -> HashMap 
	 * 		<table>
	 * 			<tr><td>key</td><td>valueType</td></tr>
	 * 			<tr><td>DOKBESK</td><td>Dokbeskriv</td></tr>
	 * 		<table>
	 * RETURNVAL = Dokbeskriv
	 * @see no.trygdeetaten.integration.framework.service.IntegrationService#doExecute(no.trygdeetaten.common.framework.service.ServiceRequest)
	 */
	protected ServiceResponse doExecute(ServiceRequest request) throws ServiceFailedException {
		String function = (String) request.getData(FUNCTION);
		ServiceResponse response = new ServiceResponse();
		if (RETRIEVE_BESK.equals(function)) {

			Long dokId = (Long) request.getData(INCLUDE);
			Long jpId = (Long) request.getData(INCLUDE);
			Integer include = (Integer) request.getData(INCLUDE);

			Object ret = retrieveDokbeskriv(jpId, dokId, include.intValue());

			response.setData(RETURNVAL, ret);
		} else if (RETRIEVE_VER.equals(function)) {
			Long dokId = (Long) request.getData(DOKUMENT_ID);
			Long versjon = (Long) request.getData(VERSJON);
			String variant = (String) request.getData(VARIANT);
			int inc = ((Integer) request.getData(INCLUDE)).intValue();
			Dokversjon ver = retrieveDokversjon(dokId, versjon, variant, inc);
			response.setData(RETURNVAL, ver);
		} else if (UPDATE.equals(function)) {
			Dokbeskriv besk = (Dokbeskriv) request.getData(DOKBESK);
			besk = updateDokbeskriv(besk);
			response.setData(RETURNVAL, besk);
		} else if (NEW_DOKBESKRIVELSE.equals(function)) {
			Long jpId = (Long) request.getData(JOURNALPOST_ID);
			Dokbeskriv dokbeskriv = null;
			dokbeskriv = newDokbeskriv(jpId);
			response.setData(RETURNVAL, dokbeskriv);
		} else if (NEW_DOKVERSJON.equals(function)) {
			String variant = (String) request.getData(VARIANT);
			Dokversjon versjon = null;
			versjon = newDokversjon(variant);
			response.setData(RETURNVAL, versjon);
		} else {
			throw new ServiceFailedException(FrameworkError.ELDOK_FUNCTION_ERROR);
		}
		return response;
	}

	/**
	 * Inkapsling av EADokument for testbarhet
	 * @param jpid - Journalpost id
	 * @param dokid - Dokument id
	 * @param includes - INCLUDES
	 * @throws ELDOKException - Hvis feil mote eldok
	 * @return Dokbeskriv - Beskrivelsen
	 */
	public Dokbeskriv retrieveDokbeskriv(Long jpid, Long dokid, int includes) throws ELDOKException {
		init();
		try {
			return dokument.retrieveDokbeskriv(jpid, dokid, includes);
		} catch (Exception e) {
			throw new ELDOKException(FrameworkError.ELDOK_DOC_ERROR, e);
		}
	}

	/**
	 * Inkapsling av EADokument for testbarhet
	 * @param jpid - Journalpost id
	 * @throws ELDOKException - Hvis feil mote eldok
	 * @return Dokbeskriv - Beskrivelsen
	 */
	public Dokbeskriv newDokbeskriv(Long jpid) throws ELDOKException {
		init();
		try {
			return dokument.newDokbeskriv(jpid);
		} catch (Exception e) {
			throw new ELDOKException(FrameworkError.ELDOK_DOC_ERROR, e);
		}
	}

	/**
	 * Inkapsling av EADokument for testbarhet
	 * @param jpid - Journalpost id
	 * @throws ELDOKException - Hvis feil mote eldok
	 * @return Dokbeskriv - Beskrivelsen
	 */
	public Dokversjon newDokversjon(String variant) throws ELDOKException {
		init();
		try {
			return dokument.newDokversjon(variant);
		} catch (Exception e) {
			throw new ELDOKException(FrameworkError.ELDOK_DOC_ERROR, e);
		}
	}

	/**
	 * Inkapsling av EADokument for testbarhet
	 * @param dokid - Dokument id
	 * @param versjon - Versjon
	 * @param variant - Variant
	 * @param includes - INCLUDES
	 * @throws ELDOKException - Hvis feil mote eldok
	 * @return Dokbeskriv - Beskrivelsen
	 */
	public Dokversjon retrieveDokversjon(Long dokid, Long versjon, String variant, int includes) throws ELDOKException {
		init();
		try {
			return dokument.retrieveDokversjon(dokid, versjon, variant, includes);
		} catch (Exception e) {
			throw new ELDOKException(FrameworkError.ELDOK_DOC_ERROR, e);
		}
	}

	/**
	 * Inkapsling av EADokument for testbarhet
	 * @param modifiedDokbeskriv - Endret dokbeskrivelse
	 * @throws ELDOKException - Hvis feil mote eldok
	 * @return Dokbeskriv - Beskrivelsen
	 */
	public Dokbeskriv updateDokbeskriv(Dokbeskriv modifiedDokbeskriv) throws ELDOKException {
		init();
		try {
			long before = System.currentTimeMillis();

			if (log.isDebugEnabled()) {
				log.debug("Calling EADokument.updateDokbeskriv() at " + before);
			}
			Dokbeskriv dokBeskriv = dokument.updateDokbeskriv(modifiedDokbeskriv);
			if (log.isDebugEnabled()) {
				long after = System.currentTimeMillis();
				log.debug("Returning from EADokument.updateDokbeskriv() at " + after);
				log.debug("Response time was " + (after - before) + " milliseconds");
			}

			return dokBeskriv;
		} catch (Exception e) {
			throw new ELDOKException(FrameworkError.ELDOK_DOC_ERROR, e);
		}
	}

	/**
	 * Setter for Dokument
	 * @param dokument - Dokument
	 */
	public void setDokument(EADokument dokument) {
		this.dokument = dokument;
	}
	/**
	 * Setter for EAContext
	 * @param context - EAContext
	 */
	public void setContext(EAContext context) {
		this.context = context;
	}
	/**
	 * Setter for password
	 * @param string - password
	 */
	public void setPassword(String string) {
		password = string;
	}

	/**
	 * Setter for username
	 * @param string - username
	 */
	public void setUsername(String string) {
		username = string;
	}

}
