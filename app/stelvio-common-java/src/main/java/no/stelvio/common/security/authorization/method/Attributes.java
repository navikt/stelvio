package no.stelvio.common.security.authorization.method;

public interface Attributes {
        java.util.Collection getAttributes(java.lang.Class aClass);

        java.util.Collection getAttributes(java.lang.Class aClass, java.lang.Class aClass1);

        java.util.Collection getAttributes(java.lang.reflect.Method method);

        java.util.Collection getAttributes(java.lang.reflect.Method method, java.lang.Class aClass);

        java.util.Collection getAttributes(java.lang.reflect.Field field);

        java.util.Collection getAttributes(java.lang.reflect.Field field, java.lang.Class aClass);

}
