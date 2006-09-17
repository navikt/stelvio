package no.stelvio.integration.framework.jms;

import javax.jms.Message;

import no.stelvio.integration.framework.jms.MessageHandler;
import no.stelvio.common.framework.service.ServiceResponse;

/**
 * 
 * @author person356941106810, Accenture
 */
public class DummyMessageHandler implements MessageHandler {

	/* (non-Javadoc)
	 * @see no.stelvio.integration.framework.jms.MessageHandler#handleMessage(javax.jms.Message)
	 */
	public ServiceResponse handleMessage(Message msg) {
		return new ServiceResponse();
	}

	/* (non-Javadoc)
	 * @see no.stelvio.integration.framework.jms.MessageHandler#init()
	 */
	public void init() {

	}

}
