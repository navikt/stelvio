package no.stelvio.consumer.ws;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.i18n.LocaleContextHolder;

import no.stelvio.common.context.RequestContextHolder;

/**
 * Handler for the ConsumerContext when working with JAX-RPC. For JAX-WS, see JaxWsConsumerContextHandler.
 * 
 * @author lschnell
 *
 * @deprecated see {@link ConsumerFacadeBase}
 */
@Deprecated
public class ConsumerContextHandler extends GenericHandler {

	/** Logger for this class. */
	protected final Log log = LogFactory.getLog(this.getClass());

	/** Handler info. */
	protected HandlerInfo info = null;

	/**
	 * Creates a new instance of ConsumerContextHandler.
	 */
	public ConsumerContextHandler() {
	}

	@Override
	public QName[] getHeaders() {
		return info.getHeaders();
	}

	/**
	 * Sets then handler info for the object.
	 * 
	 * @param arg the HandlerInfo
	 */
	@Override
	public void init(HandlerInfo arg) {
		info = arg;
	}

	/**
	 * Processes the SOAP MessageContext.
	 * <p>
	 * The header element created should look like this:
	 * 
	 * <pre>
	 * &lt;sc:StelvioContext xmlns:sc=&quot;http://www.nav.no/StelvioContextPropagation&quot;&gt;
	 *     &lt;sc:userId&gt;myUserId&lt;sc:userId/&gt;
	 *     &lt;sc:correlationId&gt;myCorrelationId&lt;/sc:correlationId&gt;
	 *     &lt;sc:languageId&gt;myLanguageId&lt;/sc:languageId&gt;
	 *     &lt;sc:applicationId&gt;myApplicationId&lt;/sc:applicationId&gt;
	 * &lt;/sc:StelvioContext&gt;
	 * </pre>
	 * 
	 * @param context the MessageContext
	 * @return true Always returns true
	 */
	@Override
	public boolean handleRequest(MessageContext context) {
		try {

			SOAPMessageContext smc = (SOAPMessageContext) context;
			SOAPEnvelope se = smc.getMessage().getSOAPPart().getEnvelope();
			SOAPHeader sh = se.getHeader();
			SOAPHeaderElement mainElement = sh.addHeaderElement(se.createName("StelvioContext", "sc",
					"http://www.nav.no/StelvioContextPropagation"));
			SOAPElement el = mainElement.addChildElement("userId");
			el.addTextNode(RequestContextHolder.currentRequestContext().getUserId());
			el = mainElement.addChildElement("correlationId");
			el.addTextNode(RequestContextHolder.currentRequestContext().getTransactionId());
			el = mainElement.addChildElement("languageId");
			if (LocaleContextHolder.getLocaleContext() != null && LocaleContextHolder.getLocaleContext().getLocale() != null) {
				el.addTextNode(LocaleContextHolder.getLocaleContext().getLocale().getLanguage());
			} else {
				el.addTextNode("unknown");
			}
			el = mainElement.addChildElement("applicationId");
			el.addTextNode(RequestContextHolder.currentRequestContext().getComponentId());

		} catch (SOAPException x) {
			log.error("Soap Error while creating request context header", x);
		}
		return true;
	}
}