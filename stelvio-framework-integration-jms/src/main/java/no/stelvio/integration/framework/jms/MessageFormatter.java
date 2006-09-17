package no.stelvio.integration.framework.jms;

import javax.jms.Message;
import javax.jms.Session;

import no.stelvio.common.framework.service.ServiceFailedException;
import no.stelvio.common.framework.service.ServiceRequest;

/**
 * Formats a message so that it is understandable by a backend system.
 * 
 * @author person356941106810, Accenture
 */
public interface MessageFormatter {
	
	/**
	 * Format a message according to internal and backend system rules.
	 * 
	 * @param session the session to use for creating messages.
	 * @param input the input to format.
	 * @throws ServiceFailedException the formatting failed due to application error.
	 * @return the finshed JMS message.
	 */
	Message formatMessage( Session session, ServiceRequest input ) throws ServiceFailedException;
	
	/**
	 * Performs additional initialization and validation of the configuration of this
	 * MessageFormatter.
	 *
	 */
	void init();
	
}
