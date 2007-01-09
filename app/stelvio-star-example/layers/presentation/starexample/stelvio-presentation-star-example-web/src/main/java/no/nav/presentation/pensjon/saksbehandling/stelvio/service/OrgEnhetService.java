package no.nav.presentation.pensjon.saksbehandling.stelvio.service;

import no.nav.presentation.pensjon.saksbehandling.stelvio.exceptions.DatabaseNotFoundException;

/**
 * 
 * @author Therese Steense
 *
 */
public interface OrgEnhetService {

	public String getOrgEnhetNavn(String regenhetnr) throws DatabaseNotFoundException;
	
}
