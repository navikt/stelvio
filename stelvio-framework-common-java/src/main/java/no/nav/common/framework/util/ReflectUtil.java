package no.nav.common.framework.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;

import no.nav.common.framework.FrameworkError;
import no.nav.common.framework.error.SystemException;

/**
 * This class is a statical helper for doing reflection tasks.
 * 
 * @author person5b7fd84b3197, Accenture
 *
 */
public final class ReflectUtil {
	/**
	 * Should not be instantiated.
	 */
	private ReflectUtil() {
	}

	/**
	 * Gets a new instance of a given class.
	 *
	 * @param clazz Class object to call newInstance on
	 * @return the result of Class.newInstance
	 */
	public static Object getNewInstance(Class clazz) {
		Object result;

		try {
			result = clazz.newInstance();
		} catch (InstantiationException ie) {
			throw new SystemException(FrameworkError.REFLECTION_INSTANTIATION_ERROR, ie, clazz.getName());
		} catch (IllegalAccessException iae) {
			throw new SystemException(FrameworkError.REFLECTION_INSTANTIATION_ERROR, iae, clazz.getName());
		}

		return result;
	}

	/**
	 * Method gets a property form a given class.
	 * This method uses reflection to get property from a class
	 * 
	 * @return Object - The value of the property
	 * @param instance - The instance of the class to get it form
	 * @param prop - The proerty to get
	 */
	public static Object getPropertyFromClass(Object instance, String prop) {
		try {
			return PropertyUtils.getSimpleProperty(instance, prop);
		} catch (IllegalAccessException e) {
			throw new SystemException(
				FrameworkError.REFLECTION_GET_PROPERTY_ERROR,
				e,
				new String[] { prop, instance.getClass().getName()});
		} catch (InvocationTargetException e) {
			throw new SystemException(
				FrameworkError.REFLECTION_GET_PROPERTY_ERROR,
				e,
				new String[] { prop, instance.getClass().getName()});
		} catch (NoSuchMethodException e) {
			throw new SystemException(
				FrameworkError.REFLECTION_GET_PROPERTY_ERROR,
				e,
				new String[] { prop, instance.getClass().getName()});
		}
	}

	/**
	 * This method sets a property on a given class by using reflection.
	 *
	 * @param valueIn - The value to set
	 * @param instance - The instance of the class
	 * @param property - Name of the property to set
	 * @throws NullPointerException if null is used as a value when the property is of a primitive type. 
	 */
	public static void setPropertyOnClass(Object valueIn, Object instance, String property) throws NullPointerException {
		try {
			Method writeMethod = PropertyUtils.getWriteMethod(PropertyUtils.getPropertyDescriptor(instance, property));
			Class[] classes = writeMethod.getParameterTypes();
			Object val;

			if (valueIn instanceof String) {
				String value = StringHelper.removeEndingSpaces(valueIn.toString());
				val = ConvertUtils.convert(value, classes[0]);
			} else {
				val = valueIn;
			}

			PropertyUtils.setSimpleProperty(instance, property, val);
		} catch (IllegalAccessException e) {
			throw new SystemException(
				FrameworkError.REFLECTION_SET_PROPERTY_ERROR,
				e,
				new String[] { property, instance.getClass().getName()});
		} catch (InvocationTargetException e) {
			throw new SystemException(
				FrameworkError.REFLECTION_SET_PROPERTY_ERROR,
				e,
				new String[] { property, instance.getClass().getName()});
		} catch (NoSuchMethodException e) {
			throw new SystemException(
				FrameworkError.REFLECTION_SET_PROPERTY_ERROR,
				e,
				new String[] { property, instance.getClass().getName()});
		}
	}

	/**
	 * Helper method for calling a method on an instance.
	 *
	 * @param object the instance to call a method on.
	 * @param methodName the name of the method to call.
	 * @return the returned value from the invocation or null if the method is of void type.
	 */
	public static Object callMethodOnInstance(Object object, String methodName) {
		try {
			Method method = object.getClass().getMethod(methodName, null);
			return method.invoke(object, null);
		} catch (NoSuchMethodException nsme) {
			throw new SystemException(FrameworkError.SYSTEM_UNAVAILABLE_ERROR, nsme, methodName);
		} catch (IllegalAccessException iae) {
			throw new SystemException(FrameworkError.SYSTEM_UNAVAILABLE_ERROR, iae, methodName);
		} catch (InvocationTargetException ite) {
			throw new SystemException(FrameworkError.SYSTEM_UNAVAILABLE_ERROR, ite, methodName);
		}
	}
}
