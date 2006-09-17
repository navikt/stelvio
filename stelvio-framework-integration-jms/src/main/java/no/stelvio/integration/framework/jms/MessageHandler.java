package no.stelvio.integration.framework.jms;

import javax.jms.Message;

import no.stelvio.common.framework.service.ServiceFailedException;
import no.stelvio.common.framework.service.ServiceResponse;

/**
 * MessageHandler performs special handling of JMS messages. This can include
 * validation, parsing and transformation og the message received. 
 * 
 * @author person356941106810, Accenture
 */
public interface MessageHandler {

	/**
	 * Handles the messgage according to implemented rules.
	 * 
	 * @param msg the message to handle.
	 * @throws ServiceFailedException handling of message failed du to application error.
	 * @return the result of the handling.
	 */
	ServiceResponse handleMessage(Message msg) throws ServiceFailedException;

	/**
	 * Performs additional initialization and validation of the configuration of this
	 * MessageHandler.
	 *
	 */
	void init();
}
