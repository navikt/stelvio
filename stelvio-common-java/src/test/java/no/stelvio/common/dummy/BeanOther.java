package no.stelvio.common.dummy;

/**
 * Dummy bean for testing.
 * 
 * @version $Id: BeanOther.java 2361 2005-06-23 12:39:43Z jrd2920 $
 */
public class BeanOther {
	private Integer property1 = null;
	private Double property2 = null;
	private Long property3 = null;
	
	/**
	 * Dummy property.
	 * 
	 * @return property1
	 */
	public Integer getProperty1() {
		return property1;
	}

	/**
	 * Dummy property.
	 * 
	 * @param integer dummy property
	 */
	public void setProperty1(Integer integer) {
		property1 = integer;
	}

	/**
	 * Dummy property.
	 * 
	 * @return property2
	 */
	public Double getProperty2() {
		return property2;
	}

	/**
	 * Dummy property.
	 * 
	 * @param double1 property1
	 */
	public void setProperty2(Double double1) {
		property2 = double1;
	}

	/**
	 * Dummy.
	 * 
	 * @return property3
	 */
	public Long getProperty3() {
		return property3;
	}

	/**
	 * Dummy property.
	 * 
	 * @param long1 property3
	 */
	public void setProperty3(Long long1) {
		property3 = long1;
	}

}
