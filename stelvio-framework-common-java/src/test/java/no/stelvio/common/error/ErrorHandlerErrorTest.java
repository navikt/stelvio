package no.stelvio.common.error;

import java.io.File;
import java.io.IOException;

import no.stelvio.common.error.ErrorHandler;

import junit.framework.TestCase;

/**
 * ErrorHandler Unit Test.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 1037 $ $Author: psa2920 $ $Date: 2004-08-16 15:25:10 +0200 (Mon, 16 Aug 2004) $
 */
public class ErrorHandlerErrorTest extends TestCase {

	/**
	 * Constructor for ErrorHandlerTest.
	 * @param arg0
	 */
	public ErrorHandlerErrorTest(String arg0) {
		super(arg0);
	}

	public void testErrorInErrorHandlingConfig() {
		try {
			rename("error-handling.xml", "error-handling.xml.backup");
			ErrorHandler.init();
			rename("error-handling.xml.backup", "error-handling.xml");
		} catch (IOException io) {
			io.printStackTrace();
		}
	}

	private void rename(String filename, String newFilename) throws IOException {
		File file = new File(Thread.currentThread().getContextClassLoader().getResource(filename).getFile());
		file.renameTo(new File(file.getParentFile(), newFilename));
	}

}
