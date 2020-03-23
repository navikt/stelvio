package no.stelvio.presentation.binding.context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.application.FacesMessage;

import no.stelvio.presentation.jsf.context.FieldInListError;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.binding.message.DefaultMessageContext;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.binding.message.MessageResolver;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;

/**
 * MessageUtil to simplify setting messages on the context.
 * 
 * @version $Id$
 */
public final class MessageContextUtil {

	/** Logger. */
	private static final Log LOGGER = LogFactory.getLog(MessageContextUtil.class);

	/**
	 * Class should never be instantiated.
	 */
	private MessageContextUtil() {

	}

	/**
	 * Setting a global message on the message context.
	 * 
	 * @param message
	 *            the global message to put on context
	 */
	public static void setGlobalMessageOnMessageContext(String message) {
		setMessageOnMessageContext(null, message);
	}

	/**
	 * Setting a message on the context with a componentId.
	 * 
	 * @param componentId
	 *            the id of the <code>h:message</code> telling where to show the message. 
	 *            using componentId=<code>null</code> will set a global message
	 * @param message the message to put on the context
	 */
	public static void setMessageOnMessageContext(String componentId, String message) {
		String defaultMessage = null;
		boolean messageEqualsDefaultMessage = false;
		if (message != null) {
			String msg = getMessage(message, null, true);
			if (message.equalsIgnoreCase(msg)) {
				defaultMessage = message;
				messageEqualsDefaultMessage = true;
			} else {
				defaultMessage = msg;
			}
		}

		setMessageOnMessageContext(componentId, messageEqualsDefaultMessage ? null : message, null, defaultMessage);
	}

	/**
	 * Set global message on message context.
	 * 
	 * @param code code
	 * @param arguments arguments
	 */
	public static void setGlobalMessageOnMessageContext(String code, Object[] arguments) {
		setMessageOnMessageContext(null, code, arguments);
	}

	/**
	 * Set global message on message context.
	 * 
	 * @param componentId component id.
	 * @param code code
	 * @param arguments arguments
	 */
	public static void setMessageOnMessageContext(String componentId, String code, Object[] arguments) {
		setMessageOnMessageContext(componentId, code, arguments, null);
	}

	/**
	 * Set global message on message context.
	 * 
	 * @param componentId component id
	 * @param code code
	 * @param arguments arguments
	 * @param defaultMessage default message
	 */
	public static void setMessageOnMessageContext(String componentId, String code, Object[] arguments, String defaultMessage) {
		if (LOGGER.isDebugEnabled()) {
			String argumentsString = arguments != null ? Arrays.toString(arguments) : null;
			LOGGER.debug("Message is set on componentId '" + componentId + "' (null=Global message) with code: " + code
					+ ", arguments: " + argumentsString + " defaultMessage: " + defaultMessage);
		}

		MessageResolver messageResolver = new MessageBuilder().error().source(componentId).code(code).args(
				getArgMessages(arguments)).defaultText(defaultMessage).build();
		MessageContext messageContext = getMessageContext();
		messageContext.addMessage(messageResolver);
	}

	/**
	 * Adds messages to the current message context based on a set of Spring validation errors. Runs through all errors in
	 * <code>Errors</code> object, and adds formatted messages to the MessageContext. Supports both field errors and global
	 * errors. FieldErrors are tied to the associated JSF <code>UIComponent</code>.
	 * 
	 * @param errors -
	 *            Spring Errors object to retrieve Faces messages from
	 */
	@SuppressWarnings("unchecked")
	public static void addMessagesOnMessageContext(Errors errors) {
		List<ObjectError> objectErrors = errors.getAllErrors();
		for (ObjectError objError : objectErrors) {
			String componentId = null;

			/* Look up UIComponent if field error. */
			/* Necessary due to <h:message for="" /> */
			if (objError instanceof FieldInListError) {
				StringBuffer hackedComponentId = new StringBuffer();
				hackedComponentId.append(((FieldInListError) objError).getListIndex() + FieldInListError.SEPERATOR);
				hackedComponentId.append(((FieldError) objError).getField());

				componentId = hackedComponentId.toString();
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("FieldInListError has been found with field id: " + ((FieldError) objError).getField());
				}
			} else if (objError instanceof FieldError) {
				componentId = ((FieldError) objError).getField();
			}

			setMessageOnMessageContext(componentId, objError.getCode(), objError.getArguments(), objError.getDefaultMessage());
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Error has been set on messageContext with componentId: " + componentId);
			}
		}
	}

	/**
	 * Retrieves a message from the messageSource using a specified key.
	 * 
	 * @param messageKey
	 *            the key used to retrieve the message form the messageSource
	 * @return a message from the messageSource
	 */
	public static String getMessage(String messageKey) {
		return getMessage(messageKey, null);
	}

	/**
	 * Retrieves a message from the messageSource using a specified key.
	 * 
	 * @param messageKey
	 *            the key used to retrieve the message form the messageSource
	 * @param args
	 *            arguments to be inserted into the message
	 * @return a message from the messageSource
	 */
	public static String getMessage(String messageKey, Object[] args) {
		return getMessage(messageKey, getArgMessages(args), true);
	}

	/**
	 * Retrieves a message from the messageSource using a specified key.
	 * 
	 * @param messageKey
	 *            the key used to retrieve the message form the messageSource
	 * @param args
	 *            arguments to be inserted into the message
	 * @param useCodeAsDefault
	 *            use messageKey as default message if can't find a message in messageSource
	 * @return a message from the messageSource
	 */
	public static String getMessage(String messageKey, Object[] args, boolean useCodeAsDefault) {

		String defaultMessage = null;
		if (useCodeAsDefault) {
			defaultMessage = messageKey;
		}

		MessageSource messageSource = getMessageSource();
		return messageSource.getMessage(messageKey, getArgMessages(args), defaultMessage, LocaleContextHolder.getLocale());
	}

	/**
	 * Looks up a message creates a FacesMessage instance.
	 * 
	 * @param messageKey -
	 *            key of message to look up
	 * @param args -
	 *            arguments to be binded to message
	 * @return FacesMessage with the message looked up in resource bundle
	 */
	public static FacesMessage getFacesMessage(String messageKey, Object[] args) {
		String message = getMessage(messageKey, args);
		return new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
	}

	/**
	 * Get argument messages.
	 * 
	 * @param arguments arguments
	 * @return messages
	 */
	private static Object[] getArgMessages(Object[] arguments) {

		List<String> argMessages = new ArrayList<String>();
		if (arguments != null) {
			for (Object argument : arguments) {
				argMessages.add(getMessage(argument.toString(), null, true));
			}
		}

		return argMessages.toArray();
	}

	/**
	 * Get message context.
	 * 
	 * @return message context
	 */
	private static MessageContext getMessageContext() {
		RequestContext requestContext = RequestContextHolder.getRequestContext();
		MessageContext messageContext = requestContext.getMessageContext();
		return messageContext;
	}

	/**
	 * Get message source.
	 * 
	 * @return message source
	 */
	private static MessageSource getMessageSource() {
		DefaultMessageContext messageContext = (DefaultMessageContext) getMessageContext();
		MessageSource messageSource = messageContext.getMessageSource();
		return messageSource;
	}

}
