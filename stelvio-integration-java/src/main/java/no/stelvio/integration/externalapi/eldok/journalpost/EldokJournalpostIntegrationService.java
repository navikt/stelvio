package no.stelvio.integration.externalapi.eldok.journalpost;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;

import no.ibm.egov.earkiv.api.EAContext;
import no.ibm.egov.earkiv.api.EAServiceLocatorException;
import no.ibm.egov.earkiv.api.dataobject.Journpost;
import no.ibm.egov.earkiv.api.journpost.EAJournalpost;
import no.ibm.egov.earkiv.api.sok.SearchParm;
import no.ibm.egov.earkiv.api.sok.SearchSort;
import no.ibm.egov.earkiv.util.exceptions.EArkivException;
import no.stelvio.integration.service.IntegrationService;

import org.apache.commons.lang.builder.ToStringBuilder;

import no.stelvio.common.FrameworkError;
import no.stelvio.common.error.SystemException;
import no.stelvio.common.error.system.ELDOKException;
import no.stelvio.common.service.ServiceFailedException;
import no.stelvio.common.service.ServiceRequest;
import no.stelvio.common.service.ServiceResponse;

/**
 * @author person5b7fd84b3197, Accenture
 * @version $Revision: 2801 $ $Author: skb2930 $ $Date: 2006-03-01 12:29:46 +0100 (Wed, 01 Mar 2006) $
 */
public class EldokJournalpostIntegrationService extends IntegrationService {

	private EAJournalpost eajournalpost = null;

	private EAContext context = null;

	private String username = null;

	private String password = null;

	private static final String FUNCTION = "FUNCTION";

	private static final String SEARCH = "SEARCH";

	private static final String NEW = "NEW";

	private static final String UPDATE = "UPDATE";

	private static final String RETRIEVE = "RETRIEVE";

	private static final String SEARCHPARAM = "SEARCHPARAM";

	private static final String SEARCHSORT_ARRAY = "SEARCHSORT_ARRAY";

	private static final String INCLUDE = "INCLUDE";

	private static final String JOURNALPOST = "JOURNALPOST";

	private static final String JOURNALPOST_ID = "JOURNALPOST_ID";

	private static final String DOKUMENTTYPE = "DOKUMENTTYPE";

	private static final String RETURNVAL = "RETURNVAL";

	/**
	 * Initmethod.
	 * Only called once and will establishe connection to eldok
	 * when called.
	 */
	public void init() {
		if (null != eajournalpost) {
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
			eajournalpost = new EAJournalpost(context);
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
	 * Search Journalpost:
	 * 	FUNCTION = SEARCH
	 * 	PARAMETERS -> HashMap 
	 * 		<table>
	 * 			<tr><td>key</td><td>valueType</td></tr>
	 * 			<tr><td>SEARCHPARAM</td><td>SearchParam</td></tr>
	 * 			<tr><td>SEARCHSORT_ARRAY</td><td>ArrayList with SearchSort</td></tr>
	 * 			<tr><td>INCLUDE</td><td>Integer</td></tr>
	 * 		<table>
	 * RETURNVAL = ArrayList with Journalpost
	 * 
	 * Retrieve Journalpost:
	 * 	FUNCTION = RETRIEVE
	 * 	PARAMETERS -> HashMap 
	 * 		<table>
	 * 			<tr><td>key</td><td>valueType</td></tr>
	 * 			<tr><td>JOURNALPOST_ID</td><td>Long</td></tr>
	 * 			<tr><td>INCLUDE</td><td>Integer</td></tr>
	 * 		<table>
	 * RETURNVAL = Journalpost
	 * 
	 * Update Journalpost:
	 * 	FUNCTION = UPDATE
	 * 	PARAMETERS -> HashMap 
	 * 		<table>
	 * 			<tr><td>key</td><td>valueType</td></tr>
	 * 			<tr><td>JOURNALPOST</td><td>Journalpost</td></tr>
	 * 		<table>
	 * RETURNVAL = Journalpost
	 * 
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

			Journpost[] jps = searchJournpost(parm, sort, include.intValue());
			// makes a check to make sure that we dont get any nullpointer
			ArrayList ret = new ArrayList();
			if (jps != null) {
				ret.addAll(Arrays.asList(jps));
			}
			response.setData(RETURNVAL, ret);
		} else if (RETRIEVE.equals(function)) {
			Long jpid = (Long) request.getData(JOURNALPOST_ID);
			int inc = ((Integer) request.getData(INCLUDE)).intValue();
			Journpost jp = retrieveJournpost(jpid, inc);
			response.setData(RETURNVAL, jp);
		} else if (UPDATE.equals(function)) {

			Journpost jp = (Journpost) request.getData(JOURNALPOST);
			jp = updateJournpost(jp);
			response.setData(RETURNVAL, jp);
		} else if (NEW.equals(function)) {
			String dokType = (String) request.getData(DOKUMENTTYPE);
			Journpost jp = null;
			jp = newJournpost(dokType);
			response.setData(RETURNVAL, jp);
		} else {
			throw new ServiceFailedException(FrameworkError.ELDOK_FUNCTION_ERROR);
		}
		return response;
	}

	/**
	 * Inkapsling av EAJournalpost for testbarhet
	 * @param param - search param
	 * @param sort - Sort param
	 * @param include - Include
	 * @return Journpost[] - Sokeresultat
	 * @throws ELDOKException - hvis feil i eldok
	 */

	private Journpost[] searchJournpost(SearchParm param, SearchSort[] sort, int include) throws ELDOKException {
		init();
		try {
			return eajournalpost.searchJournpost(param, sort, include);
		} catch (Exception e) {
			throw new ELDOKException(FrameworkError.ELDOK_JOURNALPOST_ERROR, e);
		}
	}

	/**
		 * Oppretter en ny journalpost
		 *
		 * @param dokType dokumenttypen
		 * @return Journalpost resultat
		 * @throws ELDOKException hvis feil i eldok
		 */
	private Journpost newJournpost(String dokType) throws ELDOKException {
		init();
		try {
			return eajournalpost.newJournpost(null, dokType);
		} catch (Exception e) {
			throw new ELDOKException(FrameworkError.ELDOK_JOURNALPOST_ERROR, e);
		}
	}

	/**
	 * Inkapsling av EAJournalpost for testbarhet
	 * @param journalpostId - JP id
	 * @param include - Include
	 * @return Journalpost - resultat
	 * @throws ELDOKException - hvis feil i eldok
	 */
	private Journpost retrieveJournpost(Long journalpostId, int include) throws ELDOKException {
		init();

		try {
			Journpost journ = eajournalpost.retrieveJournpost(journalpostId, include);

			if (log.isDebugEnabled() && journ != null) {
				ToStringBuilder.reflectionToString(journ);
			}
			return journ;
		} catch (Exception e) {
			throw new ELDOKException(FrameworkError.ELDOK_JOURNALPOST_ERROR, e);
		}
	}

	/**
	 * Inkapsling av EAJournalpost for testbarhet
	 * @param journalpost - Journalpost 
	 * @return Journalpost - oppdatert jp
	 * @throws ELDOKException - hvis feil i eldok
	 */

	private Journpost updateJournpost(Journpost journalpost) throws ELDOKException {

		init();
		try {
			long before = System.currentTimeMillis();

			if (log.isDebugEnabled()) {
				log.debug("Journalpost før lagring" + ToStringBuilder.reflectionToString(journalpost));
				log.debug("Calling EAJournalpost.updateJournpost() at " + before);
			}
	
			Journpost nyJp = eajournalpost.updateJournpost(journalpost);
			if (log.isDebugEnabled()) {
				long after = System.currentTimeMillis();
				log.debug("Returning from EAJournalpost.updateJournpost() at " + after);
				log.debug("Response time was " + (after - before) + " milliseconds");
				log.debug("har lagret, henter oppdatert journalpost");
			}
		
			if (log.isDebugEnabled() && nyJp != null) {
				log.debug("Journalposten etter lagring: ");
				log.debug(ToStringBuilder.reflectionToString(nyJp));
			}

			return nyJp;

		} catch (Exception e) {
			throw new ELDOKException(FrameworkError.ELDOK_JOURNALPOST_ERROR, e);
		}
	}

	/**
	 * Setter for EAContext
	 * @param context - EAContext
	 */
	public void setContext(EAContext context) {
		this.context = context;
	}

	/**
	 * Setter for EAJournalpost
	 * @param journalpost - EAJournalpost
	 */
	public void setEajournalpost(EAJournalpost journalpost) {
		eajournalpost = journalpost;
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
