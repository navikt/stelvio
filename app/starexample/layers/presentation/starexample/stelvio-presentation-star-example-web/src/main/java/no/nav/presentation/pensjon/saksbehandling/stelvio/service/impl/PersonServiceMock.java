/**
 * 
 */
package no.nav.presentation.pensjon.saksbehandling.stelvio.service.impl;

import java.util.ArrayList;
import java.util.List;

import no.nav.presentation.pensjon.saksbehandling.stelvio.dao.PersonDO;
import no.nav.presentation.pensjon.saksbehandling.stelvio.dao.PersonSearchDO;
import no.nav.presentation.pensjon.saksbehandling.stelvio.enums.AddressHint;
import no.nav.presentation.pensjon.saksbehandling.stelvio.exceptions.DatabaseNotFoundException;
import no.nav.presentation.pensjon.saksbehandling.stelvio.exceptions.PersonNotFoundException;
import no.nav.presentation.pensjon.saksbehandling.stelvio.exceptions.TPSException;
import no.nav.presentation.pensjon.saksbehandling.stelvio.service.PersonService;

import org.apache.commons.lang.StringUtils;


/**
 * @author person4f9bc5bd17cc
 */
public class PersonServiceMock implements PersonService {
	private static PersonDO ola = new PersonDO();
	private static PersonDO ole = new PersonDO();
	private static PersonDO kirsten = new PersonDO();
	private static PersonDO per = new PersonDO();
	private static ArrayList<PersonDO> mockPersons;
	
	static {
		ola.setFodselsNr("12345678901");
		ola.setEnhet("0026 Oslo");
		ola.setKommentar("Donald Duck er en uheldig and og er stort sett misunnelig på sin fetter Anton.");
		ola.setFornavn("Donald");
		ola.setEtternavn("Duck");
		ola.setKjonn("Mann");
		ola.setFodselsDato("19.06");
		ola.setFodselsAar(1922);
		ola.setNavEnhet("0026 Oslo");
		ola.setGateAdresse("Apalveien");
		ola.setGateNr("111");
		ola.setKontoNrNorsk("12345678901");
		ola.setKontoNrUtenland("54321");
		ola.setUtenlandsRef("11");
		ola.setPostNr("0123");
		ola.setPostSted("Andeby");
		
		ole.setFodselsNr("22222222222");
		ole.setEnhet("4005 person21eaa6ad9a5aanger");
		ole.setKommentar("Anton er, imotsetning til Donald, verdens heldigste and.");
		ole.setFornavn("Anton");
		ole.setEtternavn("Duck");
		ole.setKjonn("Mann");
		ole.setFodselsDato("10.03");
		ole.setFodselsAar(1925);
		ole.setNavEnhet("4005 person21eaa6ad9a5aanger");
		ole.setGateAdresse("Flaksegata");
		ole.setGateNr("6");
		ole.setKontoNrNorsk("22222222222");
		ole.setKontoNrUtenland("98765");
		ole.setUtenlandsRef("22");
		ole.setPostNr("0123");
		ole.setPostSted("Andeby");
		
		kirsten.setFodselsNr("33333333333");
		kirsten.setEnhet("4604 Kristiansand");
		kirsten.setKommentar("Dolly er en klassisk \"high-maintainance\" frue-and som til stadighet står ovenfor krangler mellom Anton og Donald for å vinne hennes gunst.");
		kirsten.setFornavn("Dolly");
		kirsten.setEtternavn("Duck");
		kirsten.setKjonn("Kvinne");
		kirsten.setFodselsDato("25.11");
		kirsten.setFodselsAar(1923);
		kirsten.setNavEnhet("4604 Kristiansand");
		kirsten.setGateAdresse("Andelia");
		kirsten.setGateNr("3");
		kirsten.setKontoNrNorsk("33333333333");
		kirsten.setKontoNrUtenland("09876");
		kirsten.setUtenlandsRef("33");
		kirsten.setPostNr("0123");
		kirsten.setPostSted("Andeby");
		
		per.setFodselsNr("44444444444");
		per.setEnhet("4604 Kristiansand");
		per.setKommentar("Gråstein er Skrue McDuck's erkefiende.");
		per.setFornavn("Gulbrand");
		per.setEtternavn("Gråstein");
		per.setKjonn("Mann");
		per.setFodselsDato("17.05");
		per.setFodselsAar(1870);
		per.setNavEnhet("4604 Kristiansand");
		per.setGateAdresse("Frydenlund");
		per.setGateNr("108");
		per.setKontoNrNorsk("44444444444");
		per.setKontoNrUtenland("65432");
		per.setUtenlandsRef("44");
		per.setPostNr("1234");
		per.setPostSted("Gåseby");
		
		mockPersons = new ArrayList<PersonDO>();
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
	public PersonDO readPerson(String fnr) throws TPSException, PersonNotFoundException, DatabaseNotFoundException {
		System.out.println("Entering PersonServiceMock.readPerson()...");
		PersonDO person = new PersonDO();
		
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
	public void updatePerson(PersonDO person) throws DatabaseNotFoundException {
		if (person.getFodselsNr().equals("12345678901")) {
			ola = person;
		}
		else if (person.getFodselsNr().equals("22222222222")) {
			ole = person;
		}
		else if (person.getFodselsNr().equals("33333333333")) {
			kirsten = person;
		}
		else if (person.getFodselsNr().equals("44444444444")) {
			per = person;
		}
	}

	/**
	 * TODO: Document me
	 */
	public List<PersonDO> findPerson(PersonSearchDO personSearch) throws DatabaseNotFoundException {
		System.out.println("Entering PersonServiceMock.findPerson()...");
		ArrayList<PersonDO> persons = new ArrayList<PersonDO>();
		
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
		
		for (PersonDO mockPerson : mockPersons) {
			if (isSearchHit(personSearch, mockPerson)) {
				persons.add(mockPerson);
			}
		}
		
		System.out.println("Searched "+mockPersons.size()+" person(s) and returned "+persons.size()+" match(es)...\n");
		
		return persons;
	}
	
	public boolean isSearchHit(PersonSearchDO personSearch, PersonDO person) {
		boolean isHit = false;
		
		// Navn
		if (personSearch.isNavneSok()) {
			System.out.println("\nSearching name...");
			
			isHit = personSearch.getFornavn().equalsIgnoreCase(person.getFornavn()) && StringUtils.isEmpty(personSearch.getEtternavn());
			if (!isHit) isHit = personSearch.getEtternavn().equalsIgnoreCase(person.getEtternavn()) && StringUtils.isEmpty(personSearch.getFornavn());
			if (!isHit) isHit = personSearch.getFornavn().equalsIgnoreCase(person.getFornavn()) && personSearch.getEtternavn().equalsIgnoreCase(person.getEtternavn());
			if (isHit && personSearch.getFodselsDato().length() > 0) {
				isHit = person.getFodselsDato().equalsIgnoreCase(personSearch.getFodselsDato()) ? true : false;
			}
			if (isHit && personSearch.getFodselsAr() != null && personSearch.getFodselsAr().length() > 0) isHit = person.getFodselsAar().equals(new Integer(personSearch.getFodselsAr())) ? true : false;
			if (isHit) isHit = person.getKjonn().equalsIgnoreCase(personSearch.getKjonn()) ? true : personSearch.getKjonn().equalsIgnoreCase("Begge");
			if (isHit && personSearch.getNavEnhet().length() > 0) isHit = person.getNavEnhet().equalsIgnoreCase(personSearch.getNavEnhet()) ? true : false;
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
					if (person.getGateNr() != null && person.getGateNr().length() > 0) {
						int gateNr = Integer.parseInt(person.getGateNr());
						isHit = (gateNr >= from) ?  (gateNr <= to) : false;
					}
				}
				catch (NumberFormatException e) { System.out.println("Street Address Number is not a number.\n"+e); }
			}
			if (isHit && personSearch.getAdresse().length() > 0) isHit = person.getGateAdresse().equalsIgnoreCase(personSearch.getAdresse()) ? true : false;
			if (isHit && personSearch.getFodselsDato().length() > 0) isHit = person.getFodselsDato().equalsIgnoreCase(personSearch.getFodselsDato()) ? true : false;
			if (isHit && personSearch.getFodselsAr() != null && personSearch.getFodselsAr().length() > 0) isHit = person.getFodselsAar().equals(new Integer(personSearch.getFodselsAr())) ? true : false;
			if (isHit) isHit = person.getKjonn().equalsIgnoreCase(personSearch.getKjonn()) ? true : personSearch.getKjonn().equalsIgnoreCase("Begge");
			if (isHit && personSearch.getNavEnhet().length() > 0) isHit = person.getNavEnhet().equalsIgnoreCase(personSearch.getNavEnhet()) ? true : false;
		}
		// Annen adresse TODO: add address types conditions
		else if (personSearch.isAnnenAdresseSok()) {
			System.out.println("\nSearching different address...");
			if (personSearch.getAdresse() != null && personSearch.getAdresse().length() > 0) {
				//if (personSearch.getAddressType().equalsIgnoreCase("postadresse")) {
					if (personSearch.getAdresseHint().equalsIgnoreCase(AddressHint.STARTER_MED.value()))
						isHit = person.getGateAdresse().toLowerCase().startsWith(personSearch.getAdresse().toLowerCase()) ? true : false;
					else if (personSearch.getAdresseHint().equalsIgnoreCase(AddressHint.INNEHOLDER.value()))
						isHit = person.getGateAdresse().toLowerCase().contains(personSearch.getAdresse().toLowerCase()) ? true : false;
				//}
				if (isHit && personSearch.getNavEnhet().length() > 0) isHit = person.getNavEnhet().equalsIgnoreCase(personSearch.getNavEnhet()) ? true : false;
			}
			else {
				isHit = false;
			}
		}
		// Fødselsdato
		else if (personSearch.isFodselsdatoSok()) {
			System.out.println("\nSearching birthdate...");
			String fullDate = person.getFodselsDato()+"."+person.getFodselsAar();
			isHit = fullDate.equalsIgnoreCase(personSearch.getFullFodelsDato()) ? true : false;
			if (isHit) isHit = person.getKjonn().equalsIgnoreCase(personSearch.getKjonn()) ? true : personSearch.getKjonn().equalsIgnoreCase("Begge");
			if (isHit && personSearch.getNavEnhet().length() > 0) isHit = person.getNavEnhet().equalsIgnoreCase(personSearch.getNavEnhet()) ? true : false;
		}
		// Fødselsnummer
		else if (personSearch.isFnrSok()) {
			System.out.println("\nSearching birthnumber...");
			isHit = person.getFodselsNr().equalsIgnoreCase(personSearch.getFnr()) ? true : false;
		}
		// Kontonummer norsk
		else if (personSearch.isNorskKontonummerSok()) {
			System.out.println("\nSearching norwegian account nummber...");
			System.out.println(person.getKontoNrNorsk()+" - "+personSearch.getNorskKontonummer());
			isHit = person.getKontoNrNorsk().equalsIgnoreCase(personSearch.getNorskKontonummer()) ? true : false;
		}
		// Kontonummer utland
		else if (personSearch.isUtenlandskKontonummerSok()) {
			System.out.println("\nSearching foreign account number...");
			isHit = person.getKontoNrUtenland().equalsIgnoreCase(personSearch.getUtenlandskKontonummer()) ? true : false;
		}
		
		return isHit;
	}
}