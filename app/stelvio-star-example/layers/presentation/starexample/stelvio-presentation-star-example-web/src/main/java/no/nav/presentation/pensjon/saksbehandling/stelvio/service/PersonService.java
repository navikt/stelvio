/**
 * 
 */
package no.nav.presentation.pensjon.saksbehandling.stelvio.service;

import java.util.List;

import no.nav.presentation.pensjon.saksbehandling.stelvio.dao.PersonDO;
import no.nav.presentation.pensjon.saksbehandling.stelvio.dao.PersonSearchDO;
import no.nav.presentation.pensjon.saksbehandling.stelvio.exceptions.DatabaseNotFoundException;
import no.nav.presentation.pensjon.saksbehandling.stelvio.exceptions.PersonNotFoundException;
import no.nav.presentation.pensjon.saksbehandling.stelvio.exceptions.TPSException;

/**
 * @author person4f9bc5bd17cc
 *
 */
public interface PersonService {
	public PersonDO readPerson(String fnr) throws TPSException, PersonNotFoundException, DatabaseNotFoundException;
	public void updatePerson(PersonDO person) throws DatabaseNotFoundException;
	public List<PersonDO> findPerson(PersonSearchDO personSearch) throws DatabaseNotFoundException;
}