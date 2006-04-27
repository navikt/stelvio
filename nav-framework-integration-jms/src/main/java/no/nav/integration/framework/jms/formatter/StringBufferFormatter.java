package no.nav.integration.framework.jms.formatter;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import no.nav.integration.framework.jms.MessageFormatter;
import no.nav.common.framework.FrameworkError;
import no.nav.common.framework.error.SystemException;
import no.nav.common.framework.service.ServiceFailedException;
import no.nav.common.framework.service.ServiceRequest;


/**
 * Message formatter for StringBuffer
 * 
 * @author persond56073296bff Røren, Accenture
 * @version $Revision: 2017 $ $Author: psa2920 $ $Date: 2005-03-02 14:04:29 +0100 (Wed, 02 Mar 2005) $
 */
public class StringBufferFormatter implements MessageFormatter {

	/** 
	 * {@inheritDoc}
	 * @see no.nav.integration.framework.jms.MessageFormatter#formatMessage(javax.jms.Session, no.nav.common.framework.service.ServiceRequest)
	 */
	public Message formatMessage(Session session, ServiceRequest input) throws ServiceFailedException {

		StringBuffer buffer = (StringBuffer) input.getData("IN_DATA_BUFFER");
		if (buffer == null || buffer.length() == 0) {
			throw new ServiceFailedException(FrameworkError.JMS_MISSING_INPUT_ERROR, "IN_DATA_BUFFER");
		}

		TextMessage msg = null;
		try {
			msg = session.createTextMessage(buffer.toString());
		} catch (JMSException e) {
			throw new SystemException(FrameworkError.JMS_MESSAGE_CREATE_ERROR, e);
		}
		return msg;
	}

	/**
	 * Performs additional initialization and validation of the configuration of this
	 * MessageFormatter.
	 */
	public void init() {
	}


}
