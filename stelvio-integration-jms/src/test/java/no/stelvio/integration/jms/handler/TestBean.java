package no.stelvio.integration.jms.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author person356941106810, Accenture
 */
public class TestBean {
	private TestBean nestedBean = null;
	private String stringValue = null;
	private Character characterValue = null;
	private Double doubleValue = null;
	private Short shortValue = null;
	private Integer integerValue = null;
	private Boolean booleanValue = null;
	private Float floatValue = null;
	private Byte byteValue = null;
	private Long longValue = null;
	
	private int iValue = 0;
	private double dValue = 0.0;
	private short sValue = 0;
	private char cValue = ' ';
	private long lValue = 0;
	private boolean bValue = false;
	private float fValue = 0;
	private byte byValue = 0;
	 
	private List nestedList = null;
	private Map nestedMap = null;
	private TestBean[] nestedArrayBean = null;
	private ArrayList typedListBean = null;
	private List interfaceListBean = null;
	private HashMap typedMapBean = null;
	private Map interfaceMapBean = null;
	
	/**
	 * @return
	 */
	public Boolean getBooleanValue() {
		return booleanValue;
	}

	/**
	 * @return
	 */
	public boolean isBValue() {
		return bValue;
	}

	/**
	 * @return
	 */
	public Byte getByteValue() {
		return byteValue;
	}

	/**
	 * @return
	 */
	public byte getByValue() {
		return byValue;
	}

	/**
	 * @return
	 */
	public Character getCharacterValue() {
		return characterValue;
	}

	/**
	 * @return
	 */
	public char getCValue() {
		return cValue;
	}

	/**
	 * @return
	 */
	public Double getDoubleValue() {
		return doubleValue;
	}

	/**
	 * @return
	 */
	public double getDValue() {
		return dValue;
	}

	/**
	 * @return
	 */
	public Float getFloatValue() {
		return floatValue;
	}

	/**
	 * @return
	 */
	public float getFValue() {
		return fValue;
	}

	/**
	 * @return
	 */
	public Integer getIntegerValue() {
		return integerValue;
	}

	/**
	 * @return
	 */
	public int getIValue() {
		return iValue;
	}

	/**
	 * @return
	 */
	public Long getLongValue() {
		return longValue;
	}

	/**
	 * @return
	 */
	public long getLValue() {
		return lValue;
	}

	/**
	 * @return
	 */
	public TestBean getNestedBean() {
		return nestedBean;
	}

	/**
	 * @return
	 */
	public Short getShortValue() {
		return shortValue;
	}

	/**
	 * @return
	 */
	public String getStringValue() {
		return stringValue;
	}

	/**
	 * @return
	 */
	public short getSValue() {
		return sValue;
	}

	/**
	 * @param boolean1
	 */
	public void setBooleanValue(Boolean boolean1) {
		booleanValue = boolean1;
	}

	/**
	 * @param b
	 */
	public void setBValue(boolean b) {
		bValue = b;
	}

	/**
	 * @param byte1
	 */
	public void setByteValue(Byte byte1) {
		byteValue = byte1;
	}

	/**
	 * @param b
	 */
	public void setByValue(byte b) {
		byValue = b;
	}

	/**
	 * @param character
	 */
	public void setCharacterValue(Character character) {
		characterValue = character;
	}

	/**
	 * @param c
	 */
	public void setCValue(char c) {
		cValue = c;
	}

	/**
	 * @param double1
	 */
	public void setDoubleValue(Double double1) {
		doubleValue = double1;
	}

	/**
	 * @param d
	 */
	public void setDValue(double d) {
		dValue = d;
	}

	/**
	 * @param float1
	 */
	public void setFloatValue(Float float1) {
		floatValue = float1;
	}

	/**
	 * @param f
	 */
	public void setFValue(float f) {
		fValue = f;
	}

	/**
	 * @param integer
	 */
	public void setIntegerValue(Integer integer) {
		integerValue = integer;
	}

	/**
	 * @param i
	 */
	public void setIValue(int i) {
		iValue = i;
	}

	/**
	 * @param long1
	 */
	public void setLongValue(Long long1) {
		longValue = long1;
	}

	/**
	 * @param l
	 */
	public void setLValue(long l) {
		lValue = l;
	}

	/**
	 * @param bean
	 */
	public void setNestedBean(TestBean bean) {
		nestedBean = bean;
	}

	/**
	 * @param short1
	 */
	public void setShortValue(Short short1) {
		shortValue = short1;
	}

	/**
	 * @param string
	 */
	public void setStringValue(String string) {
		stringValue = string;
	}

	/**
	 * @param s
	 */
	public void setSValue(short s) {
		sValue = s;
	}

	/**
	 * @return
	 */
	public TestBean[] getNestedArrayBean() {
		return nestedArrayBean;
	}

	/**
	 * @return
	 */
	public List getNestedList() {
		return nestedList;
	}

	/**
	 * @return
	 */
	public Map getNestedMap() {
		return nestedMap;
	}

	/**
	 * @param beans
	 */
	public void setNestedArrayBean(TestBean[] beans) {
		nestedArrayBean = beans;
	}

	/**
	 * @param list
	 */
	public void setNestedList(List list) {
		nestedList = list;
	}

	/**
	 * @param map
	 */
	public void setNestedMap(Map map) {
		nestedMap = map;
	}

	/**
	 * @return
	 */
	public ArrayList getTypedListBean() {
		return typedListBean;
	}

	/**
	 * @param list
	 */
	public void setTypedListBean(ArrayList list) {
		typedListBean = list;
	}

	/**
	 * @return
	 */
	public List getInterfaceListBean() {
		return interfaceListBean;
	}

	/**
	 * @param list
	 */
	public void setInterfaceListBean(List list) {
		interfaceListBean = list;
	}

	/**
	 * @return
	 */
	public Map getInterfaceMapBean() {
		return interfaceMapBean;
	}

	/**
	 * @return
	 */
	public HashMap getTypedMapBean() {
		return typedMapBean;
	}

	/**
	 * @param map
	 */
	public void setInterfaceMapBean(Map map) {
		interfaceMapBean = map;
	}

	/**
	 * @param map
	 */
	public void setTypedMapBean(HashMap map) {
		typedMapBean = map;
	}

}
