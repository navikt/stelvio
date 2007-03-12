package no.stelvio.domain.star.example.saksbehandling;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** Value object that represents a saksbehandler. */
@Entity
public class Saksbehandler implements Serializable {
	private static final long serialVersionUID = 2991840796786871777L;

	/** Holds the saksbehandler's internal number. */
	@Id
	private long saksbehandlerNr;
	/** Holds the fornavn of the saksbehandler. */
	private String fornavn;
	/** Holds the etternavn of the saksbehandler. */
	private String etternavn;
	/** Holds the enhet the saksbehandler is working for. */
	private String enhet;

	/** Used for initializing the instance from repository. */
	protected Saksbehandler() {
	}

	/**
	 * Constructs a new instance of saksbehandler.
	 *
	 * @param saksbehandlernr the saksbehandler's internal number.
	 * @param fornavn the saksbehandler's fornavn.
	 * @param etternavn the saksbehandler's etternavn.
	 * @param enhet the enhet the saksbehandler belongs to.
	 */
	public Saksbehandler(long saksbehandlernr, String fornavn, String etternavn, String enhet) {
		this.saksbehandlerNr = saksbehandlernr;
		this.fornavn = fornavn;
		this.etternavn = etternavn;
		this.enhet = enhet;
	}

	public long getSaksbehandlerNr() {
		return saksbehandlerNr;
	}

	public String getEtternavn() {
		return etternavn;
	}

	public String getFornavn() {
		return fornavn;
	}

	public String getEnhet() {
		return enhet;
	}

	public boolean equals(Object other) {
		if (null == other) {
			return false;
		} else if (!this.getClass().equals(other.getClass())) {
			return false;
		} else {
			Saksbehandler errDef = (Saksbehandler) other;
			return new EqualsBuilder().append(saksbehandlerNr, errDef.saksbehandlerNr).isEquals();
		}
	}

	public int hashCode() {
		return new HashCodeBuilder().append(saksbehandlerNr).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).
				append("saksbehandlerNr", saksbehandlerNr).
				append("fornavn", fornavn).
				append("etternavn", etternavn).
				append("enhet", enhet).
				toString();
	}
}
