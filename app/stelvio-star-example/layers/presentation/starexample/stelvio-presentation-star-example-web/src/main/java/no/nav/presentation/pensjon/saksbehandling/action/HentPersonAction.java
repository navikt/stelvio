package no.nav.presentation.pensjon.saksbehandling.action;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;

import no.nav.domain.pensjon.person.Person;
import no.nav.presentation.pensjon.saksbehandling.form.HentPersonForm;
import no.nav.service.pensjon.exception.DatabaseNotFoundException;
import no.nav.service.pensjon.exception.TPSException;
import no.nav.service.pensjon.person.PersonService;
import no.nav.service.pensjon.person.exception.PersonNotFoundException;


public class HentPersonAction {
	private PersonService personService;
	DataModel dataModel;
	
	
	/**
	 * 
	 * @param form
	 * @return
	 */
	public Person hentPerson(HentPersonForm form )throws PersonNotFoundException
	{
		Person person = null;
	
		try {
			person = personService.readPerson(form.getFodselsnummer());
		}
		catch (TPSException e) {
			System.err.println(e);
		}
		catch (PersonNotFoundException e) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			FacesMessage msg = new FacesMessage( "Fant ingen person med fødselsnummer " + form.getFodselsnummer(), "nei da igjen" );
			facesContext.addMessage(null, msg);
			throw e;
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
