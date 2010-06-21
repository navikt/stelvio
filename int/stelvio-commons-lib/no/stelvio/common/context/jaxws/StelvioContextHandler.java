package no.stelvio.common.context.jaxws;

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

import no.stelvio.common.context.StelvioContextData;
import no.stelvio.common.context.StelvioContextRepository;
import no.stelvio.common.util.ExceptionUtils;

public class StelvioContextHandler implements SOAPHandler<SOAPMessageContext> {
	private static final String CLASSNAME = StelvioContextHandler.class.getName();
	private static final Logger LOGGER = Logger.getLogger(CLASSNAME);

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
		final String methodName = "handleResponse";

		try {
			StelvioContextRepository.removeContext();
		} catch (Exception e) {
			LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "CatchedError: " + ExceptionUtils.getStackTrace(e));
		}
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
					break;
				case 1:
					StelvioContextData contextData = (StelvioContextData) headers[0];
					StelvioContextRepository.createOrUpdateContext(contextData);
					break;
				default:
					throw new RuntimeException("Expected zero or one " + STELVIO_CONTEXT_QNAME + " headers - got "
							+ headers.length + ".");
				}
			}
		} catch (Exception e) {
			LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "CatchedError: " + ExceptionUtils.getStackTrace(e));
		}

		return true;
	}
}
