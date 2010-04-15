package no.nav.java.mediation.handlers;

import javax.xml.rpc.handler.MessageContext;

import com.ibm.websphere.sib.mediation.handler.MediationHandler;
import com.ibm.websphere.sib.mediation.handler.MessageContextException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.websphere.sib.SIDestinationAddress;
import com.ibm.websphere.sib.SIDestinationAddressFactory;
import com.ibm.websphere.sib.SIMessage;
import com.ibm.websphere.sib.mediation.messagecontext.SIMessageContext;
import com.ibm.websphere.sib.mediation.session.SIMediationSession;

/**
 * @author Dag Christoffersen
 * 
 * Created 26.05.2008
 * 
 * Custom mediation class for Oppdrag. 1. Sets the JMSReplyTo field in the
 * JMSHeader of the message. 2. Forwards the message from the local queue to the
 * foreign queue.
 * 
 * This sibus mediation should only be used until the new WPS 6.1 is in use - as
 * this version of WPS allows the JMSReplyTo-field to be set by a mediation
 * module.
 *  
 */
public class OppdragMediation implements MediationHandler {

	//	 Sample - log.logp(Level.FINEST, className, <yourMethod>, <yourText>);
	private final static String className = OppdragMediation.class.getName();

	private final Logger log = Logger.getLogger(className);

	//	 Mediation parameter
	private String routeMessageDestination = "routeToQueue";

	private final static String ROUTE_MESSAGE_DESTINATION = "routeMessageDestination";

	//	 JMSHeader prefixes
	private final static String JMS_PREFIX = "JMS";

	private final static String JMS_REPLY_TO = "JMSOnlineReplyTo";
	private final static String JMS_REPLY_PROCESS_TO = "JMSProcessReplyTo";
	private final static String JMS_REPLY_BATCH_TO = "JMSBatchReplyTo";
	
	/**
	 * Getter for the routeMessageDestination
	 * 
	 * @return
	 */
	public String getRouteMessageDestination() {
		return routeMessageDestination;
	}

	/**
	 * Setter for the routeMessageDestination
	 * 
	 * @param routeMessageDestination
	 */
	public void setRouteMessageDestination(String routeMessageDestination) {
		this.routeMessageDestination = routeMessageDestination;
	}

	/*
	 * This Mediation Handler does two things: 1. add the JMSReplyTo from the
	 * custom property to the JMS Header 2. forwards this message to the foreign
	 * queue given in the custom propery.
	 * 
	 * The functionality provided by the JMSPropertyMediation is kept.
	 *  
	 */
	public boolean handle(MessageContext ctx) throws MessageContextException {

		try {
			log.logp(Level.FINE, className, "handle()", "OppdragMediation: " + "****** OppdragMediation started! ******* ");

			SIMessageContext siCtx = (SIMessageContext) ctx;
			SIMessage message = siCtx.getSIMessage();
			
			// get Kilde property sent with message. This decides where to reverse reply the message
			String kilde = getSingleUserProperty(message, "Kilde" );
			
			for (Iterator it = ctx.getPropertyNames(); it.hasNext();) {

				String propName = (String) it.next();
				if (propName.indexOf(JMS_PREFIX) >= 0) {
					if (propName.indexOf(JMS_REPLY_TO) >= 0) {
						// if Kilde is onlineMelding, read the reverse reply from JMS_REPLY_TO
						if ("onlineMelding".equalsIgnoreCase(kilde)) {
							setJMSHeaderProperties(propName, (Serializable)ctx.getProperty(propName), message);
						}
					}
					else if (propName.indexOf(JMS_REPLY_PROCESS_TO) >= 0) {
						// if Kilde is prosessMelding, read the reverse reply from JMS_REPLY_PROCESS_TO
						if( "prosessMelding".equalsIgnoreCase(kilde)) {
							setJMSHeaderProperties(propName, (Serializable)ctx.getProperty(propName), message);
						}
					}
					else if (propName.indexOf(JMS_REPLY_BATCH_TO) >= 0) {
						// if Kilde is batchMelding, read the reverse reply from JMS_REPLY_BATCH_TO
						if( "batchMelding".equalsIgnoreCase(kilde)) {
							setJMSHeaderProperties(propName, (Serializable)ctx.getProperty(propName), message);
						}
					}
					// all other valid JMS_IBM_PROPERTIES check documentation
					else {
						message.setMessageProperty(propName, (Serializable)ctx.getProperty(propName));
					}
					
				} else if (propName.indexOf(ROUTE_MESSAGE_DESTINATION) >= 0) {
					setRoutingPath(propName, siCtx);
				} else {
					message.setUserProperty(propName, (Serializable) ctx.getProperty(propName));
				}
			}

			log.logp(Level.FINE, className, "handle()", "Context Properties: " + getContextProperties(siCtx));
			log.logp(Level.FINE, className, "handle()", "Message Info: " + getMessageInfo(siCtx.getSIMessage()));
			log.logp(Level.FINE, className,  "handle()", "Session Info: " + getSessionInfo(siCtx.getSession()));
			
			log.logp(Level.FINE, className, "handle()", "OppdragMediation: " + "****** OppdragMediation ended! ******* ");
		} catch (Exception ex) {
			log.logp(Level.SEVERE, className, "handle()", "Mediation Error appeared: " + ex.getMessage());
			// message should not be discarded if an error occurs -> pass
			// further to the consumer
			return true;
		}
		return true;
	}

	/**
	 * Private method that sets the new destination (routing path)
	 * 
	 * @param propName
	 * @param siCtx
	 */
	private void setRoutingPath(String propName, SIMessageContext siCtx) {
		//		 Mediating the routing endpoint
		log.logp(Level.FINE, className, "handle()", "OppdragMediation: " + "****** " + propName + ": " + " mediating routing endpoint.");

		SIMessage msg = siCtx.getSIMessage();
		
		List<SIDestinationAddress> frp = new ArrayList<SIDestinationAddress>(1);
		String destName = getRouteMessageDestination(siCtx);
		String destination[] = destName.split(":");
		
		frp.add(SIDestinationAddressFactory.getInstance().createSIDestinationAddress(destination[1], destination[0]));
		msg.setForwardRoutingPath(frp);
		log.logp(Level.FINE, className, "handle()", "OppdragMediation: " + "****** " + propName + ": " + " new queue destination: "	+ destName);
	}
	
	/** 
     * Returns a for a provided jms properties 
     * 
     * @param msgCtx 
     * @param propName 
     * @return java.lang.String a textual representation of jms user properties value 
     * @throws ClassNotFoundException 
     * @throws IOException 
     */ 
    private String getSingleUserProperty(SIMessage mesg, String propName)
    { 
    	
            try {
				String propNameValue= "NO_VALUE"; 
				propNameValue = (String) mesg.getUserProperty(propName); 
				return propNameValue;
			} catch (IOException e) {
				throw new RuntimeException(e);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			} 
    } 
    
	/**
	 * Private mthod to get the new message destination from context
	 * 
	 * @param siMsgCtx
	 * @return
	 */
	private String getRouteMessageDestination(SIMessageContext siMsgCtx) {
		String value = (String) siMsgCtx.getProperty(ROUTE_MESSAGE_DESTINATION);
		if ((value == null) || (value.equals(""))) {
			value = getRouteMessageDestination();
		}
		return value;
	}

	/**
	 * Private method to set the JMSHeaderProperties. Custom code to add
	 * JMSReplyTo-field, while other fields are supported by IBM
	 * 
	 * @param propName
	 * @param ctx
	 * @param message
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private void setJMSHeaderProperties(String propName, Serializable propValue, SIMessage message) throws IOException {
		log.logp(Level.FINE, className, "handle()", "OppdragMediation: " + "****** " + propName + ": " + " jms system header properties applied.");
		
		//if (propName.indexOf(JMS_REPLY_TO) >= 0) {
		log.logp(Level.FINE, className, "handle()", "****** Inside IF" + propName);
		// now let's insert the response destination into the
		// reverse routing
		// path, so that we can catch the response message
		// first build the replyAddress to be added
		SIDestinationAddress replyAddress;
		replyAddress = SIDestinationAddressFactory.getInstance()
				.createSIDestinationAddress((String)propValue, false);

		// get a copy of the current reverse routing path
		List<SIDestinationAddress> rrp = message.getReverseRoutingPath();
		// Add the response destination to the list (so that the
		// new destination will be the first destination that
		// will be visited
		// by the reply message)
		rrp.add(0, replyAddress);
		// Change the message's reverse routing path
		message.setReverseRoutingPath(rrp);
		//}
		
		/*
				// now let's insert the response destination into the
				// reverse routing
				// path, so that we can catch the response message
				// first build the replyAddress to be added
				SIDestinationAddress replyAddress;
				
				replyAddress = SIDestinationAddressFactory.getInstance()
					.createSIDestinationAddress((String)propValue, false);
				//	get a copy of the current reverse routing path
				List rrp = message.getReverseRoutingPath();
				// Add the response destination to the list (so that the
				// new destination will be the first destination that
				// will be visited
				// by the reply message)
				rrp.add(0, replyAddress);
				// Change the message's reverse routing path
				message.setReverseRoutingPath(rrp);
		*/
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
