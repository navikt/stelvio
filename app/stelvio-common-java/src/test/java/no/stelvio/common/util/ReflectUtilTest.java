package no.stelvio.common.util;

import junit.framework.TestCase;

/**
 * JUnit testclass for ReflectUtil.
 * 
 * @author person5b7fd84b3197, Accenture
 */
public class ReflectUtilTest extends TestCase {
	private DummyBean bean;

	/**
	 * Tests GetNewInstance
	 */
	public void testGetNewInstance() {
		ReflectUtil.getNewInstance(DummyBean.class);
	}
	
	/**
	 * Tests GetPropertyFromClass
	 */
	public void testGetPropertyFromClass() {
		bean.setProp("test");

		String prop = (String) ReflectUtil.getPropertyFromClass(bean, "prop");
		assertEquals("Not the correct value is retrieved:", "test", prop);
	}

	/**
	 * Tests setPropertyOnClass
	 */
	public void testSetPropertyOnClass() {
		ReflectUtil.setPropertyOnClass("test", bean, "prop");

		assertEquals("Not the correct value is set:", "test", bean.getProp());
	}

	public void setUp() {
		bean = new DummyBean();
	}

	public static class DummyBean {
		String prop;
		long val;

		public void setProp(String prop) {
			this.prop = prop;
		}

		public String getProp() {
			return prop;
		}

		public long getVal() {
			return val;
		}

		public void setVal(long val) {
			this.val = val;
		}
	}
}
