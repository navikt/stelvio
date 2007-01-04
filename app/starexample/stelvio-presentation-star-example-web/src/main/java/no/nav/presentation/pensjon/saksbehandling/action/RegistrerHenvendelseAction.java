package no.nav.presentation.pensjon.saksbehandling.action;




import no.nav.presentation.pensjon.saksbehandling.form.HenvendelseForm;
import no.nav.presentation.pensjon.saksbehandling.stelvio.dao.HenvendelserDO;
import no.nav.presentation.pensjon.saksbehandling.stelvio.dao.PersonDO;
import no.nav.presentation.pensjon.saksbehandling.stelvio.dao.SaksbehandlerDO;
import no.nav.presentation.pensjon.saksbehandling.stelvio.exceptions.DatabaseNotFoundException;
import no.nav.presentation.pensjon.saksbehandling.stelvio.exceptions.PersonNotFoundException;
import no.nav.presentation.pensjon.saksbehandling.stelvio.exceptions.TPSException;
import no.nav.presentation.pensjon.saksbehandling.stelvio.service.HenvendelseService;
import no.nav.presentation.pensjon.saksbehandling.stelvio.service.OrgEnhetService;
import no.nav.presentation.pensjon.saksbehandling.stelvio.service.PersonService;
import no.nav.presentation.pensjon.saksbehandling.util.PagedSortableList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.webflow.action.MultiAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 * 
 * @author personb66fa0b5ff6e
 *
 */
public class RegistrerHenvendelseAction extends MultiAction {

	HenvendelseService henvendelseService;
	OrgEnhetService orgEnhetService;
	PersonService personService;
	
	HenvendelseAction henvendelseAction;
	
	
	public void setHenvendelseAction(HenvendelseAction henvendelseAction) {
		this.henvendelseAction = henvendelseAction;
	}

	/**
	 * 
	 * @param henvendelse
	 * @param saksbehandler
	 * @return
	 */
	public HenvendelseForm setupForm( HenvendelserDO henvendelse, SaksbehandlerDO saksbehandler )
	{
		System.out.println( "* henvendelse = " + henvendelse );
		
		System.out.println( "* saksbehandler = " + saksbehandler );
		
		HenvendelseForm form = new HenvendelseForm();
		
		 //Fagområde - gyldige verdier fra K_FAGOMRADE
		form.setFagomrader(henvendelseService.getFagomrade());
		
		//Type stønad
		form.setStonadsTyper(henvendelseService.getStonadstype());
		
		//Type henvendelse - gyldige verdier fra K_HENVENDELSE_T
		form.setHenvendelsesTyper(henvendelseService.getHenvendelsesType());
		
		//Kanal - gyldige verdier fra K_KANAL_T
		form.setKanaler(henvendelseService.getKanal()); 
		
		System.out.println( "henvendelse = " + henvendelse );
		
		if( henvendelse == null )
		{
			return loadDefaultValues( form, saksbehandler );
		}
		else
		{
			return loadHenvendelse( form, henvendelse );
		}
	}
	
	private HenvendelseForm loadDefaultValues( HenvendelseForm form, SaksbehandlerDO saksbehandler )
	{
		form.setFagomrade( "Pensjon" );
		form.setKanal( "Telefon" );
		
		form.setRegAv(saksbehandler.getFornavn() + " " +saksbehandler.getEtternavn());
		form.setRegEnhet(saksbehandler.getEnhet());
		
		//Dagens dato
		Date regDate = new Date(System.currentTimeMillis());
		form.setRegDato(regDate); 
		
		form.setStatus(henvendelseService.getStatus());
		form.setTidsbruk(henvendelseService.getTidsbruk());
		
		form.setBeskrivelseLagret(Boolean.FALSE);
		
		System.out.println( "mitt form = " + form );
		return form;
	}
	
	private HenvendelseForm loadHenvendelse( HenvendelseForm form, HenvendelserDO henvendelse )
	{
		//PagedSortableList<HenvendelserDO> sortableList = (PagedSortableList)context.getFlowScope().get("henvendelser");
		//form = getHenvendelse((Long)context.getFlowScope().get("henvendelseId"), sortableList.getList());
		
		
		form.setBeskrivelseLagret(Boolean.TRUE);
		
		form.setHid( henvendelse.getId() );
		form.setBeskrivelse( henvendelse.getBeskrivelse() );
		form.setFagomrade( henvendelse.getFagomrode() );
		form.setHenvendelsesType( henvendelse.getType() );
		form.setStonadsType( henvendelse.getStonadstype() );
		form.setTidsbruk( henvendelse.getTidsbruk() );
		form.setStat( henvendelse.getStatus() );
		form.setNotat( henvendelse.getNotat() );
		
		form.setRegAv( henvendelse.getRegav() );
		form.setRegDato( henvendelse.getRegdato() );
		form.setRegEnhet( henvendelse.getRegenhetnr() );
		
		return form;
	}
	
	
	
	public void lagreHenvendelse( HenvendelseForm form )
	{
		System.out.println( "form = " + form );
		
		HenvendelserDO henvendelse = makeHenvendelseFromForm( form );
		
		if( henvendelse.getId() != null && henvendelse.getId() > 0 )
		{
			henvendelseAction.updateHenvendelse( henvendelse );
		}
		else
		{
			form.setHid(henvendelseAction.createHenvendelse( henvendelse ).getId());
			form.setBeskrivelseLagret(Boolean.TRUE);
		}
	}
	
	
	private HenvendelserDO makeHenvendelseFromForm( HenvendelseForm form )
	{
		HenvendelserDO henvendelse = new HenvendelserDO( 	form.getHid(), 
															form.getFagomrade(), 
															null, 
															null, 
															form.getRegAv(), 
															form.getRegDato(), 
															form.getRegEnhet(), 
															form.getStat(), 
															form.getNotat(), 
															form.getBeskrivelse(), 
															form.getKanal(), 
															form.getStonadsType(), 
															form.getTidsbruk(), 
															form.getOppfolging() );
		return henvendelse;
	}
	
	
	
	
	
	
	/*
	public Event setupForm(RequestContext context)
	{					
		HenvendelseForm hform = new HenvendelseForm();
						
		//Henvendelsesid er null - ny henvendelse skal opprettes
		if(context.getFlowScope().get("henvendelseId") == null){
			//Dagens dato
			Date regDate = new Date(System.currentTimeMillis());
			hform.setRegDato(regDate); 
			
			//Informasjon om pålogget saksbehandler
			SaksbehandlerDO saksbehandler = (SaksbehandlerDO)context.getExternalContext().getSessionMap().get("saksbehandlerDO");
			hform.setRegAv(saksbehandler.getFornavn() +" " +saksbehandler.getEtternavn()); 
			hform.setRegEnhet(saksbehandler.getEnhet()); 
			
			hform.setBeskrivelseLagret(Boolean.FALSE);
			hform.setKanal("Telefon"); 
			hform.setStatus(henvendelseService.getStatus()); //Status - gyldige verdier fra HENV_STATUS
			hform.setTidsbruk(henvendelseService.getTidsbruk());
		}
		else{
			PagedSortableList<HenvendelserDO> sortableList = (PagedSortableList)context.getFlowScope().get("henvendelser");
			hform = getHenvendelse((Long)context.getFlowScope().get("henvendelseId"), sortableList.getList());
			hform.setHid((Long)context.getFlowScope().get("henvendelseId"));
			hform.setBeskrivelseLagret(Boolean.TRUE);		
		}
			*/							
		//Person
		//hform.setFodselsnummer(context.getFlowScope().getRequiredString("fnr"));
		
		/*
		try{
			PersonDO person = personService.readPerson(hform.getFodselsnummer());
			hform.setNavn(person.getFornavn() + " " + person.getEtternavn());
		}
		catch(DatabaseNotFoundException err){
			System.out.println(err);
		}
		catch(PersonNotFoundException err){
			System.out.println(err);
		}
		catch(TPSException err){
			System.out.println(err);
		}	
		*/
		
		//Nedtrekkslistene
		/*hform.setFagomrader(henvendelseService.getFagomrade()); //Fagområde - gyldige verdier fra K_FAGOMRADE	
		hform.setStonadsTyper(henvendelseService.getStonadstype()); //Type stønad 	
		hform.setHenvendelsesTyper(henvendelseService.getHenvendelsesType()); //Type henvendelse - gyldige verdier fra K_HENVENDELSE_T
		hform.setKanaler(henvendelseService.getKanal()); //Kanal - gyldige verdier fra K_KANAL_T

		context.getFlowScope().put("henvendelseForm", hform);
						
		return success();
	}*/
		
	/**
	 * Henter en registrert henvendelse
	 * @param hid the identificator of an application
	 * @return
	 */
	/*
	public HenvendelseForm getHenvendelse(Long hid, List<HenvendelserDO> sortableList){
		
		HenvendelserDO h = henvendelseService.readHenvendelse(hid, sortableList);
		
		String regEnhet = "";
		
		try{
			regEnhet = orgEnhetService.getOrgEnhetNavn(h.getRegenhetnr());
		}
		catch(DatabaseNotFoundException err){	
			System.out.println(err);
		}
			
		HenvendelseForm form = new HenvendelseForm(h.getRegav(), regEnhet, h.getRegdato(), h.getFagomrode(), h.getStonadstype(), h.getType(), h.getKanal(), h.getTidsbruk(), h.getBeskrivelse(), h.getNotat(), h.getStatus());
				
		return form;
	}
	*/
	/**
	 *  TODO: Document me
	 * @param context
	 * @return
	 */
	/*
	public Event saveHenvendelse(RequestContext context){
			
		HenvendelseForm hf = (HenvendelseForm)context.getFlowScope().get("henvendelseForm");
		PagedSortableList<HenvendelserDO> sortableList = (PagedSortableList)context.getFlowScope().get("henvendelser");
		
		//Splitte opp enhetsnr og enhetsnavn - kun enhetsnr skal lagres
		//TODO: Sjekke opp formatet på enhetsnummeret
		String enhetsnr = hf.getRegEnhet().substring(0, 4);

		if(hf.getHid() != null){	
			//Hent ut allerede lagret informasjon om en henvendelse som ikke lagres i HenvendelseForm
			HenvendelserDO savedHenvendelseDO = new HenvendelserDO();
			
			for(HenvendelserDO hDO : sortableList.getList()){
				if(hDO.getId() == hf.getHid()){
					savedHenvendelseDO = hDO;
				}
			}

			HenvendelserDO henvendelserDO = new HenvendelserDO(hf.getHid(), hf.getFagomrade(), hf.getHenvendelsesType(), savedHenvendelseDO.getSaksnummer(), hf.getRegAv(), hf.getRegDato(), enhetsnr, savedHenvendelseDO.getStatus(), savedHenvendelseDO.getNotat(), hf.getBeskrivelse(), hf.getKanal(), hf.getStonadsType(), hf.getTidsbruk(), hf.getOppfolging());
			henvendelseService.updateHenvendelse(henvendelserDO);
			sortableList.setList(henvendelseService.updateHenvendelseList(henvendelserDO, sortableList.getList()));
		}
		else{
			//Når det skal være mulig å registrere saksnummer, status og notat i skjermbildet må disse sendes med til konstruktøren
			HenvendelserDO henvendelserDO = new HenvendelserDO(hf.getHid(), hf.getFagomrade(), hf.getHenvendelsesType(), null, hf.getRegAv(), hf.getRegDato(), enhetsnr, null, null, hf.getBeskrivelse(), hf.getKanal(), hf.getStonadsType(), hf.getTidsbruk(), hf.getOppfolging());
			hf.setHid(henvendelseService.createHenvendelse(henvendelserDO).getId());
			sortableList.setList(henvendelseService.addHenvendelseToList(henvendelserDO, sortableList.getList()));
			hf.setBeskrivelseLagret(Boolean.TRUE);
		}
		
		context.getFlowScope().put("sortableList", sortableList);
		
		return success();
	}*/
	
	/**
	 *  TODO: Document me
	 * @param f
	 * @return
	 */
	public Event createEPost(HenvendelseForm f){
		
		//Kalles når bruker angir at han ønsker å lage en e-post.
		//Metoden får inn HenvendelseForm som parameter og lager 
		//en webside med brødteksten som skal være med i e-posten 
		//ut i fra data i HenvendelseForm-objektet.
		//Den legger også teksten i utklippstavlen og åpner en tom
		//e-post i Outlook som teksten kan kopieres inn i.
		
		return success();
	}

	/**
	 *  TODO: Document me
	 * @param f
	 * @return
	 */
	public Event avtalMote(HenvendelseForm f){
		
		//Får inn HenvendelseForm som parameter og sørger for at et møte 
		//blir avtalt med en saksbehandler angående henvendelsen. Dette gjør 
		//den ved at det blir åpnet en avtale i Outlook med informasjon fra 
		//HenvendelseForm i avtalens tekstfelt.
		
		return success();
	}		
	
	/**
	 * @param henvendelsesService the henvendelsesService to set
	 */
	public void setHenvendelsesService(HenvendelseService henvendelsesService) {
		this.henvendelseService = henvendelsesService;
	}
	
	/**
	 * @param orgEnhetService the orgEnhetService to set
	 */
	public void setOrgEnhetService(OrgEnhetService orgEnhetService){
		this.orgEnhetService = orgEnhetService;
	}

	/**
	 * @param personService(PersonService personService
	 */
	public void setPersonService(PersonService personService){
		this.personService = personService;
	}
}
