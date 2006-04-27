/*
 * Created on 03.mai.04
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package no.nav.integration.framework.hibernate.mapping;

import net.sf.hibernate.MappingException;
import net.sf.hibernate.mapping.PersistentClass;

/**
 * @author TKC2920
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class OneToMany extends net.sf.hibernate.mapping.OneToMany {

	private int length = 0;
	private int offset = 0;
	private String countColumn = null;
	/**
	 * Default constructor.
	 * @param owner - The owner
	 * @throws net.sf.hibernate.MappingException - When error in mapping
	 */
	public OneToMany(PersistentClass owner) throws MappingException {
		super(owner);
	}



	/**
	 * Getter for length.
	 * @return int - The Length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Getter for offset.
	 * @return int - The offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * Setter for length.
	 * @param length - The length
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * Setter for offset.
	 * @param off - The offset
	 */
	public void setOffset(int off) {
		offset = off;
	}

	/**
	 * Getter for count column
	 * @return String - The name of the count column
	 */
	public String getCountColumn() {
		return countColumn;
	}

	/**
	 * Setter for count column.
	 * @param string - The name of the count column
	 */
	public void setCountColumn(String string) {
		countColumn = string;
	}

}
