package no.stelvio.common.error.strategy;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 * @todo refactor common tests inta an abstract test class for the interface.
 */
public interface ExceptionHandlerStrategy {
    <T extends Throwable> T handleException(T e) throws T;
}
