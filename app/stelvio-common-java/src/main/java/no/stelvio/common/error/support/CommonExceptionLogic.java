package no.stelvio.common.error.support;

import java.io.Serializable;
import java.text.Format;
import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;

import no.stelvio.common.context.RequestContext;
import no.stelvio.common.context.RequestContextHolder;
import no.stelvio.common.security.SecurityContext;
import no.stelvio.common.security.SecurityContextHolder;
import no.stelvio.common.util.SequenceNumberGenerator;

/**
 * Helper class for root exceptions.
 *
 * @todo better javadoc
 * @todo better class name
 */
public abstract class CommonExceptionLogic implements Serializable {
    private long errorId;
    private String userId;
    private String screenId;
    private String processId;
    private String transactionId;
    private Object[] templateArguments;
    /** Says whether the exception has been logged or not */
    private boolean logged;
    private String message;

    protected CommonExceptionLogic(Object[] templateArguments) {
	    RequestContext requestContext = RequestContextHolder.currentRequestContext();
	    SecurityContext securityContext = SecurityContextHolder.currentSecurityContext();
	    init(SequenceNumberGenerator.getNextId("ErrorId"), securityContext.getUserId(), requestContext.getScreenId(),
                requestContext.getProcessId(), requestContext.getTransactionId(), templateArguments, false);
    }

    protected CommonExceptionLogic(CommonExceptionLogic cel) {
        init(cel.errorId, cel.userId, cel.screenId, cel.processId, cel.transactionId, cel.templateArguments, cel.logged);
    }

    private void init(long errorId,
                      String userId,
                      String screenId,
                      String processId,
                      String transactionId,
                      Object[] templateArguments,
                      boolean logged) {
        this.templateArguments = copyArguments(templateArguments);

        for (Object templateArgument : this.templateArguments) {
            if (null == templateArgument) {
                throw new IllegalArgumentException("None of the template arguments can be null");
            }
        }
        
        constructMessage();

        this.errorId = errorId;
        this.userId = userId;
        this.screenId = screenId;
        this.processId = processId;
        this.transactionId = transactionId;
        this.logged = logged;
    }

    /**
     * Implemented by subclasses by returning the template to use for constructing the exception's messageFrom.
     *
     * @param numArgs the number of arguments to the exception's constructor. Can be used if there is a need to
     * dynamically build a template to fit the number of arguments.
     * @return the template to use for constructing the exception's messageFrom.
     */
    protected abstract String messageTemplate(final int numArgs);

    public long getErrorId() {
        return errorId;
    }

    public String getUserId() {
        return userId;
    }

    public String getScreenId() {
        return screenId;
    }

    public String getProcessId() {
        return processId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public Object[] getTemplateArguments() {
        return copyArguments(templateArguments);
    }

    public String getMessage() {
        return message;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged() {
        logged = true;
    }

    private Object[] copyArguments(Object[] templateArguments) {
        if (null == templateArguments || templateArguments.length == 0) {
            throw new IllegalArgumentException("The template arguments cannot be empty");
        }

        Object[] copy = new Object[templateArguments.length];
        System.arraycopy(templateArguments, 0, copy, 0, templateArguments.length);

        return copy;
    }

    private void constructMessage() {
        String template = messageTemplate(templateArguments.length);

        if (StringUtils.isBlank(template)) {
            throw new IllegalArgumentException("The messageFrom template should not be empty");
        }

        MessageFormat messageFormat = new MessageFormat(template);
        Format[] formats = messageFormat.getFormats();

        if (formats.length < 1) {
            throw new IllegalArgumentException("The template must have at least 1 argument");
        }

        if (formats.length != templateArguments.length) {
            throw new IllegalArgumentException("The template arguments doesn't match template; should be " +
                    formats.length + " arguments, but is " + templateArguments.length);
        }
        
        message = messageFormat.format(templateArguments);
    }
}
