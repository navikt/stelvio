package no.stelvio.common.error.strategy.support;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * maybe have its own subinterface taking both errors, that is, the original and the one thrown when trying to
 * log, etc. the original.
 * 
 */
public class FallbackExceptionHandlerStrategy extends AbstractOrderedExceptionHandlerStrategy {

	/**
	 * Handles the exception being passed, by writing the stack trace to System.err and returning the 
	 * exception.
	 * 
	 * @param <T> a Throwable type variable
	 * @param throwable an exception
	 * @return the exception that was passed into the method
	 */
	@Override
	public <T extends Throwable> T handleException(T throwable) {
		printException(throwable);
		
		return throwable;
	}

	/**
	 * Writes the exception's stack trace to System.err.
	 * 
	 * @param throwable an exception
	 */
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
