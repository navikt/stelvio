package no.stelvio.common.codestable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import no.stelvio.common.codestable.support.AbstractCodesTablePeriodicItem;

/**
 * Abstract base class for classes representing a codes table's entries, that is, rows in the codes table's corresponding
 * database tables where code is used as the key in the table. <p/> In addition to the capabilities in
 * <code>CodesTableItem</code>, this also specifies a time period in which the instances are valid. <p/> This class is a
 * <code>MappedSuperclass</code>, meaning that Entities that inherits from this class must map to a table that defines
 * columns set up by this class
 * 
 * @author personb66fa0b5ff6e (Accenture)
 * @author person66cdf88a8f67 (Accenture)
 * @version $Id$
 * @see CodesTableItem
 * 
 * @param <K>
 *            an enum type variable
 * @param <V>
 *            a type variable
 */
@MappedSuperclass
public abstract class CodesTablePeriodicItem<K extends Enum, V> extends AbstractCodesTablePeriodicItem<K, V> {
	private static final long serialVersionUID = -2501698338040432765L;

	/** The code for this item. */
	@Id
	@Column(name = "code")
	private String code;

	/**
	 * Constructs a new instance. Should only be used by the persistence provider and in some cases architecture code for
	 * mapping between layers.
	 */
	protected CodesTablePeriodicItem() {
	}

	/**
	 * {@inheritDoc}
	 */
	public String getCodeAsString() {
		return code;
	}

	/**
	 * Must NOT be used except when using the CodesTableManager is not possible. CodesTableItems should be instantiated through
	 * the CodesTableManager.
	 * 
	 * @param code
	 *            The code value to set
	 */
	public void setCodeAsString(String code) {
		this.code = code;
	}
}
