package no.nav.integration.framework.jms.formatter;

import java.util.List;

/**
 * Utility for testing formatters tailored for the Oppdrag system.
 * 
 * @author persone5d69f3729a8, Accenture
 * @version $Id: OppdragTestBean.java 2392 2005-06-29 07:50:09Z psa2920 $
 */
public class OppdragTestBean {
	private String testField = null;
	private List elements;
	private double d;
	private int intTest;

	/**
	 * @return
	 */
	public String getTestField() {
		return testField;
	}

	/**
	 * @param string
	 */
	public void setTestField(String string) {
		testField = string;
	}

	/**
	 * 
	 * @return
	 */
	public List getElements() {
		return elements;
	}

	/**
	 * @param list
	 */
	public void setElements(List list) {
		elements = list;
	}

	/** 
	 * @return
	 */
	public double getD() {
		return d;
	}

	/**
	 * @param d
	 */
	public void setD(double d) {
		this.d = d;
	}

	/**
	 * @return
	 */
	public int getIntTest() {
		return intTest;
	}

	/**
	 * @param i
	 */
	public void setIntTest(int i) {
		intTest = i;
	}
}
