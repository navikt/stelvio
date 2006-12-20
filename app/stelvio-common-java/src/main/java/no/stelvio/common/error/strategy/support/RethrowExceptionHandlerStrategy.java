package no.stelvio.common.error.strategy.support;

import no.stelvio.common.error.StelvioException;
import no.stelvio.common.error.strategy.ExceptionHandlerStrategy;
import no.stelvio.common.error.support.ExceptionToCopyHolder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 * @todo not the correct name; doesn't throw the exception itself, only creates a new instance of it and removing causes
 * @todo maybe morpher or something would be a better name
 */
public class RethrowExceptionHandlerStrategy implements ExceptionHandlerStrategy {
    /**
     * @param exception
     * @return
     */
    public <T extends Throwable> T handleException(T exception) {
        T copy;

        if (exception instanceof StelvioException) {
            try {
                // TODO is it possible to remove the cast here?
                Constructor<T> cons =
                        (Constructor<T>) exception.getClass().getDeclaredConstructor(ExceptionToCopyHolder.class);
                cons.setAccessible(true);
                copy = cons.newInstance(new ExceptionToCopyHolder<StelvioException>((StelvioException) exception));
            } catch (NoSuchMethodException e) {
                // TODO correct exceptionS? Programming error...
                throw new IllegalStateException("Need to have the correct constructor", e);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Need to have the correct constructor", e);
            } catch (InvocationTargetException e) {
                throw new IllegalStateException("Need to have the correct constructor", e);
            } catch (InstantiationException e) {
                throw new IllegalStateException("Need to have the correct constructor", e);
            }
        } else {
            // TODO maybe create an exception to hold exceptions not being our own or expand Imitator to extend SystemUnrecoverableException
            copy = (T) new ImitatorException(exception);
        }

        Throwable origCopy = copy;

        for (Throwable cause = exception.getCause(); cause != null; cause = cause.getCause()) {
            ImitatorException imitator = new ImitatorException(cause);
            origCopy.initCause(imitator);
            origCopy = imitator;
        }

        return copy;
    }
}
