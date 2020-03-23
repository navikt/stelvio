package no.stelvio.common.security.authorization.method;

import java.lang.reflect.Method;

import no.stelvio.common.security.SecurityException;

import org.springframework.security.access.annotation.Secured;

/**
 * Exception thrown if the attribute of a <code>Secured</code> annotation is not present in a
 * <code>AnnotationAttributesMapping</code>.
 * 
 */
public class SecureAnnotationNotInMappingException extends SecurityException {

	private static final long serialVersionUID = 1L;

	private Secured annotation;

	private Class annotatedClass;

	private transient Method annotatedMethod;

	/**
	 * Constructs a <code>SecureAnnotationNotInMappingException</code> with annotation, annotated method and message.
	 * 
	 * @param annotation -
	 *            the annotation.
	 * @param annotatedMethod -
	 *            the annotated method.
	 * @param message -
	 *            the exception message.
	 */
	public SecureAnnotationNotInMappingException(Secured annotation, Method annotatedMethod, String message) {
		super(message);
		this.annotation = annotation;
		this.annotatedMethod = annotatedMethod;
	}

	/**
	 * Constructs a <code>SecureAnnotationNotInMappingException</code> with annotation, annotated class and message.
	 * 
	 * @param annotation -
	 *            the annotation.
	 * @param annotatedClass -
	 *            the annotated class.
	 * @param message -
	 *            the exception message.
	 */
	public SecureAnnotationNotInMappingException(Secured annotation, Class annotatedClass, String message) {
		super(message);
		this.annotation = annotation;
		this.annotatedClass = annotatedClass;
	}

	/**
	 * Constructs a <code>SecureAnnotationNotInMappingException</code> with message and cause.
	 * 
	 * @param message -
	 *            the exception message.
	 * @param cause -
	 *            the throwable that caused the exception to be raised.
	 */
	public SecureAnnotationNotInMappingException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a <code>SecureAnnotationNotInMappingException</code> with message.
	 * 
	 * @param message -
	 *            the exception message.
	 */
	public SecureAnnotationNotInMappingException(String message) {
		super(message);
	}

	/**
	 * Get annotated class.
	 * 
	 * @return annotaed class
	 */
	public Class getAnnotatedClass() {
		return annotatedClass;
	}

	/**
	 * Get annotation.
	 * 
	 * @return annotation
	 */
	public Secured getAnnotation() {
		return annotation;
	}

	/**
	 * Get annotation method.
	 * 
	 * @return annotation method
	 */
	public Method getAnnotatedMethod() {
		return annotatedMethod;
	}

}
