/**
 * 
 */
package no.nav.service.pensjon.person.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import no.nav.domain.pensjon.person.BostedsAdresse;
import no.nav.domain.pensjon.person.Fodselsnummer;
import no.nav.domain.pensjon.person.Person;
import no.nav.domain.pensjon.person.PersonSearch;
import no.nav.domain.pensjon.person.PersonUtland;
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
	private static ArrayList<Person> mockPersons;
	
	static {
		Fodselsnummer fnrOla = new Fodselsnummer("12345678901");
		ola.setFodselsnummer( fnrOla );
		ola.setFornavn( "Donald" );
		ola.setEtternavn( "Duck" );
		ola.setKortnavn( ola.getFornavn() + " " + ola.getEtternavn() );
		
		BostedsAdresse boAdrOla = new BostedsAdresse();
		boAdrOla.setBoadresse1("Apalveien");
		boAdrOla.setBoadresse2("");
		boAdrOla.setBolignr("15");
		boAdrOla.setPostnummer("0123");
		boAdrOla.setPoststed("Andeby");
		boAdrOla.setKommunenr("0123");
		boAdrOla.setNavEnhet("Oslo");
		ola.setBostedsAdresse(boAdrOla);
		
		PersonUtland persUtlandOla = new PersonUtland();
		persUtlandOla.setStatsborgerskap("NORGE");
		ola.setPersonUtland(persUtlandOla);		
		ola.setTilknyttetEnhet( "0909 NAV Harstad" );
		//ola.setBostedsAdresse( new BostedsAdresse("Apalveien", "", "111", "0123", "Andeby", "59", "0026 Oslo", "", "", "", "") );
		//ola.setPostAdresse( new AnnenAdresse("postadresse", "dette er en postadresse", "Postboks 9", "", "", "1234", "Oslo", "NO", "Norge", "", "", "Endreren Jensen", "Systemet Som Endrer", "12345678") );
		//ola.setPersonUtland( new PersonUtland(String sprakKode, String sprakBeskrivelse, String sprakDatoFom, "NORGE", String statsborgerskapFOM, String innvandretKode, String innvandretFra, String innvandretDato, String utvandretKode, String utvandretTil, String utvandretDato, String oppholdstillatelseType, String beskrOppholdType, String oppholdstillatelseFom, String oppholdstillatelseTom, String arbeidstillatelseType, String beskrArbeidstilType, String arbeidstillatelseFom, String arbeidstillatelseTom, String utlIdentType, String utlOppholdstillatelseType, String utlIdent, String utlIdentLandkode, String utlIdentLand, String utlIdFom, String utlIdTom));
		
		Fodselsnummer fnrOle = new Fodselsnummer("22222222222");
		ole.setFodselsnummer(fnrOle );
		ole.setFornavn( "Anton" );
		ole.setEtternavn( "Duck" );
		ole.setKortnavn( ole.getFornavn() + " " + ole.getEtternavn() );
		ole.setTilknyttetEnhet( "1234 NAV Hauketo" );
		ole.setDodsdato( Calendar.getInstance() );
		//ole.setBostedsAdresse( new BostedsAdresse("Flaksegata", "", "6", "0123", "Andeby", "59", "0026 Oslo", "", "", "", "") );
		//ole.setPostAdresse( new AnnenAdresse("postadresse", "dette er en postadresse", "Postboks 9", "", "", "1234", "Oslo", "NO", "Norge", "", "", "Endreren Jensen", "Systemet Som Endrer", "12345678") );
		
		Fodselsnummer fnrKirsten = new Fodselsnummer("33333333333");
		kirsten.setFodselsnummer(fnrKirsten );
		kirsten.setFornavn( "Dolly" );
		kirsten.setEtternavn( "Duck" );
		kirsten.setKortnavn( kirsten.getFornavn() + " " + kirsten.getEtternavn() );
		kirsten.setTilknyttetEnhet( "0202 NAV Prinsdal" );
		kirsten.setUmyndiggjortDato( Calendar.getInstance() );
		kirsten.setDiskresjonskode( "6" );
		//kirsten.setBostedsAdresse( new BostedsAdresse("Andelia", "", "3", "0123", "Andeby", "59", "0026 Oslo", "", "", "", "") );
		//kirsten.setPostAdresse( new AnnenAdresse("postadresse", "dette er en postadresse", "Postboks 9", "", "", "1234", "Oslo", "NO", "Norge", "", "", "Endreren Jensen", "Systemet Som Endrer", "12345678") );
		
		mockPersons = new ArrayList<Person>();
		mockPersons.add(ola);
		mockPersons.add(ole);
		mockPersons.add(kirsten);
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
		else {
			throw new PersonNotFoundException("Fant ingen personer med fødselsnummer "+fnr+".");
		}
		
		
		return person;
	}

	/**
	 * TODO: Document me
	 */
	public void updatePerson(Person person) throws DatabaseNotFoundException {
		if (person.getFodselsnummer().getFodselsnummer().equals("12345678901")) {
			ola = person;
		}
		else if (person.getFodselsnummer().getFodselsnummer().equals("22222222222")) {
			ole = person;
		}
		else if (person.getFodselsnummer().getFodselsnummer().equals("33333333333")) {
			kirsten = person;
		}
	}

	/**
	 * TODO: Document me
	 */
	public List<Person> findPerson(PersonSearch personSearch) throws DatabaseNotFoundException {
		System.out.println("Entering PersonServiceMock.findPerson()...");
		ArrayList<Person> persons = new ArrayList<Person>();
		
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
			if (isHit && personSearch.getFodselsDato().length() > 0) {
				isHit = person.getFodselsnummer().getFodselsnummer().equalsIgnoreCase(personSearch.getFodselsDato()) ? true : false;
			}
			if (isHit && personSearch.getFodselsAr() != null && personSearch.getFodselsAr().length() > 0) isHit = person.getFodselsnummer().getFodselsnummer().equals(new Integer(personSearch.getFodselsAr())) ? true : false;
			if (isHit) isHit = person.getFodselsnummer().getFodselsnummer().equalsIgnoreCase(personSearch.getKjonn()) ? true : personSearch.getKjonn().equalsIgnoreCase("Begge");
			//if (isHit && personSearch.getNavEnhet().length() > 0) isHit = person.getNavEnhet().equalsIgnoreCase(personSearch.getNavEnhet()) ? true : false;
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
					if (person.getBostedsAdresse().getBolignr() != null && person.getBostedsAdresse().getBolignr().length() > 0) {
						int gateNr = Integer.parseInt(person.getBostedsAdresse().getBolignr());
						isHit = (gateNr >= from) ?  (gateNr <= to) : false;
					}
				}
				catch (NumberFormatException e) { System.out.println("Street Address Number is not a number.\n"+e); }
			}
			if (isHit && personSearch.getAdresse().length() > 0) isHit = person.getBostedsAdresse().getBoadresse1().equalsIgnoreCase(personSearch.getAdresse()) ? true : false;
			if (isHit && personSearch.getFodselsDato().length() > 0) isHit = person.getFodselsnummer().getFodselsnummer().equalsIgnoreCase(personSearch.getFodselsDato()) ? true : false;
			if (isHit && personSearch.getFodselsAr() != null && personSearch.getFodselsAr().length() > 0) isHit = person.getFodselsnummer().getFodselsnummer().equals(new Integer(personSearch.getFodselsAr())) ? true : false;
			if (isHit) isHit = person.getFodselsnummer().getFodselsnummer().equalsIgnoreCase(personSearch.getKjonn()) ? true : personSearch.getKjonn().equalsIgnoreCase("Begge");
			//if (isHit && personSearch.getNavEnhet().length() > 0) isHit = person.getNavEnhet().equalsIgnoreCase(personSearch.getNavEnhet()) ? true : false;
		}
		// Annen adresse TODO: add address types conditions
		else if (personSearch.isAnnenAdresseSok()) {
			System.out.println("\nSearching different address...");
			if (personSearch.getAdresse() != null && personSearch.getAdresse().length() > 0) {
				//if (personSearch.getAddressType().equalsIgnoreCase("postadresse")) {
					if (personSearch.getAdresseHint().equalsIgnoreCase(""/*AddressHint.STARTER_MED.value()*/))
						isHit = person.getBostedsAdresse().getBoadresse1().toLowerCase().startsWith(personSearch.getAdresse().toLowerCase()) ? true : false;
					else if (personSearch.getAdresseHint().equalsIgnoreCase(""/*AddressHint.INNEHOLDER.value()*/))
						isHit = person.getBostedsAdresse().getBoadresse1().toLowerCase().contains(personSearch.getAdresse().toLowerCase()) ? true : false;
				//}
				//if (isHit && personSearch.getNavEnhet().length() > 0) isHit = person.getNavEnhet().equalsIgnoreCase(personSearch.getNavEnhet()) ? true : false;
			}
			else {
				isHit = false;
			}
		}
		// Fødselsdato
		else if (personSearch.isFodselsdatoSok()) {
			String fullDate = person.getFodselsnummer().getFodselsnummer();
			isHit = fullDate.equalsIgnoreCase(personSearch.getFullFodelsDato()) ? true : false;
			if (isHit) isHit = person.getFodselsnummer().getFodselsnummer().equalsIgnoreCase(personSearch.getKjonn()) ? true : personSearch.getKjonn().equalsIgnoreCase("Begge");
			//if (isHit && personSearch.getNavEnhet().length() > 0) isHit = person.getNavEnhet().equalsIgnoreCase(personSearch.getNavEnhet()) ? true : false;
		}
		// Fødselsnummer
		else if (personSearch.isFnrSok()) {
			isHit = person.getFodselsnummer().getFodselsnummer().equalsIgnoreCase(personSearch.getFnr()) ? true : false;
		}
		// Kontonummer norsk
		else if (personSearch.isNorskKontonummerSok()) {
			//isHit = person.getKontoNrNorsk().equalsIgnoreCase(personSearch.getNorskKontonummer()) ? true : false;
		}
		// Kontonummer utland
		else if (personSearch.isUtenlandskKontonummerSok()) {
			//isHit = person.getKontoNrUtenland().equalsIgnoreCase(personSearch.getUtenlandskKontonummer()) ? true : false;
		}
		
		return isHit;
	}
}