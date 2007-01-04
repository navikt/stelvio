/**
 * 
 */
package no.nav.presentation.pensjon.saksbehandling.stelvio.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import no.nav.domain.pensjon.henvendelse.Henvendelse;
import no.nav.domain.pensjon.henvendelse.NewHenvendelse;
import no.nav.domain.pensjon.person.Fodselsnummer;
import no.nav.presentation.pensjon.saksbehandling.stelvio.service.HenvendelseService;
import no.nav.presentation.pensjon.saksbehandling.util.PagedSortableList;


/**
 * @author person4f9bc5bd17cc
 * @author personb66fa0b5ff6e
 */
public class HenvendelsServiceMock implements HenvendelseService {
	private long henvendelseIndex = 0;
		
	/**
	 * Method to get a List of Henvendelse objects based on the given parameter.
	 * 
	 * @param fnr
	 * @return List
	 */
	public List<NewHenvendelse> readHenvendelseList(Fodselsnummer fodselsnummer) {
		this.henvendelseIndex = 0;
		
		List<NewHenvendelse> henvendelser = new ArrayList<NewHenvendelse>();
		
		String fnr = fodselsnummer.getFodselsnummer();
		
		Date date = new Date(106, 03, 24);	
		
		if (fnr.equals("12345678901")) {
			//TODO: riktig enehetsnr?
			
			NewHenvendelse h1 = new NewHenvendelse();
			h1.setFagomrKode( "Pensjon" );
			h1.setOpprettetDato( Calendar.getInstance() );
			h1.setOpprettetEnhet( "0129 Oslo" );
			h1.setSakId( "974816723" );
			h1.setHenvtype( "Regelverk" );
			/*
			 * TODO where to get "Henvendelse gjelder" ???
			 */
			
			henvendelser.add( h1 );
			
			NewHenvendelse h2 = new NewHenvendelse();
			h2.setFagomrKode( "Bidrag" );
			h2.setOpprettetDato( Calendar.getInstance() );
			h2.setOpprettetEnhet( "1357 Bergen" );
			h2.setSakId( "923665" );
			h2.setHenvtype( "Skjema" );
			
			henvendelser.add( h2 );
			
		}
		else if (fnr.equals("22222222222")) {
		}
		else if (fnr.equals("33333333333")) {
		}
		else if (fnr.equals("44444444444")) {
		}		
		
		System.out.println("Returning "+henvendelser.size()+" event(s) for fnr: "+fnr);
		
		return henvendelser;
	}
	
	/**
	 * TODO: Document me
	 */
	public List<Henvendelse> addHenvendelseToList(Henvendelse henvendelserDO, List<Henvendelse> sortableList){		
		sortableList.add(henvendelserDO);
		return sortableList;
	}
	
	/**
	 * TODO: Document me
	 */
	public List<Henvendelse> updateHenvendelseList(Henvendelse henvendelserDO, List<Henvendelse> sortableList){
		Integer index = 0;
		
		
		
		for(Henvendelse hDO : sortableList){
			if(hDO.getId() == henvendelserDO.getId()){
				break;
			}
			index++;
		}
				
		sortableList.set(index, henvendelserDO); 
		return sortableList;
	}

	/**
	 * Returns information about a specific application.
	 * @param henvendelseId the id of the desired application
	 * @return henvendelseDO
	 */
	public Henvendelse readHenvendelse(Long henvendelseId, List<Henvendelse> sortableList){
		Henvendelse henvendelseDO = new Henvendelse();
		
		for(Henvendelse hDO : sortableList){
			if(hDO.getId() == henvendelseId){
				henvendelseDO = hDO;
			}
		}
		
		return henvendelseDO;
	}
	
	/**
	 * DOCUMENT ME!
	 * 
	 * @param henvendelse
	 * @return Henvendelse
	 */
	private Henvendelse addId(Henvendelse henvendelse) {
		henvendelse.setId(henvendelseIndex++);
		return henvendelse;
	}
	
	private long addId(){
		return henvendelseIndex++;
	}

	/**
	 * TODO: Document me
	 */
	public Henvendelse createHenvendelse(Henvendelse henvendelse) {
		System.out.println("I createHenvendelse");

		henvendelse.setSaksnummer(new Long(henvendelseIndex+1).toString());
		henvendelse.setStatus("Mottatt");
		henvendelse.setNotat("Nei");
		return addId(henvendelse);	
	}

	/**
	 * TODO: Document me
	 */
	public void updateHenvendelse(Henvendelse henvendelse) {
		System.out.println("I updateHenvendelse");
	}
	
	/**
	 * TODO: Document me
	 */
	public ArrayList<SelectItem> getFagomrade(){
		 
		ArrayList<SelectItem> fagomrade = new ArrayList<SelectItem>();
		fagomrade.add(new SelectItem("Bidrag", "Bidrag"));
		fagomrade.add(new SelectItem("Pensjon", "Pensjon"));
		fagomrade.add(new SelectItem("Annet", "Annet"));
		
		return fagomrade;	
	}
	
	/**
	 * TODO: Document me
	 */
	public ArrayList<SelectItem> getHenvendelsesType(){
		
		ArrayList<SelectItem> henvendelsestype = new ArrayList<SelectItem>();
		henvendelsestype.add(new SelectItem("Veiledning - søkand/krav", "Veiledning - søkand/krav"));
		henvendelsestype.add(new SelectItem("Veiledning - pensjonsbeslutning", "Veiledning - pensjonsbeslutning"));
		henvendelsestype.add(new SelectItem("Veiledning - regelverk", "Veiledning - regelverk" ));
		henvendelsestype.add(new SelectItem("Forespørsel - skjemautfylling", "Forespørsel - skjemautfylling"));
		henvendelsestype.add(new SelectItem("Forespørsel - infomateriale", "Forespørsel - infomateriale"));
		henvendelsestype.add(new SelectItem("Annet", "Annet"));
		
		return henvendelsestype;
	}
	
	/**
	 * TODO: Document me
	 */
	public ArrayList<SelectItem> getStonadstype(){
		
		ArrayList<SelectItem> stonadstype = new ArrayList<SelectItem>();
		stonadstype.add(new SelectItem("Alderspensjon", "Alderspensjon"));
		stonadstype.add(new SelectItem("AFP", "AFP"));
		stonadstype.add(new SelectItem("Barnepensjon", "Barnepensjon"));
		stonadstype.add(new SelectItem("Familiepleier", "Familiepleier"));
		stonadstype.add(new SelectItem("Gjenlevende ektefelle", "Gjenlevende ektefelle"));
		stonadstype.add(new SelectItem("Krigspensjon", "Krisgspensjon"));
		stonadstype.add(new SelectItem("Omsorgspoeng", "Omsorgspoeng"));
		stonadstype.add(new SelectItem("Uførepensjon", "Uførepensjon"));
				
		return stonadstype;
	}
	
	/**
	 * TODO: Document me
	 */
	public ArrayList<SelectItem> getKanal(){
		
		ArrayList<SelectItem> kanal = new ArrayList<SelectItem>();

		kanal.add(new SelectItem("E-post", "E-post"));
		kanal.add(new SelectItem("Fremmøte", "Fremmøte"));
		kanal.add(new SelectItem("Selvbetjening", "Selvbetjening"));
		kanal.add(new SelectItem("Fax", "Fax"));
		kanal.add(new SelectItem("Brev", "Brev"));
		kanal.add(new SelectItem("Telefon", "Telefon"));
		
		return kanal;
	}
	
	/**
	 * TODO: Document me
	 */
	public String getTidsbruk(){
		String tidsbruk = "5";
		return tidsbruk;
	}
	
	/**
	 * TODO: Document me
	 */
	public ArrayList<SelectItem> getStatus(){
		ArrayList<SelectItem> status = new ArrayList<SelectItem>();
		status.add(new SelectItem("Mottatt", "Mottatt"));
		status.add(new SelectItem("Under behandling", "Under behandling"));
		status.add(new SelectItem("Ferdig behandlet", "Ferdig behandlet"));
		
		return status;
	}
}