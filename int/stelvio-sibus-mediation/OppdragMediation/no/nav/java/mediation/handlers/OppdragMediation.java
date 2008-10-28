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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ibm.websphere.sib.SIDestinationAddress;
import com.ibm.websphere.sib.SIDestinationAddressFactory;
import com.ibm.websphere.sib.SIMessage;
import com.ibm.websphere.sib.mediation.messagecontext.SIMessageContext;

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

	private final static String JMS_REPLYT_TO = "JMSReplyTo";

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
			log.logp(Level.FINE, className, "handle()", "OppdragMediation: "
					+ "****** OppdragMediation started! ******* ");

			SIMessageContext siCtx = (SIMessageContext) ctx;
			SIMessage message = siCtx.getSIMessage();

			for (Iterator it = ctx.getPropertyNames(); it.hasNext();) {

				String propName = (String) it.next();
				log.logp(Level.FINE, className, "handle()",
						"OppdragMediation: " + "****** " + propName + ": "
								+ ctx.getProperty(propName) + " preparing...");
				if (propName.indexOf(JMS_PREFIX) >= 0) {
					setJMSHeaderProperties(propName, ctx, message);

				} else if (propName.indexOf(ROUTE_MESSAGE_DESTINATION) >= 0) {
					setRoutingPath(propName, siCtx);
				} else {

					log.logp(Level.FINE, className, "handle()",
							"OppdragMediation: " + "****** " + propName + ": "
									+ " jms user header properties applied.");
					message.setUserProperty(propName, (Serializable) ctx
							.getProperty(propName));
				}
			}
			log.logp(Level.FINE, className, "handle()", "OppdragMediation: "
					+ "****** PropertyMediation ended! ******* ");
		} catch (Exception ex) {
			log.logp(Level.SEVERE, className, "handle()",
					"Mediation Error appeared: " + ex.getMessage());
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
		log.logp(Level.FINE, className, "handle()", "OppdragMediation: "
				+ "****** " + propName + ": " + " mediating routing endpoint.");
		SIMessage msg = siCtx.getSIMessage();
		List frp = new ArrayList(1);
		String destName = getRouteMessageDestination(siCtx);

		String destination[] = destName.split(":");

		frp.add(SIDestinationAddressFactory.getInstance()
				.createSIDestinationAddress(destination[1], destination[0]));
		msg.setForwardRoutingPath(frp);
		log.logp(Level.FINE, className, "handle()", "OppdragMediation: "
				+ "****** " + propName + ": " + " new queue destination: "
				+ destName);

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
	private void setJMSHeaderProperties(String propName, MessageContext ctx,
			SIMessage message) throws IOException {
		log.logp(Level.FINE, className, "handle()", "OppdragMediation: "
				+ "****** " + propName + ": "
				+ " jms system header properties applied.");

		if (propName.indexOf(JMS_REPLYT_TO) >= 0) {
			// now let's insert the response destination into the
			// reverse routing
			// path, so that we can catch the response message
			// first build the replyAddress to be added
			SIDestinationAddress replyAddress;
			replyAddress = SIDestinationAddressFactory.getInstance()
					.createSIDestinationAddress(
							(String) ctx.getProperty(propName), false);

			// get a copy of the current reverse routing path
			List rrp = message.getReverseRoutingPath();
			// Add the response destination to the list (so that the
			// new destination will be the first destination that
			// will be visited
			// by the reply message)
			rrp.add(0, replyAddress);
			// Change the message's reverse routing path
			message.setReverseRoutingPath(rrp);
		}
		// all other valid JMS_IBM_PROPERTIES check documentation
		else {
			message.setMessageProperty(propName, (Serializable) ctx
					.getProperty(propName));
		}
		
		// Applying two header properties needed by Oppdrag to understand the incoming message
		//message.setMessageProperty("JMS_IBM_Character_Set", "277");
		//message.setMessageProperty("JMS_IBM_Encoding", "785");
		message.setMessageProperty("JMS_IBM_MsgType", new Integer(2));
		
		
	}
}
