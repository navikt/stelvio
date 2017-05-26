package no.stelvio.common.codestable.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.util.ReflectUtil;

import org.junit.Test;

/**
 * Test class that aims to test that CodesTable components works with enum codes where enum.getIllegalCode() is the code, and
 * not enum.name().
 * 
 * @author person983601e0e117 (Accenture)
 * 
 */
public class IllegalEnumCodeTest {

	/**
	 * Tests that CodesTableItems getCode works with IllegalEnumCode.
	 * 
	 * @throws NoSuchFieldException no field
	 * 
	 */
	@Test
	public void testGetCode() throws NoSuchFieldException {
		PostnrCti cti = new PostnrCti();
		ReflectUtil.setField(PostnrCti.class, cti, "code", "0116");
		PostnrCode code = cti.getCode();
		assertTrue(code.equals(PostnrCode.OSLO1));

		cti = new PostnrCti();
		ReflectUtil.setField(PostnrCti.class, cti, "code", "2300");
		code = cti.getCode();
		assertTrue(code.equals(PostnrCode.HAMAR));
	}

	/**
	 * Tests that the isCodeEqualTo defined in {@link AbstractCodesTableItem} works with enums that implements
	 * {@link IllegalCodeEnum}.
	 * 
	 * @throws NoSuchFieldException no field
	 * 
	 */
	@Test
	public void testCodeEqualTo() throws NoSuchFieldException {
		PostnrCti cti = new PostnrCti();
		ReflectUtil.setField(PostnrCti.class, cti, "code", "0116");
		assertTrue(cti.isCodeEqualTo(PostnrCode.OSLO1));
	}

	/**
	 * Tests that the CodesTable-implementation is capable using CodesTableItems with Enums that implement IllegalEnumCode.
	 * 
	 * @throws NoSuchFieldException no field
	 * 
	 */
	@Test
	public void testCodesTable() throws NoSuchFieldException {
		PostnrCti oslo = new PostnrCti();
		ReflectUtil.setField(PostnrCti.class, oslo, "code", "0116");

		PostnrCti hamar = new PostnrCti();
		ReflectUtil.setField(PostnrCti.class, hamar, "code", "2300");
		ReflectUtil.setField(PostnrCti.class, hamar, "decode", "hamar");

		PostnrCti jar = new PostnrCti();
		ReflectUtil.setField(PostnrCti.class, jar, "code", "1358");

		ArrayList<PostnrCti> items = new ArrayList<PostnrCti>();
		items.add(oslo);
		items.add(hamar);
		items.add(jar);

		DefaultCodesTable<PostnrCti, PostnrCode, String> table = new DefaultCodesTable<PostnrCti, PostnrCode, String>(items,
				PostnrCti.class);

		assertEquals(hamar, table.getCodesTableItem("2300"));
		assertEquals(oslo, table.getCodesTableItem(PostnrCode.OSLO1));
		assertTrue(table.validateCode(PostnrCode.JAR));
		assertFalse(table.validateCode(PostnrCode.STAVANGER));
		assertEquals("hamar", table.getDecode("2300"));
		assertEquals("hamar", table.getDecode(PostnrCode.HAMAR));

	}

	/**
	 * Postnr test class.
	 * 
	 * @author person983601e0e117 (Accenture)
	 */
	class PostnrCti extends CodesTableItem<PostnrCode, String> {
		private static final long serialVersionUID = 1L;
	}

	/**
	 * PostnrCode that has "illegal" Enum codes.
	 * 
	 * @author person983601e0e117 (Accenture)
	 * 
	 */
	enum PostnrCode implements IllegalCodeEnum {

		OSLO1("0116"), HAMAR("2300"), JAR("1358"), STAVANGER("4000");

		private String realCode = "";

		/**
		 * Creates a new instance of PostnrCode.
		 *
		 * @param realCode code
		 */
		private PostnrCode(String realCode) {
			this.realCode = realCode;
		}

		/**
		 * Get illegal code.
		 * 
		 * @return illegal code
		 */
		public String getIllegalCode() {
			return realCode;
		}

	}

}
