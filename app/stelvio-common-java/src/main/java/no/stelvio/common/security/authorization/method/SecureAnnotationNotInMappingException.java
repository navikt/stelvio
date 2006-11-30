package no.stelvio.common.security.authorization.method;

import java.lang.reflect.Method;

import no.stelvio.common.security.SecurityException;

import org.acegisecurity.annotation.Secured;

/**
 * Exception thrown if the attribute of a <code>Secured</code> annotation is
 * not present in a <code>AnnotationAttributesMapping</code>.
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 */
public class SecureAnnotationNotInMappingException extends SecurityException {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates an exception with the supplied parameters in the message.
	 * 
	 * @param annotation
	 *            the annotation.
	 * @param annotatedClass
	 *            the annotated class.
	 */
	public SecureAnnotationNotInMappingException(Secured annotation,
			Class annotatedClass) {
		super(annotation, annotatedClass);
	}

	/**
	 * Creates an exception with the supplied parameters in the message.
	 * 
	 * @param annotation
	 *            the annotation.
	 * @param annotatedMethod
	 *            the annotated method.
	 */
	public SecureAnnotationNotInMappingException(Secured annotation,
			Method annotatedMethod) {
		super(annotation, annotatedMethod);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String messageTemplate() {
		return "The @Secured annotation attribute {0} for {1} was not found in the annotation mapping.";
	}

}
