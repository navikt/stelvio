package no.nav.integration.framework.hibernate.mapping;

import net.sf.hibernate.MappingException;
import net.sf.hibernate.mapping.SimpleValue;
import net.sf.hibernate.mapping.Table;

/**
 * Hibernate mapping for one-to-one relationships.
 * @author person356941106810, Accenture
 */
public class OneToOne extends net.sf.hibernate.mapping.OneToOne {
	private int length = 0;
	private int offset = 0;

	/**
	 * @param arg0 - Table
	 * @param arg1 - SimpleValue
	 * @throws net.sf.hibernate.MappingException - if any error
	 */
	public OneToOne(Table arg0, SimpleValue arg1) throws MappingException {
		super(arg0, arg1);
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

}
