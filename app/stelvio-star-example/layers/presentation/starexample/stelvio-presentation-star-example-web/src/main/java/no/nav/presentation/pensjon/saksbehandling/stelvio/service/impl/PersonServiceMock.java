/**
 * 
 */
package no.nav.presentation.pensjon.saksbehandling.stelvio.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import no.nav.domain.pensjon.person.Fodselsnummer;
import no.nav.domain.pensjon.person.Person;
import no.nav.domain.pensjon.person.PersonSearch;
import no.nav.service.pensjon.exception.DatabaseNotFoundException;
import no.nav.service.pensjon.exception.TPSException;
import no.nav.service.pensjon.person.PersonService;
import no.nav.service.pensjon.person.exception.PersonNotFoundException;


/**
 * @author person4f9bc5bd17cc
 */
public class PersonServiceMock implements PersonService {
	private static Person ola = new Person();
	private static Person ole = new Person();
	private static Person kirsten = new Person();
	private static Person per = new Person();
	private static List<Person> mockPersons;
	
	static {
		ola.setFodselsnummer(new Fodselsnummer("12345678901"));
		ola.setFornavn("Donald");
		ola.setEtternavn("Duck");
		
		ole.setFodselsnummer(new Fodselsnummer("22222222222"));
		ole.setFornavn("Anton");
		ole.setEtternavn("Duck");
		
		kirsten.setFodselsnummer(new Fodselsnummer("33333333333"));
		kirsten.setFornavn("Dolly");
		kirsten.setEtternavn("Duck");
		
		per.setFodselsnummer(new Fodselsnummer("44444444444"));
		per.setFornavn("Gulbrand");
		per.setEtternavn("Gråstein");
		
		mockPersons = new ArrayList<Person>();
		mockPersons.add(ola);
		mockPersons.add(ole);
		mockPersons.add(kirsten);
		mockPersons.add(per);
	}
	
	/**
	 * Method to get a Person object based on the given argument.
	 * 
	 * @param fnr
	 * @return Person
	 * @throws TPSException
	 */
	public Person readPerson(String fnr) throws TPSException, PersonNotFoundException {
		System.out.println("Entering PersonServiceMock.readPerson()...");
		Person person = new Person();
		
		if (fnr.equals("12345678901")) {
			person = ola;
		}
		else if (fnr.equals("22222222222")) {
			person = ole;
		}
		else if (fnr.equals("33333333333")) {
			person = kirsten;
		}
		else if (fnr.equals("44444444444")) {
			person = per;
		}
		else {
			throw new PersonNotFoundException("Fant ingen personer med fødselsnummer "+fnr+".");
		}
		
		
		return person;
	}

	/**
	 * TODO: Document me
	 */
	public void updatePerson(Person person) throws DatabaseNotFoundException {
		if (person.getFodselsnummer().equals("12345678901")) {
			ola = person;
		}
		else if (person.getFodselsnummer().equals("22222222222")) {
			ole = person;
		}
		else if (person.getFodselsnummer().equals("33333333333")) {
			kirsten = person;
		}
		else if (person.getFodselsnummer().equals("44444444444")) {
			per = person;
		}
	}

	/**
	 * TODO: Document me
	 */
	public List<Person> findPerson(PersonSearch personSearch) throws DatabaseNotFoundException {
		System.out.println("Entering PersonServiceMock.findPerson()...");
		List<Person> persons = new ArrayList<Person>();
		
		// Return all persons on "*"-searches
		if (personSearch.isNavneSok() &&
				(!StringUtils.isEmpty(personSearch.getFornavn()) && !StringUtils.isEmpty(personSearch.getEtternavn())) &&
				(personSearch.getFornavn().equals("*") && personSearch.getEtternavn().equals("*")) ||
				(personSearch.getFornavn().equals("*") && StringUtils.isEmpty(personSearch.getEtternavn())||
				(personSearch.getEtternavn().equals("*") && StringUtils.isEmpty(personSearch.getFornavn())))) {
			System.out.println("Search: "+personSearch.getFornavn()+" "+personSearch.getEtternavn());
			System.out.println("Returning all mock persons.");
			return mockPersons;
		}
		
		for (Person mockPerson : mockPersons) {
			if (isSearchHit(personSearch, mockPerson)) {
				persons.add(mockPerson);
			}
		}
		
		System.out.println("Searched "+mockPersons.size()+" person(s) and returned "+persons.size()+" match(es)...\n");
		
		return persons;
	}
	
	public boolean isSearchHit(PersonSearch personSearch, Person person) {
		boolean isHit = false;
		
		// Navn
		if (personSearch.isNavneSok()) {
			System.out.println("\nSearching name...");
			
			isHit = personSearch.getFornavn().equalsIgnoreCase(person.getFornavn()) && StringUtils.isEmpty(personSearch.getEtternavn());
			if (!isHit) isHit = personSearch.getEtternavn().equalsIgnoreCase(person.getEtternavn()) && StringUtils.isEmpty(personSearch.getFornavn());
			if (!isHit) isHit = personSearch.getFornavn().equalsIgnoreCase(person.getFornavn()) && personSearch.getEtternavn().equalsIgnoreCase(person.getEtternavn());
		}
		// Adresse
		else if (personSearch.isBostedsAdresseSok()) {
			System.out.println("\nSearching address...");
			if (personSearch.getNrFra() != null && personSearch.getNrFra().length() > 0) {
				if (personSearch.getNrTil() == null || personSearch.getNrTil().length() <= 0) {
					personSearch.setNrTil(personSearch.getNrFra());
				}
				
				try {
					int from = Integer.parseInt(personSearch.getNrFra());
					int to = Integer.parseInt(personSearch.getNrTil());
				}
				catch (NumberFormatException e) { System.out.println("Street Address Number is not a number.\n"+e); }
			}
		}
		// Annen adresse TODO: add address types conditions
		else if (personSearch.isAnnenAdresseSok()) {
			System.out.println("\nSearching different address...");
			if (personSearch.getAdresse() != null && personSearch.getAdresse().length() > 0) {
				//if (personSearch.getAddressType().equalsIgnoreCase("postadresse")) {
				//}
			}
			else {
				isHit = false;
			}
		}
		// Fødselsdato
		else if (personSearch.isFodselsdatoSok()) {
			System.out.println("\nSearching birthdate...");
		}
		// Fødselsnummer
		else if (personSearch.isFnrSok()) {
			System.out.println("\nSearching birthnumber...");
			isHit = person.getFodselsnummer().getFodselsnummer().equalsIgnoreCase(personSearch.getFnr()) ? true : false;
		}
		// Kontonummer norsk
		else if (personSearch.isNorskKontonummerSok()) {
			System.out.println("\nSearching norwegian account nummber...");
		}
		// Kontonummer utland
		else if (personSearch.isUtenlandskKontonummerSok()) {
			System.out.println("\nSearching foreign account number...");
		}
		
		return isHit;
	}
}