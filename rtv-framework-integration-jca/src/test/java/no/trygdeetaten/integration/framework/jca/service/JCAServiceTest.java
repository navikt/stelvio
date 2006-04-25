package no.trygdeetaten.integration.framework.jca.service;

import java.util.HashMap;
import java.util.Map;

import javax.resource.ResourceException;
import javax.resource.cci.Connection;
import javax.resource.cci.ConnectionFactory;
import javax.resource.cci.Interaction;
import javax.resource.cci.InteractionSpec;
import javax.resource.cci.Record;
import javax.resource.spi.ResourceAllocationException;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.core.Stub;
import org.jmock.core.stub.ThrowStub;

import no.trygdeetaten.common.framework.FrameworkError;
import no.trygdeetaten.common.framework.ejb.LookupHelper;
import no.trygdeetaten.common.framework.error.SystemException;
import no.trygdeetaten.common.framework.service.ServiceFailedException;
import no.trygdeetaten.common.framework.service.ServiceRequest;

/**
 * Unit test for JCAService.
 * 
 * @author person5b7fd84b3197, Accenture
 * @version $Id: JCAServiceTest.java 2756 2006-02-01 12:57:00Z skb2930 $
 */
public class JCAServiceTest extends MockObjectTestCase {

	private JCAService jcaservice = null;

	private Mock recordMapperMock = null;
	private Mock lookupHelperMock = null;
	private Mock interactionSpecMock = null;
	private Mock interaction = null;
	private Mock recordMock = null;
	private Mock connectionFactoryMock = null;
	private Mock connectionMock = null;

	protected void setUp() {
		jcaservice = new JCAService();

		recordMapperMock = mock(RecordMapper.class);
		lookupHelperMock = mock(LookupHelper.class);
		interactionSpecMock = mock(InteractionSpec.class);
		interaction = mock(Interaction.class);
		connectionMock = mock(Connection.class);
		recordMock = mock(Record.class);
		connectionFactoryMock = mock( ConnectionFactory.class );

		jcaservice.setInteractions(new HashMap());
		jcaservice.setLookupHelper((LookupHelper) lookupHelperMock.proxy());
		jcaservice.setRecordMapper((RecordMapper) recordMapperMock.proxy());
	}

	public void testInit() {
		final JCAService initService = new JCAService();

		try {
			initService.init();
			fail("Should have thrown SystemException");
		} catch (SystemException e) {
			assertEquals("Wrong errorcode;", FrameworkError.JCA_SERVICE_PROPERTY_MISSING.getCode(), e.getErrorCode());
		}

		initService.setLookupHelper((LookupHelper) lookupHelperMock.proxy());
		try {
			initService.init();
			fail("Should have thrown SystemException");
		} catch (SystemException e) {
			assertEquals("Wrong errorcode;", FrameworkError.JCA_SERVICE_PROPERTY_MISSING.getCode(), e.getErrorCode());
		}

		initService.setRecordMapper((RecordMapper) recordMapperMock.proxy());
		try {
			initService.init();
			fail("Should have thrown SystemException");
		} catch (SystemException e) {
			assertEquals("Wrong errorcode;", FrameworkError.JCA_SERVICE_PROPERTY_MISSING.getCode(), e.getErrorCode());

		}

		initService.setJndiName("set/jndi/name");
		try {
			initService.init();
			fail("Should have thrown SystemException");
		} catch (SystemException e) {
			assertEquals("Wrong errorcode;", FrameworkError.JCA_SERVICE_PROPERTY_MISSING.getCode(), e.getErrorCode());
		}

		Map interactions = new HashMap();
		initService.setInteractions(interactions);
		try {
			initService.init();
			fail("Should have thrown SystemException");
		} catch (SystemException e) {
			assertEquals("Wrong errorcode;", FrameworkError.JCA_SERVICE_PROPERTY_MISSING.getCode(), e.getErrorCode());
		}

		interactions.put("TEST", new Object());
		initService.init();
	}

	public void testDoExecuteServiceNull() {
		try{
			jcaservice.doExecute( new ServiceRequest() );
			fail("Should have thrown ServiceFailedException");
		} catch( ServiceFailedException e ) {
			assertEquals("Wrong errorcode;", FrameworkError.SERVICE_INPUT_MISSING.getCode(), e.getErrorCode());
		}
	}

	public void testDoExecute() throws ServiceFailedException {
		ServiceRequest request = setupCommon(returnValue(interaction.proxy()), throwException(new ResourceException("test")));

		try{
			jcaservice.doExecute( request );
			fail("Should have thrown SystemException");
		} catch( SystemException e ) {
			assertEquals("Wrong errorcode;", FrameworkError.JCA_GET_CONNECTION_ERROR.getCode(), e.getErrorCode());
		}
	}

	public void testDoExecuteCreateInteractionFail() throws ServiceFailedException {
		ServiceRequest request =
				setupCommon(throwException(new ResourceException("test")), returnValue(connectionMock.proxy()));

		try{
			jcaservice.doExecute( request );
			fail("Should have thrown SystemException");
		} catch( SystemException e ) {
			assertEquals("Wrong errorcode;", FrameworkError.JCA_CREATE_INTERACTION_ERROR.getCode(), e.getErrorCode());
		}
	}

	public void testDoExecuteCreateInteraction() throws ServiceFailedException {
		ServiceRequest request = setupCommon(returnValue(interaction.proxy()), returnValue(connectionMock.proxy()));
		interaction.expects(once()).method("execute").withAnyArguments()
				.will(throwException(new ResourceException("test")));

		try{
			jcaservice.doExecute( request );
			fail("Should have thrown SystemException");
		} catch( SystemException e ) {
			assertEquals("Wrong errorcode;", FrameworkError.JCA_INTERACT_ERROR.getCode(), e.getErrorCode());
		}
	}

	public void testErrorWorkRefusedFromCtgThrowsSpecificException() throws ServiceFailedException {
		ServiceRequest request = setupCommon(returnValue(interaction.proxy()), returnValue(connectionMock.proxy()));
		interaction.expects(once()).method("execute").withAnyArguments()
				.will(throwException(new ResourceAllocationException("reason", JCAService.WORK_WAS_REFUSED_ERROR)));

		try{
			jcaservice.doExecute( request );
			fail("Should have thrown SystemException");
		} catch( SystemException e ) {
			assertEquals("Wrong errorcode;", FrameworkError.JCA_WORK_WAS_REFUSED_ERROR.getCode(), e.getErrorCode());
		}
	}

	public void testDoExecuteCloseCon() throws ServiceFailedException {
		ServiceRequest request = setupCommon(returnValue(interaction.proxy()), returnValue(connectionMock.proxy()));

		interaction.expects(once()).method("execute").withAnyArguments().will(returnValue(true));
		recordMapperMock.expects(once()).method("recordToClass").withAnyArguments().will(returnValue(new Object()));

		jcaservice.doExecute( request );
	}

	private ServiceRequest setupCommon(final Stub createInteractionWillStub, final Stub getConnectionWillStub) {
		connectionFactoryMock.expects(once()).method("getConnection").withAnyArguments()
				.will(getConnectionWillStub);
		lookupHelperMock.expects(once()).method("lookup").withAnyArguments()
				.will(returnValue(connectionFactoryMock.proxy()));

		// Only setup expectations on Connection if the retrieval of the Connection is supposed to go through without an
		// Exception being thrown
		if (!(getConnectionWillStub instanceof ThrowStub)) {
			connectionMock.expects(once()).method("createInteraction").withNoArguments().will(createInteractionWillStub);
			connectionMock.expects(once()).method("close").withNoArguments();
		}

		recordMapperMock.expects(once()).method("createInteractionSpec").withAnyArguments()
				.will(returnValue(interactionSpecMock.proxy()));
		recordMapperMock.expects(once()).method("classToRecord").withAnyArguments().will(returnValue(recordMock.proxy()));

		ServiceRequest request = new ServiceRequest();
		request.setData("SystemServiceName", "test");

		return request;
	}
}
