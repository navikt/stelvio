package no.stelvio.domain.person;

import no.stelvio.common.error.FunctionalUnrecoverableException;
import no.stelvio.common.error.support.ExceptionToCopyHolder;

/**
 * Exception thrown when Pid can't be created due to validation failure.
 * 
 * @author person983601e0e117, Accenture
 *
 * @see Pid
 *
 */
public class PidValidationException extends FunctionalUnrecoverableException {

	public PidValidationException(ExceptionToCopyHolder holder) {
		super(holder);
	}

	public PidValidationException(String pidNum) {
		super(pidNum);
	}

	public PidValidationException(Throwable cause, String pidNum) {
		super(cause, pidNum);
	}

	/**
	 * {@inheritDoc no.stelvio.common.error.FunctionalUnrecoverableException}
	 */
	@Override
	protected String messageTemplate(int numArgs) {
		StringBuffer template = new StringBuffer("Pid validation failed,");
		template.append(" {0}").append(" is not a valid personal identification number");
		return template.toString();
	}

}
