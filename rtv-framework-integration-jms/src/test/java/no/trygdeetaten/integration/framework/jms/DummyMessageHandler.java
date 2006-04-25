package no.trygdeetaten.integration.framework.jms;

import javax.jms.Message;

import no.trygdeetaten.common.framework.service.ServiceResponse;

/**
 * 
 * @author person356941106810, Accenture
 */
public class DummyMessageHandler implements MessageHandler {

	/* (non-Javadoc)
	 * @see no.trygdeetaten.integration.framework.jms.MessageHandler#handleMessage(javax.jms.Message)
	 */
	public ServiceResponse handleMessage(Message msg) {
		return new ServiceResponse();
	}

	/* (non-Javadoc)
	 * @see no.trygdeetaten.integration.framework.jms.MessageHandler#init()
	 */
	public void init() {

	}

}
