package no.stelvio.business.model.pidnum;

import no.stelvio.common.error.FunctionalUnrecoverableException;
import no.stelvio.common.error.support.ExceptionToCopyHolder;

/**
 * Exception thrown when PidNum can't be created due to validation failure.
 * 
 * @author person983601e0e117, Accenture
 *
 * @see PidNum
 *
 */
public class PidNumException extends FunctionalUnrecoverableException {

	public PidNumException(ExceptionToCopyHolder holder) {
		super(holder);
	}

	public PidNumException(String pidNum) {
		super(pidNum);
	}

	public PidNumException(Throwable cause, String pidNum) {
		super(cause, pidNum);
	}

	/**
	 * {@inheritDoc no.stelvio.common.error.FunctionalUnrecoverableException}
	 */
	@Override
	protected String messageTemplate(int numArgs) {
		StringBuffer template = new StringBuffer("PidNum could not be created");
		template.append(" {0}").append(" is not a valid personal identification number");
		return template.toString();
	}

}
