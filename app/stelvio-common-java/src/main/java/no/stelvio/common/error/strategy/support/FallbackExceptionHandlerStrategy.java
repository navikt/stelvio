package no.stelvio.common.error.strategy.support;

import java.io.PrintWriter;
import java.io.StringWriter;

import no.stelvio.common.error.strategy.ExceptionHandlerStrategy;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 * @todo falls back to making our own hard coded error as it is not in the database, should be put into the list of
 * strategies by DefaultExceptionHandler
 */
public class FallbackExceptionHandlerStrategy implements ExceptionHandlerStrategy {
    public <T extends Throwable> T handleException(T throwable) throws T {
        printException(throwable);

        return throwable;
    }

    private void printException(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        pw.println("FallbackExceptionHandler.handleException: ");
        throwable.printStackTrace(pw);
        pw.flush();
        pw.close();

        System.err.println(sw.toString());
    }
}
