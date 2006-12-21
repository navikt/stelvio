package no.stelvio.common.error.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.sun.beans.ObjectHandler;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

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
        handleInternal(e);
    }

    public <T extends Throwable> void rethrow(T e) throws Throwable {
        throw handleInternal(e);
    }

    public <T extends Throwable> void throwNew(Class<T> exceptionToThrow, Throwable cause, Object... params) throws Throwable {
        T se = createException(exceptionToThrow, cause, params);
        throw handleInternal(se);
    }

    public <T extends Throwable> void throwNew(Class<T> exceptionToThrow, Object... params) throws Throwable {
        T se = createException(exceptionToThrow, params);
        throw handleInternal(se);
    }

    private <T extends Throwable> T createException(Class<T> exceptionToThrow, Object... params) {
        return created(exceptionToThrow, new ArrayList<Object>(params.length), params);
    }

    private <T extends Throwable> T createException(final Class<T> exceptionToThrow, final Throwable cause, final Object... params) {
        Collection<Object> paramList = new ArrayList<Object>(params.length);
        paramList.add(cause);

        return created(exceptionToThrow, paramList, params);
    }

    /**
     * @param exceptionToThrow
     * @param paramList
     * @param params
     * @return
     * @todo rename
     */
    private <T extends Throwable> T created(Class<T> exceptionToThrow, Collection<Object> paramList, Object... params) {
        paramList.addAll(Arrays.asList(params));
        Collection<Class> paramClasses = CollectionUtils.collect(paramList, new Transformer() {
            public Object transform(Object input) {
                return input.getClass();
            }
        });

        T t;

        try {
            final Constructor<T> constructor = getConstructor(exceptionToThrow, paramClasses.toArray(new Class[paramClasses.size()]));
//            final Constructor<T> constructor = exceptionToThrow.getDeclaredConstructor(paramClasses.toArray(new Class[paramClasses.size()]));
            t = constructor.newInstance(paramList.toArray(new Object[paramList.size()]));
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("No access", e);
        } catch (InvocationTargetException e) {
            throw new IllegalStateException("Problems invoking", e);
        } catch (InstantiationException e) {
            throw new IllegalStateException("Problems instantiating", e);
        }

        return t;
    }

    /**
     * Return a constructor on the class with the arguments.
     *
     * @throws exception if the method is ambiguios.
     */
    private static Constructor getConstructor(Class cls, Class[] args) {
        Constructor constructor = null;

        // PENDING: Implement the resolutuion of ambiguities properly.
        Constructor[] ctors = cls.getDeclaredConstructors();
        for (Constructor ctor : ctors) {
            if (matchArguments(args, ctor.getParameterTypes())) {
                constructor = ctor;
            }
        }
        return constructor;
    }

    private static boolean matchArguments(Class[] argClasses,
                                          Class[] argTypes) {

        boolean match = (argClasses.length == argTypes.length);
        for (int j = 0; j < argClasses.length && match; j++) {
            Class argType = argTypes[j];
            if (argType.isPrimitive()) {
                argType = typeToClass(argType);
            }

            // Consider null an instance of all classes.
            if (argClasses[j] != null &&
                    !(argType.isAssignableFrom(argClasses[j]))) {
                match = false;
            }
        }
        return match;
    }

    public static Class typeToClass(Class type) {
        return type.isPrimitive() ? ObjectHandler.typeNameToClass(type.getName()) : type;
    }

    private <T extends Throwable> T handleInternal(T e) {
        T t;

        try {
            t = defaultStrategy.handleException(e);
        } catch (Throwable throwable) {
            // Original exception
            fallbackStrategy.handleException(e);
            // Just print out new exception (database down, etc)
            fallbackStrategy.handleException(throwable);
        }

        return t;
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
