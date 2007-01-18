package no.stelvio.common.error.resolver.support;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.util.Assert;

import no.stelvio.common.error.ErrorConfigurationException;
import no.stelvio.common.error.StelvioException;
import no.stelvio.common.error.resolver.ErrorDefinitionNotFoundException;
import no.stelvio.common.error.resolver.ErrorDefinitionResolver;
import no.stelvio.common.error.support.ErrorDefinition;

/**
 * Returns the <code>ErrorDefinition</code> corresponding to the given class or one of its superclasses or superinterfaces.
 *
 * @author personf8e9850ed756
 * @todo better javadoc
 * @todo this class has quite a lot of aspects now; should it be divided?
 */
public class StaticErrorDefinitionResolver implements ErrorDefinitionResolver {
    private final HashMap<Class, ErrorDefinition> errorMap;

    public StaticErrorDefinitionResolver(Collection<ErrorDefinition> errors) {
        errorMap = new HashMap<Class, ErrorDefinition>(errors.size());

        for (ErrorDefinition error : errors) {
            // TODO should another class validate that the class exist before loading the Class object here?
            // Could really only be the ErrorDefinitionResolverFactoryBean as the EJB that retrieves the errors are in another class loader
            errorMap.put(loadClass(error.getClassName()), error);
        }
    }

    /**
     * @param throwable
     * @return
     * @todo should cache the eventual mapping between clazz and the class in the errorMap; that is, so finding super
     * classes and interfaces don't have to be done each time. Maybe use the cache framework?
     */
    public ErrorDefinition resolve(Throwable throwable) {
        // TODO should we allow throwing IllegalArgumentException for coding errors? Maybe make our own version of Assert.x()?
        Assert.notNull(throwable);

        ErrorDefinition error = errorMap.get(throwable.getClass());

        if (null == error) {
            // TODO use builtin method in Commons Collections for searching a map if it exists
            for (Class classOrInterface : findSuperClassesAndInterfaces(throwable.getClass())) {
                ErrorDefinition errorInner = errorMap.get(classOrInterface);

                if (null != errorInner) {
                    error = errorInner;
                    break;
                }
            }
        }

        checkError(error, throwable);

        return error;
    }

    /**
     * Checks that the error definition fits with the exception. That is, that they have the same number of template
     * arguments.
     * <p/>
     * This will not check that a <code>StelvioException</code> has the correct amount of template arguments; this is
     * done in <code>CommonExceptionLogic</code>.
     *
     * @param error
     * @param throwable
     * @see no.stelvio.common.error.support.CommonExceptionLogic
     */
    private void checkError(ErrorDefinition error, Throwable throwable) {
        if (null == error) {
            throw new ErrorDefinitionNotFoundException(throwable.getClass());
        }

        int argsInException;

        if (throwable instanceof StelvioException) {
            argsInException = ((StelvioException) throwable).getTemplateArguments().length;
        } else {
            // TODO template for other exceptions should always have 1 argument: the message from the exception
            argsInException = 1;
        }

        checkArgsLength(error, throwable, argsInException);
    }

    private void checkArgsLength(ErrorDefinition error, Throwable throwable, final int argsInException) {
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

    private Class<?> loadClass(String className) throws IllegalStateException {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Definition of error has the wrong class name", e);
        }
    }
}
