package no.nav.integration.framework.hibernate.mapping;

/**
 * @author TKC2920
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Column extends net.sf.hibernate.mapping.Column {

	private int offset;
	
	private String formater;

	/**
	 * Setter for property offset
	 * @return int - The offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * Getter for property offset
	 * @param offset - The offset of the column
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}
	

	/**
	 * Getter for formater
	 * @return the class name of the formater
	 */
	public String getFormater() {
		return formater;
	}

	/**
	 * Setter for formater
	 * @param string - Class name of the formater
	 */
	public void setFormater(String string) {
		formater = string;
	}

}
