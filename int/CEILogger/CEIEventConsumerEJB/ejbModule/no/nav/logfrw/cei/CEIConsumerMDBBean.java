/*
 * (c) Copyright IBM Corp 2006.
 * Created on 15-11-2006.
 * persona2c5e3b49756 Schnell, IBM Software Services for WebSphere Business Integration.
 */

package no.nav.logfrw.cei;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.eclipse.hyades.logging.events.cbe.CommonBaseEvent;
import org.eclipse.hyades.logging.events.cbe.ExtendedDataElement;
import org.eclipse.hyades.logging.events.cbe.util.EventFormatter;

import no.nav.logfrw.cei.util.EventData;
import no.nav.logfrw.cei.util.EventDataConstant;




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

			//Log elements from CBEmsg
			EventData eventData = generateEventData(CBEmsg);
			
			//Put info in log-signature
			MDC.put("module", eventData.getSourceModule());
			MDC.put("component", eventData.getSourceComponent());
			MDC.put("interface", eventData.getSourceInterface());
			MDC.put("method", eventData.getSourceMethod());
			
			//Get message from hex-XML, convert and log
			logMessage(getCBEmsgXML(), eventData.isError());
		}
	}
	
	/**
	 * Henter ut info fra CommonBaseEvent og legger i EventData for lettere aksess.
	 * 
	 * @param CBEmsg
	 * @return eventData EventData.
	 */
	public EventData generateEventData(CommonBaseEvent CBEmsg) {
		EventData eventData = new EventData();

		List extendedDataElementsList = new ArrayList(CBEmsg
				.getExtendedDataElements());
		Iterator EDEIterator = extendedDataElementsList.iterator();
		while (EDEIterator.hasNext()) {
			ExtendedDataElement ede = (ExtendedDataElement) EDEIterator.next();
			String name = ede.getName();

			if (name.equals(EventDataConstant.EVENT_TYPE)) {
				eventData.setEventType(ede.getValuesAsString());
				if (EventDataConstant.EVENT_TYPE_FAILURE.equals(eventData
						.getEventType()))
					eventData.setError(true);
			} else if (name.equals(EventDataConstant.PAYLOAD_TYPE)) {
				eventData.setPayloadType(ede.getValuesAsString());
			} else if (name.equals(EventDataConstant.SOURCE_COMPONENT)) {
				eventData.setSourceComponent(ede.getValuesAsString());
			} else if (name.equals(EventDataConstant.SOURCE_INTERFACE)) {
				eventData.setSourceInterface(ede.getValuesAsString());
			} else if (name.equals(EventDataConstant.SOURCE_METHOD)) {
				eventData.setSourceMethod(ede.getValuesAsString());
			} else if (name.equals(EventDataConstant.SOURCE_MODULE)) {
				eventData.setSourceModule(ede.getValuesAsString());
			} else if (name.equals(EventDataConstant.TARGET_COMPONENT)) {
				eventData.setTargetComponent(ede.getValuesAsString());
			} else if (name.equals(EventDataConstant.TARGET_INTERFACE)) {
				eventData.setTargetInterface(ede.getValuesAsString());
			} else if (name.equals(EventDataConstant.TARGET_METHOD)) {
				eventData.setTargetMethod(ede.getValuesAsString());
			} else if (name.equals(EventDataConstant.TARGET_MODULE)) {
				eventData.setTargetModule(ede.getValuesAsString());
			}
		}
		eventData.setCreationTime(CBEmsg.getCreationTime());
		eventData.setLocaleInstanceId(CBEmsg.getLocalInstanceId());
		eventData.setGlobaleInstanceId(CBEmsg.getGlobalInstanceId());
		return eventData;
	}
	
	private void logMessage(String xmlMsg, boolean error) {
		String temp = null;
		String hexValue = null;
		String splitOne[] = xmlMsg.split("<hexValue>");

		for (int i = 0; i < splitOne.length; i++) {
			if (i == 1) {
				temp = splitOne[i];
			}
		}
		if (temp != null && temp.length() > 0) {
			String splitTwo[] = temp.split("</hexValue>");
			for (int i = 0; i < splitTwo.length; i++) {
				if (i == 0) {
					hexValue = splitTwo[i];
				}
			}
			String hexConverted = decodeHexString(hexValue);
			if(error){
				log.error("FEILMELDING:  " + hexConverted);
			} else{
				if (log.isInfoEnabled()) {
					log.info("MELDING: " + hexConverted);
				}
			}
		}
	}
	
	/*
	 * public static String encodeHexString(String sourceText) {
	 * 
	 * Byte[] rawData = sourceText.getBytes(); StringBuffer hexText= new
	 * StringBuffer(); String initialHex = null; int initHexLength=0;
	 * 
	 * for(int i=0; i <rawData.length; i++) { int positiveValue = rawData[i] &
	 * 0x000000FF; initialHex = Integer.toHexString(positiveValue);
	 * initHexLength=initialHex.length(); while(initHexLength++ < 2) {
	 * hexText.append("0"); } hexText.append(initialHex); } return
	 * hexText.toString(); }
	 */

	public static String decodeHexString(String hexText) {

		String decodedText = null;
		String chunk = null;

		if (hexText != null && hexText.length() > 0) {

			int numBytes = hexText.length() / 2;
			byte[] rawToByte = new byte[numBytes];
			int offset = 0;
			int bCounter = 0;
			for (int i = 0; i < numBytes; i++) {
				chunk = hexText.substring(offset, offset + 2);
				offset += 2;
				rawToByte[i] = (byte) (Integer.parseInt(chunk, 16) & 0x000000FF);
				decodedText = new String(rawToByte);
			}
		}
		return decodedText;
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
