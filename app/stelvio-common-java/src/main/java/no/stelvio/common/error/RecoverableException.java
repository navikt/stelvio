package no.stelvio.common.error;

import no.stelvio.common.error.strategy.support.MorpherExceptionHandlerStrategy;
import no.stelvio.common.error.support.CommonExceptionLogic;
import no.stelvio.common.error.support.ExceptionToCopyHolder;

/**
 * Thrown to indicate that a recoverable exception in the application logic
 * occured. <p/> This is the base exception that all application specific
 * exceptions must extend.
 * 
 * @author person7553f5959484, Accenture
 * @version $Id: RecoverableException.java 2839 2006-04-25 10:23:10Z psa2920 $
 * @todo change javadoc to say something about only use it when the client MUST catch it else use UnrecoverableException.
 * @todo is the name correct when the previous todo is implemented? Maybe call it RecoverableExcpetion?
 */
abstract class RecoverableException extends Exception implements StelvioException {
    private final CommonExceptionLogic commonExceptionLogic;

    /**
	 * Constructs a new RecoverableException with the specified list of getTemplateArguments for the messageFrom template.
	 * 
	 * @param templateArguments the getTemplateArguments to use when filling out the messageFrom template.
     */
    protected RecoverableException(Object... templateArguments) {
		this(null, templateArguments);
	}

    /**
     * Constructs a new RecoverableException with the specified cause and list of templateArguments for
     * the messageFrom template.
	 *
	 * @param cause the cause of this exception.
     * @param templateArguments the getTemplateArguments to use when filling out the messageFrom template.
     */
    protected RecoverableException(Throwable cause, Object... templateArguments) {
		super(cause);

        commonExceptionLogic = new RecoverableExceptionLogic(templateArguments);
    }

    /**
	 * Constructs a copy of the specified RecoverableException without the cause.
     * <p>
     * Is used by the framework to make a copy for rethrowing without getting class path problems with the exception
     * classes that is part of the cause stack.
	 *
	 * @param holder
     * @see MorpherExceptionHandlerStrategy
     */
	protected RecoverableException(ExceptionToCopyHolder holder) {
		super();

        RecoverableException recoverableException = RecoverableException.class.cast(holder.value());
        commonExceptionLogic = new RecoverableExceptionLogic(recoverableException.commonExceptionLogic);
    }

    /**
	 * {@inheritDoc}
	 *
	 * @see no.stelvio.common.error.LoggableException#isLogged()
	 */
	public final boolean isLogged() {
		return commonExceptionLogic.isLogged();
	}

    /**
	 * {@inheritDoc}
	 *
	 * @see no.stelvio.common.error.LoggableException#setLogged()
	 */
	public final void setLogged() {
		commonExceptionLogic.setLogged();
	}

    /**
	 * {@inheritDoc}
	 */
	public final long getErrorId() {
		return commonExceptionLogic.getErrorId();
	}

    /**
	 * {@inheritDoc}
	 */
	public final String getUserId() {
		return commonExceptionLogic.getUserId();
	}

    /**
	 * {@inheritDoc}
	 */
	public final String getProcessId() {
		return commonExceptionLogic.getProcessId();
	}

    /**
	 * {@inheritDoc}
	 */
	public final String getTransactionId() {
		return commonExceptionLogic.getTransactionId();
	}

    /**
	 * {@inheritDoc}
	 */
	public final String getScreenId() {
		return commonExceptionLogic.getScreenId();
	}

    /**
	 * {@inheritDoc}
	 */
	public final Object[] getTemplateArguments() {
        return commonExceptionLogic.getTemplateArguments();
	}

    /**
	 * Gets a String representation of the error code.
	 *
	 * {@inheritDoc}
	 *
	 * @see java.lang.Throwable#getMessage()
     * @todo not correct in new version
	 */
	@Override
    public final String getMessage() {
		return commonExceptionLogic.getMessage();
	}

    /**
	 * Returns a short description of this exception instance, containing the
	 * following:
	 * <ul>
	 * <li>The name of the actual class of this object
	 * <li>The getTemplateArguments this object was created with
	 * </ul>
	 *
	 * {@inheritDoc}
	 *
	 * @see Object#toString()
	 */
	@Override
    public final String toString() {
		StringBuffer sb = new StringBuffer(getClass().getName());
		sb.append(": ErrId=").append(commonExceptionLogic.getErrorId());
		sb.append(",Screen=").append(commonExceptionLogic.getScreenId());
		sb.append(",Process=").append(commonExceptionLogic.getProcessId());
		sb.append(",TxId=").append(commonExceptionLogic.getTransactionId());
		sb.append(",User=").append(commonExceptionLogic.getUserId());
		sb.append(",Message=").append(commonExceptionLogic.getMessage());

        return sb.toString();
	}

    /**
     * Implemented by subclasses by returning the template to use for constructing the exception's messageFrom.
     *
     * @param numArgs the number of arguments to the exception's constructor. Can be used if there is a need to
     * dynamically build a template to fit the number of arguments.
     * @return the template to use for constructing the exception's messageFrom.
     */
    protected abstract String messageTemplate(final int numArgs);

    /**
     * @todo is it okay to have a non-static inner class?
     */
    private class RecoverableExceptionLogic extends CommonExceptionLogic {
        public RecoverableExceptionLogic(Object[] templateArguments) {
            super(templateArguments);
        }

        public RecoverableExceptionLogic(CommonExceptionLogic cel) {
            super(cel);
        }

        protected String messageTemplate(final int numArgs) {
            return RecoverableException.this.messageTemplate(numArgs);
        }
    }
}