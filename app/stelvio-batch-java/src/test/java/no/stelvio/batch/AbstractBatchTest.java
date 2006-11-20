package no.stelvio.batch;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jmock.integration.junit3.MockObjectTestCase;

import no.stelvio.batch.domain.BatchDO;
import no.stelvio.common.error.UnrecoverableException;
import no.stelvio.common.util.MessageFormatter;

/**
 * @author person356941106810, Accenture
 * @version $Id: AbstractBatchTest.java 1955 2005-02-08 15:43:02Z psa2920 $
 * @todo make a MockJpaTemplate to use here
 */
public class AbstractBatchTest extends MockObjectTestCase {

	private AbstractBatch testClass = null;
	private int desiredStatus = BatchStatus.BATCH_OK;

	protected void setUp() throws Exception {
		// this line is added just to get 100% on batchstatus
		new BatchStatus();
		// create an implementation of the batch which we can use to test
		testClass = new AbstractBatch("TestBatch") {
			public int executeBatch() {
				return desiredStatus;
			}
		};

		// Setting a dummy formattter
		testClass.setMsgFormatter(new MessageFormatter() {
			public String formatMessage(Object[] params) {
				return null;
			}
		});

//		mockHibernateTemplate = new MockHibernateTemplate();
//		testClass.setHibernateTemplate(mockHibernateTemplate);
	}

	private void initHibernateTemplateFindExpectations() {
		final BatchDO batchDO = new BatchDO();
		batchDO.setParameters("param1=value1;param2=value2");

		List list = new ArrayList();
		list.add(batchDO);

/*
		mockHibernateTemplate.
		        expects(once()).
		        method("findByNamedQueryAndNamedParam").
		        with(eq("BATCH_BY_BATCHNAME"), eq("batchname"), eq("testBatch")).
		        will(returnValue(list));
*/
	}

	private void initHibernateTemplateSaveExpectations() {
/*
		mockHibernateTemplate.
		        expects(once()).
		        method("saveOrUpdate").with(eq(null));
*/
	}

	public void testReadBatchParameters() {
		initHibernateTemplateFindExpectations();
		testClass.readBatchParameters("testBatch");
	}

	public void testBatchPropertiesAreReadCorrectly() {
		initHibernateTemplateFindExpectations();
		BatchDO b = testClass.readBatchParameters("testBatch");
		Properties props = testClass.getBatchProperties(b);

		assertNotNull("No props were loaded!", props);
		assertEquals("Incorrect property", "value1", props.getProperty("param1"));
		assertEquals("Incorrect property", "value2", props.getProperty("param2"));
	}

	public void testBatchHasNoParameters() {
		initHibernateTemplateFindExpectations();
		BatchDO b = testClass.readBatchParameters("testBatch");
		b.setParameters(null);
		Properties props = testClass.getBatchProperties(b);

		assertNull("Props were not null", props);
	}

	public void testExceptionThrownWhenBatchPropertiesIsNull() {
		try {
			testClass.getBatchProperties(null);
			fail("UnrecoverableException should have been thrown");
		} catch (UnrecoverableException se) {
            // should happen
		}
	}

	public void testBatchPropertiesInUnexpectedFormat() {
		initHibernateTemplateFindExpectations();
		BatchDO b = testClass.readBatchParameters("testBatch");
		Properties props;

		b.setParameters("xyz");
		props = testClass.getBatchProperties(b);
		assertEquals("Unexpected value", "", props.getProperty("xyz"));

		b.setParameters("=xxx");
		props = testClass.getBatchProperties(b);
		assertEquals("Unexpected value", "xxx", props.getProperty(""));
	}

	public void testSetNullBatchProperties() {
		BatchDO b = new BatchDO();
		b.setParameters("x=y");
		testClass.setBatchProperties(b, null);

		assertNull("The props should have been null", b.getParameters());
	}

	public void testSetNullBatch() {
		try {
			testClass.setBatchProperties(null, new Properties());
			fail("UnrecoverableException should have been thrown");
		} catch (UnrecoverableException se) {
            // should happen
		}
	}

	public void testSetBatchPropertiesOk() {
		BatchDO b = new BatchDO();
		Properties props = new Properties();
		props.setProperty("A", "B");
		props.setProperty("C", "D");

		testClass.setBatchProperties(b, props);

		assertNotNull("Props should not be null", b.getParameters());
		assertTrue("Unexpected properties format",
		        ("A=B;C=D".equals(b.getParameters()) || "C=D;A=B".equals(b.getParameters())));
	}

	public void testUpdateBatchStatus() {
		initHibernateTemplateSaveExpectations();
		testClass.updateBatchStatus(null);
	}
}
