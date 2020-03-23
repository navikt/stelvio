package no.stelvio.consumer.ws;

import java.util.Collections;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.transform.dom.DOMResult;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.stelvio.common.context.RequestContext;
import no.stelvio.common.context.RequestContextHolder;

/**
 * SOAPHandler that adds Stelvio Context to the SOAP header, for use by all JAX-WS clients on the Stelvio platform.
 * 
 */
public class JaxWsConsumerContextHandler implements SOAPHandler<SOAPMessageContext> {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	
	protected static final QName STELVIO_CONTEXT_QNAME = new QName("http://www.nav.no/StelvioContextPropagation",
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
	public boolean handleMessage(SOAPMessageContext context) {
		final Boolean outbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (outbound) {
			StelvioContextData stelvioContextData = getCurrentStelvioContextData();
			try {
				SOAPEnvelope envelope = context.getMessage().getSOAPPart().getEnvelope();
				SOAPHeader header = envelope.getHeader();
				if (header == null) {
					header = envelope.addHeader();
				}
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbMarshaller.marshal(stelvioContextData, new DOMResult(header));
			} catch (SOAPException e) {
				log.error("Errpr with the SOAP envelope/header: ", e);
			} catch (JAXBException e) {
				log.error("Error while marshalling the stelvioContextData element: ", e);			}
		}
		return true;
	}

	private StelvioContextData getCurrentStelvioContextData() {
		RequestContext requestContext = RequestContextHolder.currentRequestContext();
		StelvioContextData stelvioContextData = new StelvioContextData();
		stelvioContextData.setApplicationId(requestContext.getComponentId());
		stelvioContextData.setCorrelationId(requestContext.getTransactionId());
		stelvioContextData.setUserId(requestContext.getUserId());
		return stelvioContextData;
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		return true;
	}

	@Override
	public void close(MessageContext context) {
	}

	@Override
	public Set<QName> getHeaders() {
		return PROCESSED_HEADERS_QNAME;
	}

}
