package no.nav.presentation.pensjon.saksbehandling.stelvio.service;

import no.nav.service.pensjon.exception.DatabaseNotFoundException;

/**
 * 
 * @author Therese Steense
 *
 */
public interface OrgEnhetService {

	public String getOrgEnhetNavn(String regenhetnr) throws DatabaseNotFoundException;
	
}
