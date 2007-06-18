/*
 * Created on Jul 15, 2007
 * persona2c5e3b49756 Schnell, JMS property mediation
 */
package no.stelvio.common.bus.handlers.sib;

import java.io.Serializable;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.rpc.handler.MessageContext;
import com.ibm.websphere.sib.SIMessage;
import com.ibm.websphere.sib.mediation.handler.MediationHandler;
import com.ibm.websphere.sib.mediation.handler.MessageContextException;
import com.ibm.websphere.sib.mediation.messagecontext.SIMessageContext;

/**
 * @author persona2c5e3b49756 Schnell
 *  
 */
public class JMSPropertyMediation implements MediationHandler {

	//	 Sample - log.logp(Level.FINEST, className, <yourMethod>, <yourText>);
	private final static String className = JMSPropertyMediation.class.getName();
	private final Logger log = Logger.getLogger(className);

	/*
	 * This Mediation Handler Iterates through all Custom Properties and adds
	 * them as JMS property
	 */
	public boolean handle(MessageContext ctx) throws MessageContextException {
		try {
			log.logp(Level.FINE, className, "handle()",
					"JMSPropertyMediation: "
							+ "****** Mediation started! ******* ");
			SIMessageContext siCtx = (SIMessageContext) ctx;
			SIMessage message = siCtx.getSIMessage();

			for (Iterator it = ctx.getPropertyNames(); it.hasNext();) {
				String propName = (String) it.next();
				log.logp(Level.FINE, className, "handle()",
						"JMSPropertyMediation: " + "****** " + propName + ": "+ ctx.getProperty(propName) + " applied.");
				message.setUserProperty(propName, (Serializable) ctx
						.getProperty(propName));
			}
			log.logp(Level.FINE, className, "handle()",
					"JMSPropertyMediation: "
							+ "****** Mediation ended! ******* ");
		} catch (Exception ex) {
			log.logp(Level.SEVERE, className, "handle()", "CatchedError: "+ ex.getMessage());
			// message should be ikke discarded
			return true;
		}

		return true;
	}
}
