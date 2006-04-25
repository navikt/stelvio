package no.trygdeetaten.integration.framework.jca.cics.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ibm.connector2.cics.ECIInteractionSpec;
import javax.resource.cci.InteractionSpec;
import javax.resource.cci.Record;
import net.sf.hibernate.mapping.RootClass;
import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;

import no.trygdeetaten.common.framework.service.ServiceFailedException;
import no.trygdeetaten.integration.framework.hibernate.cfg.Configuration;
import no.trygdeetaten.integration.framework.hibernate.helper.RecordConverter;
import no.trygdeetaten.integration.framework.hibernate.records.Foreldre;
import no.trygdeetaten.integration.framework.jca.cics.records.CICSGenericRecord;

/**
 * Testclass for CicsStaticRecordMapperTest.
 * 
 * @author person5b7fd84b3197, Accenture
 */
public class CicsStaticRecordMapperTest extends MockObjectTestCase {
	private CicsStaticRecordMapper mapper;
	private Mock mockRecordConverter;
	private Mock mockConfiguration;

	/**
	 * Test class to record generation.
	 */
	public void testClassToRecord() throws ServiceFailedException {
		Foreldre parent = new Foreldre();

		Record rec = mapper.classToRecord("K460C700", createInterationProps(),
		                                  parent);
		assertTrue("Instanse of: ", rec instanceof CICSGenericRecord);
	}

	/**
	 * Test record to class convertion.
	 */
	public void testRecordToClass() throws ServiceFailedException {
		CICSGenericRecord rec = new CICSGenericRecord();
		char arr[] = new char[2000];
		String str = new String(arr);
		rec.setText(str);
		Object parent = mapper.recordToClass("K460C700",
		                                     createInterationProps(), rec);

		assertTrue("InteractionSpec correct: ", parent instanceof Foreldre);
	}


	/**
	 * Test Interaction creation.
	 */
	public void testCreateInteraction() {
		InteractionSpec spek = mapper.createInteraction(createInterationProps());

		assertTrue("InteractionSpec correct: ",
		           spek instanceof ECIInteractionSpec);
	}

	private InteractionProperties createInterationProps() {
		InteractionProperties interProp = new InteractionProperties();
		interProp.setCommareaLength(2000);
		interProp.setExecuteTimeout(10);
		interProp.setFunctionName("K460C700");
		List l = new ArrayList();
		l.add("bidragsberegning_K460M700.hbm.xml");
		interProp.setMappingFiles(l);
		interProp.setReplyLength(2000);

		return interProp;
	}

	protected void setUp() throws Exception {
		mockRecordConverter = mock(RecordConverter.class);
		mockRecordConverter.stubs().method("classToRecord");
		mockRecordConverter.stubs().method("recordToClass").will(returnValue(new Foreldre()));

		mockConfiguration = mock(Configuration.class);
		mockConfiguration.stubs().method("addInputStream");
		mockConfiguration.stubs().method("getClassMappings").will(returnValue(new Iterator() {
			public boolean hasNext() {
				return false;
			}

			public Object next() {
				return new RootClass();
			}

			public void remove() {
				// Not used
			}
		}));

		mapper = new CicsStaticRecordMapper() {
			Configuration newConfig() {
				return (Configuration) mockConfiguration.proxy();
			}
		};
		mapper.setRecordConverter((RecordConverter) mockRecordConverter.proxy());
	}
}