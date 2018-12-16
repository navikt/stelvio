package no.stelvio.provider.error.logging.support;

import java.lang.reflect.Field;

import no.stelvio.common.error.logging.ExceptionLogger;
import no.stelvio.common.error.logging.support.DefaultExceptionLogger;
import no.stelvio.dto.exception.FunctionalRecoverableDtoException;
import no.stelvio.dto.exception.FunctionalUnrecoverableDtoException;
import no.stelvio.dto.exception.SystemUnrecoverableDtoException;

/**
 * Logging class that is adapted to handle DTO exceptions. Uses default behavior in {@link ExceptionLogger} where no special
 * adaptions are needed.
 * 
 * @author person983601e0e117 (Accenture)
 * 
 */
public class DtoExceptionLogger extends DefaultExceptionLogger {

	@Override
	protected boolean shouldExtractProperties(Class declaringClass) {
		boolean shouldExtract = super.shouldExtractProperties(declaringClass);

		boolean isADtoExceptionSubclass = (SystemUnrecoverableDtoException.class.isAssignableFrom(declaringClass)
				|| FunctionalUnrecoverableDtoException.class.isAssignableFrom(declaringClass) 
				|| FunctionalRecoverableDtoException.class.isAssignableFrom(declaringClass));

		return shouldExtract || isADtoExceptionSubclass;
	}

	@Override
	protected boolean shouldFieldBeAppended(Field field) {

		boolean shouldBeAppended = true;
		Class<?> declaringClass = field.getDeclaringClass();

		// Do not append properties that are declared by classes other than Stelvio sub classes
		if (!shouldExtractProperties(declaringClass)) {
			shouldBeAppended = false;

			// Do not append TemplateArguments, this is done explicitly if they are present
		} else if (field.getName().equalsIgnoreCase("templateArguments")) {
			shouldBeAppended = false;

			// Do not append errorCodes, this is done explicitly in the message assembly
		} else if (field.getName().equalsIgnoreCase("errorcode")) {
			shouldBeAppended = false;
		}

		return shouldBeAppended;
	}

	/**
	 * Checks whether the exception class may specify template arguments used in the deprecated exception framework.
	 * 
	 * @param t throwable
	 * @return true if template arguments
	 */
	@Override
	protected boolean mayHaveTemplateArguments(Throwable t) {
		return super.mayHaveTemplateArguments(t) || t instanceof SystemUnrecoverableDtoException
				|| t instanceof FunctionalUnrecoverableDtoException || t instanceof FunctionalRecoverableDtoException;
	}

	@Override
	protected boolean isExceptionWithErrorCode(Throwable t) {
		return super.isExceptionWithErrorCode(t) || (t instanceof SystemUnrecoverableDtoException);
	}

}
