package no.stelvio.consumer.ws;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import no.stelvio.common.context.support.RequestContextSetter;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for JaxWsConsumerContextHandler
 * 
 *
 */
public class JaxWsConsumerContextHandlerTest {

	JaxWsConsumerContextHandler jaxWsConsumerContextHandler;
	SOAPMessageContext contextMock;
	
	@Before
	public void setUp() {
		jaxWsConsumerContextHandler = new JaxWsConsumerContextHandler();
		contextMock = mock(SOAPMessageContext.class);	
		RequestContextSetter.setRequestContextForUnitTest();
	}
	
	@Test 
	public void testHandleMessage() throws Exception {
		SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();
		when(contextMock.getMessage()).thenReturn(soapMessage);
		when(contextMock.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY)).thenReturn(Boolean.TRUE);
		jaxWsConsumerContextHandler.handleMessage(contextMock);
		SOAPHeader header = soapMessage.getSOAPPart().getEnvelope().getHeader();
		assertThat(header, is(notNullValue()));
		assertThat(header.getChildElements().hasNext(), is(true));
	}
}
