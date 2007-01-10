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

	/**
	 * The id used to check version of object when serializing.
	 */
	private static final long serialVersionUID = 6146570031382768191L;

	/**
	 * {@inheritDoc no.stelvio.common.error.FunctionalUnrecoverableException}
	 */
	public PidValidationException(ExceptionToCopyHolder holder) {
		super(holder);
	}

	/**
	 * Constructor.
	 * @param pid Pid value that failed validation.
	 */
	
	public PidValidationException(String pid) {
		super(pid);
	}

	/**
	 * Constructor.
	 * @param cause Exception root cause
	 * @param pid Pid value thast failed validation 
	 */
	
	public PidValidationException(Throwable cause, String pid) {
		super(cause, pid);
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
