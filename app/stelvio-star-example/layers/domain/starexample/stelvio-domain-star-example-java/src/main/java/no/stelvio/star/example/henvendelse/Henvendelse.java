package no.stelvio.star.example.henvendelse;

import java.io.Serializable;
import java.util.Calendar;

import no.stelvio.domain.person.Pid;

public class Henvendelse implements Serializable  {
	
	private Pid fodselsnummer;
	
    private String beskrivelse;
    private String enhetstype;
    private String fagNokkel;
    private String fagomrKode;
    private String henvendelseId;
    private String henvendelseStatus;
    private String henvKanalKode;
    private String henvtype;
    private String kanaltype;
    private String oppfolging;
    private NewOppgave[] oppgaver;
    private String opprettetAv;
    private Calendar opprettetDato;
    private String opprettetEnhet;
    private NewPerson person;
    private String sakId;
    private String sakKatKode;
    private String sistEndretEnhet;
    private String stonadstype;
    private String tidsbruk;
    private String aapenOpgKode;

    public String getFlereOppgaver()
    {
    	if( oppgaver == null )
    	{
    		return "Nei";
    	}
    	else if( oppgaver.length > 0 )
    	{
    		return "Ja";
    	}
    	else
    	{
    		return "Nei";
    	}
    }
    
    public Henvendelse( String fodselsnummer ) {
    	this.fodselsnummer = new Pid( fodselsnummer );
    }

    public Pid getFodselsnummer() {
	    return fodselsnummer;
    }
    
    public String getBeskrivelse() {
        return beskrivelse;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    public String getEnhetstype() {
        return enhetstype;
    }

    public void setEnhetstype(String enhetstype) {
        this.enhetstype = enhetstype;
    }

    public String getFagNokkel() {
        return fagNokkel;
    }

    public void setFagNokkel(String fagNokkel) {
        this.fagNokkel = fagNokkel;
    }

    public String getFagomrKode() {
        return fagomrKode;
    }

    public void setFagomrKode(String fagomrKode) {
        this.fagomrKode = fagomrKode;
    }

    public String getHenvendelseId() {
        return henvendelseId;
    }

    public void setHenvendelseId(String henvendelseId) {
        this.henvendelseId = henvendelseId;
    }

    public String getHenvendelseStatus() {
        return henvendelseStatus;
    }

    public void setHenvendelseStatus(String henvendelseStatus) {
        this.henvendelseStatus = henvendelseStatus;
    }

    public String getHenvKanalKode() {
        return henvKanalKode;
    }

    public void setHenvKanalKode(String henvKanalKode) {
        this.henvKanalKode = henvKanalKode;
    }

    public String getHenvtype() {
        return henvtype;
    }

    public void setHenvtype(String henvtype) {
        this.henvtype = henvtype;
    }

    public String getKanaltype() {
        return kanaltype;
    }

    public void setKanaltype(String kanaltype) {
        this.kanaltype = kanaltype;
    }

    public String getOppfolging() {
        return oppfolging;
    }

    public void setOppfolging(String oppfolging) {
        this.oppfolging = oppfolging;
    }

    public NewOppgave[] getOppgaver() {
        return oppgaver;
    }

    public void setOppgaver(NewOppgave[] oppgaver) {
        this.oppgaver = oppgaver;
    }

    public NewOppgave getOppgaver(int i) {
        return this.oppgaver[i];
    }

    public void setOppgaver(int i, NewOppgave value) {
        this.oppgaver[i] = value;
    }

    public String getOpprettetAv() {
        return opprettetAv;
    }

    public void setOpprettetAv(String opprettetAv) {
        this.opprettetAv = opprettetAv;
    }

    public Calendar getOpprettetDato() {
        return opprettetDato;
    }

    public void setOpprettetDato(Calendar opprettetDato) {
        this.opprettetDato = opprettetDato;
    }

    public String getOpprettetEnhet() {
        return opprettetEnhet;
    }

    public void setOpprettetEnhet(String opprettetEnhet) {
        this.opprettetEnhet = opprettetEnhet;
    }

    public NewPerson getPerson() {
        return person;
    }

    public void setPerson(NewPerson person) {
        this.person = person;
    }

    public String getSakId() {
        return sakId;
    }

    public void setSakId(String sakId) {
        this.sakId = sakId;
    }

    public String getSakKatKode() {
        return sakKatKode;
    }

    public void setSakKatKode(String sakKatKode) {
        this.sakKatKode = sakKatKode;
    }

    public String getSistEndretEnhet() {
        return sistEndretEnhet;
    }

    public void setSistEndretEnhet(String sistEndretEnhet) {
        this.sistEndretEnhet = sistEndretEnhet;
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

    public String getAapenOpgKode() {
        return aapenOpgKode;
    }

    public void setAapenOpgKode(String aapenOpgKode) {
        this.aapenOpgKode = aapenOpgKode;
    }

}
