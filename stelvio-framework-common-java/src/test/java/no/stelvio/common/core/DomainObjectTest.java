package no.stelvio.common.core;

import no.stelvio.common.core.Behaviour;
import no.stelvio.common.core.DomainObject;
import junit.framework.TestCase;

/**
 * DomainObject unit test.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 2215 $ $Author: psa2920 $ $Date: 2005-04-15 12:41:20 +0200 (Fri, 15 Apr 2005) $
 */
public class DomainObjectTest extends TestCase {

	/**
	 * Constructor for DomainObjectTest.
	 * @param arg0
	 */
	public DomainObjectTest(String arg0) {
		super(arg0);
	}

	public void testGetSetBehaviour() {

		DomainObject obj = new ConcreteDomainObject();
		assertNull(obj.getBehaviour());

		Behaviour beh = new TestBehaviour();
		assertNull(beh.getDomainObject());

		obj.setBehaviour(beh);

		assertSame(beh, obj.getBehaviour());
		assertSame(obj, beh.getDomainObject());

	}

	private class ConcreteDomainObject extends DomainObject {
	}

	private class TestBehaviour extends Behaviour {
	}
}
