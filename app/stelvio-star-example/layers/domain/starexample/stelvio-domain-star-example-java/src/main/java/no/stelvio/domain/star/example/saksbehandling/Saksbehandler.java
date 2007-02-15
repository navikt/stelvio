package no.stelvio.domain.star.example.saksbehandling;

import org.apache.commons.lang.builder.ToStringBuilder;

/** Value object that represents a saksbehandler. */
public class Saksbehandler {
	/** Holds the fornavn of the saksbehandler. */
	String fornavn;
	/** Holds the etternavn of the saksbehandler. */
	String etternavn;
	/** Holds the enhet the saksbehandler is working for. */
	String enhet;
	/** Holds the saksbehandler's internal number. */
	long saksbehandlerNr;

	/**
	 * Constructs a new instance of saksbehandler.
	 *
	 * @param fornavn the saksbehandler's fornavn.
	 * @param etternavn the saksbehandler's etternavn.
	 * @param enhet the enhet the saksbehandler belongs to.
	 * @param saksbehandlernr the saksbehandler's internal number.
	 */
	public Saksbehandler(String fornavn, String etternavn, String enhet, long saksbehandlernr) {
		this.fornavn = fornavn;
		this.etternavn = etternavn;
		this.enhet = enhet;
		this.saksbehandlerNr = saksbehandlernr;
	}

	public String getEnhet() {
		return enhet;
	}

	public String getEtternavn() {
		return etternavn;
	}

	public String getFornavn() {
		return fornavn;
	}

	public long getSaksbehandlerNr() {
		return saksbehandlerNr;
	}

	public String toString() {
		return new ToStringBuilder(this).
				append("fornavn", fornavn).
				append("etternavn", etternavn).
				append("enhet", enhet).
				append("saksbehandlerNr", saksbehandlerNr).
				toString();
	}
}
