package no.stelvio.common.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link ReflectUtil}.
 * 
 * @author person5b7fd84b3197, Accenture
 */
public class ReflectUtilTest {
	private DummyBean bean;

	/**
	 * Test that creating new instance should not fail.
	 */
	@Test
	public void creatingNewInstanceShouldNotFail() {
		ReflectUtil.createNewInstance(DummyBean.class);
	}

	/**
	 * Test retrievied property is correct.
	 */
	@Test
	public void retrievedPropertyIsCorrect() {
		bean.setProp("test");

		String prop = ReflectUtil.getPropertyFromClass(bean, "prop");
		assertThat(prop, is(equalTo("test")));
	}

	/**
	 * Test that property is set coorectly.
	 */
	@Test
	public void propertyIsSetCorrectly() {
		ReflectUtil.setPropertyOnClass("test", bean, "prop");

		assertThat(bean.getProp(), is(equalTo("test")));
	}

	/**
	 * Set up before test.
	 */
	@Before
	public void setUp() {
		bean = new DummyBean();
	}

	/**
	 * A dummy bean for testing.
	 */
	public static class DummyBean {
		private String prop;

		private long val;

		/**
		 * Set property.
		 * 
		 * @param prop
		 *            property
		 */
		public void setProp(String prop) {
			this.prop = prop;
		}

		/**
		 * Get property.
		 * 
		 * @return property
		 */
		public String getProp() {
			return prop;
		}

		/**
		 * Get value.
		 * 
		 * @return value
		 */
		public long getVal() {
			return val;
		}

		/**
		 * Set value.
		 * 
		 * @param val
		 *            value
		 */
		public void setVal(long val) {
			this.val = val;
		}
	}
}
