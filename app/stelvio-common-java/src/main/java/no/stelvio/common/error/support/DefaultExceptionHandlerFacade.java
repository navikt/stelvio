package no.stelvio.common.error.support;

import no.stelvio.common.error.ExceptionHandlerFacade;
import no.stelvio.common.error.StelvioException;
import no.stelvio.common.error.strategy.ExceptionHandlerStrategy;
import no.stelvio.common.error.strategy.support.FallbackExceptionHandlerStrategy;

import java.util.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 */
public class DefaultExceptionHandlerFacade implements ExceptionHandlerFacade {
    // TODO javadoc... default is FallBack...
    private ExceptionHandlerStrategy fallbackStrategy = new FallbackExceptionHandlerStrategy();
    private ExceptionHandlerStrategy defaultStrategy;

    public <T extends Throwable> void handle(T e) {
        try {
            defaultStrategy.handleException(e);
        } catch (Throwable throwable) {
	        // Original exception
            fallbackStrategy.handleException(e);
	        // Just print out new exception (database down, etc)
            fallbackStrategy.handleException(throwable);
        }
    }


	public <T extends Throwable> void rethrow(T e) throws Throwable {
		T t;

		try {
		    t = defaultStrategy.handleException(e);
		} catch (Throwable throwable) {
			// Original exception
		    t = fallbackStrategy.handleException(e);
			// Just print out new exception (database down, etc)
		    fallbackStrategy.handleException(throwable);
		}

		throw t;
	}

	public <T extends Throwable> void throwNew(Class<T> exceptionToThrow, Class<? extends StelvioException> cause, Object... params) throws Throwable {
		T se = createException(exceptionToThrow, cause, params);
		T t;

		try {
			t = defaultStrategy.handleException(se);
		} catch (Throwable throwable) {
			// Original exception
			t = fallbackStrategy.handleException(se);
			// Just print out new exception (database down, etc)
			fallbackStrategy.handleException(throwable);
		}

		throw t;
	}

	private <T extends Throwable> T createException(final Class<T> exceptionToThrow, final Class<? extends StelvioException> cause, final Object... params) {
		final List<Class> classes = new ArrayList<Class>();
		classes.add(cause.getClass());

		for (Object param : params) {
			classes.add(param.getClass());
		}

		T t = null;
		try {
			final Constructor<T> constructor = exceptionToThrow.getConstructor(classes.toArray(new Class[classes.size()]));
			t = constructor.newInstance(cause, params);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();  // TODO: implement body
		} catch (IllegalAccessException e) {
			e.printStackTrace();  // TODO: implement body
		} catch (InvocationTargetException e) {
			e.printStackTrace();  // TODO: implement body
		} catch (InstantiationException e) {
			e.printStackTrace();  // TODO: implement body
		}

		return t;
	}

	public void setFallbackStrategy(ExceptionHandlerStrategy fallbackStrategy) {
        this.fallbackStrategy = fallbackStrategy;
    }

    public void setDefaultStrategy(ExceptionHandlerStrategy defaultStrategy) {
        this.defaultStrategy = defaultStrategy;
    }
}
