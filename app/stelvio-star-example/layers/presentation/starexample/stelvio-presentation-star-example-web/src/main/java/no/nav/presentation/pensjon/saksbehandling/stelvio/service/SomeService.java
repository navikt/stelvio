package no.nav.presentation.pensjon.saksbehandling.stelvio.service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import no.nav.domain.pensjon.person.Fodselsnummer;
import no.nav.domain.pensjon.person.Person;
import no.nav.presentation.pensjon.saksbehandling.stelvio.dao.HenvendelserDO;


public class SomeService {

	
	public Person readPerson( String fnr )
	{
		Person person = new Person();
		
		person.setFodselsnummer(new Fodselsnummer(fnr));
		person.setFornavn( "Petter" );
		person.setEtternavn( "person7b6db0c03aa3" );
		
		return person;
	}
	
	public List hentHenvendelser( Person person )
	{
		return new ArrayList();
	}
	
	public List<HenvendelserDO> hentHenvendelser()
	{
		List<HenvendelserDO> henvendelser = new ArrayList<HenvendelserDO>();
		
		Date date = new Date(2009, 03, 24);		
		
		henvendelser.add(new HenvendelserDO("Bidrag", "Elektronisk", "1", "1264 Kirstiansand", date, "Behandlet", "Nei", "Vedtak fattet etter..."));
		henvendelser.add(new HenvendelserDO("Pensjon", "Telefon", "2", "1264 Kirstiansand", date, "Behandlet", "Nei", "Vedtak fattet etter..."));
		henvendelser.add(new HenvendelserDO("Syke", "Skjema", "3", "1264 Kirstiansand", date, "Behandlet", "Nei", "Vedtak fattet etter..."));
		henvendelser.add(new HenvendelserDO("AP", "Telefon", "4", "1264 Kirstiansand", date, "Behandlet", "Nei", "Vedtak fattet etter..."));
		henvendelser.add(new HenvendelserDO("Bidrag", "Elektronisk", "5", "1264 Kirstiansand", date, "Behandlet", "Nei", "Vedtak fattet etter..."));
		henvendelser.add(new HenvendelserDO("Pen", "Statisk", "6", "1264 Kirstiansand", date, "Behandlet", "Nei", "Vedtak fattet etter..."));
		henvendelser.add(new HenvendelserDO("Syk", "Dynamisk", "7", "1264 Kirstiansand", date, "Behandlet", "Nei", "Vedtak fattet etter..."));
		henvendelser.add(new HenvendelserDO("Pensjon", "Dyrisk", "8", "1264 Kirstiansand", date, "Behandlet", "Nei", "Vedtak fattet etter..."));
		henvendelser.add(new HenvendelserDO("KLF", "Laminarisk", "9", "1264 Kirstiansand", date, "Behandlet", "Nei", "Vedtak fattet etter..."));
		
		
		return henvendelser;
	}
	
	public ArrayList<SelectItem> getFagomrade(){
		 
		ArrayList<SelectItem> fagomrade = new ArrayList<SelectItem>();
		fagomrade.add(new SelectItem("Bidrag", "Bidrag"));
		fagomrade.add(new SelectItem("Pensjon", "Pensjon"));
		fagomrade.add(new SelectItem("Annet", "Annet"));
		
		return fagomrade;	
	}
	
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
	
	public Integer getTidsbruk(){
		Integer tidsbruk = 5;
		return tidsbruk;
	}
	
	public ArrayList<SelectItem> getStatus(){
		ArrayList<SelectItem> status = new ArrayList<SelectItem>();
		status.add(new SelectItem("Mottatt", "Mottatt"));
		status.add(new SelectItem("Under behandling", "Under behandling"));
		status.add(new SelectItem("Ferdig behandlet", "Ferdig behandlet"));
		
		return status;
	}
}