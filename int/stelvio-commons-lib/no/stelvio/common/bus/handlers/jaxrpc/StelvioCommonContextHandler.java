/*
 * Created on Feb 26, 2007
 *
 */
package no.stelvio.common.bus.handlers.jaxrpc;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;

import no.stelvio.common.context.StelvioContextData;
import no.stelvio.common.context.StelvioContextRepository;
import no.stelvio.common.util.ExceptionUtils;

/**
 * @author persona2c5e3b49756 Schnell
 * @author test@example.com
 */
public class StelvioCommonContextHandler extends GenericHandler {
	private static final String CLASSNAME = StelvioCommonContextHandler.class.getName();
	private static final Logger LOGGER = Logger.getLogger(CLASSNAME);

	private static final String HEADERTAGNAME = "StelvioContext";

	private HandlerInfo info = null;

	public QName[] getHeaders() {
		return info.getHeaders();
	}

	@Override
	public void init(HandlerInfo arg0) {
		info = arg0;
	}

	@Override
	public boolean handleFault(MessageContext arg0) {
		final String methodName = "handleFault";

		try {
			StelvioContextRepository.removeContext();
		} catch (Exception e) {
			LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "CatchedError: " + ExceptionUtils.getStackTrace(e));
		}
		return true;
	}

	@Override
	public boolean handleResponse(MessageContext arg0) {
		final String methodName = "handleResponse";

		try {
			StelvioContextRepository.removeContext();
		} catch (Exception e) {
			LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "CatchedError: " + ExceptionUtils.getStackTrace(e));
		}
		return true;
	}

	@Override
	public boolean handleRequest(MessageContext context) {
		final String methodName = "handleRequest";

		try {
			// get SOAP message context
			SOAPMessageContext smc = (SOAPMessageContext) context;

			// get SOAP envelope from SOAP message
			SOAPEnvelope se = smc.getMessage().getSOAPPart().getEnvelope();

			// get the headers from envelope
			SOAPHeader sh = se.getHeader();

			if (sh == null) {
				LOGGER.logp(Level.WARNING, CLASSNAME, methodName, "No SOAP headers found in the SOAP request message");
			} else {
				// call method to process header
				processSOAPHeader(sh);
			}
		} catch (Exception e) {
			LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "CatchedError: " + ExceptionUtils.getStackTrace(e));
		}

		// even no header exists we return true to avoid exception
		return true;
	}

	/**
	 * <p>
	 * This method is called by handleRequest method and it retrieves the SOAP headers in the message.
	 * </p>
	 * 
	 * @param SOAPHeader
	 *            The SOAPHeader
	 * @return boolean true = SOAPHeader with specific name exists false = SOAPHeader with specific name doesn't exist
	 */
	private static boolean processSOAPHeader(SOAPHeader sh) {
		final String methodName = "processSOAPHeader";

		boolean foundElements = false;

		Name sName;

		// if there are no headers
		if (sh == null) {
			LOGGER.logp(Level.WARNING, CLASSNAME, methodName, "-- No SOAP headers within request message");
		} else {
			// process header
			LOGGER
					.logp(Level.FINE, CLASSNAME, methodName, "-- Found SOAP headers within request message: "
							+ sh.getLocalName());

			// look for StelvioContext header element inside the HEADER
			Iterator childElems = sh.getChildElements();
			SOAPElement child;

			// iterate through child elements
			while (childElems.hasNext()) {
				Object elem = childElems.next();
				if (elem instanceof SOAPElement) {
					// get child element and its name
					child = (SOAPElement) elem;
					sName = child.getElementName();
					// check if this is required header
					if (sName.getLocalName().equalsIgnoreCase(HEADERTAGNAME)) {
						// found a SOAP header by this name
						LOGGER.logp(Level.FINE, CLASSNAME, methodName, "-- SOAPHeader " + HEADERTAGNAME
								+ " found and matched with " + sName.getLocalName());
						// call method to perform workArea
						foundElements = processSOAPHeaderInfo(child);
					}
				}
			}
		}
		return foundElements;
	}

	/**
	 * <p>
	 * This method retrieves the necessary information for the request header and validates it.
	 * </p>
	 * 
	 * @param SOAPElement
	 *            The SOAPElement
	 * @return boolean true = SOAPElement with specific name exists false = SOAPElement with specific name doesn't exist
	 */
	private static boolean processSOAPHeaderInfo(SOAPElement e) {
		final String methodName = "processSOAPHeaderInfo";

		boolean found = false;

		Name sName;

		// get the name of SOAP element
		sName = e.getElementName();

		LOGGER.logp(Level.FINE, CLASSNAME, methodName, "--- \tElementTag=" + e.getElementName().getQualifiedName());

		// get an iterator on child elements of SOAP element
		Iterator childElems = e.getChildElements();

		SOAPElement child;
		// loop through child elements

		while (childElems.hasNext()) {
			// get next child element
			Object elem = childElems.next();

			if (elem instanceof SOAPElement) {
				child = (SOAPElement) elem;
				sName = child.getElementName();

				StelvioContextData contextData = new StelvioContextData();

				// get the value of userId element
				if (sName.getLocalName().equalsIgnoreCase("userId")) {
					LOGGER.logp(Level.FINE, CLASSNAME, methodName, "---\t\tElement=userId=" + child.getValue());
					contextData.setUserId(child.getValue());
					found = true;
				}

				// get the value of correlationId element
				if (sName.getLocalName().equalsIgnoreCase("correlationId")) {
					LOGGER.logp(Level.FINE, CLASSNAME, methodName, "---\t\tElement=correlationId=" + child.getValue());
					contextData.setCorrelationId(child.getValue());
					found = true;
				}

				// get the value of languageId element
				if (sName.getLocalName().equalsIgnoreCase("languageId")) {
					LOGGER.logp(Level.FINE, CLASSNAME, methodName, "---\t\tElement=languageId=" + child.getValue());
					contextData.setLanguageId(child.getValue());
					found = true;
				}

				// get the value of applicationId element
				if (sName.getLocalName().equalsIgnoreCase("applicationId")) {
					LOGGER.logp(Level.FINE, CLASSNAME, methodName, "---\t\tElement=applicationId=" + child.getValue());
					contextData.setApplicationId(child.getValue());
					found = true;
				}

				if (found) {
					createContext(contextData);
				}
			}
		}

		return found;
	}

	/**
	 * <p>
	 * This creates the context.
	 * </p>
	 * 
	 * @return void
	 */
	private static void createContext(StelvioContextData contextData) {
		final String methodName = "createContext";

		try {
			StelvioContextRepository.createOrUpdateContext(contextData);
		} catch (Exception e) {
			LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "CatchedError: " + ExceptionUtils.getStackTrace(e));
		}
	}
}
