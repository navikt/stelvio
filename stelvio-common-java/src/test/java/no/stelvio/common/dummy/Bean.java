package no.stelvio.common.dummy;

/**
 * Dummy test bean.
 */
public class Bean {
	private String property1 = null;
	private String property2 = null;
	private String property4 = null;
	private int property5;
	private double property6;
	private long property7;
	
	/**
	 * Dummy property.
	 * @return the property
	 */
	public String getProperty1() {
		return property1;
	}

	/**
	 * Dummy property.
	 * @return the property
	 */
	public String getProperty2() {
		return property2;
	}

	/**
	 * Dummy property.
	 * @param string - the property
	 */
	public void setProperty1(String string) {
		property1 = string;
	}

	/**
	 * Dummy property.
	 * @param string - the property
	 */
	public void setProperty2(String string) {
		property2 = string;
	}

	/**
	 * Dummy property.
	 * @throws Exception - Always
	 * @return String - will never happen
	 */
	public String getProperty4() throws Exception {
		if (true) {
			throw new Exception();
		}
		return property4;
	}

	/**
	 * Dummy property.
	 * @throws Exception - Always
	 * @param string - not needed
	 */
	public void setProperty4(String string) throws Exception {
		throw new Exception();
	}

	/**
	 * Dummy property.
	 * 
	 * @return property5
	 */
	public int getProperty5() {
		return property5;
	}

	/**
	 * Dummy property.
	 * 
	 * @param i property5
	 */
	public void setProperty5(int i) {
		property5 = i;
	}

	/**
	 * Dummy property.
	 * 
	 * @return property6
	 */
	public double getProperty6() {
		return property6;
	}

	/**
	 * Dummy property.
	 * 
	 * @param d property6
	 */
	public void setProperty6(double d) {
		property6 = d;
	}

	/**
	 * Dummy property.
	 * 
	 * @return property7
	 */
	public long getProperty7() {
		return property7;
	}

	/**
	 * Dummy property.
	 * 
	 * @param l property 7
	 */
	public void setProperty7(long l) {
		property7 = l;
	}

}
