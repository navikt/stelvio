package no.stelvio.common.codestable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import no.stelvio.common.codestable.support.AbstractCodesTableItem;

/**
 * Abstract base class for classes representing a codes table's entries, that is, rows in the codes table's corresponding
 * database tables where code is used as the key in the table. <p> This class is a <code>MappedSuperclass</code>, meaning
 * that Entities that inherits from this class must map to a table that defines columns set up by this class
 * 
 * @version $Id$
 * 
 * @param <K>
 *            an enum type variable
 * @param <V>
 *            a type variable
 */
@MappedSuperclass
public abstract class CodesTableItem<K extends Enum, V> extends AbstractCodesTableItem<K, V> {
	private static final long serialVersionUID = 8512658758978068886L;

	/** The code for this item. */
	@Id
	@Column(name = "code")
	private String code;

	/**
	 * Constructs a new instance. Should only be used by the persistence provider and in some cases architecture code for
	 * mapping between layers.
	 */
	protected CodesTableItem() {
	}

	@Override
	public String getCodeAsString() {
		return code;
	}
}