package no.stelvio.common.error;

import no.stelvio.common.error.strategy.support.RethrowExceptionHandlerStrategy;
import no.stelvio.common.error.support.CommonExceptionLogic;
import no.stelvio.common.error.support.ExceptionToCopyHolder;

/**
 * Thrown to indicate that an unrecoverable system exception has occured. <p/>
 * Applications will typically not handle recovery from system exceptions.
 * 
 * @author person7553f5959484
 * @version $Revision: 2837 $ $Author: psa2920 $ $Date: 2006-01-09 16:12:14 +0100 (ma, 09 jan 2006) $
 * @todo change javadoc to say something about usage, see RecoverableException.
 * @todo is the name correct when the previous todo is implemented?
 * @todo should it have an abstract method, or something, maybe just use protected constructors.
 */
public abstract class SystemException extends RuntimeException implements StelvioException {
    private final CommonExceptionLogic commonExceptionLogic;

    /**
	 * Constructs a new SystemException with the specified list of templateArguments for the message template.
	 * 
	 * @param templateArguments the templateArguments to use when filling out the message template.
     */
    protected SystemException(Object... templateArguments) {
		this(null, templateArguments);
	}

    /**
     * Constructs a new SystemException with the specified cause and list of templateArguments for the message template.
	 *
	 * @param cause the cause of this exception.
     * @param templateArguments the templateArguments to use when filling out the message template.
     */
    protected SystemException(Throwable cause, Object... templateArguments) {
		super(cause);

        commonExceptionLogic = new SystemExceptionLogic(templateArguments);
    }

    /**
	 * Constructs a copy of the specified SystemException without the cause.
     * <p>
     * Is used by the framework to make a copy for rethrowing without getting class path problems with the exception
     * classes that is part of the cause stack.
	 *
	 * @param holder
     * @see RethrowExceptionHandlerStrategy
     */
	protected SystemException(ExceptionToCopyHolder holder) {
		super();

        SystemException systemException = SystemException.class.cast(holder.value());
        commonExceptionLogic = new SystemExceptionLogic(systemException.commonExceptionLogic);
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
     * Implemented by subclasses by returning the template to use for constructing the exception's message.
     *
     * @return the template to use for constructing the exception's message.
     */
    protected abstract String getMessageTemplate();

    /**
     * @todo is it okay to have a non-static inner class?
     */
    private class SystemExceptionLogic extends CommonExceptionLogic {
        public SystemExceptionLogic(Object[] templateArguments) {
            super(templateArguments);
        }

        public SystemExceptionLogic(CommonExceptionLogic cel) {
            super(cel);
        }

        protected String getMessageTemplate() {
            return SystemException.this.getMessageTemplate();
        }
    }
}
