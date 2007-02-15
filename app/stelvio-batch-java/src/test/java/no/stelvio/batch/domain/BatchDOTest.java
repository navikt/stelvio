package no.stelvio.batch.domain;

import junit.framework.TestCase;

/**
 * 
 * @author person356941106810, Accenture
 */
public class BatchDOTest extends TestCase {

	/**
	 * Constructor for BatchTest.
	 * @param arg0
	 */
	public BatchDOTest(String arg0) {
		super(arg0);
	}

	public void testHashCode() {
		try {
			BatchDO b1 = new BatchDO();
			b1.setBatchname("Batch");
			assertTrue("Test 1: not equals", b1.equals(b1));
			assertTrue("Test 2: not equals", b1.hashCode() == b1.hashCode());

			BatchDO b2 = new BatchDO();
			b1.setBatchname("Batch");
			assertFalse("Test 3: not equals", b1.equals(b2));

		} catch (Throwable t) {
			t.printStackTrace(System.err);
			fail("Unexpected exception:" + t.getMessage());
		}
	}

	/*
	 * Test for void Batch()
	 */
	public void testBatch() {
		try {
			BatchDO b = new BatchDO();
			assertNull(b.getBatchname());
			assertNull(b.getParameters());
			assertNull(b.getStatus());
		} catch (Throwable t) {
			t.printStackTrace(System.err);
			fail("Unexpected exception:" + t.getMessage());
		}
	}

	public void testSetBatchname() {
		try {
			BatchDO b = new BatchDO();
			assertNull(b.getBatchname());
			b.setBatchname("name");
			assertEquals("Test 1: not same batch name", "name", b.getBatchname());
		} catch (Throwable t) {
			t.printStackTrace(System.err);
			fail("Unexpected exception:" + t.getMessage());
		}
	}

	public void testSetParameters() {
		try {
			BatchDO b = new BatchDO();
			assertNull(b.getParameters());
			b.setParameters("param");
			assertEquals("Test 1: not same batch param", "param", b.getParameters());
		} catch (Throwable t) {
			t.printStackTrace(System.err);
			fail("Unexpected exception:" + t.getMessage());
		}
	}

	public void testSetStatus() {
		try {
			BatchDO b = new BatchDO();
			assertNull(b.getStatus());
			b.setStatus("status");
			assertEquals("Test 1: not same batch status", "status", b.getStatus());
		} catch (Throwable t) {
			t.printStackTrace(System.err);
			fail("Unexpected exception:" + t.getMessage());
		}
	}

	/*
	 * Test for String toString()
	 */
	public void testToString() {
		try {
			BatchDO batch = new BatchDO();
			batch.setBatchname("batch");
			assertNotNull(batch.toString());
		} catch (Throwable t) {
			t.printStackTrace(System.err);
			fail("Unexpected exception:" + t.getMessage());
		}
	}

}
