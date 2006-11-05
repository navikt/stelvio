package no.stelvio.common.error.strategy;

import no.stelvio.common.error.ExceptionHandlerStrategy;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 * @todo could use Throwable.get/setStackTrace to remove classes from the stacktrace, actually it dont hold any ref to class, but cause holds this and needs to be removed from there
 * @todo So with this, one could make an exception wrapper that returns the same output that the original exception did  
 */
public class RethrowExceptionHandlerStrategy implements ExceptionHandlerStrategy {
}
