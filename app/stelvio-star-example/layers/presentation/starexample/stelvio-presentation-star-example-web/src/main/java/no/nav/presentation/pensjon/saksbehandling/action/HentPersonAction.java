package no.nav.presentation.pensjon.saksbehandling.action;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;

import no.nav.presentation.pensjon.saksbehandling.form.HentPersonForm;
import no.nav.presentation.pensjon.saksbehandling.stelvio.dao.PersonDO;
import no.nav.presentation.pensjon.saksbehandling.stelvio.exceptions.DatabaseNotFoundException;
import no.nav.presentation.pensjon.saksbehandling.stelvio.exceptions.PersonNotFoundException;
import no.nav.presentation.pensjon.saksbehandling.stelvio.exceptions.TPSException;
import no.nav.presentation.pensjon.saksbehandling.stelvio.service.PersonService;

public class HentPersonAction {
	private PersonService personService;
	DataModel dataModel;
	
	
	/**
	 * 
	 * @param form
	 * @return
	 */
	public PersonDO hentPerson(HentPersonForm form )throws PersonNotFoundException
	{
		PersonDO person = null;
	
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
		catch (DatabaseNotFoundException e) {
			System.err.println(e);
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
