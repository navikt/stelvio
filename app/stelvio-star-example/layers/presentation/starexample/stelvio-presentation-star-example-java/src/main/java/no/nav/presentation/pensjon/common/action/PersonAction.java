package no.nav.presentation.pensjon.common.action;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;

import no.nav.domain.pensjon.person.Fodselsnummer;
import no.nav.domain.pensjon.person.Person;
import no.nav.service.pensjon.exception.TPSException;
import no.nav.service.pensjon.person.PersonService;
import no.nav.service.pensjon.person.exception.PersonNotFoundException;

public class PersonAction {
	private PersonService personService;
	DataModel dataModel;
	
	
	/**
	 * 
	 * @param form
	 * @return
	 */
	public Person hentPerson(Fodselsnummer fodselsnummer )
		throws PersonNotFoundException, TPSException
	{
		Person person = null;
		try {
			person = personService.readPerson(fodselsnummer.getFodselsnummer());
		}
		catch (TPSException e) {
			throw e;
		}
		catch (PersonNotFoundException pnfe) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			FacesMessage msg = new FacesMessage( "Fant ingen person med fødselsnummer " + fodselsnummer.getFodselsnummer(), "nei da igjen" );
			facesContext.addMessage(null, msg);
			throw pnfe;
		}
		return person;
	}


	/**
	 * @param personService the personService to set
	 */
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	
	
	
	
	
}
