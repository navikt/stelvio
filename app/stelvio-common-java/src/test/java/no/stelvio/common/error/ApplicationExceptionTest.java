package no.stelvio.common.error;

import junit.framework.TestCase;

/**
 * Unit test of ApplicationException.
 * 
 * @author person7553f5959484
 * @version $Revision: 2838 $ $Author: psa2920 $ $Date: 2006-04-25 12:22:58 +0200 (Tue, 25 Apr 2006) $
 */
public class ApplicationExceptionTest extends TestCase {
	/*
	 * Test for Object copy()
	 */
	public void testObjectCopy() {

		ApplicationException original =
                createApplicationException();
		original.setLogged();
		ApplicationException copy = (ApplicationException) original.copy();

		super.assertEquals("isLogged() should match", original.isLogged(), copy.isLogged());
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

    public void testGetArguments() {
		ApplicationException ae = createApplicationException();
		super.assertEquals("getArguments() should not be null", "Eneste argument", ae.getArguments()[0]);
	}

    public void testGetArgumentsNotSameButContentsAreTheSame() {

		String arg0 = "A";
		Object[] arguments = new Object[] { arg0 };

		ApplicationException ae = createApplicationException();

		assertNotSame("Arguments not immutable, array is the same", arguments, ae.getArguments());
		assertSame("arg0 should be the same", arg0, ae.getArguments()[0]);

	}

    public void testApplicationExceptionErrorCodeObjectArray() {
		Object[] arguments = new String[] { "Petter", "Skodvin" };
		ApplicationException ae = createApplicationException();
		assertEquals("There should be 2 arguments", 2, ae.getArguments().length);
		assertEquals("Argument 1 should be Petter", "Petter", ae.getArguments()[0]);
		assertEquals("Argument 2 should be Skodvin", "Skodvin", ae.getArguments()[1]);

	}

    public void testApplicationExceptionErrorCodeThrowable() {
		Throwable cause = new RuntimeException("The Original Cause");
		ApplicationException ae = createApplicationException();
		assertEquals("Cause is The Original Cause", cause, ae.getCause());
	}

    public void testApplicationExceptionErrorCodeThrowableObject() {
		Throwable cause = new RuntimeException("The Original Cause");
		ApplicationException ae = createApplicationException();
		assertEquals("Cause is The Original Cause", cause, ae.getCause());
		assertEquals("There should be 1 arguments", 1, ae.getArguments().length);
		assertEquals("Argument 1 should be Argument", "Argument", ae.getArguments()[0]);
	}

    private ApplicationException createApplicationException() {
        return new ApplicationException( new RuntimeException("The Original Cause"), new String[] { "1", "2", "3" }) {
            public Object copy() {
                return null;  // TODO: implement body
            }
        };
    }
}
