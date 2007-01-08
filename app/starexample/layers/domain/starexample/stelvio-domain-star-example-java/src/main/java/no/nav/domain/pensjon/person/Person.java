/**
 * Person.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf10631.06 v81706232132
 */

package no.nav.domain.pensjon.person;

import java.io.Serializable;
import java.util.Calendar;

public class Person  implements Serializable {
    private Fodselsnummer fodselsnummer;
    private String kortnavn;
    private String fornavn;
    private String mellomnavn;
    private String etternavn;
    private String diskresjonskode;
    private Calendar dodsdato;
    private Calendar umyndiggjortDato;
    private String sivilstand;
    private Calendar sivilstandDato;
    private String tlfPrivat;
    private String tlfJobb;
    private String tlfMobil;
    private String epost;
    private BostedsAdresse bostedsAdresse;
    private AnnenAdresse postAdresse;
    private AnnenAdresse tilleggsAdresse;
    private AnnenAdresse utenlandsAdresse;
    private Utbetalingsinformasjon utbetaling;
    private PersonUtland personUtland;
    private Relasjon[][] relasjoner;

    private String tilknyttetEnhet; 
    
    
    public Boolean getDiskresjon()
    {
    	if( diskresjonskode != null && ( diskresjonskode.equals( "6" ) || diskresjonskode.equals( "7" ) ))
    	{
    		return true;
    	}
    	return false;
    }
    
    public Boolean getErDod()
    {
    	if( dodsdato == null )
    	{
    		return Boolean.FALSE;
    	}
    	return Boolean.TRUE;
    }
    
    public Boolean getUmyndiggjort()
    {
    	if( umyndiggjortDato == null )
    	{
    		return Boolean.FALSE;
    	}
    	return Boolean.TRUE;
    }
    
    public String getTilknyttetEnhet() {
		return tilknyttetEnhet;
	}

	public void setTilknyttetEnhet(String tilknyttetEnhet) {
		this.tilknyttetEnhet = tilknyttetEnhet;
	}

	public Person() {
    }

    public Fodselsnummer getFodselsnummer() {
        return fodselsnummer;
    }

    public void setFodselsnummer(Fodselsnummer fodselsnummer) {
        this.fodselsnummer = fodselsnummer;
    }

    public String getKortnavn() {
        return kortnavn;
    }

    public void setKortnavn(String kortnavn) {
        this.kortnavn = kortnavn;
    }

    public String getFornavn() {
        return fornavn;
    }

    public void setFornavn(String fornavn) {
        this.fornavn = fornavn;
    }

    public String getMellomnavn() {
        return mellomnavn;
    }

    public void setMellomnavn(String mellomnavn) {
        this.mellomnavn = mellomnavn;
    }

    public String getEtternavn() {
        return etternavn;
    }

    public void setEtternavn(String etternavn) {
        this.etternavn = etternavn;
    }

    public String getDiskresjonskode() {
        return diskresjonskode;
    }

    public void setDiskresjonskode(String diskresjonskode) {
        this.diskresjonskode = diskresjonskode;
    }

    public Calendar getDodsdato() {
        return dodsdato;
    }

    public void setDodsdato(Calendar dodsdato) {
        this.dodsdato = dodsdato;
    }

    public Calendar getUmyndiggjortDato() {
        return umyndiggjortDato;
    }

    public void setUmyndiggjortDato(Calendar umyndiggjortDato) {
        this.umyndiggjortDato = umyndiggjortDato;
    }

    public String getSivilstand() {
        return sivilstand;
    }

    public void setSivilstand(String sivilstand) {
        this.sivilstand = sivilstand;
    }

    public Calendar getSivilstandDato() {
        return sivilstandDato;
    }

    public void setSivilstandDato(Calendar sivilstandDato) {
        this.sivilstandDato = sivilstandDato;
    }

    public String getTlfPrivat() {
        return tlfPrivat;
    }

    public void setTlfPrivat(String tlfPrivat) {
        this.tlfPrivat = tlfPrivat;
    }

    public String getTlfJobb() {
        return tlfJobb;
    }

    public void setTlfJobb(String tlfJobb) {
        this.tlfJobb = tlfJobb;
    }

    public String getTlfMobil() {
        return tlfMobil;
    }

    public void setTlfMobil(String tlfMobil) {
        this.tlfMobil = tlfMobil;
    }

    public String getEpost() {
        return epost;
    }

    public void setEpost(String epost) {
        this.epost = epost;
    }

    public BostedsAdresse getBostedsAdresse() {
        return bostedsAdresse;
    }

    public void setBostedsAdresse(BostedsAdresse bostedsAdresse) {
        this.bostedsAdresse = bostedsAdresse;
    }

    public AnnenAdresse getPostAdresse() {
        return postAdresse;
    }

    public void setPostAdresse(AnnenAdresse postAdresse) {
        this.postAdresse = postAdresse;
    }

    public AnnenAdresse getTilleggsAdresse() {
        return tilleggsAdresse;
    }

    public void setTilleggsAdresse(AnnenAdresse tilleggsAdresse) {
        this.tilleggsAdresse = tilleggsAdresse;
    }

    public AnnenAdresse getUtenlandsAdresse() {
        return utenlandsAdresse;
    }

    public void setUtenlandsAdresse(AnnenAdresse utenlandsAdresse) {
        this.utenlandsAdresse = utenlandsAdresse;
    }

    public Utbetalingsinformasjon getUtbetaling() {
        return utbetaling;
    }

    public void setUtbetaling(Utbetalingsinformasjon utbetaling) {
        this.utbetaling = utbetaling;
    }

    public PersonUtland getPersonUtland() {
        return personUtland;
    }

    public void setPersonUtland(PersonUtland personUtland) {
        this.personUtland = personUtland;
    }

    public Relasjon[][] getRelasjoner() {
        return relasjoner;
    }

    public void setRelasjoner(Relasjon[][] relasjoner) {
        this.relasjoner = relasjoner;
    }

    public Relasjon[] getRelasjoner(int i) {
        return this.relasjoner[i];
    }

    public void setRelasjoner(int i, Relasjon[] value) {
        this.relasjoner[i] = value;
    }

}
