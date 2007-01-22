package no.nav.domain.pensjon.codestable;

import java.util.Date;
import java.util.Locale;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.codestable.CodesTableItemPeriodic;


/**
 * CodesTableItem for HenvendelseType
 * 
 * The tablemappings defined in the MappedSuperclasses are overridden
 * 
 * Defines a <code>@NamedQuery</code>, "HenvendelseTypeCti.findAll" to retrieve all instances of HenvendelseTypeCti from database
 * 
 * @author person983601e0e117 (Accenture)
 * 
 * @see CodesTableItem
 * @see CodesTableItemPeriodic
 *
 */
@Entity
@Table(name="K_HENVENDELSE_T")
@AttributeOverrides({
    @AttributeOverride(name="code", column=@Column(name="K_HENVENDELSE_T")),
    @AttributeOverride(name="decode", column=@Column(name="dekode")),
    @AttributeOverride(name="valid", column=@Column(name="er_gyldig")),
    @AttributeOverride(name="fromDate", column=@Column(name="dato_fom")),
    @AttributeOverride(name="toDate", column=@Column(name="dato_tom"))
})
@NamedQuery(name="HenvendelseTypeCti.findAll",query="select htcti from HenvendelseTypeCti htcti")
public class HenvendelseTypeCti extends CodesTableItemPeriodic {

	private static final long serialVersionUID = 4989579646384579677L;
	
	protected HenvendelseTypeCti() {
		super();
	}

	public HenvendelseTypeCti(String code, String decode, Date fromDate, Date toDate, Locale locale, boolean valid) {
		super(code, decode, fromDate, toDate, locale, valid);
	}

}
