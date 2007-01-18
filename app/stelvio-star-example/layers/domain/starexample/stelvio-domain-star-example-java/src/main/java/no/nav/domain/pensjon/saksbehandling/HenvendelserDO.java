package no.nav.domain.pensjon.saksbehandling;

import java.util.Date;

public class HenvendelserDO implements java.io.Serializable {

	private Long id;
	private String fagomrode;
	private String type;
	private String saksnummer;
	private String regav;
	private Date regdato;
	private String regenhetnr;
	private String status;
	private String notat;
	private String beskrivelse;
	private String kanal;
	private String stonadstype;
	private String tidsbruk;
	private String oppfolging;
	
	public HenvendelserDO(){		
	}
	
	public HenvendelserDO( String fagomrode, String type, String saksnummer, String regav, Date regdato, String status, String notat, String beskrivelse)
	{
		this.fagomrode = fagomrode;
		this.type = type;
		this.saksnummer = saksnummer;
		this.regav = regav;
		this.regdato = regdato;
		this.status = status;
		this.notat = notat;
		this.beskrivelse = beskrivelse;
	}
	
	public HenvendelserDO(Long id, String fagomrode, String type, String saksnummer, String regav, Date regdato, String regenhetnr, String status, String notat, String beskrivelse, String kanal, String stonadstype, String tidsbruk, String oppfolging) {
		super();
		this.id = id;
		this.fagomrode = fagomrode;
		this.type = type;
		this.saksnummer = saksnummer;
		this.regav = regav;
		this.regdato = regdato;
		this.regenhetnr = regenhetnr;
		this.status = status;
		this.notat = notat;
		this.beskrivelse = beskrivelse;
		this.kanal = kanal;
		this.stonadstype = stonadstype;
		this.tidsbruk = tidsbruk;
		this.oppfolging = oppfolging;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getBeskrivelse() {
		return beskrivelse;
	}
	public void setBeskrivelse(String beskrivelse) {
		this.beskrivelse = beskrivelse;
	}
	public String getFagomrode() {
		return fagomrode;
	}
	public void setFagomrode(String fagomrode) {
		this.fagomrode = fagomrode;
	}
	public String getNotat() {
		return notat;
	}
	public void setNotat(String notat) {
		this.notat = notat;
	}
	public String getRegav() {
		return regav;
	}
	public void setRegav(String regav) {
		this.regav = regav;
	}
	public Date getRegdato() {
		return regdato;
	}
	public void setRegdato(Date regdato) {
		this.regdato = regdato;
	}
	public String getSaksnummer() {
		return saksnummer;
	}
	public void setSaksnummer(String saksnummer) {
		this.saksnummer = saksnummer;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getKanal() {
		return kanal;
	}

	public void setKanal(String kanal) {
		this.kanal = kanal;
	}

	public String getRegenhetnr() {
		return regenhetnr;
	}

	public void setRegenhetnr(String regenhetnr) {
		this.regenhetnr = regenhetnr;
	}

	public String getStonadstype() {
		return stonadstype;
	}

	public void setStonadstype(String stonadstype) {
		this.stonadstype = stonadstype;
	}

	public String getTidsbruk() {
		return tidsbruk;
	}

	public void setTidsbruk(String tidsbruk) {
		this.tidsbruk = tidsbruk;
	}

	public String getOppfolging() {
		return oppfolging;
	}

	public void setOppfolging(String oppfolging) {
		this.oppfolging = oppfolging;
	}	
}