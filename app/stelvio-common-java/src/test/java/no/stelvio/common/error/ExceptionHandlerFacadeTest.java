package no.stelvio.common.error;

import java.io.File;
import java.io.IOException;

import com.agical.rmock.extension.junit.RMockTestCase;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 * @todo error handler needs locale from context, have a prototype target source get it so we dont need to call static method here
 */
public abstract class ExceptionHandlerFacadeTest extends RMockTestCase {
    public void testAfterHandlingRethrow() {
        try {
            getExceptionHandlerFacade().handleError(new IllegalArgumentException());
            fail("Exception should have been thrown");
        } catch (Exception e) {
            // Should happen
        }
    }

    public void testGetErrorThrowsExceptionIfNoLocaleExistsOrGiven() {
        try {
            getExceptionHandlerFacade().getError(IllegalArgumentException.class);
            fail("Exception should have been thrown");
        } catch (Exception e) {
            // Should happen, TODO: better exception
        }
    }

    // TODO look into this
    public void testGetMessage() {
//        assertEquals("message should have been the same", t.getLocalizedMessage(), no.stelvio.common.error.old.ErrorHandler.getMessage(t));
    }

    // TODO look into this
    public void testGetStacktraceAsString() {
//        assertNull(ErrorHandler.getStacktraceAsString(null));
//        assertNotNull(ErrorHandler.getStacktraceAsString(t));
    }


    // TODO look into this
    public void testErrorInErrorHandlingConfig() {
        try {
            rename("error-handling.xml", "error-handling.xml.backup");
//            no.stelvio.common.error.old.ErrorHandler.init();
            rename("error-handling.xml.backup", "error-handling.xml");
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private void rename(String filename, String newFilename) throws IOException {
        File file = new File(Thread.currentThread().getContextClassLoader().getResource(filename).getFile());
        file.renameTo(new File(file.getParentFile(), newFilename));
    }


    public abstract ExceptionHandlerFacade getExceptionHandlerFacade();
}
