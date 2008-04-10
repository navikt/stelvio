/*
 * Created on Jul 15, 2007
 * persona2c5e3b49756 Schnell, JMS property mediation
 * Update 26.10.2007 -> Validation and logging
 */
package no.stelvio.common.bus.handlers.sib;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.rpc.handler.MessageContext;

import com.ibm.websphere.sib.SIDestinationAddress;
import com.ibm.websphere.sib.SIDestinationAddressFactory;
import com.ibm.websphere.sib.SIMessage;
import com.ibm.websphere.sib.mediation.handler.MediationHandler;
import com.ibm.websphere.sib.mediation.handler.MessageContextException;
import com.ibm.websphere.sib.mediation.messagecontext.SIMessageContext;

/**
 * @author LSb2812
 *  
 */
public class JMSPropertyMediation implements MediationHandler {

	//	 Sample - log.logp(Level.FINEST, className, <yourMethod>, <yourText>);
	private final static String className = JMSPropertyMediation.class.getName();
	private final Logger log = Logger.getLogger(className);

	private final static String JMS_PREFIX = "JMS";
	private final static String JMS_REPLYT_TO = "JMSReplyTo";
	
	/*
	 * This Mediation Handler Iterates through all Custom Properties and adds
	 * them as JMS property
	 */
	public boolean handle(MessageContext ctx) throws MessageContextException {
		try {
			log.logp(Level.FINE, className, "handle()", "JMSPropertyMediation: " + "****** PropertyMediation started! ******* ");
			SIMessageContext siCtx = (SIMessageContext) ctx;
			SIMessage message = siCtx.getSIMessage();

			for (Iterator it = ctx.getPropertyNames(); it.hasNext();) {
				
				String propName = (String) it.next();
				log.logp(Level.FINE, className, "handle()", "JMSPropertyMediation: " + "****** " + propName + ": "+ ctx.getProperty(propName) + " preparing.");
				if (propName.indexOf(JMS_PREFIX) >= 0)
				{
					log.logp(Level.FINE, className, "handle()", "JMSPropertyMediation: " + "****** " + propName + ": "+ " jms system header properties applied.");
					
					if (propName.indexOf(JMS_REPLYT_TO) >= 0)
					{
						// now let's insert the response destination into the reverse routing 
						// path, so that we can catch the response message
						// first build the replyAddress to be added
						SIDestinationAddress replyAddress;
						replyAddress = SIDestinationAddressFactory.getInstance().createSIDestinationAddress((String)ctx.getProperty(propName),false);

						// get a copy of the current reverse routing path
						List rrp = message.getReverseRoutingPath();
						// Add the response destination to the list (so that the
						// new destination will be the first destination that will be visited 
						// by the reply message)
						rrp.add(0, replyAddress);
						// Change the message's reverse routing path
						message.setReverseRoutingPath(rrp);						
					}
					// all other valid JMS_IBM_PROPERTIES check documentation
					else
					{
						message.setMessageProperty(propName, (Serializable) ctx.getProperty(propName));						
					}
				}
				else
				{	
					log.logp(Level.FINE, className, "handle()", "JMSPropertyMediation: " + "****** " + propName + ": "+ " jms user header properties applied.");
					message.setUserProperty(propName, (Serializable) ctx.getProperty(propName));
				} 
			}
			log.logp(Level.FINE, className, "handle()", "JMSPropertyMediation: " + "****** PropertyMediation ended! ******* ");
		} catch (Exception ex) {
			log.logp(Level.SEVERE, className, "handle()", "Mediation Error appeared: "+ ex.getMessage());
			// message should be not discarded if an error happens -> pass further to the consumer
			return true;
		}
		return true;
	}
}
