package no.stelvio.common.audit;

import static org.junit.Assert.*;
import no.stelvio.common.audit.AuditItem.ProtectionLevel;
import no.stelvio.common.context.support.RequestContextSetter;
import no.stelvio.common.util.DateUtil;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for AuditItem.
 * 
 *
 */
public class AuditItemTest {

	/**
	 * Setup before test.
	 */
	@Before
	public void setUp() {
		// Must reset RequestContext, as the context will have been set by
		// previously executed unit tests when using Maven to build. If
		// RequestContext is set, a different toString will be returned
		RequestContextSetter.resetRequestContext();
	}

	/**
	 * Testing toString.
	 */
	@Test
	public void toStringFormatIsOk() {
		String expectedMessage = "AccessType=null, ProtectionLevel=HIGH, Message=myMessage, Target=null, Source=null, "
				+ "UserId=a12345, Timestamp=01.01.2008 00:00:00:00, CustomInfo=[customKey1=customInfo1]";
		AuditItem item = new AuditItem();
		item.setMessage("myMessage");
		item.setTimestamp(DateUtil.parseCompactDate("01012008"));
		item.setTransactionId("0123456789");
		item.setUserId("a12345");
		item.setProtectionLevel(ProtectionLevel.HIGH);
		item.addCustomInfo("customKey1", "customInfo1");
		assertEquals("Incorrect toString implementation", expectedMessage, item.toString());
	}

	/**
	 * Testing protection level classification.
	 */
	@Test
	public void protectionLevelClasification() {
		assertTrue(ProtectionLevel.HIGH.isHigherThan(ProtectionLevel.MEDIUM));
		assertFalse(ProtectionLevel.HIGH.isHigherThan(ProtectionLevel.HIGH));
	}

	/**
	 * Tests the order of the ProtectionLevel is implemented with the correct Enum.ordinal().
	 * 
	 */
	@Test
	public void protectionLevelComparTo() {
		assertEquals(1, ProtectionLevel.SUPER_HIGH.compareTo(ProtectionLevel.EXTRA_HIGH));
		assertEquals(1, ProtectionLevel.EXTRA_HIGH.compareTo(ProtectionLevel.HIGH));
		assertEquals(1, ProtectionLevel.HIGH.compareTo(ProtectionLevel.MEDIUM));
		assertEquals(1, ProtectionLevel.MEDIUM.compareTo(ProtectionLevel.LOW));
	}

	/**
	 * Compares highes with lowest, to assert that nothing has been added in between. Will not reveal new border values (new
	 * highest or new lowest)
	 */
	@Test
	public void maxMinValues() {
		assertEquals("New ProtectionLevels has been introduced, this protectionLevelCompareTo-method"
				+ " must be expanded with more asserts and maxMinLevels re-written", 4, ProtectionLevel.SUPER_HIGH
				.compareTo(ProtectionLevel.LOW));
	}

}
