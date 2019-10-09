package no.stelvio.provider.context;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.stelvio.common.context.RequestContextHolder;
import no.stelvio.common.context.support.RequestContextSetter;

@RunWith(MockitoJUnitRunner.class)
public class StelvioContextHandlerTest {

	private StelvioContextHandler handler;
	
	@Mock
	private SOAPMessageContext messagecontext;
	
	@Before
	public void setUp() {
		handler = new StelvioContextHandler();
		when(messagecontext.get(SOAPMessageContext.MESSAGE_OUTBOUND_PROPERTY)).thenReturn(Boolean.FALSE);		
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testClose() {
		RequestContextSetter.setRequestContextForUnitTest();
		Assert.assertNotNull(RequestContextHolder.currentRequestContext());
		handler.close(messagecontext);
		// expect exception since handler should reset RequestContext in close()
		Assert.assertTrue(callToCurrentRequestContextThrowsException());
		RequestContextSetter.resetRequestContext();
	}

	@Test
	public void testHandleMessageWithEmptyMessage() {
		when(messagecontext.getHeaders(anyObject(), anyObject(), anyBoolean())).thenReturn(new Object[0]);
		handler.handleMessage(messagecontext);
		// RequestContext should be set after handleMessage
		Assert.assertNotNull(RequestContextHolder.currentRequestContext());
		// Assert default values
		Assert.assertEquals(RequestContextHolder.currentRequestContext().getScreenId(), "UNKNOWN_SCREEN");
		Assert.assertEquals(RequestContextHolder.currentRequestContext().getModuleId(), "UNKNOWN_MODULE");
		Assert.assertNotNull(RequestContextHolder.currentRequestContext().getTransactionId()); // should be generated in this case
		Assert.assertEquals(RequestContextHolder.currentRequestContext().getUserId(), "UNKNOWN_USER");
		Assert.assertEquals(RequestContextHolder.currentRequestContext().getComponentId(), "UNKNOWN_COMPONENT");
		Assert.assertEquals(RequestContextHolder.currentRequestContext().getProcessId(), "NO_PROCESS");
		
		handler.close(messagecontext);
		// expect exception since handler should reset RequestContext in close()
		Assert.assertTrue(callToCurrentRequestContextThrowsException());
	}

	@Test
	public void testHandleMessageWithOneStelvioHeader() {
		when(messagecontext.getHeaders(anyObject(), anyObject(), anyBoolean())).thenReturn(new Object[]{getStelvioContext()});
		handler.handleMessage(messagecontext);
		// RequestContext should be set after handleMessage
		Assert.assertNotNull(RequestContextHolder.currentRequestContext());
		// Assert default values
		Assert.assertEquals(RequestContextHolder.currentRequestContext().getTransactionId(),"correlationId");		
		Assert.assertEquals(RequestContextHolder.currentRequestContext().getUserId(), "userId");
		Assert.assertEquals(RequestContextHolder.currentRequestContext().getComponentId(), "applicationId");
		
		handler.close(messagecontext);
		// expect exception since handler should reset RequestContext in close()
		Assert.assertTrue(callToCurrentRequestContextThrowsException());
	}

	@Test
	public void testHandleMessageWithTwoStelvioHeaders() {
		when(messagecontext.getHeaders(anyObject(), anyObject(), anyBoolean())).thenReturn(new Object[]{new StelvioContextData(), new StelvioContextData()});
		handler.handleMessage(messagecontext);
		// expect exception since handler should not set RequestContext with two headers as input
		Assert.assertTrue(callToCurrentRequestContextThrowsException());
	}

	
	/**
	 * Checks whether a call to RequestcontextHolder.currentRequestContext() throws an IllegalStateException.
	 * 
	 * @return true if exception is thrown, otherwise false
	 */
	private boolean callToCurrentRequestContextThrowsException() {
		try {
			RequestContextHolder.currentRequestContext();
		} catch (IllegalStateException e) {
			return true;
		}
		return false;
	}
	
	private Object getStelvioContext() {
		StelvioContextData scd = new StelvioContextData();
		scd.setApplicationId("applicationId");
		scd.setCorrelationId("correlationId");
		scd.setLanguageId("languageId");
		scd.setUserId("userId");
		return scd;
	}
	
}
