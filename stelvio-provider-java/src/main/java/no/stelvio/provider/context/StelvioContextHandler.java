package no.stelvio.provider.context;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.stelvio.common.context.RequestContextHolder;
import no.stelvio.common.context.support.RequestContextSetter;
import no.stelvio.common.context.support.SimpleRequestContext;

/**
 */
public class StelvioContextHandler implements SOAPHandler<SOAPMessageContext> {

	private final Log log = LogFactory.getLog(this.getClass());

	private static final QName STELVIO_CONTEXT_QNAME = new QName("http://www.nav.no/StelvioContextPropagation",
			"StelvioContext");
	private static final Set<QName> PROCESSED_HEADERS_QNAME = Collections.singleton(STELVIO_CONTEXT_QNAME);

	private static final JAXBContext jaxbContext;

	static {
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
		RequestContextSetter.resetRequestContext();
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		return true;
	}

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		try {
			Boolean outbound = (Boolean) context.get(SOAPMessageContext.MESSAGE_OUTBOUND_PROPERTY);
			if (!outbound) {
				Object[] headers = context.getHeaders(STELVIO_CONTEXT_QNAME, jaxbContext, true);
				switch (headers.length) {
				case 0:
					RequestContextSetter.setRequestContext(new SimpleRequestContext.Builder()
							.screenId("UNKNOWN_SCREEN")
							.moduleId("UNKNOWN_MODULE")
							.transactionId(UUID.randomUUID().toString())
							.userId("UNKNOWN_USER")
							.componentId("UNKNOWN_COMPONENT")
							.processId("NO_PROCESS").build());
					break; // Burde meldinger uten header trigge en feil? Dette er jo kun inkommende meldinger
				case 1:
					StelvioContextData contextData = (StelvioContextData) headers[0];

					RequestContextSetter.setRequestContext(new SimpleRequestContext.Builder()
							.screenId("UNKNOWN_SCREEN")
							.moduleId("UNKNOWN_MODULE")
							.transactionId(contextData.getCorrelationId())
							.userId(contextData.getUserId()).
							componentId(contextData.getApplicationId())
							.processId("NO_PROCESS").build());
					break;
				default:
					throw new RuntimeException("Expected zero or one " + STELVIO_CONTEXT_QNAME + " headers - got "
							+ headers.length + ".");
				}

				if (log.isDebugEnabled()) {
					log.debug("RequestContext: " + RequestContextHolder.currentRequestContext());
				}
			}
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			log.error("Error propagating StelvioContext: " + sw.toString());
		}

		return true;
	}
}
