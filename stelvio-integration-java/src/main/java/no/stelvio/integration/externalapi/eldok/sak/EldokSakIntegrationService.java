package no.stelvio.integration.externalapi.eldok.sak;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;

import no.ibm.egov.earkiv.api.EAContext;
import no.ibm.egov.earkiv.api.EAServiceLocatorException;
import no.ibm.egov.earkiv.api.dataobject.Sak;
import no.ibm.egov.earkiv.api.sak.EASak;
import no.ibm.egov.earkiv.api.sok.SearchParm;
import no.ibm.egov.earkiv.api.sok.SearchSort;
import no.ibm.egov.earkiv.util.exceptions.EArkivException;
import no.stelvio.integration.service.IntegrationService;

import no.stelvio.common.FrameworkError;
import no.stelvio.common.error.SystemException;
import no.stelvio.common.error.system.ELDOKException;
import no.stelvio.common.service.ServiceFailedException;
import no.stelvio.common.service.ServiceRequest;
import no.stelvio.common.service.ServiceResponse;


/**
 * 
 * @author person5b7fd84b3197, Accenture
 * @version $Id: EldokSakIntegrationService.java 2738 2006-01-13 15:15:11Z skb2930 $
 */
public class EldokSakIntegrationService extends IntegrationService {

	private EASak easak = null;

	private EAContext context = null;
	
	private String username = null;
	
	private String password = null;
	
	private static final String FUNCTION = "FUNCTION";

	private static final String SEARCH = "SEARCH";

	private static final String UPDATE = "UPDATE";

	private static final String RETRIEVE = "RETRIEVE";

	private static final String SEARCHPARAM = "SEARCHPARAM";
	
	private static final String SEARCHSORT_ARRAY = "SEARCHSORT_ARRAY";
	
	private static final String INCLUDE = "INCLUDE";
	
	private static final String SAK = "SAK";
	
	private static final String SAKSNUMMER = "SAKSNUMMER";

	private static final String RETURNVAL = "RETURNVAL";
	
	/**
	 * Initmethod.
	 * Only called once and will establishe connection to eldok
	 * when called.
	 */
	public void init() {
		if (null != easak) {
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
			easak = new EASak(context);
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
	 * This method should be used throgh the ItegrationService.
	 * This method have several functions available configurable through
	 * the ServiceRequest.
	 * 
	 * Search Sak:
	 * 	FUNCTION = SEARCH
	 * 	PARAMETERS -> HashMap 
	 * 		<table>
	 * 			<tr><td>key</td><td>valueType</td></tr>
	 * 			<tr><td>SEARCHPARAM</td><td>SearchParam</td></tr>
	 * 			<tr><td>SEARCHSORT_ARRAY</td><td>ArrayList with SearchSort</td></tr>
	 * 			<tr><td>INCLUDE</td><td>Integer</td></tr>
	 * 		<table>
	 * RETURNVAL = ArrayList with Sak
	 * 
	 * Retrieve Sak:
	 * 	FUNCTION = RETRIEVE
	 * 	PARAMETERS -> HashMap 
	 * 		<table>
	 * 			<tr><td>key</td><td>valueType</td></tr>
	 * 			<tr><td>SAKSNUMMER</td><td>Long</td></tr>
	 * 			<tr><td>INCLUDE</td><td>Integer</td></tr>
	 * 		<table>
	 * RETURNVAL = Sak
	 * 
	 * Update Sak:
	 * 	FUNCTION = UPDATE
	 * 	PARAMETERS -> HashMap 
	 * 		<table>
	 * 			<tr><td>key</td><td>valueType</td></tr>
	 * 			<tr><td>SAK</td><td>Sak</td></tr>
	 * 		<table>
	 * RETURNVAL = Sak
	 * @see no.stelvio.integration.service.IntegrationService#doExecute(no.stelvio.common.service.ServiceRequest)
	 */
	protected ServiceResponse doExecute(ServiceRequest request) throws ServiceFailedException {
		String function = (String) request.getData(FUNCTION);
		ServiceResponse response = new ServiceResponse();
		if (SEARCH.equals(function)) {
			SearchParm parm = (SearchParm) request.getData(SEARCHPARAM);

			ArrayList sps = (ArrayList) request.getData(SEARCHSORT_ARRAY);
			SearchSort[] sort = new SearchSort[sps.size()];
			System.arraycopy(sps.toArray(), 0, sort, 0, sort.length);

			Integer include = (Integer) request.getData(INCLUDE);

			Sak[] saks = searchSak(parm, sort, include.intValue());
			ArrayList ret = new ArrayList();
			ret.addAll(Arrays.asList(saks));
			response.setData(RETURNVAL, ret);
		} else if (RETRIEVE.equals(function)) {
			Long saksnr = (Long) request.getData(SAKSNUMMER);
			int inc = ((Integer) request.getData(INCLUDE)).intValue();
			Sak jp = retrieveSak(saksnr, inc);
			response.setData(RETURNVAL, jp);
		} else if (UPDATE.equals(function)) {
			Sak sak = (Sak) request.getData(SAK);
			sak = updateSak(sak);
			response.setData(RETURNVAL, sak);
		} else {
			throw new ServiceFailedException(FrameworkError.ELDOK_FUNCTION_ERROR);
		}
		return response;
	}

	/**
	 * Inkapsling av EASak for testbarhet
	 * @param sak - Sak å oppdatere
	 * @return Sak - Oppdatert sak
	 * @throws ELDOKException - Hvis feil i eldok
	 */
	private Sak updateSak(Sak sak) throws ELDOKException {
		init();
		try {
			return easak.updateSak(sak);
		} catch (Exception e) {
			throw new ELDOKException(FrameworkError.ELDOK_SAK_ERROR, e);
		} 
	}

	/**
	 * Inkapsling av EASak for testbarhet
	 * @param searchParm - Sokeparams
	 * @param sort - Sortering
	 * @param includes - Hvilke objekter skal inkluderes
	 * @return Sak[] - Skaker funnet
	 * @throws ELDOKException - Hvis feil i eldok
	 */
	private Sak[] searchSak(SearchParm searchParm, SearchSort[] sort, int includes) throws ELDOKException {
		init();		
		try {
			return easak.searchSak( searchParm, sort, includes );
		} catch (Exception e) {
			throw new ELDOKException(FrameworkError.ELDOK_SAK_ERROR, e);
		} 
	}

	/**
	 * 
	 * @param saksnummer - Saksnummer
	 * @param include - Include
	 * @return Sak - Hentet sak
	 * @throws ELDOKException - Hvis feil i eldok
	 */
	private Sak retrieveSak(Long saksnummer, int include) throws ELDOKException {
		init();
		try {
			return easak.retrieveSak( saksnummer, include );
		} catch (Exception e) {
			throw new ELDOKException(FrameworkError.ELDOK_SAK_ERROR, e);
		} 
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
