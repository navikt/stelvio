/*
 * (c) Copyright IBM Corp 2006.
 * Created on 15-11-2006.
 * persona2c5e3b49756 Schnell, IBM Software Services for WebSphere Business Integration.
 */

package no.nav.logfrw.cei;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;
import org.eclipse.hyades.logging.events.cbe.CommonBaseEvent;
import org.eclipse.hyades.logging.events.cbe.util.EventFormatter;

import no.nav.logfrw.cei.util.EventData;
import no.nav.logfrw.cei.util.LogMessage;



/**
 * Bean implementation class for Enterprise Bean: CEIConsumerMDB
 */
public class CEIConsumerMDBBean implements javax.ejb.MessageDrivenBean,	javax.jms.MessageListener 
{
	private Logger log = Logger.getLogger(CEIConsumerMDBBean.class);
	private CommonBaseEvent cbeMsgPlain = null;
	private String cbeMsgXML = null;
	
	private javax.ejb.MessageDrivenContext fMessageDrivenCtx;

	/**
	 * getMessageDrivenContext
	 */
	public javax.ejb.MessageDrivenContext getMessageDrivenContext() {
		return fMessageDrivenCtx;
	}

	/**
	 * setMessageDrivenContext
	 */
	public void setMessageDrivenContext(javax.ejb.MessageDrivenContext ctx) {
		fMessageDrivenCtx = ctx;
	}

	/**
	 * ejbCreate
	 */
	public void ejbCreate() {
	}

	/**
	 * onMessage
	 * @throws JMSException
	 */
	public void onMessage(javax.jms.Message msg) {
		
        // get the time that the onMessage started
		long startTime = System.currentTimeMillis();
		
		if (log.isInfoEnabled()) {
			log.info("onMessage CEI >>");
		}

		// get message body into CBE object 
		CommonBaseEvent CBEmsg = null;
		try {
			CBEmsg = (CommonBaseEvent) ((ObjectMessage) msg).getObject();
			setCBEmsgPlain(CBEmsg);
			setCBEmsgXML(CBEmsg);
			
		} catch (JMSException e) {
			log.error("Error getting CommonBaseEvent", e);
		} 
		
		if(CBEmsg != null){
			LogMessage logMsg = new LogMessage();

			//Log elements from CBEmsg
			EventData eventData = logMsg.generateEventData(CBEmsg);
			logMsg.logMessageFromMDB(eventData);
			
			//Get hexValue from XML, convert and log
			logMsg.logHexData(getCBEmsgXML(), eventData.isError());
			
			long endTime = System.currentTimeMillis();
			
			// the time that it took to generate the information
			if (log.isDebugEnabled()) {
				log.debug("CBE Time: " + (endTime - startTime) + " milliseconds");
			}	
	
			if (log.isInfoEnabled()) {
				log.info("onMessage CEI <<");
			}
		}
	}

	/**
	 * ejbRemove
	 */
	public void ejbRemove() {
	}

	/**
	 * @param msgContent as CBE
	 * The CEIConsumer msg to set.
	 */
	public void setCBEmsgPlain (CommonBaseEvent msgContent) {
		this.cbeMsgPlain = msgContent;
	}

	/**
	 * @return CBE message plain format as CommonBaseEvent
	 * The CEIConsumer msg to get.
	 */
	public CommonBaseEvent getCBEmsgPlain() {
		return cbeMsgPlain;
	}

	/**
	 * @param msgContent as CBE
	 * The CEIConsumer msg to set.
	 */
	public void setCBEmsgXML(CommonBaseEvent msgContent) {

		this.cbeMsgXML = EventFormatter.toCanonicalXMLString(msgContent);
	}

	/**
	 * @return CBE message XML format as String
	 * The CEIConsumer msg to get.
	 */
	public String getCBEmsgXML() {
		return cbeMsgXML;
	}
	
}
