/**
 * 
 */
package no.nav.presentation.pensjon.saksbehandling.action;

import no.nav.domain.pensjon.person.Person;
import no.nav.domain.pensjon.person.PersonSearch;
import no.nav.presentation.pensjon.saksbehandling.form.SokePersonForm;
import no.nav.presentation.pensjon.saksbehandling.stelvio.enums.SokePersonOption;
import no.nav.presentation.pensjon.saksbehandling.util.PagedSortableList;
import no.nav.service.pensjon.exception.DatabaseNotFoundException;
import no.nav.service.pensjon.person.PersonService;

/**
 * @author person4f9bc5bd17cc
 *
 */
public class SokePersonAction {
	private PersonService personService;
	
	/**
	 * TODO: Document me
	 * 
	 * @param form
	 * @return
	 * @throws DatabaseNotFoundException
	 */
	public PagedSortableList<Person> sokPerson(SokePersonForm form) throws DatabaseNotFoundException
	{
		PersonSearch personSearch = genereateSearchObject(form);
		
		try {
			return new PagedSortableList<Person>(personService.findPerson(personSearch), 5, "kortnavn");
		}
		catch (DatabaseNotFoundException e) {
			throw e;
			// TODO: Add error-handling 
		}
	}
	
	/**
	 * TODO: Document me
	 * @param form
	 * @return
	 */
	public void resetForm(SokePersonForm form) {
		form.reset();
	}
	
	/**
	 * TODO: Document me
	 * @param personService the personService to set
	 */
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
	
	/**
	 * TODO: Document me
	 * 
	 * @param form
	 * @return
	 */
	private PersonSearch genereateSearchObject(SokePersonForm form) {
		System.out.println("Entering SokePersonAction.generateSearchObject()...");
		SokePersonOption selectedOption = getSelectedOption(form.getValgtSok());
		PersonSearch personSearch = new PersonSearch();

		switch (selectedOption) {
		case ADDRESS:
			personSearch.setAdresse(form.getBostedsadresse());
			personSearch.setNrFra(form.getNrFra());
			personSearch.setNrTil(form.getNrTil());
			personSearch.setFodselsDato(form.getFodselsDato());
			personSearch.setFodselsAr(form.getFodselsAr());
			personSearch.setKjonn(form.getKjonn1());
			personSearch.setNavEnhet(form.getNavEnhet1());
			personSearch.setBostedsAdresseSok(true);
			break;
		case DIFFERENT_ADDRESS:
			personSearch.setAdresse(form.getAnnenAdresse());
			personSearch.setAdresseType(form.getAdresseType());
			personSearch.setAdresseHint(form.getAdresseHint());
			personSearch.setNavEnhet(form.getNavEnhet2());
			personSearch.setAnnenAdresseSok(true);
			break;
		case NAME:
			personSearch.setFornavn(form.getFornavn());
			personSearch.setEtternavn(form.getEtternavn());
			personSearch.setFodselsDato(form.getFodselsDato());
			personSearch.setFodselsAr(form.getFodselsAr());
			personSearch.setKjonn(form.getKjonn1());
			personSearch.setNavEnhet(form.getNavEnhet1());
			personSearch.setNavneSok(true);
			break;
		case BIRTHDATE:
			personSearch.setFullFodelsDato(form.getFullFodselsDato());
			personSearch.setKjonn(form.getKjonn2());
			personSearch.setNavEnhet(form.getNavEnhet3());
			personSearch.setFodselsdatoSok(true);
			break;
		case ACCOUNT_NO_NORWEGIAN:
			personSearch.setNorskKontonummer(form.getNorskKontonummer());
			personSearch.setNorskKontonummerSok(true);
			break;
		case ACCOUNT_NO_FOREIGN:
			personSearch.setUtenlandskKontonummer(form.getUtenlandskKontonummer());
			personSearch.setUtenlandskKontonummerSok(true);
			break;
		case BIRTHNUMBER:
			personSearch.setFnr(form.getFnr());
			personSearch.setFnrSok(true);
			break;
		}
		
		return personSearch;
	}
	
	/**
	 * TODO: Document me
	 * 
	 * @param selectedOption
	 * @return SokePersonOption
	 */
	private SokePersonOption getSelectedOption(String selectedOption) {
		System.out.println("Entering SokePersonAction.getSelectedOption()...");
		for (SokePersonOption option : SokePersonOption.values()) {
			if (option.value().equalsIgnoreCase(selectedOption)) {
				return option;
			}
		}
		
		return SokePersonOption.NAME;
	}
}