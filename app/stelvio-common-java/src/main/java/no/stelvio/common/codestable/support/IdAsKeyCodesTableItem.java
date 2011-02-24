package no.stelvio.common.codestable.support;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Abstract base class for classes representing a codes table's entries, that is, rows in the codes table's
 * corresponding database tables where code is not used as the key in the table, but instead an extra id column is
 * used for this.
 * <p/>
 * This class is a <code>MappedSuperclass</code>, meaning that Entities that inherits from this class must map to a
 * table that defines columns set up by this class
 *
 * @author person66cdf88a8f67 (Accenture)
 * @version $Id$
 * 
 * @param <K> an enum type variable
 * @param <V> an type variable
 */
@MappedSuperclass
public abstract class IdAsKeyCodesTableItem<K extends Enum, V> extends AbstractCodesTableItem<K, V> {
	private static final long serialVersionUID = -131941273781433404L;

	/** The key for this item. */
	@Id
	@Column(name = "id")
	private String id;

	/** The code for this item. */
	@Column(name = "code")
	private String code;

	/**
	 * Constructs a new instance. Should only be used by the persistence provider and in some cases architecture code
	 * for mapping between layers.
	 */
	protected IdAsKeyCodesTableItem() {
	}
	
	/**
	 * Get id.
	 * 
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the code as a String.
	 * 
	 * @return the code
	 */
	public String getCodeAsString() {
		return code;
	}
}
