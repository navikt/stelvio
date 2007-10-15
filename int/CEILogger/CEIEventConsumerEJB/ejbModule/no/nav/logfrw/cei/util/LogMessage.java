/*
 * Created on Nov 17, 2006
 *
 */
package no.nav.logfrw.cei.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.hyades.logging.events.cbe.CommonBaseEvent;
import org.eclipse.hyades.logging.events.cbe.ExtendedDataElement;

/**
 * @author Solfrid Hengebøl
 * 
 *  * Preferences - Java - Code Style - Code Templates
 */
public class LogMessage {

	private Logger log = Logger.getLogger(LogMessage.class);

	public void logMessageFromMDB(EventData eventData) {

		if (eventData.isError()) {
			log.error("Error in module: " + eventData.getSourceModule());
			log.error("CBE Time: " + eventData.getCreationTime());
			log.error("Source Component: " + eventData.getSourceComponent());
			log.error("Source Interface: " + eventData.getSourceInterface());
			log.error("Source Method: " + eventData.getSourceMethod());
			
		} else {
			if (log.isInfoEnabled()) {
				log.info("Module: " + eventData.getSourceModule());
				log.info("CBE Time: " + eventData.getCreationTime());
				log.info("Source Component: " + eventData.getSourceComponent());
				log.info("Source Interface: " + eventData.getSourceInterface());
				log.info("Source Method: " + eventData.getSourceMethod());
			}
		}
		if (log.isInfoEnabled()) {
			log.info("Target Module: " + eventData.getTargetModule());
			log.info("Target Component: " + eventData.getTargetComponent());
			log.info("Target Interface: " + eventData.getTargetInterface());
			log.info("Target Method: " + eventData.getTargetMethod());
		}
	}

	/**
	 * @param CBEmsg
	 * @return
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
		return eventData;
	}

	public void logHexData(String xmlMsg, boolean error) {
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
				log.error("HEXCONVERTED " + hexConverted);
			} else{
				if (log.isInfoEnabled()) {
					log.info("HEXCONVERTED " + hexConverted);
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
}
