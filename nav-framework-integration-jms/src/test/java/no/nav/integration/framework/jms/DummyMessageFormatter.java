package no.nav.integration.framework.jms;

import javax.jms.Message;
import javax.jms.Session;

import no.nav.integration.framework.jms.MessageFormatter;
import no.nav.common.framework.FrameworkError;
import no.nav.common.framework.service.ServiceFailedException;
import no.nav.common.framework.service.ServiceRequest;

/**
 * 
 * @author person356941106810, Accenture
 */
public class DummyMessageFormatter implements MessageFormatter {
	
	private boolean failOnFormat = false;
	private DummyTextMessage msg = new DummyTextMessage();
	
	/* (non-Javadoc)
	 * @see no.nav.integration.framework.jms.MessageFormatter#formatMessage(javax.jms.Session, no.nav.common.framework.service.ServiceRequest)
	 */
	public Message formatMessage(Session session, ServiceRequest input) throws ServiceFailedException {
		if( failOnFormat ) {
			throw new ServiceFailedException(FrameworkError.JMS_INVALID_MESSAGE_TYPE);
		}
		return msg;

	}

	/* (non-Javadoc)
	 * @see no.nav.integration.framework.jms.MessageFormatter#init()
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
