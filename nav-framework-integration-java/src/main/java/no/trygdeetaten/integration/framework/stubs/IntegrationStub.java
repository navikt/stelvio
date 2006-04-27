package no.trygdeetaten.integration.framework.stubs;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import no.trygdeetaten.common.framework.service.ServiceFailedException;
import no.trygdeetaten.common.framework.service.ServiceRequest;
import no.trygdeetaten.common.framework.service.ServiceResponse;

import no.trygdeetaten.integration.framework.service.IntegrationService;

/**
 * Generell stub for integrasjon. Denne konfigrurers via SPRING og returnerer objekt(er) for
 * definerte tjenester.
 * 
 * @author person1f201b37d484, Accenture
 */
public class IntegrationStub extends IntegrationService {

	Map interactions = new HashMap();
	String inputServiceName = null;

	/**
	 * This method is responsible for executing the service.
	 * 
	 * @param request - The service request
	 * @throws ServiceFailedException - When the service fails to execute
	 * @return ServiceResponse - The result from the backend execution
	 */
	protected ServiceResponse doExecute(ServiceRequest request) throws ServiceFailedException {
		if (log.isDebugEnabled()) {
			log.debug("START: doExecute");
		}
		ServiceResponse resp = new ServiceResponse();

		// Sjekk hvilken service som skal kalles og finn returobjekter
		String returObjKey = (String) request.getData(inputServiceName);
		Map returObjekter = (Map) interactions.get(returObjKey);

		if (returObjekter != null) {
			// Returner alle objekter med KEY som navn
			Set keys = returObjekter.keySet();
			Iterator i = keys.iterator();
			while (i.hasNext()) {
				String key = (String) i.next();

				resp.setData(key, returObjekter.get(key));

				if (log.isDebugEnabled()) {
					log.debug("Setting return data, key:" + key + " data:" + returObjekter.get(key));
				}
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("END: doExecute");
		}
		return resp;
	}

	/**
	 * Set the interactions.
	 * 
	 * @param i map of interactions.
	 */
	public void setInteractions(Map i) {
		interactions = i;
	}

	/**
	 * Set the name of the input service.
	 * 
	 * @param s the name.
	 */
	public void setInputServiceName(String s) {
		inputServiceName = s;
	}

}
