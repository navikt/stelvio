package no.stelvio.common.error;

import junit.framework.TestCase;

/**
 * Unit test of SystemException.
 * 
 * @author person7553f5959484
 * @version $Revision: 2836 $ $Author: psa2920 $ $Date: 2006-04-25 12:15:25 +0200 (Tue, 25 Apr 2006) $
 */
public class SystemExceptionTest extends TestCase {
    /*
	 * Test for Object copy()
	 */
	public void testObjectCopy() {

		SystemException original = createSystemException();
		original.setLogged();
		SystemException copy = (SystemException) original.copy();

		assertEquals("isLogged() should match", original.isLogged(), copy.isLogged());
		super.assertEquals("getErrorId() should match", original.getErrorId(), copy.getErrorId());
		super.assertEquals("getMessage() should match", original.getMessage(), copy.getMessage());
		super.assertEquals("getLocalizedMessage() should match", original.getLocalizedMessage(), copy.getLocalizedMessage());
		super.assertEquals("getProcessId() should match", original.getProcessId(), copy.getProcessId());
		super.assertEquals("getScreenId() should match", original.getScreenId(), copy.getScreenId());
		super.assertEquals("getTransactionId() should match", original.getTransactionId(), copy.getTransactionId());
		super.assertEquals("getUserId() should match", original.getUserId(), copy.getUserId());
		super.assertEquals("toString() should match", original.toString(), copy.toString());

		Object[] argsOrig = original.getArguments();
		Object[] argsCopy = copy.getArguments();

		super.assertEquals("getArguments() should not be null", argsOrig != null, argsCopy != null);

		if (null != argsOrig && null != argsCopy) {
			for (int i = 0; i < argsOrig.length; i++) {
				super.assertEquals("Contents of getArguments() should match", argsOrig[i], argsCopy[i]);
				super.assertSame("Objects of getArguments() should be the same", argsOrig[i], argsCopy[i]);
			}
		}

		super.assertNull("getCause() should have been removed", copy.getCause());
	}

    private SystemException createSystemException() {
        return new SystemException(new RuntimeException("The Original Cause"), new String[] { "1", "2", "3" }) {
            public Object copy() {
                return null;  // TODO: implement body
            }
        };
    }

    public void testGetArguments() {
		SystemException se = createSystemException();
		super.assertEquals("getArguments() should not be null", "Eneste argument", se.getArguments()[0]);
	}
	
	public void testGetArgumentsNotSameButContentsAreTheSame() {

			String arg0 = "A";
			Object[] arguments = new Object[] { arg0 };

		SystemException se = createSystemException();

			assertNotSame("Arguments not immutable, array is the same", arguments, se.getArguments());
			assertSame("arg0 should be the same", arg0, se.getArguments()[0]);

		}
	
	public void testSystemExceptionErrorCodeObjectArray() {
		Object[] arguments = new String[] { "Petter", "Skodvin" };
		SystemException se = createSystemException();
		assertEquals("There should be 2 arguments", 2, se.getArguments().length);
		assertEquals("Argument 1 should be Petter", "Petter", se.getArguments()[0]);
		assertEquals("Argument 2 should be Skodvin", "Skodvin", se.getArguments()[1]);

	}

	public void testSystemExceptionErrorCodeThrowable() {
		Throwable cause = new RuntimeException("The Original Cause");
		SystemException se = createSystemException();
		assertEquals("Cause is The Original Cause", cause, se.getCause());
	}

	public void testSystemExceptionErrorCodeThrowableObject() {
		Throwable cause = new RuntimeException("The Original Cause");
		SystemException se = createSystemException();
		assertEquals("Cause is The Original Cause", cause, se.getCause());
		assertEquals("There should be 1 arguments", 1, se.getArguments().length);
		assertEquals("Argument 1 should be Argument", "Argument", se.getArguments()[0]);
	}

}
