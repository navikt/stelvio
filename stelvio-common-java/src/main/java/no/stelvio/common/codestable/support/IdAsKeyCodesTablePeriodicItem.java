package no.stelvio.common.codestable.support;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Abstract base class for classes representing a codes table's entries, that is, rows in the codes table's
 * corresponding database tables where code is not used as the key in the table, but instead an extra id column is
 * used for this.
 * <p>
 * In addition to the capabilities in <code>IdAsKeyCodesTableItem</code>, this also specifies a time period in which
 * the instances are valid.
 * <p>
 * This class is a <code>MappedSuperclass</code>, meaning that Entities that inherits from this class must map to a
 * table that defines columns set up by this class
 *
 * @version $Id$
 * @see IdAsKeyCodesTableItem
 * 
 * @param <K> an enum type variable
 * @param <V> a type variable
 */
@MappedSuperclass
public abstract class IdAsKeyCodesTablePeriodicItem<K extends Enum, V> extends AbstractCodesTablePeriodicItem<K, V> {
	private static final long serialVersionUID = -2501698338040432765L;

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
	protected IdAsKeyCodesTablePeriodicItem() {
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
	 * Gets the code.
	 * 
	 * @return the code
	 */
	public String getCodeAsString() {
		return code;
	}
	
	/**
	 * Must NOT be used except when the using the CodesTableManager is not possible. IdAsKeyCodesTablePeriodicItems should be 
	 * instantiated through the CodesTableManager.
	 * 
	 * @param id The id value to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Must NOT be used except when the using the CodesTableManager is not possible. IdAsKeyCodesTablePeriodicItems should be 
	 * instantiated through the CodesTableManager.
	 * 
	 * @param code The code value to set
	 */
	public void setCodeAsString(String code) {
		this.code = code;
	}
	
}