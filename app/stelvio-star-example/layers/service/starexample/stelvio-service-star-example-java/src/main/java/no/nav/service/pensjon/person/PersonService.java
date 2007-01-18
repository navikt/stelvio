/**
 * 
 */
package no.nav.service.pensjon.person;

import java.util.List;

import no.nav.domain.pensjon.person.Person;
import no.nav.domain.pensjon.person.PersonSearch;
import no.nav.service.pensjon.exception.DatabaseNotFoundException;
import no.nav.service.pensjon.exception.TPSException;
import no.nav.service.pensjon.person.exception.PersonNotFoundException;

/**
 * @author person4f9bc5bd17cc
 *
 */
public interface PersonService {
	
	public Person readPerson(String fnr) 
		throws TPSException, PersonNotFoundException;
	
	public void updatePerson(Person person) 
		throws DatabaseNotFoundException;
	
	public List<Person> findPerson(PersonSearch personSearch) 
		throws DatabaseNotFoundException;
	
}