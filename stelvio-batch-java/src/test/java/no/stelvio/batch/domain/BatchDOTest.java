package no.stelvio.batch.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import no.stelvio.common.context.RequestContext;
import no.stelvio.common.context.support.RequestContextSetter;
import no.stelvio.common.context.support.SimpleRequestContext;
import no.stelvio.domain.time.ChangeStamp;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for BatchDO.
 *
 * @see BatchDO
 * 
 */
public class BatchDOTest {

	private String userId = "xxx9999";
	/**
	 * Setup before testing. Creates a new request context.
	 */
	@Before
	public void setUp() {
		RequestContext ctx = new SimpleRequestContext("id", "module", "process", "transaction");
		RequestContextSetter.setRequestContext(ctx);
	}
	
	/**
	 * Testing hashCode().
	 * 
	 * @see BatchDO#hashCode()
	 */
	@Test
	public void testHashCode() {
		BatchDO batch = new BatchDO();
		batch.setChangeStamp(new ChangeStamp(userId));
			
		batch.setBatchname("Batch");
		assertTrue("Test 1: not equals", batch.equals(batch));
		assertTrue("Test 2: not equals", batch.hashCode() == batch.hashCode());

		BatchDO b2 = new BatchDO();
		batch.setBatchname("Batch");
		assertFalse("Test 3: not equals", batch.equals(b2));

	}

	/**
	 * Testing BatchDO().
	 * 
	 * @see BatchDO#BatchDO()
	 */
	@Test
	public void testBatch() {
		BatchDO batch = new BatchDO();
		assertNull(batch.getBatchname());
		assertNull(batch.getParameters());
		assertNull(batch.getStatus());
	}

	/**
	 * Testing setBatchname().
	 * 
	 * @see BatchDO#setBatchname(String)
	 */
	@Test
	public void testSetBatchname() {
		BatchDO batch = new BatchDO();
		assertNull(batch.getBatchname());
		batch.setChangeStamp(new ChangeStamp(userId));
		batch.setBatchname("name");
		assertEquals("Test 1: not same batch name", "name", batch.getBatchname());
	}

	/**
	 * Testing setParameters().
	 * 
	 * @see BatchDO#setParameters(String)
	 */
	@Test
	public void testSetParameters() {
		BatchDO batch = new BatchDO();
		assertNull(batch.getParameters());
		batch.setChangeStamp(new ChangeStamp(userId));
		batch.setParameters("param");
		assertEquals("Test 1: not same batch param", "param", batch.getParameters());
	}

	/**
	 * Testing setStatus().
	 * 
	 * @see BatchDO#setStatus(String)
	 */
	@Test
	public void testSetStatus() {
		BatchDO batch = new BatchDO();
		assertNull(batch.getStatus());
		batch.setChangeStamp(new ChangeStamp(userId));
		batch.setStatus("status");
		assertEquals("Test 1: not same batch status", "status", batch.getStatus());

	}

	/**
	 * Testing setVersion().
	 * 
	 * @see BatchDO#setVersion(long)
	 */
	@Test
	public void testSetVersion() {
		BatchDO batch = new BatchDO();
		assertEquals(batch.getVersion(), 0L);
		batch.setChangeStamp(new ChangeStamp(userId));
		batch.setVersion(1);
		assertEquals("not same version", 1L, batch.getVersion());
	}

	/**
	 * Testing setChangeStamp().
	 * 
	 * @see BatchDO#setChangeStamp(ChangeStamp)
	 */
	@Test
	public void testSetChangeStamp() {
		ChangeStamp stamp = new ChangeStamp(userId);
		BatchDO batch = new BatchDO();
		assertNull(batch.getChangeStamp());
		batch.setChangeStamp(stamp);
		assertEquals("not same changeStamp", userId, batch.getChangeStamp().getCreatedBy());
	}

	/**
	 * Testing toString().
	 * 
	 * @see BatchDO#toString()
	 */
	@Test
	public void testToString() {
		BatchDO batch = new BatchDO();
		batch.setChangeStamp(new ChangeStamp(userId));
		batch.setBatchname("batch");
		assertNotNull(batch.toString());
	}

}
