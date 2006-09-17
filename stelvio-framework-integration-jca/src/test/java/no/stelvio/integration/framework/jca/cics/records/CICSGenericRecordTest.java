/*
 * Created on 18.aug.04
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package no.stelvio.integration.framework.jca.cics.records;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import no.stelvio.integration.framework.jca.cics.records.CICSGenericRecord;

import junit.framework.TestCase;

/**
 * @author TKC2920
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CICSGenericRecordTest extends TestCase {

	private CICSGenericRecord record1 = new CICSGenericRecord("ibm");
	private CICSGenericRecord record = new CICSGenericRecord();

	/**
	 * Constructor for CICSGenericRecordTest.
	 * @param arg0
	 */
	public CICSGenericRecordTest(String arg0) {
		super(arg0);
	}

	public void testHashCode() {
		record.hashCode();
		
		record.setText(null);
		
		record.hashCode();
		
	}


	public void testGetRecordName() {
		record.setRecordName("test");
		assertEquals("Test GetRecordName","test",record.getRecordName());		
	}

	public void testGetRecordShortDescription() {
		record.setRecordShortDescription("test");
		assertEquals("Test GetRecordShortDescription","test",record.getRecordShortDescription());	
	}

	/*
	 * Test for boolean equals(Object)
	 */
	public void testEqualsObject() {
		assertEquals("Test1 EqualsObject",true,record.equals(record));
		
		assertEquals("Test2 EqualsObject",false,record.equals(new Object()));
		record.setText("test");
		record1.setText("test");
		assertEquals("Test2 EqualsObject",true,record.equals(record1));
	}

	/*
	 * Test for Object clone()
	 */
	public void testClone() {
		boolean test = true;
		try {
			record.clone();
		} catch(Exception e) {
			test = false;
		}
		assertEquals("Test Clone",true,test);
	}

	public void testRead() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		byte[] data = {};
		try {
			os.write(data);
		} catch (Exception e) {
		}
		record.write( os );
		byte[] result = {};
		ByteArrayInputStream is = new ByteArrayInputStream(result);
		record.read( is );
		
	}


	public void testGetText() {
		record.setText("text");
		
		assertEquals("Test GetText","text",record.getText());
	}


	public void testGetEncoding() {
		record.setEncoding("ibm111");
		
		assertEquals("Test GetEncoding","ibm111",record.getEncoding());
	}


}
