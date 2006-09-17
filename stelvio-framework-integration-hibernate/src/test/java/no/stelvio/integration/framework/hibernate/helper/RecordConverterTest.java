package no.stelvio.integration.framework.hibernate.helper;

import java.io.InputStream;
import java.util.HashSet;

import junit.framework.TestCase;

import no.stelvio.integration.framework.hibernate.cfg.Configuration;
import no.stelvio.integration.framework.hibernate.records.Barn;
import no.stelvio.integration.framework.hibernate.records.Foreldre;

/**
 * JUnit test class for RecordConverter.
 * 
 * @author Thoams Kvalvåg (Accenture)
 */
public class RecordConverterTest extends TestCase {

	private Configuration config = new Configuration();

	private static final ClassLoader resourceLoader = Thread.currentThread().getContextClassLoader();

	/**
	 * Prepares the JUnit test
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		InputStream stream = resourceLoader.getResourceAsStream("bidragsberegning_junit.hbm.xml");
		config.addInputStream(stream);

	}

	/**
	 * Constructor for RecordConverterTest.
	 * 
	 * @param arg0
	 */
	public RecordConverterTest(String arg0) {
		super(arg0);
	}

	/**
	 * Tests recordToClass
	 * 
	 */
	public void testRecordToClass() {

	}

	/**
	 * Tessts classToRecord
	 * 
	 */
	public void testClassToRecord() {
		Barn barn = new Barn();
		barn.setPersonId("12345678901");
		barn.setObjectNr("03");

		Foreldre parent = new Foreldre();
		parent.setAksjonskode("HENT");
		parent.setSakNr("0200790");
		parent.setSoknadsType("BB");
		parent.setUgKode("S2");
		parent.setDatoSoknad("20021024");
		parent.setDatoSokFOM("20031001");
		parent.setDatoBerTOM("20040514");

		parent.setBrukerId("THOMAS");

		parent.setObjectnrBP("01");
		parent.setPersonIdBP("12345678901");
		parent.setSoktGebyrFriBP("J");

		parent.setObjectnrBM("02");
		parent.setPersonIdBM("12345678901");
		parent.setSoktGebyrFriBM("J");

		HashSet barna = new HashSet();
		barna.add(barn);
		parent.setBarn(barna);
		parent.setNumRecords(barna.size());
	}

}
