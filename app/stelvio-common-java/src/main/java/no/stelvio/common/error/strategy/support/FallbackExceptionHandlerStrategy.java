package no.stelvio.common.error.strategy.support;

import java.io.PrintWriter;
import java.io.StringWriter;

import no.stelvio.common.error.strategy.ExceptionHandlerStrategy;

/**
 * @author personf8e9850ed756
 * @todo maybe have its own subinterface taking both errors, that is, the original and the one thrown when trying to
 * log, etc. the original.
 */
public class FallbackExceptionHandlerStrategy implements ExceptionHandlerStrategy {
    public <T extends Throwable> T handleException(T throwable) {
        printException(throwable);

        return throwable;
    }

    private void printException(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        pw.println("FallbackExceptionHandler.handle: ");
        throwable.printStackTrace(pw);
        pw.flush();
        pw.close();

        System.err.println(sw.toString());
    }
}
