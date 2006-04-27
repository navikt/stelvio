package no.trygdeetaten.integration.framework.jms.handler;

import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import no.trygdeetaten.common.framework.service.ServiceResponse;

/**
 * Handles XML messages using SAX parsing.
 * 
 * @author person356941106810, Accenture
 * @version $Id: SAXXMLHandler.java 2456 2005-09-09 14:03:51Z lma2970 $
 */
public class SAXXMLHandler extends DefaultHandler {

	// The response whihc wil be  populated
	private ServiceResponse response = null;

	// the response fields
	private Map responseFields = null;

	// the response fields which need iterative behaviour
	private Map iterativeResponseFields = null;

	// keeps track of which element we are processing
	private Stack elementStack = new Stack();

	// keeps track of the iterators
	private Stack iteratorStack = new Stack();

	// a flag which indicates wether or not to set the element data
	private boolean setData = false;

	// set a flag which indicates wether or not to set the error data
	private boolean setErrorData = false;

	// set a flag which indicates wether or not to set the status data
	private boolean setStatusData = false;

	// the name of the element currently being processed
	private String currElementName = null;

	// the handler
	private XMLMessageHandler handler = null;

	// the value found in the error field
	private String errorValue = null;

	// the value found in the status field
	private String statusValue = null;

	// the error xpath
	private String errorField = null;

	// the status xpath
	private String statusField = null;

	// holds the current iterator values
	private LinkedList iteratorValues = new LinkedList();

	// holds the current depth of the xml structure (only used for iterative elements)
	private int currentDepth = 0;
	
	private StringBuffer characterValue = null;

	/**
	 * Constructs a new SAXXMLHandler which will read the given response fields.
	 * 
	 * @param responseFields the fields to set on the response.
	 * @param iterativeResponseFields the iterative fields to set on the response
	 * @param response the ServiceResponse to populate
	 * @param handler the XMLMessageHandler handler that invoked this SAX handler
	 * @param statusField the xpath of the element holding the status
	 * @param errorField the xpath of the element holding the error message
	 */
	protected SAXXMLHandler(
		Map responseFields,
		Map iterativeResponseFields,
		ServiceResponse response,
		XMLMessageHandler handler,
		String statusField,
		String errorField) {
		this.responseFields = responseFields;
		this.iterativeResponseFields = iterativeResponseFields;
		this.response = response;
		this.handler = handler;
		this.errorField = errorField;
		this.statusField = statusField;
	}

	/**
	 * Handles all character data that are a part of an element's body.
	 * 
	 * @param chars the element body.
	 * @param start the start location in the character array.
	 * @param length the number of characters to use from the character array.
	 * 
	 * @throws SAXException there was an error reading the element data
	 * 
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	public void characters(char[] chars, int start, int length) throws SAXException {
		super.characters(chars, start, length);
		characterValue.append(chars, start, length);
		
	}

	/**
	 * Receive notification of the end of an element. 
	 * The SAX parser will invoke this method at the end of every element in the XML document; 
	 * there will be a corresponding startElement event for every endElement event (even when the element is empty).
	 * 
	 * For information on the names, see startElement.
	 * 
	 * @param uri the namespace uri
	 * @param localName the local name (without prefix), or the empty string if Namespace processing is not being performed
	 * @param qName the qualified XML 1.0 name (with prefix), or the empty string if qualified names are not available
	 * 
	 * @throws SAXException there was an error processing the end of the element
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String qName) throws SAXException {

		super.endElement(uri, localName, qName);
		
// ------------------		
		String value = characterValue.toString();
		if (!"".equals(value.trim())) {
			if (setData) {
				handler.setField(response, currElementName, iteratorValues, 0, value, null);
			}
			if (setErrorData) {

				errorValue = value;
			}
			if (setStatusData) {
				statusValue = value;
			}
		}
		setData = false;
		setStatusData = false;
		setErrorData = false;
//---------------
		
		String currLocation = (String) elementStack.peek();

		if (!iteratorStack.isEmpty() && ((String) iteratorStack.peek()).startsWith(currLocation + "/")) {
			iteratorValues.removeLast();
			iteratorStack.pop();
		}

		if (!iteratorStack.isEmpty() && iteratorStack.peek().equals(currLocation)) {
			currentDepth--;
		}
		elementStack.pop();
	}

	/**
	 * Receive notification of the beginning of an element.
	 * 
	 * The Parser will invoke this method at the beginning of every element in the XML document; 
	 * there will be a corresponding endElement event for every startElement event (even when the element is empty). 
	 * All of the element's content will be reported, in order, before the corresponding endElement event.
	 * 
	 * This event allows up to three name components for each element:
	 * the Namespace URI
	 * the local name
	 * the qualified (prefixed) name.
	 * 
	 * Any or all of these may be provided, depending on the values of the http://xml.org/sax/features/namespaces and the 
	 * http://xml.org/sax/features/namespace-prefixes properties:
	 * 
	 * the Namespace URI and local name are required when the namespaces property is true (the default), and are optional 
	 * when the namespaces property is false (if one is specified, both must be);
	 * the qualified name is required when the namespace-prefixes property is true, and is optional when the 
	 * namespace-prefixes property is false (the default).
	 * Note that the attribute list provided will contain only attributes with explicit values (specified or defaulted): 
	 * #IMPLIED attributes will be omitted. The attribute list will contain attributes used for Namespace declarations 
	 * (xmlns* attributes) only if the http://xml.org/sax/features/namespace-prefixes property is true (it is false by 
	 * default, and support for a true value is optional).
	 *
	 * @param uri the Namespace URI
	 * @param localName The local name (without prefix), or the empty string if Namespace processing is not being performed.
	 * @param qName The qualified name (with prefix), or the empty string if qualified names are not available.
	 * @param atts The attributes attached to the element. If there are no attributes, it shall be an empty Attributes object. 
	 *  
	 * @throws SAXException there was an error processing the start of an element
	 * 
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		super.startElement(uri, localName, qName, atts);
		String currLocation = elementStack.peek() + "/" + qName;

		
		characterValue = new StringBuffer();
		
		// check if this is an iterator field
		String iteratorName = (String) iterativeResponseFields.get(currLocation);

		if (iteratorName != null) {
			currLocation += "[" + iteratorName + "]";
			int attIndex = atts.getIndex(iteratorName);
			if (attIndex != -1) {
				String attValue = atts.getValue(attIndex);
				if (!iteratorStack.isEmpty() && iteratorStack.peek().equals(currLocation)) {
					iteratorValues.removeLast();
				} else {
					iteratorStack.push(currLocation);
				}
				iteratorValues.addLast(attValue);
			} else {
				String iteratorValue = null;
				if (iteratorStack.isEmpty() || !iteratorStack.peek().equals(currLocation)) {
					iteratorValue = Integer.toString(0);
					iteratorStack.push(currLocation);
				} else {
					iteratorValue = (String) iteratorValues.removeLast();
					iteratorValue = Integer.toString(1 + Integer.parseInt(iteratorValue));
				}
				iteratorValues.addLast(iteratorValue);
			}
			currentDepth++;
		}

		// check if this is an element which should be set 			
		String responseFieldName = (String) responseFields.get(currLocation);
		if (responseFieldName != null) {
			setData = true;
			currElementName = responseFieldName;
		}
		elementStack.push(currLocation);

		if (errorField != null && errorField.equals(currLocation)) {
			setErrorData = true;
		}

		if (statusField != null && statusField.equals(currLocation)) {
			setStatusData = true;
		}

		// set all attributes
		int numAtts = atts.getLength();
		String attName = null;
		String fieldName = null;
		String attValue = null;
		String attribLocation = null;
		for (int i = 0; i < numAtts; i++) {
			attName = atts.getQName(i);
			attValue = atts.getValue(i);
			if (attValue != null && !"".equals(attValue.trim())) {
				attribLocation = currLocation + "/@" + attName;
				fieldName = (String) responseFields.get(attribLocation);
				if (fieldName != null && attValue != null) {
					handler.setField(response, fieldName, iteratorValues, 0, attValue, null);
				}
				if (errorField != null && errorField.equals(attribLocation)) {
					errorValue = attValue;

				}

				if (statusField != null && statusField.equals(attribLocation)) {
					statusValue = attValue;
				}
			}
		}
	}

	/**
	 * Receive notification of the end of the document.
	 * @throws SAXException handling of the document end failed.
	 * @see org.xml.sax.ContentHandler#startDocument()
	 */
	public void startDocument() throws SAXException {
		super.startDocument();
		elementStack.push("");
	}

	/**
	 * Receive notification of the beginning of the document.
	 * @throws SAXException handling doucment start failed.
	 * @see org.xml.sax.ContentHandler#endDocument()
	 */
	public void endDocument() throws SAXException {
		super.endDocument();
		elementStack.pop();
	}

	/**
	 * Returns the error message found in the error element.
	 * @return the error message
	 */
	public String getErrorValue() {
		return errorValue;
	}

	/**
	 * Returns the status found in the status element
	 * @return the status value
	 */
	public String getStatusValue() {
		return statusValue;
	}

}
