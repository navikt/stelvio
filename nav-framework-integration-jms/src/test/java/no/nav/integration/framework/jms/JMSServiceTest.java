package no.nav.integration.framework.jms;

import junit.framework.TestCase;

import no.nav.integration.framework.jms.JMSService;
import no.trygdeetaten.common.framework.FrameworkError;
import no.trygdeetaten.common.framework.service.ServiceFailedException;
import no.trygdeetaten.common.framework.service.ServiceRequest;
import no.trygdeetaten.common.framework.service.ServiceResponse;
import no.trygdeetaten.common.framework.error.SystemException;

/**
 * Enhetstest for {@link JMSService}.
 *
 * @author person356941106810, Accenture
 * @version $Revision: 2737 $ $Author: skb2930 $ $Date: 2006-01-12 03:24:04 +0100 (Thu, 12 Jan 2006) $
 */
public class JMSServiceTest extends TestCase {
	private JMSService service;
	private DummyJndiHelper helper;
	private DummyMessageFormatter formatter;

	protected void setUp() throws Exception {
		service = new JMSService();
		helper = new DummyJndiHelper();
		formatter = new DummyMessageFormatter();

		// init the Service
		service.setLookupHelper(helper);
		service.setMessageFormatter(formatter);
		service.setQueueConnectionFactoryJndi("jms/qcf/testQcf");
		service.setQueueJndi("jms/queue/testQueue");
		service.setSpecifyReturnQueue(true);
		service.setUseTempQueue(true);
		service.setIsSynchronous(true);
		service.setMessageHandler(new DummyMessageHandler());
		service.init();
		service.setMessageTtl(100);
		service.setBeanName("JMSService");
	}

	public void testShouldReceiveAndReturnNothingWhenNotSynchronous() throws ServiceFailedException {
		service.setIsSynchronous(false);

		assertNull("Should return null", service.doExecute(new ServiceRequest()));
	}


	public void testExceptionIsTranslatedLookingUpReturnQueue() {
		service.setUseTempQueue(false);
		service.setReturnQueueJndi("jms/queue/dummyQueue");

		try {
			service.init();
			fail("SystemException should have been thrown");
		} catch (SystemException e) {
			assertEquals("Wrong error code;", FrameworkError.UNSPECIFIED_ERROR.getCode(), e.getErrorCode());
		}
	}

	public void testExceptionIsTranslatedFailingLookingUpQueue() {
		service.setQueueJndi("jms/queue/dummyQueue");

		try {
			service.init();
			fail("SystemException should have been thrown");
		} catch (SystemException e) {
			assertEquals("Wrong error code;", FrameworkError.UNSPECIFIED_ERROR.getCode(), e.getErrorCode());
		}
	}

	public void testExceptionIsTranslatedFailingLookingUpQcf() {
		service.setQueueConnectionFactoryJndi("jms/qcf/dummyQcf");

		try {
			service.init();
			fail("SystemException should have been thrown");
		} catch (SystemException e) {
			assertEquals("Wrong error code;", FrameworkError.UNSPECIFIED_ERROR.getCode(), e.getErrorCode());
		}
	}

	public void testExceptionIsTranslatedWhenHandlerNotSet() {
		service.setMessageHandler(null);

		try {
			service.init();
			fail("SystemException should have been thrown");
		} catch (SystemException e) {
			assertEquals("Wrong error code;", FrameworkError.JMS_SERVICE_PROPERTY_MISSING.getCode(), e.getErrorCode());
		}
	}

	public void testExceptionIsTranslatedWhenMessageFormatterNotSet() {
		service.setMessageFormatter(null);

		try {
			service.init();
			fail("SystemException should have been thrown");
		} catch (SystemException e) {
			assertEquals("Wrong error code;", FrameworkError.JMS_SERVICE_PROPERTY_MISSING.getCode(), e.getErrorCode());
		}
	}

	public void testExceptionIsTranslatedForErrorInInit() {
		// test: use temp = true and return queue JNDI is not null
		service.setIsSynchronous(false);
		service.setReturnQueueJndi("test");

		try {
			service.init();
			fail("SystemException should have been thrown");
		} catch (SystemException e) {
			assertEquals("Wrong error code;", FrameworkError.JMS_INVALID_RETURN_QUEUE.getCode(), e.getErrorCode());
		}
	}

	public void testExceptionIsTranslatedSyncTrue() {
		service.setSpecifyReturnQueue(false);
		service.setUseTempQueue(false);
		service.setReturnQueueJndi(null);

		try {
			service.init();
			fail("SystemException should have been thrown");
		} catch (SystemException e) {
			assertEquals("Wrong error code;", FrameworkError.JMS_INVALID_SYNCH_CONFIG_ERROR.getCode(), e.getErrorCode());
		}
	}

	public void testExceptionIsTranslatedWhenReturnQueueIsNotSet() {
		// test: spec. return queue = true and return queue JNDI not set and temp queue not set
		service.setUseTempQueue(false);
		service.setSpecifyReturnQueue(true);
		service.setReturnQueueJndi(null);
		try {
			service.init();
			fail("SystemException should have been thrown");
		} catch (SystemException e) {
			assertEquals("Wrong error code;", FrameworkError.JMS_INVALID_RETURN_QUEUE.getCode(), e.getErrorCode());
		}
	}

	public void testExceptionIsTranslatedWhenReturnQueueSet() {
		// test: spec. return queue = false and return queue jndi != null
		service.setUseTempQueue(false);
		service.setSpecifyReturnQueue(false);
		service.setReturnQueueJndi("test");
		try {
			service.init();
			fail("SystemException should have been thrown");
		} catch (SystemException e) {
			assertEquals("Wrong error code;", FrameworkError.JMS_INVALID_RETURN_QUEUE.getCode(), e.getErrorCode());
		}
	}

	public void testExceptionIsTranslatedWhenTempQueueNotSet() {
		// test: spec. return queue = false and use temp queue = true
		service.setSpecifyReturnQueue(false);
		service.setUseTempQueue(true);

		try {
			service.init();
			fail("SystemException should have been thrown");
		} catch (SystemException e) {
			assertEquals("Wrong error code;", FrameworkError.JMS_INVALID_RETURN_QUEUE.getCode(), e.getErrorCode());
		}
	}

	public void testExceptionIsTranslatedWhenJndiHelperNotSet() {
		service.setLookupHelper(null);

		try {
			service.init();
			fail("SystemException should have been thrown");
		} catch (SystemException e) {
			assertEquals("Wrong error code;", FrameworkError.JMS_SERVICE_PROPERTY_MISSING.getCode(), e.getErrorCode());
		}
	}

	public void testExceptionIsTranslatedWhenQueueNotSet() {
		service.setQueueJndi(null);

		try {
			service.init();
			fail("SystemException should have been thrown");
		} catch (SystemException e) {
			assertEquals("Wrong error code;", FrameworkError.JMS_SERVICE_PROPERTY_MISSING.getCode(), e.getErrorCode());
		}
	}

	public void testExceptionIsTranslatedWhenQcfNotSet() {
		service.setQueueConnectionFactoryJndi(null);

		try {
			service.init();
			fail("SystemException should have been thrown");
		} catch (SystemException e) {
			assertEquals("Wrong error code;", FrameworkError.JMS_SERVICE_PROPERTY_MISSING.getCode(), e.getErrorCode());
		}
	}

	public void testReturnsNonNullWhenExecutionOk() throws ServiceFailedException {
		assertNotNull("Should not be null", service.doExecute(new ServiceRequest()));
	}

	public void testNothingHappensWhenCloseConnectionFails() throws ServiceFailedException {
		helper.getFactory().getConn().setFailOnClose(true);

		assertNotNull("Should not be null", service.doExecute(new ServiceRequest()));
	}

	public void testExceptionIsTranslatedWhenSendFails() throws ServiceFailedException {
		helper.getFactory().getConn().getSession().getSender().setErrorOnSend(true);

		try {
			service.doExecute(new ServiceRequest());
			fail("SystemException should have been thrown");
		} catch (SystemException e) {
			assertEquals("Wrong error code;", FrameworkError.JMS_SEND_ERROR.getCode(), e.getErrorCode());
		}
	}

	public void testExceptionIsTranslatedWhenCreateSenderFails() throws ServiceFailedException {
		helper.getFactory().getConn().getSession().setFailOnSenderCreate(true);

		try {
			service.doExecute(new ServiceRequest());
			fail("SystemException should have been thrown");
		} catch (SystemException e) {
			assertEquals("Wrong error code;", FrameworkError.JMS_SENDER_CREATION_ERROR.getCode(), e.getErrorCode());
		}
	}

	public void testExceptionIsTranslatedWhenCreateSessionFails() throws ServiceFailedException {
		helper.getFactory().getConn().setFailOnSessionCreate(true);

		try {
			service.execute(null);
			fail("SystemException should have been thrown");
		} catch (SystemException e) {
			assertEquals("Wrong error code;", FrameworkError.JMS_SESSION_CREATION_ERROR.getCode(), e.getErrorCode());
		}
	}

	public void testExceptionIsTranslatedWhenCreateQueueConnectionFails() throws ServiceFailedException {
		helper.getFactory().setFailOnCreate(true);

		try {
			service.execute(null);
			fail("SystemException should have been thrown");
		} catch (SystemException e) {
			assertEquals("Wrong error code;", FrameworkError.JMS_CONNECTION_ERROR.getCode(), e.getErrorCode());
		}
	}

	public void testExceptionIsTranslatedWhenSettingReplyToFails() throws ServiceFailedException {
		formatter.getMsg().setFailOnReplyTo(true);

		try {
			service.doExecute(new ServiceRequest());
			fail("SystemException should have been thrown");
		} catch (SystemException e) {
			assertEquals("Wrong error code;", FrameworkError.JMS_INVALID_RETURN_QUEUE.getCode(), e.getErrorCode());
		}
	}

	public void testExceptionIsTranslatedWhenTempQueueFails() throws ServiceFailedException {
		helper.getFactory().getConn().getSession().setFailOnCreateTemp(true);

		try {
			service.execute(null);
			fail("SystemException should have been thrown");
		} catch (SystemException e) {
			assertEquals("Wrong error code;", FrameworkError.JMS_ERROR_CREATE_TEMP_QUEUE.getCode(), e.getErrorCode());
		}
	}

	public void testThrowsSystemExceptionWhenReturnMessageIsNul() throws ServiceFailedException {
		helper.getFactory().getConn().getSession().getReceiver().setReturnNull(true);

		try {
			service.doExecute(new ServiceRequest());
			fail("SystemException should have been thrown");
		} catch (SystemException e) {
			assertEquals("Wrong error code;", FrameworkError.JMS_RECEIVE_ERROR.getCode(), e.getErrorCode());
		}
	}

	public void testExceptionIsTranslatedWhenWaitingFails() throws ServiceFailedException {
		helper.getFactory().getConn().getSession().getReceiver().setErrorOnReceive(true);
		service.setTimeout(100l);

		try {
			service.doExecute(new ServiceRequest());
			fail("SystemException should have been thrown");
		} catch (SystemException e) {
			assertEquals("Wrong error code;", FrameworkError.JMS_RECEIVE_ERROR.getCode(), e.getErrorCode());
		}
	}

	public void testExceptionIsTranslatedWhenReceiveNoWaitFails() throws ServiceFailedException {
		helper.getFactory().getConn().getSession().getReceiver().setErrorOnReceiveNoWait(true);
		service.setTimeout(0);

		try {
			service.doExecute(new ServiceRequest());
			fail("SystemException should have been thrown");
		} catch (SystemException e) {
			assertEquals("Wrong error code;", FrameworkError.JMS_RECEIVE_ERROR.getCode(), e.getErrorCode());
		}
	}

	public void testExceptionIsTranslatedWhenStartingConnectionFails() throws ServiceFailedException {
		helper.getFactory().getConn().setFailOnStart(true);

		try {
			service.doExecute(new ServiceRequest());
			fail("SystemException should have been thrown");
		} catch (SystemException e) {
			assertEquals("Wrong error code;", FrameworkError.JMS_RECEIVE_ERROR.getCode(), e.getErrorCode());
		}
	}

	public void testExceptionIsTranslatedWhenCreatingReceiverWithoutMessageSelectorFails() throws ServiceFailedException {
		service.setUseTempQueue(false);
		service.setMessageSelector(null);
		helper.getFactory().getConn().getSession().setFailOnCreateOfReceiverWithoutMessageSelector(true);

		try {
			service.doExecute(new ServiceRequest());
			fail("SystemException should have been thrown");
		} catch (SystemException e) {
			assertEquals("Wrong error code;", FrameworkError.JMS_RECEIVER_CREATION_ERROR.getCode(), e.getErrorCode());
		}
	}

	public void testExceptionIsTranslatedWhenCreatingReceiverWithMessageSelectorFails() throws ServiceFailedException {
		service.setUseTempQueue(false);
		service.setMessageSelector("test");
		helper.getFactory().getConn().getSession().setFailOnCreateOfReceiverWithMessageSelector(true);

		try {
			service.doExecute(new ServiceRequest());
			fail("SystemException should have been thrown");
		} catch (SystemException e) {
			assertEquals(
					"Wrong error code;",
			        FrameworkError.JMS_RECEIVER_CREATION_ERROR.getCode(),
			        e.getErrorCode());
		}
	}

	public void testExceptionIsTranslatedWhenCreatingReceiverUsingTempQueueFails() throws ServiceFailedException {
		helper.getFactory().getConn().getSession().setFailOnCreateOfReceiverWithoutMessageSelector(true);

		try {
			service.doExecute(new ServiceRequest());
			fail("SystemException should have been thrown");
		} catch (SystemException e) {
			assertEquals("Wrong error code;", FrameworkError.JMS_RECEIVER_CREATION_ERROR.getCode(), e.getErrorCode());
		}
	}

	public void testRecursiveRequestsResultsInRecursiveResponses() throws ServiceFailedException {
		final ServiceRequest request = new ServiceRequest();
		final ServiceRequest[] requests = new ServiceRequest[]{
				new ServiceRequest("serviceName", "fnr", "fnr"),
		        new ServiceRequest("serviceName", "fnr", "fnr"),
		        new ServiceRequest("serviceName", "fnr", "fnr")};

		request.setData("JMSSERVICE_MULTIPLE_MESSAGES", requests);
		ServiceResponse response = service.doExecute(request);

		assertNotNull("Should not be null", response);

		final ServiceResponse[] responses = (ServiceResponse[]) response.getData("JMSSERVICE_MULTIPLE_MESSAGES");
		assertEquals("Wrong number of messages returned;", 3, responses.length);
	}

	public void testSingleRequestResultsInSingleResponse() throws ServiceFailedException {
		ServiceResponse response = service.doExecute(new ServiceRequest());

		assertNotNull("Should not be null", response);

		final ServiceResponse[] responses = (ServiceResponse[]) response.getData("JMSSERVICE_MULTIPLE_MESSAGES");
		assertNull("Multiple messages should not be set", responses);
	}
}
