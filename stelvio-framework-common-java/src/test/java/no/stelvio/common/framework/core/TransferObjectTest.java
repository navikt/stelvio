package no.stelvio.common.framework.core;

import no.stelvio.common.framework.core.TransferObject;
import junit.framework.TestCase;

/**
 * TransferObject unit test.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 2599 $ $Author: jla2920 $ $Date: 2005-10-31 13:42:13 +0100 (Mon, 31 Oct 2005) $
 */
public class TransferObjectTest extends TestCase {
	private TransferObject concreteTransferObject;

	public void testSetGet() {
		String a = "A";
		String b = "B";
		String c = null;

		concreteTransferObject.setData("a", a);
		concreteTransferObject.setData("b", b);
		concreteTransferObject.setData("c", c);

		assertEquals(concreteTransferObject.getData("a"), a);
		assertEquals(concreteTransferObject.getData("b"), b);
		assertNull(concreteTransferObject.getData("c"));
		assertNull(concreteTransferObject.getData("d"));

	}

	public void testTestHandlesNullValues() {
		concreteTransferObject.setData("nullKey", null);

		assertEquals("Should handle null value", "[map={nullKey=null}]", concreteTransferObject.toString());
	}

	public void testIsEmpty() {
		assertTrue(concreteTransferObject.isEmpty());
		concreteTransferObject.setData("nullKey", null);

		assertFalse(concreteTransferObject.isEmpty());
	}

	protected void setUp() {
		concreteTransferObject = new ConcreteTransferObject();
	}

	private class ConcreteTransferObject extends TransferObject {
	}
}
