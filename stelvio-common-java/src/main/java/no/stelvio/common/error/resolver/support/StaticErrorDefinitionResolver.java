package no.stelvio.common.error.resolver.support;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import no.stelvio.common.error.resolver.ErrorDefinitionResolver;
import no.stelvio.common.error.support.ErrorDefinition;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

/**
 * Returns the <code>ErrorDefinition</code> corresponding to the given class or
 * one of its superclasses or superinterfaces.
 * 
 * @author personf8e9850ed756
 * @author person983601e0e117 
 */
public class StaticErrorDefinitionResolver implements ErrorDefinitionResolver {
	private static final Log LOG = LogFactory.getLog(StaticErrorDefinitionResolver.class);

	private final Map<Class, List<ErrorDefinition>> errorMap;

	/**
	 * Initializes a map with Throwable class as key and a list of all available
	 * ErrorDefinitions as value.
	 * 
	 * @param errors
	 *            Collection of ErrorDefinitions
	 * @throws ClassNotFoundException
	 *             class not found
	 */
	public StaticErrorDefinitionResolver(Collection<ErrorDefinition> errors) throws ClassNotFoundException {
		errorMap = new HashMap<Class, List<ErrorDefinition>>(errors.size());

		if (LOG.isDebugEnabled()) {
			LOG.debug("Creating lookup map from " + errors.size() + " error definitions");
		}

		for (ErrorDefinition error : errors) {
			// should another class validate that the class exist before
			// loading the Class object here?
			// Could really only be the ErrorDefinitionResolverFactoryBean as
			// the EJB that retrieves the errors
			// are in another class loader
			Class clazz = loadThrowableClass(error.getClassName());
			if (errorMap.get(clazz) == null) {
				errorMap.put(clazz, new ArrayList<ErrorDefinition>());
			}
			errorMap.get(clazz).add(error);
		}
	}

	/**
	 * Method that provided a Throwable resolves it's ErrorDefinition.
	 * 
	 * @param throwable
	 *            a throwable object
	 * @return ErrorDefinition for the specified Throwable, <code>null</code> if
	 *         no match is found 
	 */
	public ErrorDefinition resolve(Throwable throwable) {
		Assert.notNull(throwable);

		if (LOG.isDebugEnabled()) {
			LOG.debug("Trying to resolve throwable [" + throwable.getClass().getName() + "]");
		}

		// Gets list of ErrorDefinition for this specific Throwable class
		List<ErrorDefinition> errorList = errorMap.get(throwable.getClass());
		ErrorDefinition errorDefinition = null;

		// If no ErrorDefinition available for specific class, look for
		// definitions for superclass/interfaces
		if (null == errorList || errorList.size() == 0) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Definition for " + throwable.getClass().getName() + " not found; "
						+ "searching super classes and interfaces");
			}
			errorList = findErrorDefinitionListForSuperclass(throwable);
		}

		// Provided a list of ErrorDefinition, find the one that matches
		// Throwable instance
		errorDefinition = findErrorDefinitionForThrowable(throwable, errorList);

		return errorDefinition;
	}

	/**
	 * Provided a throwable and a list of ErrorDefinitions for this throwable
	 * the method will find the ErrorDefinition that matches the supplied
	 * throwable's number of arguments.
	 * 
	 * @param throwable
	 *            a Throwable object
	 * @param errorDefinitionList
	 *            an error definition
	 * @return <code>ErrorDefinition</code> if a match is present in the
	 *         supplied list otherwise <code>null</code>
	 */
	private ErrorDefinition findErrorDefinitionForThrowable(Throwable throwable, List<ErrorDefinition> errorDefinitionList) {
		// If parameters are null or empty, there can be no match
		if (throwable == null || errorDefinitionList == null || errorDefinitionList.size() == 0) {
			return null;
		}
		
		ErrorDefinition definition = null;
		for (ErrorDefinition errorDefinition : errorDefinitionList) {
			if (isMatch(throwable, errorDefinition)) {
				definition = errorDefinition;
				break;
			}
		}
		return definition;
	}

	/**
	 * Checks if the Throwable instance is a match for the supplied
	 * ErrorDefinition. This is done by comparing the number of arguments
	 * specified in the ErrorDefinition message is the same as the number of
	 * arguments passed to the Throwable instance.
	 * 
	 * @param throwable
	 *            a Throwable object
	 * @param errorDefinition
	 *            an error definition
	 * @return <code>true</code> if definition is a match for the trowable,
	 *         otherwise <code>false</code>
	 */
	private boolean isMatch(Throwable throwable, ErrorDefinition errorDefinition) {
		int argsInException = 1;

		String message = errorDefinition.getMessage();
		MessageFormat messageFormat = new MessageFormat(message);
		int argsInErrorDefinition = messageFormat.getFormats().length;

		return (argsInErrorDefinition == argsInException);

	}

	/**
	 * Given a throwable returns a list of ErrorDefinitions for classes the
	 * throwable extends and/or interfaces the throwable implements.
	 * 
	 * @param throwable
	 *            a Throwable object
	 * @return List of ErrorDefinition
	 */
	private List<ErrorDefinition> findErrorDefinitionListForSuperclass(Throwable throwable) {
		// use builtin method in Commons Collections for searching a map if
		// it exists
		
		List<ErrorDefinition> definitions = null;
		for (Class classOrInterface : findSuperClassesAndInterfaces(throwable.getClass())) {
			List<ErrorDefinition> errorList = errorMap.get(classOrInterface);

			if (null != errorList && errorList.size() != 0) {
				definitions = errorList;
				break;
			}
		}
		return definitions;
	}

	/**
	 * Returns a queue with all the given class' super classes and interfaces in
	 * the "order of apparence".
	 * 
	 * add synchronization, use some of the new Java 5
	 * sync. functionality or just use the one provided by the cache
	 * framework
	 *         
	 * @param clazz
	 *            the class to find super classes and interfaces for.
	 * @return an ordered queue with all the class' super classes and
	 *         interfaces. 
	 *         
	 */
	private Set<Class> findSuperClassesAndInterfaces(Class<? extends Throwable> clazz) {
		Set<Class> classesAndInterfaces = new LinkedHashSet<Class>();
		Class<?> superClass = clazz;

		while (null != superClass) {
			addAllInterfacesFor(superClass, classesAndInterfaces);
			superClass = superClass.getSuperclass();
			classesAndInterfaces.add(superClass);
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("Found the following super classes and interfaces: " + classesAndInterfaces);
		}

		return classesAndInterfaces;
	}

	/**
	 * Adds all the interfaces of the class, and its parents to the interfaces
	 * set.
	 * 
	 * @param clazz
	 *            the class to be processed
	 * @param allInterfaces
	 *            the interfaces set
	 */
	private void addAllInterfacesFor(Class<?> clazz, Set<Class> allInterfaces) {
		Class[] interfaces = clazz.getInterfaces();

		allInterfaces.addAll(Arrays.asList(interfaces));

		for (Class interfaze : interfaces) {
			addAllInterfacesFor(interfaze, allInterfaces);
		}
	}

	/**
	 * Returns a throwable class from the className.
	 * 
	 * @param className
	 *            the name of the class to be returned
	 * @return the throwable class
	 * @throws ClassNotFoundException
	 *             class not found
	 */
	private Class<?> loadThrowableClass(String className) throws ClassNotFoundException {
		Class<?> throwableClass;

		if (LOG.isDebugEnabled()) {
			LOG.debug("Trying to load class [" + className + "]");
		}

		throwableClass = Class.forName(className, true, Thread.currentThread().getContextClassLoader());

		return throwableClass;
	}
}
