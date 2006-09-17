package no.stelvio.common.framework.test.web;

import junit.framework.AssertionFailedError;

import no.stelvio.common.framework.test.web.JMockableStrutsTestCase;

import org.jmock.MockObjectTestCase;

/**
 * Unit test for {@link JMockableStrutsTestCase}.
 *
 * @author personf8e9850ed756
 * @version $Revision: 2711 $, $Date: 2005-12-13 15:06:24 +0100 (Tue, 13 Dec 2005) $
 */
public class JMockableStrutsTestCaseTest extends MockObjectTestCase {
	private JMockableStrutsTestCase jMockableStrutsTestCase;

	public void testShouldThrowAssertionFailedErrorIfFrameworkIsNotInitializedWhenStartingTest() {
		try {
			jMockableStrutsTestCase.actionPerform();
			fail("AssertionFailedError should have been thrown");
		} catch (AssertionFailedError error) {
			// should be thrown
		}

		try {
			jMockableStrutsTestCase.registerActionInstance(null);
			fail("AssertionFailedError should have been thrown");
		} catch (AssertionFailedError error) {
			// should be thrown
		}

		try {
			jMockableStrutsTestCase.setActionForm(null);
			fail("AssertionFailedError should have been thrown");
		} catch (AssertionFailedError error) {
			// should be thrown
		}
	}

	protected void setUp() throws Exception {
		jMockableStrutsTestCase = new JMockableStrutsTestCase();
	}
}
