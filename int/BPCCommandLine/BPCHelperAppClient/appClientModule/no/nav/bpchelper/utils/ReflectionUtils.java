package no.nav.bpchelper.utils;

import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;

public class ReflectionUtils {
    public static Method getReadMethod(Class<?> beanClass, String propertyName) {
	StringBuilder methodName = new StringBuilder("get").append(StringUtils.capitalize(propertyName));
	try {
	    return beanClass.getMethod(methodName.toString(), (Class[]) null);
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

    public static Object invokeMethod(Method method, Object object) {
	try {
	    return method.invoke(object, (Object[]) null);
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }
}
