/**
 * 
 */
package no.nav.presentation.pensjon.saksbehandling.action;

import no.nav.presentation.pensjon.saksbehandling.form.SokePersonForm;
import no.nav.presentation.pensjon.saksbehandling.stelvio.dao.PersonDO;
import no.nav.presentation.pensjon.saksbehandling.stelvio.dao.PersonSearchDO;
import no.nav.presentation.pensjon.saksbehandling.stelvio.enums.SokePersonOption;
import no.nav.presentation.pensjon.saksbehandling.stelvio.exceptions.DatabaseNotFoundException;
import no.nav.presentation.pensjon.saksbehandling.stelvio.service.PersonService;
import no.nav.presentation.pensjon.saksbehandling.util.PagedSortableList;

/**
 * @author person4f9bc5bd17cc
 *
 */
public class SokePersonAction {
	private PersonService personService;
	
	/**
	 * TODO: Document me
	 * 
	 * @param context
	 * @return
	 */
	public PagedSortableList<PersonDO> sokPerson(SokePersonForm form) throws DatabaseNotFoundException
	{
		PersonSearchDO personSearch = genereateSearchObject(form);
		
		PagedSortableList<PersonDO> results = null;
		
		try {
			results = new PagedSortableList<PersonDO>(	personService.findPerson(personSearch), 
														"kortnavn", 
														"sokPersonResultsForm", 
														"minTabell");
			
		}
		catch (DatabaseNotFoundException e) {
			throw e;
			// TODO: Add error-handling 
		}
		
		return results;
	}
	
	/**
	 * TODO: Document me
	 * @param context
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
	private PersonSearchDO genereateSearchObject(SokePersonForm form) {
		System.out.println("Entering SokePersonAction.generateSearchObject()...");
		SokePersonOption selectedOption = getSelectedOption(form.getValgtSok());
		PersonSearchDO personSearch = new PersonSearchDO();

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