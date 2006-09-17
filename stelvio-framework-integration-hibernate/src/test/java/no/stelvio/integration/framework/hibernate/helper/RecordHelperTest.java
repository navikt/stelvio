package no.stelvio.integration.framework.hibernate.helper;

import java.io.InputStream;

import junit.framework.TestCase;
import net.sf.hibernate.mapping.Property;
import net.sf.hibernate.mapping.RootClass;

import no.stelvio.integration.framework.hibernate.cfg.Configuration;
import no.stelvio.integration.framework.hibernate.helper.RecordHelper;
import no.stelvio.integration.framework.hibernate.mapping.Column;
import no.stelvio.integration.framework.hibernate.mapping.OneToMany;

/**
 * Junit testclass for RecordHelper.
 * 
 * @author person5b7fd84b3197, Accenture
 */
public class RecordHelperTest extends TestCase {


	private Configuration config = new Configuration();
	private static final ClassLoader resourceLoader  = Thread.currentThread().getContextClassLoader();
	 
	/**
	 * Constructor for RecordHelperTest.
	 * @param arg0
	 */
	public RecordHelperTest(String arg0) {
		super(arg0);
	}
	
	/**
	 * Prepares the JUnit test
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		InputStream stream = resourceLoader.getResourceAsStream("bidragsberegning_junit.hbm.xml");
		config.addInputStream( stream );

	}

	/**
	 * Tests getColumn
	 *
	 */
	public void testGetColumn() {
		RootClass clas = RecordHelper.getServiceRootClass(config, "K460M700Param");
		Column col = RecordHelper.getColumn(clas, "aksjonskode");
		
		assertEquals(col.getLength(), 8);
		
	}

	/**
	 * Tests GetOneToMany
	 *
	 */
	public void testGetOneToMany() {
		String role = "no.stelvio.integration.framework.hibernate.records.Foreldre.barn";
		
		OneToMany otm = RecordHelper.getOneToMany( config, role );

		assertNotNull( otm );
		
	}
	/**
	 * Tests GetChildClass
	 *
	 */	
	public void testGetChildClass() {
		String role = "no.stelvio.integration.framework.hibernate.records.Foreldre.barn";
		
		OneToMany otm = RecordHelper.getOneToMany( config, role );
		
		String clazz = RecordHelper.getChildClass( otm );
		System.out.println(clazz);
		assertEquals("no.stelvio.integration.framework.hibernate.records.Barn", clazz);
	}
	
	/**
	 * Tests GetRoleName
	 *
	 */
	public void testGetRoleName() {
		RootClass clas = RecordHelper.getServiceRootClass(config, "K460M700Param");
		String str = RecordHelper.getRoleName( clas, "barn" );
		
		String solution = "no.stelvio.integration.framework.hibernate.records.Foreldre.barn";
		
		assertEquals(solution,str);	
	}
	
	/**
	 * Tests GetServiceRootClass
	 *
	 */
	public void testGetServiceRootClass() {
		RootClass clazz = RecordHelper.getServiceRootClass(config, "K460M700Param");
		assertNotNull( clazz );
	}
	
	/**
	 * Tests GetRootClass
	 *
	 */
	public void testGetRootClass() {
		RootClass correct = RecordHelper.getServiceRootClass(config, "K460M700Param");
		RootClass clazz = RecordHelper.getRootClass( config, "no.stelvio.integration.framework.hibernate.records.Foreldre" );
		
		assertEquals(correct, clazz);
		
	}
	
	/**
	 * Tests GetProperty
	 *
	 */
	public void testGetProperty() {
		RootClass correct = RecordHelper.getServiceRootClass(config, "K460M700Param");
		Property prop = RecordHelper.getProperty(correct,"M700-ANT-BARN-ELEMENTER");
		
		assertEquals( prop.getName() ,"numRecords");
	}
	
	/**
	 * Tests GetRecordLength
	 *
	 */
	public void testGetRecordLength() {
		int length = RecordHelper.getRecordLength( config, "K460M700Param"  );
		assertEquals(length, 84);
	}



}
