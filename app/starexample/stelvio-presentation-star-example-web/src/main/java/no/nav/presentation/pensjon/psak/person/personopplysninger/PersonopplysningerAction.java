package no.nav.presentation.pensjon.psak.person.personopplysninger;

import org.springframework.webflow.action.MultiAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

//import no.nav.presentation.pensjon.saksbehandling.form.PersonopplysningerForm;
//import no.nav.presentation.common.action.PersonAction;

/**
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public class PersonopplysningerAction extends MultiAction {
	
	//private PersonAction personAction;
	
	/*
	public void setPersonAction(PersonAction personAction){
		this.personAction = personAction;
	}*/
	
	public Event setupForm(RequestContext context)
	{
		System.out.println("I PERSONOPPLYSNINGERFORM");
		
		return success();
		
	}
	
	//public PersonopplysningerForm setupForm(Person person){
	//public PersonopplysningerForm setupForm(String fnr){
	//public void setupForm(){
		//System.out.println("I PERSONOPPLYSNINGERFORM");
		//Lager og initialiserer formet
		//PersonopplysningerForm personopplysningerForm = new PersonopplysningerForm();
		
		//Henter inn verdiene i sprakList, 
		//utbetalTilListe, bankLandListe, valudtaListe, utenlandsadresselandListe fra kodeverket

		
//		Kall loadpersonopp med fnr og form-objektet som arg
		//loadPersonopplysninger(person, personopplysningerForm);
		
		//Returner formet når det er ferdig initialisert
		//return personopplysningerForm;
	//}
	
	//private void loadPersonopplysninger(Person person, PersonopplysningerForm f){
		//kaller PersonAction.getperson() med fnr som arg, og får tilbake et Person-objekt
		//Person person = personAction.getPerson(fnr);
		
		//Dataene i Person-objektet blir deretter kopiert over i personopplysningerForm-objektet
	//}
	
	public void updatePersonopplysninger(PersonopplysningerForm f){
		//Lager først et Person-objekt som informasjon i PersonopplysningerForm blir kopiert 
		//over i. 
		//Person person = new Person();
		
		
		//Deretter kalles PersonAction.updatePerson() med Person-objektet som arg
		//personAction.updatePerson(person);
	}
}