/*
 * Created on Feb 26, 2007
 *
 */
package no.stelvio.common.bus.handlers.jaxrpc;

import java.io.PrintWriter;
import java.io.StringWriter;
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

import com.ibm.websphere.workarea.UserWorkArea;

/**
 * @author persona2c5e3b49756 Schnell
 *  
 */
public class StelvioCommonContextHandler extends GenericHandler {

	// Sample - log.logp(Level.FINEST, className, <yourMethod>, <yourText>);
	private final static String className = StelvioCommonContextHandler.class
			.getName();

	private final Logger log = Logger.getLogger(className);

	// internal
	protected HandlerInfo info = null;

	// initial UserWorkArea
	protected UserWorkArea workArea = null;

	protected InitialContext workAreaCtx = null;

	// nameContext variables
	final static String headerTagName = "StelvioContext";

	final static String workAreaName = "BUS_STELVIO_CONTEXT";

	// variable for userId, transactionId, corrleationId, ...
	String userId = null;

	String correlationId = null;

	String languageId = null;

	String applicationId = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.xml.rpc.handler.Handler#getHeaders()
	 */
	public QName[] getHeaders() {
		return info.getHeaders();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.xml.rpc.handler.Handler#destroy()
	 */
	public void destroy() {
		// housekeeping workArea
		try {
			if (workArea.getName().equalsIgnoreCase(workAreaName))
				workArea.remove(workAreaName);
			workArea.complete();
			log.logp(Level.FINE, className, "destroy()", "WorkArea "
					+ workAreaName + " removed.");
		} catch (Exception e) {
			log.logp(Level.SEVERE, className, "destroy()", "Error: "
					+ e.getMessage());
		}
		// inherit
		super.destroy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.xml.rpc.handler.Handler#handleFault(javax.xml.rpc.handler.MessageContext)
	 */
	public boolean handleFault(MessageContext context) {
		boolean exit = true;
		try {
			// get SOAP message context
			SOAPMessageContext smc = (SOAPMessageContext) context;

			// get SOAP envelope from SOAP message
			SOAPEnvelope se = smc.getMessage().getSOAPPart().getEnvelope();

			// get the headers from envelope
			SOAPHeader sh = se.getHeader();

			if (sh == null) {
				log.logp(Level.INFO, className, "handleFault()",
						"No SOAP headers found in the SOAP request message");
				exit = false;
			} else
				// call method to process header
				exit = processSOAPHeader(sh);
		} catch (Exception e) {
			log.logp(Level.SEVERE, className, "handleFault()", "CatchedError: "
					+ getExceptionTrace(e));
		}

		// even no header exists we return true to avoid exception
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.xml.rpc.handler.Handler#handleRequest(javax.xml.rpc.handler.MessageContext)
	 */
	public boolean handleRequest(MessageContext context) {
		boolean exit = true;
		try {
			// get SOAP message context
			SOAPMessageContext smc = (SOAPMessageContext) context;

			// get SOAP envelope from SOAP message
			SOAPEnvelope se = smc.getMessage().getSOAPPart().getEnvelope();

			// get the headers from envelope
			SOAPHeader sh = se.getHeader();

			if (sh == null) {
				log.logp(Level.INFO, className, "handleRequest()",
						"No SOAP headers found in the SOAP request message");
				exit = false;
			} else
				// call method to process header
				exit = processSOAPHeader(sh);
		} catch (Exception e) {
			log.logp(Level.SEVERE, className, "handleRequest()",
					"CatchedError: " + getExceptionTrace(e));
		}

		// even no header exists we return true to avoid exception
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.xml.rpc.handler.Handler#handleResponse(javax.xml.rpc.handler.MessageContext)
	 */
	public boolean handleResponse(MessageContext context) {
		// nothing todo with response
		return super.handleResponse(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.xml.rpc.handler.Handler#init(javax.xml.rpc.handler.HandlerInfo)
	 */
	public void init(HandlerInfo arg) {
		info = arg;
		try {
			log.logp(Level.FINE, className, "init()",
					"-- operate workArea with name " + workAreaName);
			workAreaCtx = new InitialContext();
			workArea = (UserWorkArea) workAreaCtx
					.lookup("java:comp/websphere/UserWorkArea");

		} catch (Exception e) {
			log.logp(Level.SEVERE, className, "init()", "CatchedError: "
					+ getExceptionTrace(e));
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

		boolean foundElements = false;
		boolean found = false;

		javax.xml.soap.Name sName;

		// if there are no headers
		if (sh == null) {
			log.logp(Level.INFO, className, "processSOAPHeader",
					"-- No SOAP headers within request message");
		} else {
			// process header
			log.logp(Level.FINE, className, "processSOAPHeader",
					"-- Found SOAP headers within request message: "
							+ sh.getLocalName());

			//look for StelvioContext header element inside the HEADER
			java.util.Iterator childElems = sh.getChildElements();
			SOAPElement child;

			// iterate through child elements
			while (childElems.hasNext()) {
				Object elem = childElems.next();
				if (elem instanceof SOAPElement) {
					// get child element and its name
					child = (SOAPElement) elem;
					sName = child.getElementName();
					// check if this is required header
					if (sName.getLocalName().equalsIgnoreCase(headerTagName)) {
						// found a SOAP header by this name
						found = true;
						log.logp(Level.FINE, className, "processSOAPHeader",
								"-- SOAPHeader " + headerTagName
										+ " found and matched with "
										+ sName.getLocalName());
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

		boolean found = false;

		javax.xml.soap.Name sName;

		// get the name of SOAP element
		sName = e.getElementName();

		log.logp(Level.FINE, className, "processSOAPHeaderInfo",
				"--- \tElementTag=" + e.getElementName().getQualifiedName());

		// get an iterator on child elements of SOAP element
		java.util.Iterator childElems = e.getChildElements();

		SOAPElement child;
		// loop through child elements

		while (childElems.hasNext()) {
			// get next child element
			Object elem = childElems.next();

			if (elem instanceof SOAPElement) {
				child = (SOAPElement) elem;
				sName = child.getElementName();

				// get the value of userId element
				if (sName.getLocalName().equalsIgnoreCase("endUserId")) {
					log.logp(Level.FINE, className, "processSOAPHeaderInfo",
							"---\t\tElement=userId=" + child.getValue());
					userId = child.getValue();
					found = true;
				}

				// get the value of correlationId element
				if (sName.getLocalName().equalsIgnoreCase("correlationId")) {
					log.logp(Level.FINE, className, "processSOAPHeaderInfo",
							"---\t\tElement=correlationId=" + child.getValue());
					correlationId = child.getValue();
					found = true;
				}

				// get the value of languageId element
				if (sName.getLocalName().equalsIgnoreCase("languageId")) {
					log.logp(Level.FINE, className, "processSOAPHeaderInfo",
							"---\t\tElement=languageId=" + child.getValue());
					languageId = child.getValue();
					found = true;
				}

				// get the value of applicationId element
				if (sName.getLocalName().equalsIgnoreCase("applicationId")) {
					log.logp(Level.FINE, className, "processSOAPHeaderInfo",
							"---\t\tElement=applicationId=" + child.getValue());
					applicationId = child.getValue();
					found = true;
				}

			}
		}

		if (found)
			createWorkArea();

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

		try {
			log.logp(Level.FINE, className, "createWorkArea()",
					"-- set BUS_STELVIO_CONTEXT workArea context");

			if (workArea != null) {

				workArea.begin(workAreaName);

				// userId
				if (userId != null)
					workArea.set("userId", userId);

				// correlationId
				if (correlationId != null)
					workArea.set("correlationId", correlationId);

				// languageId
				if (languageId != null)
					workArea.set("languageId", languageId);

				// applicationId
				if (applicationId != null)
					workArea.set("applicationId", applicationId);

				log.logp(Level.FINE, className, "createWorkArea()",
						"-- BUS_STELVIO_CONTEXT: userId=" + userId
								+ " correlationId=" + correlationId
								+ " languageId=" + languageId
								+ " applicationId=" + applicationId);

			} else {
				log.logp(Level.SEVERE, className, "createWorkArea()",
						"WorkArea is null");
			}
		} catch (Exception e) {
			log.logp(Level.SEVERE, className, "createWorkArea()",
					"Error operate workArea: " + getExceptionTrace(e));
		}
	}

	/**
	 * Convert exception stacktrace to string
	 * 
	 * @param exception
	 *            the Exception to convert
	 * @return the string representation of the Exception
	 */
	private String getExceptionTrace(Exception ex) {
		StringWriter sw = new StringWriter();
		ex.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}
}
