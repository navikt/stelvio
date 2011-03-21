package stelvio.example.echo.provider.context;

import java.util.Collections;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import no.stelvio.common.context.RequestContext;
import no.stelvio.common.context.RequestContextHolder;
import no.stelvio.common.context.support.SimpleRequestContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author test@example.com
 * @author person5dc3535ea7f4
 */
public class StelvioContextHandler implements SOAPHandler<SOAPMessageContext> {
	private static final String CLASSNAME = StelvioContextHandler.class.getName();

	private static final Logger LOGGER = Logger.getLogger(CLASSNAME);

	protected final Log log = LogFactory.getLog(this.getClass());

	private static final QName STELVIO_CONTEXT_QNAME = new QName("http://www.nav.no/StelvioContextPropagation",
			"StelvioContext");
	private static final Set<QName> PROCESSED_HEADERS_QNAME = Collections.singleton(STELVIO_CONTEXT_QNAME);

	private final JAXBContext jaxbContext;

	public StelvioContextHandler() {
		try {
			jaxbContext = JAXBContext.newInstance(StelvioContextData.class);
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Set<QName> getHeaders() {
		return PROCESSED_HEADERS_QNAME;
	}

	@Override
	public void close(MessageContext context) {
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		return true;
	}

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		final String methodName = "handleMessage";

		try {
			Boolean outbound = (Boolean) context.get(SOAPMessageContext.MESSAGE_OUTBOUND_PROPERTY);
			if (!outbound) {
				Object[] headers = context.getHeaders(STELVIO_CONTEXT_QNAME, jaxbContext, true);
				switch (headers.length) {
				case 0:
					break; // Burde meldinger uten header trigge en feil? Dette er jo kun inkommende meldinger
				case 1:
					StelvioContextData contextData = (StelvioContextData) headers[0];

					RequestContext requestContext = new SimpleRequestContext.Builder().screenId("UNKNOWN_SCREEN").moduleId(
							"UNKNOWN_MODULE").transactionId(contextData.getCorrelationId()).userId(contextData.getUserId())
							.componentId(contextData.getApplicationId()).processId("NO_PROCESS").build();

					RequestContextSetter.setRequestContext(requestContext);
					break;
				default:
					throw new RuntimeException("Expected zero or one " + STELVIO_CONTEXT_QNAME + " headers - got "
							+ headers.length + ".");
				}

				// DEBUG Preliminary debug output
				if (log.isDebugEnabled()) {
					log.debug("Header values: " + RequestContextHolder.currentRequestContext().getComponentId() + ", "
							+ RequestContextHolder.currentRequestContext().getModuleId() + ", "
							+ RequestContextHolder.currentRequestContext().getProcessId() + ", "
							+ RequestContextHolder.currentRequestContext().getScreenId() + ", "
							+ RequestContextHolder.currentRequestContext().getTransactionId() + ", "
							+ RequestContextHolder.currentRequestContext().getUserId());
				}
			}
		} catch (Exception e) {
			LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "CatchedError: " + ExceptionUtils.getStackTrace(e));
		}

		return true;
	}
}
