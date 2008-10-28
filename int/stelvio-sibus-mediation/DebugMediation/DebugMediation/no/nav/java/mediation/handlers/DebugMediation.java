/**
 * 
 */
package no.nav.java.mediation.handlers;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.rpc.handler.MessageContext;

import com.ibm.websphere.sib.SIMessage;
import com.ibm.websphere.sib.mediation.handler.MediationHandler;
import com.ibm.websphere.sib.mediation.handler.MessageContextException;
import com.ibm.websphere.sib.mediation.messagecontext.SIMessageContext;
import com.ibm.websphere.sib.mediation.session.SIMediationSession;


/**
 * @author persona2c5e3b49756 Schnell
 * @use For debuging 
 */
public class DebugMediation implements MediationHandler {

	//	Sample - log.logp(Level.FINEST, className, <yourMethod>, <yourText>);
	private final static String className = DebugMediation.class.getName();
	private final Logger log = Logger.getLogger(className);


	
	/* (non-Javadoc)
	 * @see com.ibm.websphere.sib.mediation.handler.MediationHandler#handle(javax.xml.rpc.handler.MessageContext)
	 */
	public boolean handle(MessageContext msgCtx) throws MessageContextException {
		SIMessageContext siMsgCtx = (SIMessageContext) msgCtx;
		if (log.isLoggable(Level.INFO)) 
		{
			log.logp(Level.INFO, className, "handle()", "invoked", msgCtx);
			log.info("Context Properties: " + getContextProperties(siMsgCtx));
			log.info("Message Info: " + getMessageInfo(siMsgCtx.getSIMessage()));
			log.info("Session Info: " + getSessionInfo(siMsgCtx.getSession()));
		}
		return true;
	}

	/**
	 * Returns a printable representation of the SIMediationSession.
	 *
	 * @param session
	 *            see
	 * @com.ibm.websphere.sib.mediation.session.SIMediationSession
	 */
	private String getSessionInfo(SIMediationSession session) {
		StringBuffer data = new StringBuffer();
		data.append("Mediation: ");
		data.append(session.getMediationName());
		data.append(" @ Destination: ");
		data.append(session.getDestinationName());
		if (session.getDiscriminator() != null
				&& !session.getDiscriminator().equals("")) {
			data.append(" (discriminator = ");
			data.append(session.getDiscriminator());
			data.append(')');
		}
		if (session.getMessageSelector() != null
				&& !session.getMessageSelector().equals("")) {
			data.append(" (message selector = ");
			data.append(session.getMessageSelector());
			data.append(')');
		}
		data.append(" on Bus: ");
		data.append(session.getBusName());
		data.append(" in ME: ");
		data.append(session.getMessagingEngineName());
		return data.toString();
	}

	/**
	 * Returns a printable representation of the SIMessage.
	 *
	 * @param message
	 * @see com.ibm.websphere.sib.SIMessage
	 */
	private String getMessageInfo(SIMessage message) {
		StringBuffer data = new StringBuffer();
		data.append("API message id = ");
		data.append(message.getApiMessageId());
		data.append(", System message id = ");
		data.append(message.getSystemMessageId());
		data.append(", Correlation id = ");
		data.append(message.getCorrelationId());
		data.append(", Message format = \"");
		data.append(message.getFormat());
		data.append('\"');
		data.append(", Message descriminator = \"");
		data.append(message.getDiscriminator());
		data.append('\"');
		List list = message.getUserPropertyNames();
		if (list != null && !list.isEmpty()) {
			data.append(", User properties = ");
			data.append(list.toString());
		}
		list = message.getForwardRoutingPath();
		if (list != null && !list.isEmpty()) {
			data.append(", Forward routing path = ");
			data.append(list.toString());
		}
		list = message.getReverseRoutingPath();
		if (list != null && !list.isEmpty()) {
			data.append(", Reverse routing path = ");
			data.append(list.toString());
		}
		data.append(", Reliability = ");
		data.append(message.getReliability());
		data.append(", Priority = ");
		data.append(message.getPriority());
		data.append(", Redelivered Count = ");
		data.append(message.getRedeliveredCount());
		data.append(", TimeToLive = ");
		data.append(message.getRemainingTimeToLive());
		data.append(", User id = ");
		data.append(message.getUserId());
		return data.toString();
	}

	/**
	 * Returns a printable representation of the SIMediationContext.
	 * 
	 * @param msgCtx
	 * @see com.ibm.websphere.sib.mediation.messagecontext.SIMessageContext
	 * @return java.lang.String a textual representation of context properties
	 */
	private String getContextProperties(SIMessageContext msgCtx) {
		StringBuffer data = new StringBuffer();
		Iterator i = msgCtx.getPropertyNames();
		String propName;
		while (i.hasNext()) {
			propName = (String) i.next();
			data.append(propName);
			data.append(" = ");
			data.append(msgCtx.getProperty(propName));
			if (i.hasNext())
				data.append(", ");
		}
		return data.toString();
	}
}
