package no.stelvio.common.error.strategy.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import no.stelvio.common.error.StelvioException;
import no.stelvio.common.error.strategy.ExceptionHandlerStrategy;
import no.stelvio.common.error.support.ExceptionToCopyHolder;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 */
public class RethrowExceptionHandlerStrategy implements ExceptionHandlerStrategy {
    /**
     * @todo maybe return an object that holds what should be done
     * @param exception
     * @return
     */
    public <T extends Throwable> T handle(T exception) throws T {
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
            // TODO what to do with exception not being our own, wrap them for now
            copy = (T) new ImitatorException(exception);
        }

        Throwable origCopy = copy;

        for (Throwable cause = exception.getCause(); cause != null; cause = cause.getCause()) {
            ImitatorException imitator = new ImitatorException(cause);
            origCopy.initCause(imitator);
            origCopy = imitator;
        }

        throw copy;
    }
}
