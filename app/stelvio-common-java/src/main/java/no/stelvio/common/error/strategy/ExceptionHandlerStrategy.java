package no.stelvio.common.error.strategy;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 * @todo refactor common tests inta an abstract test class for the interface.
 */
public interface ExceptionHandlerStrategy {
    /**
     *
     * @param e
     * @return
     * @throws T
     */
    <T extends Throwable> T handleException(T e);
}
