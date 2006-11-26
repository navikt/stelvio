package no.stelvio.common.error.support;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.util.Assert;

import no.stelvio.common.error.ConfigurationException;
import no.stelvio.common.error.Err;
import no.stelvio.common.error.ErrorConfigurationException;
import no.stelvio.common.error.ErrorNotFoundException;
import no.stelvio.common.error.ErrorResolver;
import no.stelvio.common.error.StelvioException;

/**
 * Returns the <code>Err</code> corresponding to the given class or one of its superclasses or superinterfaces.
 *
 * @author personf8e9850ed756
 * @todo better javadoc
 * @todo this class has quite a lot of aspects now; should be divided?
 */
public class DefaultErrorResolver implements ErrorResolver {
    private final HashMap<Class, Err> errorMap;

    public DefaultErrorResolver(Collection<Err> errors) {
        errorMap = new HashMap<Class, Err>(errors.size());

        for (Err error : errors) {
            // TODO should another class validate that the class exist before loading the Class object here?
            // Could really only be the ErrorResolverFactoryBean as the EJB that retrieves the errors are in another class loader
            errorMap.put(loadClass(error.getClassName()), error);
        }
    }

    /**
     * @param throwable
     * @return
     * @todo should cache the eventual mapping between clazz and the class in the errorMap; that is, so finding super
     * classes and interfaces don't have to be done each time.
     */
    public Err resolve(Throwable throwable) {
        // TODO should we allow throwing IllegalArgumentException for coding errors? And not make our own version?
        Assert.notNull(throwable);

        Err error = errorMap.get(throwable.getClass());

        if (null == error) {
            // TODO use builtin method in Commons Collections for searching a map if it exists
            for (Class classOrInterface : findSuperClassesAndInterfaces(throwable.getClass())) {
                Err errorInner = errorMap.get(classOrInterface);

                if (null != errorInner) {
                    error = errorInner;
                    break;
                }
            }
        }

        checkError(error, throwable);

        return error;
    }

    private void checkError(Err error, Throwable throwable) {
        if (null == error) {
            throw new ErrorNotFoundException(throwable.getClass());
        }

        int argsInException;

        if (throwable instanceof StelvioException) {
            argsInException = ((StelvioException) throwable).getTemplateArguments().length;
        } else {
            argsInException = 0;
        }

        checkArgsLength(error, throwable, argsInException);
    }

    private void checkArgsLength(Err error, Throwable throwable, final int argsInException) {
        String message = error.getMessage();
        MessageFormat messageFormat = new MessageFormat(message);
        int argsInError = messageFormat.getFormats().length;

        if (argsInError != argsInException) {
            throw new ErrorConfigurationException(throwable.getClass(), argsInError, argsInException);
        }
    }

    /**
     * Returns a queue with all the given class' super classes and interfaces in the "order of apparence".
     *
     * @param clazz the class to find super classes and interfaces for.
     * @return an ordered queue with all the class' super classes and interfaces.
     * @todo remember to cache the findings; maybe using the cache framework?
     * @todo add synchronization, use some of the new Java 5 sync. functionality or just use the one provided by the
     * cache framework
     */
    private Set<Class> findSuperClassesAndInterfaces(Class<? extends Throwable> clazz) {
        Set<Class> classesAndInterfaces = new LinkedHashSet<Class>();
        Class<?> superClass = clazz;

        while (null != superClass) {
            addAllInterfacesFor(superClass, classesAndInterfaces);
            superClass = superClass.getSuperclass();
            classesAndInterfaces.add(superClass);
        }

        return classesAndInterfaces;
    }

    private void addAllInterfacesFor(Class<?> clazz, Set<Class> allInterfaces) {
        Class[] interfaces = clazz.getInterfaces();

        allInterfaces.addAll(Arrays.asList(interfaces));

        for (Class interfaze : interfaces) {
            addAllInterfacesFor(interfaze, allInterfaces);
        }
    }

    private Class<?> loadClass(String className) throws ConfigurationException {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            // TODO should use some kind of Error*Exception
            throw new ConfigurationException(e, "errorHandling");
        }
    }
}
