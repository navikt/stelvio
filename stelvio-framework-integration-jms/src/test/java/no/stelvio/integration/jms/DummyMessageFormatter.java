package no.stelvio.integration.jms;

import javax.jms.Message;
import javax.jms.Session;

import no.stelvio.integration.jms.MessageFormatter;
import no.stelvio.common.FrameworkError;
import no.stelvio.common.service.ServiceFailedException;
import no.stelvio.common.service.ServiceRequest;

/**
 * 
 * @author person356941106810, Accenture
 */
public class DummyMessageFormatter implements MessageFormatter {
	
	private boolean failOnFormat = false;
	private DummyTextMessage msg = new DummyTextMessage();
	
	/* (non-Javadoc)
	 * @see no.stelvio.integration.jms.MessageFormatter#formatMessage(javax.jms.Session, no.stelvio.common.service.ServiceRequest)
	 */
	public Message formatMessage(Session session, ServiceRequest input) throws ServiceFailedException {
		if( failOnFormat ) {
			throw new ServiceFailedException(FrameworkError.JMS_INVALID_MESSAGE_TYPE);
		}
		return msg;

	}

	/* (non-Javadoc)
	 * @see no.stelvio.integration.jms.MessageFormatter#init()
	 */
	public void init() {

	}

	/**
	 * @return
	 */
	public DummyTextMessage getMsg() {
		return msg;
	}

	/**
	 * @param message
	 */
	public void setMsg(DummyTextMessage message) {
		msg = message;
	}

}
