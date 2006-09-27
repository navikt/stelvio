package no.stelvio.integration.jms;

import javax.jms.Message;

import no.stelvio.integration.jms.MessageHandler;
import no.stelvio.common.service.ServiceResponse;

/**
 * 
 * @author person356941106810, Accenture
 */
public class DummyMessageHandler implements MessageHandler {

	/* (non-Javadoc)
	 * @see no.stelvio.integration.jms.MessageHandler#handleMessage(javax.jms.Message)
	 */
	public ServiceResponse handleMessage(Message msg) {
		return new ServiceResponse();
	}

	/* (non-Javadoc)
	 * @see no.stelvio.integration.jms.MessageHandler#init()
	 */
	public void init() {

	}

}
