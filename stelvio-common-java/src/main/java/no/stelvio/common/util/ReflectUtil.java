package no.stelvio.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/**
 * This is a helper class for reflection tasks.
 * 
 */
public final class ReflectUtil {
	
	private static final Log LOG = LogFactory.getLog(ReflectUtil.class);

	/**
	 * Should not be instantiated.
	 */
	private ReflectUtil() {
	}

	/**
	 * Creates a new instance of the given <code>Class</code>.
	 * 
	 * @param <T>
	 *            a type variable
	 * @param clazz
	 *            the <code>Class</code> object to create a new instance of.
	 * @return a new instance of the specified class.
	 * @throws ReflectionException
	 *             if reflection failed when creating new instance.
	 */
	public static <T> T createNewInstance(Class<T> clazz) throws ReflectionException {
		try {
			return clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException ie) {
			throw createReflectionException(clazz.getName(), ie);
		}
	}

	/**
	 * Returns the given property's value from the given instance.
	 * 
	 * @param <T>
	 *            a type variable
	 * @param instance
	 *            the instance of the class to get the property from.
	 * @param property
	 *            the property to get.
	 * @return the value of the property.
	 * @throws ReflectionException
	 *             if reflection failed retrieving the property.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getPropertyFromClass(Object instance, String property) throws ReflectionException {
		try {
			return (T) PropertyUtils.getSimpleProperty(instance, property);
		} catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			throw createReflectionException(property, instance.getClass().getName(), e);
		}
	}

	/**
	 * Sets a property on a given class.
	 * 
	 * @param value
	 *            the value to set.
	 * @param instance
	 *            the instance of the class.
	 * @param property
	 *            name of the property to set.
	 * @throws NullPointerException
	 *             if null is used as a value when the property is of a primitive type.
	 * @throws ReflectionException
	 *             if reflection failed setting the property.
	 */
	public static void setPropertyOnClass(Object value, Object instance, String property) throws NullPointerException,
			ReflectionException {
		try {
			Method writeMethod = PropertyUtils.getWriteMethod(PropertyUtils.getPropertyDescriptor(instance, property));
			Class[] classes = writeMethod.getParameterTypes();
			Object valueToSet;

			if (value instanceof String) {
				String trimmedValue = StringUtils.trimTrailingWhitespace((String) value);
				valueToSet = ConvertUtils.convert(trimmedValue, classes[0]);
			} else {
				valueToSet = value;
			}

			PropertyUtils.setSimpleProperty(instance, property, valueToSet);
		} catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			throw createReflectionException(property, instance.getClass().getName(), e);
		}
	}

	/**
	 * Sets a field to the specified value. The field can be in the instance's class or one of its super class.
	 * 
	 * @param instance
	 *            the instance to set the field on.
	 * @param fieldName
	 *            the name of the field to set.
	 * @param fieldValue
	 *            the value to set on the field.
	 */
	public static void setFieldOnInstance(final Object instance, final String fieldName, final Object fieldValue) {
		ReflectionUtils.doWithFields(instance.getClass(), new ReflectionUtils.FieldCallback() {
			public void doWith(final Field field) throws IllegalArgumentException, IllegalAccessException {
				field.setAccessible(true);
				field.set(instance, fieldValue);
			}
		}, new ReflectionUtils.FieldFilter() {
			public boolean matches(final Field field) {
				return field.getName().equals(fieldName);
			}
		});
	}

	/**
	 * Retrieve the value of a field from a specified instance.
	 * 
	 * NB!! This method has not been verfied through rigorous testing and should only be used in unit tests and NOT production
	 * code!!
	 * 
	 * @param instance instance
	 * @param fieldName field name
	 * @return field value
	 */
	public static Object getFielValueFromInstance(Object instance, String fieldName) {
		try {
			Field field = instance.getClass().getDeclaredField(fieldName);
			ReflectionUtils.makeAccessible(field);
			return field.get(instance);
		} catch (Exception e) {
			throw createReflectionException(fieldName, e);
		}
	}

	/**
	 * Helper method for calling a method on an instance.
	 * 
	 * @param object
	 *            the instance to call a method on.
	 * @param methodName
	 *            the name of the method to call.
	 * @return the returned value from the invocation or null if the method is of void type.
	 * @throws ReflectionException
	 *             if reflection failed when calling method.
	 */
	public static Object callMethodOnInstance(Object object, String methodName) throws ReflectionException {
		try {
			Method method = object.getClass().getMethod(methodName);
			return method.invoke(object);
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException nsme) {
			throw createReflectionException(methodName, nsme);
		}
	}

	/**
	 * Helper method for setting the value of a field on an instance.
	 * 
	 * @param classtype
	 *            the classtype
	 * @param instance
	 *            the instance the value is set to
	 * @param fieldName
	 *            the field name to assign the value to
	 * @param value
	 *            the value to be set
	 * @throws NoSuchFieldException
	 *             if the method tries to access a field that does not exist
	 */
	public static void setField(Class classtype, Object instance, String fieldName, Object value) throws NoSuchFieldException {
		Class clazz = classtype;
		do {
			try {
				Field field = clazz.getDeclaredField(fieldName);
				field.setAccessible(true);
				field.set(instance, value);
				return;
			} catch (Exception e) {
				LOG.info("Field not set: " + clazz + "." + fieldName, e);
			}
			clazz = clazz.getSuperclass();
		} while (clazz != null);

		throw new NoSuchFieldException("No field named " + fieldName);
	}
	
	/**
	 * Creates a new ReflectionException for problems with arguments.
	 * 
	 * @param e exception
	 * @param argumentName argument name
	 * @return ReflectionException
	 */
	private static ReflectionException createReflectionException(String argumentName, Exception e) {
		return new ReflectionException("Problems doing reflection with the following arguments: '" + argumentName + "'", e);
	}
	
	/**
	 * Creates a new ReflectionException for problems with properties.
	 * 
	 * @param e exception
	 * @param propertyName property name
	 * @param className the instance
	 * @return ReflectionException
	 */
	private static ReflectionException createReflectionException(String propertyName, String className, Exception e) {
		return new ReflectionException("Problems doing reflection with the property name '" + propertyName
				+ "' on an instance of '" + className + "'", e);
	}
	
}
