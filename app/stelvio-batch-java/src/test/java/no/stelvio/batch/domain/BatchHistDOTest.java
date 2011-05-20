//package no.stelvio.batch.domain;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertNull;
//import static org.junit.Assert.assertTrue;
//import no.stelvio.common.context.RequestContext;
//import no.stelvio.common.context.support.RequestContextSetter;
//import no.stelvio.common.context.support.SimpleRequestContext;
//import no.stelvio.domain.time.ChangeStamp;
//
//import org.junit.Before;
//import org.junit.Test;
//
///**
// * Test class for BatchHistDO.
// *
// * @see BatchHistDO
// * 
// * @author person5dc3535ea7f4 
// */
//public class BatchHistDOTest {
//
//	private String userId = "xxx9999";
//	/**
//	 * Setup before testing. Creates a new request context.
//	 */
//	@Before
//	public void setUp() {
//		RequestContext ctx = new SimpleRequestContext("id", "module", "process", "transaction");
//		RequestContextSetter.setRequestContext(ctx);
//	}
//	
//	/**
//	 * Testing hashCode().
//	 * 
//	 * @see BatchHistDO#hashCode()
//	 */
//	@Test
//	public void testHashCode() {
//		BatchHistDO batch = new BatchHistDO();
//		batch.setChangeStamp(new ChangeStamp(userId));
//			
//		batch.setBatchname("Batch");
//		assertTrue("Test 1: not equals", batch.equals(batch));
//		assertTrue("Test 2: not equals", batch.hashCode() == batch.hashCode());
//
//		BatchHistDO b2 = new BatchHistDO();
//		batch.setBatchname("Batch");
//		assertFalse("Test 3: not equals", batch.equals(b2));
//
//	}
//
//	/**
//	 * Testing BatchHistDO().
//	 * 
//	 * @see BatchHistDO#BatchHistDO()
//	 */
//	@Test
//	public void testBatch() {
//		BatchHistDO batch = new BatchHistDO();
//		assertNull(batch.getBatchname());
//		assertNull(batch.getParameters());
//		assertNull(batch.getStatus());
//	}
//
//	/**
//	 * Testing setBatchname().
//	 * 
//	 * @see BatchHistDO#setBatchname(String)
//	 */
//	@Test
//	public void testSetBatchname() {
//		BatchHistDO batch = new BatchHistDO();
//		assertNull(batch.getBatchname());
//		batch.setChangeStamp(new ChangeStamp(userId));
//		batch.setBatchname("name");
//		assertEquals("Test 1: not same batch name", "name", batch.getBatchname());
//	}
//
//	/**
//	 * Testing setParameters().
//	 * 
//	 * @see BatchHistDO#setParameters(String)
//	 */
//	@Test
//	public void testSetParameters() {
//		BatchHistDO batch = new BatchHistDO();
//		assertNull(batch.getParameters());
//		batch.setChangeStamp(new ChangeStamp(userId));
//		batch.setParameters("param");
//		assertEquals("Test 1: not same batch param", "param", batch.getParameters());
//	}
//
//	/**
//	 * Testing setStatus().
//	 * 
//	 * @see BatchHistDO#setStatus(String)
//	 */
//	@Test
//	public void testSetStatus() {
//		BatchHistDO batch = new BatchHistDO();
//		assertNull(batch.getStatus());
//		batch.setChangeStamp(new ChangeStamp(userId));
//		batch.setStatus("status");
//		assertEquals("Test 1: not same batch status", "status", batch.getStatus());
//
//	}
//
//	/**
//	 * Testing setVersion().
//	 * 
//	 * @see BatchHistDO#setVersion(long)
//	 */
//	@Test
//	public void testSetVersion() {
//		BatchHistDO batch = new BatchHistDO();
//		assertEquals(batch.getVersion(), 0L);
//		batch.setChangeStamp(new ChangeStamp(userId));
//		batch.setVersion(1);
//		assertEquals("not same version", 1L, batch.getVersion());
//	}
//
//	/**
//	 * Testing setChangeStamp().
//	 * 
//	 * @see BatchHistDO#setChangeStamp(ChangeStamp)
//	 */
//	@Test
//	public void testSetChangeStamp() {
//		ChangeStamp stamp = new ChangeStamp(userId);
//		BatchHistDO batch = new BatchHistDO();
//		assertNull(batch.getChangeStamp());
//		batch.setChangeStamp(stamp);
//		assertEquals("not same changeStamp", userId, batch.getChangeStamp().getCreatedBy());
//	}
//
//	/**
//	 * Testing toString().
//	 * 
//	 * @see BatchHistDO#toString()
//	 */
//	@Test
//	public void testToString() {
//		BatchHistDO batch = new BatchHistDO();
//		batch.setChangeStamp(new ChangeStamp(userId));
//		batch.setBatchname("batch");
//		assertNotNull(batch.toString());
//	}
//
//}
