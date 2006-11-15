package no.stelvio.common.error.strategy.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import no.stelvio.common.error.RecoverableException;
import no.stelvio.common.error.SystemException;
import no.stelvio.common.error.strategy.ExceptionHandlerStrategy;
import no.stelvio.common.error.support.Diversifier;

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
    public <T extends Exception> T handle(T exception) throws T {
        T copy = null;

        if (exception instanceof SystemException) {
            // TODO use reflection to create the real class
            Constructor<T> cons = null;
            try {
                // TODO is it possible to remove the cast here?
                cons = (Constructor<T>) exception.getClass().getDeclaredConstructor(exception.getClass(), Diversifier.class);
                cons.setAccessible(true);
                copy = cons.newInstance(exception, Diversifier.INSTANCE);
            } catch (NoSuchMethodException e) {
                // TODO correct exception? Programming error...
                throw new IllegalStateException("Need to have the correct constructor", e);
            } catch (IllegalAccessException e) {
                e.printStackTrace();  // TODO: implement body
            } catch (InvocationTargetException e) {
                e.printStackTrace();  // TODO: implement body
            } catch (InstantiationException e) {
                e.printStackTrace();  // TODO: implement body
            }
        } else if (exception instanceof RecoverableException) {
//            new RecoverableException(((RecoverableException) exception), Diversifier.INSTANCE);
        } else {
            // TODO what to do with exception not being our own, wrap them for now
//            throw new SystemException()
        }

        Exception origCopy = copy;

        for (Throwable cause = exception.getCause(); cause != null; cause = cause.getCause()) {
            ImitatorException imitator = new ImitatorException(cause);
            origCopy.initCause(imitator);
            origCopy = imitator;
        }

        throw copy;
    }
}
