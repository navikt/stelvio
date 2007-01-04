/**
 * 
 */
package no.nav.presentation.pensjon.saksbehandling.demo;

import java.util.List;

import no.nav.presentation.pensjon.saksbehandling.stelvio.dao.PersonDO;
import no.nav.presentation.pensjon.saksbehandling.stelvio.dao.PersonSearchDO;
import no.nav.presentation.pensjon.saksbehandling.stelvio.service.PersonService;
import no.nav.presentation.pensjon.saksbehandling.stelvio.service.impl.PersonServiceMock;


/**
 * @author person4f9bc5bd17cc
 *
 */
public class ServiceTest {
	private enum PersonServiceTestCase {
		NAME,
		ADDRESS,
		DIFFERENT_ADDRESS,
		BIRTH_DATE,
		BIRTH_NUMBER,
		ACCOUNT_NUMBER_NO,
		ACCOUNT_NUMBER_FOREIGN,
		FOREIGN_REFERANCE
	}
	
	private enum PersonServiceCase {
		SEARCH_PERSON,
		READ_PERSON
	}
	
	public static void main(String[] args) throws Exception {
		testPersonService(PersonServiceCase.SEARCH_PERSON);
//		testPersonService(PersonServiceCase.READ_PERSON);
	}
	
	public static void testPersonService(PersonServiceCase personServiceCase) throws Exception {
		switch (personServiceCase) {
		// Search person
		case SEARCH_PERSON:
//			PersonServiceTestCase testCase = PersonServiceTestCase.NAME;
//			PersonServiceTestCase testCase = PersonServiceTestCase.ADDRESS;
//			PersonServiceTestCase testCase = PersonServiceTestCase.DIFFERENT_ADDRESS;
//			PersonServiceTestCase testCase = PersonServiceTestCase.BIRTH_DATE;
//			PersonServiceTestCase testCase = PersonServiceTestCase.BIRTH_NUMBER;
			PersonServiceTestCase testCase = PersonServiceTestCase.ACCOUNT_NUMBER_NO;
//			PersonServiceTestCase testCase = PersonServiceTestCase.ACCOUNT_NUMBER_FOREIGN;
//			PersonServiceTestCase testCase = PersonServiceTestCase.FOREIGN_REFERANCE;
			PersonService personService = new PersonServiceMock();
			PersonSearchDO personSearch = new PersonSearchDO();
			
			switch (testCase) {
			case NAME:
				System.out.println("\nSearching name...");
				personSearch.setNavneSok(true);
				personSearch.setFornavn("ola");
				personSearch.setEtternavn("nordmann");
//				personSearch.setBirthDate("01.08");
//				personSearch.setBirthYear(1981);
//				personSearch.setGender(Gender.Mann);
//				personSearch.setOrgUnit("1234");
				break;
			case ADDRESS:
				personSearch.setBostedsAdresseSok(true);
				personSearch.setAdresse("Frydenlund");
				personSearch.setNrFra("1");
				personSearch.setNrTil("1000");
				personSearch.setNavEnhet("");
				break;
			case DIFFERENT_ADDRESS:
				System.out.println("\nSearching different address...");
				
				personSearch.setAdresseType("Postadresse");
				personSearch.setAdresseHint("starter med");
				personSearch.setAdresse("Frydenlund");
//				personSearch.setOrgUnit("3456");
				break;
			case BIRTH_DATE:
				personSearch.setFodselsdatoSok(true);
				personSearch.setFullFodelsDato("01.08.1982");
				personSearch.setKjonn("Begge");
				personSearch.setNavEnhet("");
				break;
			case BIRTH_NUMBER:
				personSearch.setFnrSok(true);
				personSearch.setFnr("");
				break;
			case ACCOUNT_NUMBER_NO:
				personSearch.setNorskKontonummerSok(true);
				personSearch.setNorskKontonummer("12345");
				break;
			case ACCOUNT_NUMBER_FOREIGN:
				personSearch.setUtenlandskKontonummerSok(true);
				personSearch.setUtenlandskKontonummer("");
				break;
//			case FOREIGN_REFERANCE:
//				personSearch.setRadioForeignRef(true);
//				personSearch.setForeignReference("");
//				break;
			}
			
			List<PersonDO> searchResults = personService.findPerson(personSearch);
			System.out.println("Results:\n===================");
			for (PersonDO person : searchResults) {
				System.out.println(person.getFornavn() + " " + person.getEtternavn());
			}
			
			break;
		
		// Read person
		case READ_PERSON:
			break;
		}
	}
}