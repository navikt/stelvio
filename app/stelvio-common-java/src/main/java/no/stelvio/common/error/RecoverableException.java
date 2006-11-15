package no.stelvio.common.error;

import no.stelvio.common.error.strategy.support.RethrowExceptionHandlerStrategy;
import no.stelvio.common.error.support.CommonExceptionLogic;
import no.stelvio.common.error.support.Diversifier;

/**
 * Thrown to indicate that a recoverable exception in the application logic
 * occured. <p/> This is the base exception that all application specific
 * exceptions must extend.
 * 
 * @author person7553f5959484, Accenture
 * @version $Id: RecoverableException.java 2839 2006-04-25 10:23:10Z psa2920 $
 * @todo change javadoc to say something about only use it when the client MUST catch it else use SystemException.
 * @todo is the name correct when the previous todo is implemented? Maybe call it RecoverableExcpetion?
 */
public abstract class RecoverableException extends Exception implements StelvioException {
    private final CommonExceptionLogic commonExceptionLogic;

    /**
	 * Constructs a new RecoverableException with the specified list of getTemplateArguments for the message template.
	 * 
	 * @param templateArguments the getTemplateArguments to use when filling out the message template.
     */
    protected RecoverableException(Object... templateArguments) {
		this(null, templateArguments);
	}

    /**
     * Constructs a new RecoverableException with the specified cause and list of templateArguments for
     * the message template.
	 *
	 * @param cause the cause of this exception.
     * @param templateArguments the getTemplateArguments to use when filling out the message template.
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
	 * @param other the original exception.
     * @param diversifier just here to enable calling this constructor specifically.
     * @see RethrowExceptionHandlerStrategy
     */
	protected RecoverableException(RecoverableException other, Diversifier diversifier) {
		super();

        commonExceptionLogic = new RecoverableExceptionLogic(other.commonExceptionLogic);
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
	 *
	 * @see no.stelvio.common.error.LoggableException#getErrorId()
	 */
	public final long getErrorId() {
		return commonExceptionLogic.getErrorId();
	}

    /**
	 * {@inheritDoc}
	 *
	 * @see no.stelvio.common.error.LoggableException#getUserId()
	 */
	public final String getUserId() {
		return commonExceptionLogic.getUserId();
	}

    /**
	 * {@inheritDoc}
	 *
	 * @see no.stelvio.common.error.LoggableException#getProcessId()
	 */
	public final String getProcessId() {
		return commonExceptionLogic.getProcessId();
	}

    /**
	 * {@inheritDoc}
	 *
	 * @see no.stelvio.common.error.LoggableException#getTransactionId()
	 */
	public final String getTransactionId() {
		return commonExceptionLogic.getTransactionId();
	}

    /**
	 * {@inheritDoc}
	 *
	 * @see no.stelvio.common.error.LoggableException#getScreenId()
	 */
	public final String getScreenId() {
		return commonExceptionLogic.getScreenId();
	}

    /**
	 * {@inheritDoc}
	 *
	 * @see no.stelvio.common.error.LoggableException#getTemplateArguments()
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
     * Implemented by subclasses by returning the template to use for constructing the exception's message.
     *
     * @return the template to use for constructing the exception's message.
     */
    protected abstract String getMessageTemplate();

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

        protected String getMessageTemplate() {
            return RecoverableException.this.getMessageTemplate();
        }
    }
}