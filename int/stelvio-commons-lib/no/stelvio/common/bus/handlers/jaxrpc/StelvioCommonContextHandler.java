/*
 * Created on Feb 26, 2007
 *
 */
package no.stelvio.common.bus.handlers.jaxrpc;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;

import no.stelvio.common.util.ExceptionUtils;

import com.ibm.websphere.workarea.UserWorkArea;

/**
 * @author persona2c5e3b49756 Schnell
 * 
 */
public class StelvioCommonContextHandler extends GenericHandler {

	// Sample - log.logp(Level.FINEST, className, <yourMethod>, <yourText>);
	private static final String CLASSNAME = StelvioCommonContextHandler.class.getName();

	private final Logger log = Logger.getLogger(CLASSNAME);

	// internal
	protected HandlerInfo info = null;

	// initial UserWorkArea
	protected UserWorkArea workArea = null;

	protected InitialContext workAreaCtx = null;

	// nameContext variables
	static final String HEADERTAGNAME = "StelvioContext";

	static final String WORKAREANAME = "BUS_STELVIO_CONTEXT";

	// variable for userId, transactionId, corrleationId, ...
	String userId = null;

	String correlationId = null;

	String languageId = null;

	String applicationId = null;

	/*
	 * 
	 * @see javax.xml.rpc.handler.Handler#getHeaders()
	 */
	public QName[] getHeaders() {
		return info.getHeaders();
	}

	/*
	 * 
	 * @see javax.xml.rpc.handler.Handler#destroy()
	 */
	public void destroy() {
		final String methodName = "destroy()";

		// housekeeping workArea
		try {
			if (workArea.getName().equalsIgnoreCase(WORKAREANAME)) {
				workArea.remove(WORKAREANAME);
			}
			workArea.complete();
			log.logp(Level.FINE, CLASSNAME, methodName, "WorkArea " + WORKAREANAME + " removed.");
		} catch (Exception e) {
			log.logp(Level.SEVERE, CLASSNAME, methodName, "Error: " + e.getMessage());
		}
		// inherit
		super.destroy();
	}

	/*
	 * 
	 * @see javax.xml.rpc.handler.Handler#handleFault(javax.xml.rpc.handler.MessageContext)
	 */
	public boolean handleFault(MessageContext context) {
		final String methodName = "handleFault()";

		try {
			// get SOAP message context
			SOAPMessageContext smc = (SOAPMessageContext) context;

			// get SOAP envelope from SOAP message
			SOAPEnvelope se = smc.getMessage().getSOAPPart().getEnvelope();

			// get the headers from envelope
			SOAPHeader sh = se.getHeader();

			if (sh == null) {
				log.logp(Level.WARNING, CLASSNAME, methodName, "No SOAP headers found in the SOAP request message");
			} else {
				// call method to process header
				processSOAPHeader(sh);
			}

		} catch (Exception e) {
			log.logp(Level.SEVERE, CLASSNAME, methodName, "CatchedError: " + ExceptionUtils.getStackTrace(e));
		}

		// even no header exists we return true to avoid exception
		return true;
	}

	/*
	 * 
	 * @see javax.xml.rpc.handler.Handler#handleRequest(javax.xml.rpc.handler.MessageContext)
	 */
	public boolean handleRequest(MessageContext context) {
		final String methodName = "handleRequest()";

		try {
			// get SOAP message context
			SOAPMessageContext smc = (SOAPMessageContext) context;

			// get SOAP envelope from SOAP message
			SOAPEnvelope se = smc.getMessage().getSOAPPart().getEnvelope();

			// get the headers from envelope
			SOAPHeader sh = se.getHeader();

			if (sh == null) {
				log.logp(Level.WARNING, CLASSNAME, methodName, "No SOAP headers found in the SOAP request message");
			} else {
				// call method to process header
				processSOAPHeader(sh);
			}
		} catch (Exception e) {
			log.logp(Level.SEVERE, CLASSNAME, methodName, "CatchedError: " + ExceptionUtils.getStackTrace(e));
		}

		// even no header exists we return true to avoid exception
		return true;
	}

	/*
	 * 
	 * @see javax.xml.rpc.handler.Handler#handleResponse(javax.xml.rpc.handler.MessageContext)
	 */
	public boolean handleResponse(MessageContext context) {
		// nothing todo with response
		return super.handleResponse(context);
	}

	/*
	 * 
	 * @see javax.xml.rpc.handler.Handler#init(javax.xml.rpc.handler.HandlerInfo)
	 */
	public void init(HandlerInfo arg) {
		final String methodName = "init()";

		info = arg;
		try {
			log.logp(Level.FINE, CLASSNAME, methodName, "-- operate workArea with name " + WORKAREANAME);
			workAreaCtx = new InitialContext();
			workArea = (UserWorkArea) workAreaCtx.lookup("java:comp/websphere/UserWorkArea");

		} catch (Exception e) {
			log.logp(Level.SEVERE, CLASSNAME, methodName, "CatchedError: " + ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * <p>
	 * This method is called by handleRequest method and it retrieves the SOAP
	 * headers in the message.
	 * </p>
	 * 
	 * @param SOAPHeader
	 *            The SOAPHeader
	 * @return boolean true = SOAPHeader with specific name exists false =
	 *         SOAPHeader with specific name doesn't exist
	 */
	private boolean processSOAPHeader(SOAPHeader sh) {

		final String methodName = "processSOAPHeader";

		boolean foundElements = false;

		javax.xml.soap.Name sName;

		// if there are no headers
		if (sh == null) {
			log.logp(Level.WARNING, CLASSNAME, methodName, "-- No SOAP headers within request message");
		} else {
			// process header
			log.logp(Level.FINE, CLASSNAME, methodName, "-- Found SOAP headers within request message: " + sh.getLocalName());

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
						log.logp(Level.FINE, CLASSNAME, methodName, "-- SOAPHeader " + HEADERTAGNAME
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
	 * This method retrieves the necessary information for the request header
	 * and validates it.
	 * </p>
	 * 
	 * @param SOAPElement
	 *            The SOAPElement
	 * @return boolean true = SOAPElement with specific name exists false =
	 *         SOAPElement with specific name doesn't exist
	 */
	private boolean processSOAPHeaderInfo(SOAPElement e) {

		final String methodName = "processSOAPHeaderInfo";

		boolean found = false;

		javax.xml.soap.Name sName;

		// get the name of SOAP element
		sName = e.getElementName();

		log.logp(Level.FINE, CLASSNAME, methodName, "--- \tElementTag=" + e.getElementName().getQualifiedName());

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

				// get the value of userId element
				if (sName.getLocalName().equalsIgnoreCase("userId")) {
					log.logp(Level.FINE, CLASSNAME, methodName, "---\t\tElement=userId=" + child.getValue());
					userId = child.getValue();
					found = true;
				}

				// get the value of correlationId element
				if (sName.getLocalName().equalsIgnoreCase("correlationId")) {
					log.logp(Level.FINE, CLASSNAME, methodName, "---\t\tElement=correlationId=" + child.getValue());
					correlationId = child.getValue();
					found = true;
				}

				// get the value of languageId element
				if (sName.getLocalName().equalsIgnoreCase("languageId")) {
					log.logp(Level.FINE, CLASSNAME, methodName, "---\t\tElement=languageId=" + child.getValue());
					languageId = child.getValue();
					found = true;
				}

				// get the value of applicationId element
				if (sName.getLocalName().equalsIgnoreCase("applicationId")) {
					log.logp(Level.FINE, CLASSNAME, methodName, "---\t\tElement=applicationId=" + child.getValue());
					applicationId = child.getValue();
					found = true;
				}

			}
		}

		if (found) {
			createWorkArea();
		}

		return found;
	}

	/**
	 * <p>
	 * This method set the content of WorkArea for the bus.
	 * </p>
	 * 
	 * @return void
	 */
	private void createWorkArea() {

		final String methodName = "createWorkArea()";

		try {
			log.logp(Level.FINE, CLASSNAME, methodName, "-- set BUS_STELVIO_CONTEXT workArea context");

			if (workArea != null) {

				workArea.begin(WORKAREANAME);

				// userId
				if (userId != null) {
					workArea.set("userId", userId);
				}

				// correlationId
				if (correlationId != null) {
					workArea.set("correlationId", correlationId);
				}

				// languageId
				if (languageId != null) {
					workArea.set("languageId", languageId);
				}

				// applicationId
				if (applicationId != null) {
					workArea.set("applicationId", applicationId);
				}

				log.logp(Level.FINE, CLASSNAME, methodName, "-- BUS_STELVIO_CONTEXT: userId=" + userId + " correlationId="
						+ correlationId + " languageId=" + languageId + " applicationId=" + applicationId);

			} else {
				log.logp(Level.SEVERE, CLASSNAME, methodName, "WorkArea is null");
			}
		} catch (Exception e) {
			log.logp(Level.SEVERE, CLASSNAME, methodName, "Error operate workArea: " + ExceptionUtils.getStackTrace(e));
		}
	}
}
