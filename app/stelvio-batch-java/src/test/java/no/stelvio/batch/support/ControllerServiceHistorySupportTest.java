package no.stelvio.batch.support;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import no.stelvio.batch.controller.support.DefaultBatchControllerService;
import no.stelvio.batch.domain.BatchHistDO;
import no.stelvio.batch.exception.InvalidBatchEntryException;
import no.stelvio.batch.repository.BatchHistRepository;
import no.stelvio.batch.repository.support.HibernateBatchHistRepository;
import org.mockito.Mockito;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.HibernateTemplate;

import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;

public class ControllerServiceHistorySupportTest {

	private static final String BATCH_TEST_CONTEXT = "btc-testbatch-context.xml";

	DefaultBatchControllerService batchControllerService;
	HibernateTemplate ht;
	HibernateBatchHistRepository histRepository;
	ControllerServiceHistorySupport controllerServiceHistorySupport;
	
	private SessionFactory omgLol;

	/**
	 * 
	 */
	public ControllerServiceHistorySupportTest() {
	}

	public SessionFactory getOmgLol() {
		return omgLol;
	}

	public void setOmgLol(SessionFactory omgLol) {
		this.omgLol = omgLol;
	}

	public HibernateBatchHistRepository getHistRepository() {
		return histRepository;
	}

	public void setHistRepository(HibernateBatchHistRepository histRepository) {
		this.histRepository = histRepository;
	}

	@Before
	public void prepareForTests() {
		
	
		batchControllerService = new DefaultBatchControllerService();
		batchControllerService.setBatchNameMap(new HashMap());
		batchControllerService.getBatchNameMap().put("btc.testbatch.dummyBatch", "btc.testbatch.dummyBatch");

		ClassPathXmlApplicationContext springContext = new ClassPathXmlApplicationContext(BATCH_TEST_CONTEXT);
		omgLol = (SessionFactory) springContext.getBean("btc.testbatch.sessionFactory");
		
		ht = new HibernateTemplate(omgLol);
		batchControllerService.setApplicationContext(springContext);
		
		histRepository = new HibernateBatchHistRepository();

		histRepository.setHibernateTemplate(ht);
		
		controllerServiceHistorySupport = new ControllerServiceHistorySupport();
		controllerServiceHistorySupport.setBatchHistoryRepository(histRepository);
		
		batchControllerService.setControllerServiceHistorySupport(controllerServiceHistorySupport);

	}

	@Test
	public void shouldPersistBatchHistoryBeforeStartWithMock() {

		batchControllerService.executeBatch("btc.testbatch.dummyBatch", 0);
		BatchHistRepository mockRepository = Mockito.mock(BatchHistRepository.class);

		BatchHistDO batchHistDO = new BatchHistDO();
		batchHistDO.setBatchname("dummyBatch");
		
		// Setup canned answer - before execution
		Mockito.when(mockRepository.findLastRunByNameAndSlice("dummyBatch", 0)).thenReturn(batchHistDO);

		BatchHistDO batchHistory = mockRepository.findLastRunByNameAndSlice("dummyBatch", 0);
		assertEquals(batchHistory.getBatchname(), "dummyBatch");
	}

	@Test
	public void shouldSaveBatchHistToDatabase() {

		//This "test" need a more complete case
		batchControllerService.executeBatch("btc.testbatch.dummyBatch", 0);

		BatchHistDO batchHistDO = new BatchHistDO();
		batchHistDO.setBatchname("dummyBatch");

		//TODO This tests the repository, not the implementation class
		long batchID = histRepository.setHist(batchHistDO);
		System.out.println("Generated ID: " + batchID);
		
		List <BatchHistDO> batchHistory = (List<BatchHistDO>) histRepository.findByNameAndSlice("dummyBatch", 0);
		assertEquals(batchHistory.get(0).getBatchname(), "dummyBatch");
	}

	//TODO rename
	@Test
	public void endToEndTest(){
		String batchName = "btc.testbatch.dummyBatch";
		Date testDay = new Date();
		Date fromDate = new Date();
		fromDate.setTime(fromDate.getTime()-1500000);
		//Providing the controller service with correct setup		ControllerServiceHistorySupport controller = new ControllerServiceHistorySupport();
		controller.setBatchHistoryRepository(histRepository);
		batchControllerService.setControllerServiceHistorySupport(controller);	
		//TODO Hvordan skal jeg "initialisere" den klassen

		batchControllerService.executeBatch(batchName, 0);
		
		ControllerServiceHistorySupport testController = batchControllerService.getControllerServiceHistorySupport();

		Collection <BatchHistDO> batchHistory = testController.fetchBatchHistory(batchName, fromDate, new Date());
		Collection <BatchHistDO> batchHistoryDay = testController.fetchBatchHistory(batchName, testDay);
		BatchHistDO history = (BatchHistDO)((List) batchHistory).get(0);
	}
	
	@Test
	public void shouldSaveInitialBatchInformation(){
			
		/** Start med å kjøre dette grønt før jeg utvider test **/
		
		controllerServiceHistorySupport.saveInitialBatchInformation("dummyBatch", 1);
	    //initialSave må returnere ID for videre lagring?	
	}
	
	@Test(expected = InvalidBatchEntryException.class)
	public void shouldThrowExceptionIfNonexistingBatch(){

		histRepository.findByNameAndSlice("non-existing-batch", 1);
		
		insertDummyBatches(controllerServiceHistorySupport);
		controllerServiceHistorySupport.fetchBatchHistory("dummyBatch", 1);	
		
	}
	
	@Test 
	public void shouldSaveAdditionalDataToExistingBatchHistoryEntry(){
		int finishedCode = 1;


		long batchNr = controllerServiceHistorySupport.saveInitialBatchInformation("dummyBatch", 1);
		BatchHistDO historyDO = controllerServiceHistorySupport.fetchBatchHistory(batchNr);
		assertEquals(historyDO.getBatchname(), "dummyBatch");
		
		controllerServiceHistorySupport.saveAdditionalBatchInformation(batchNr, finishedCode);
				
		BatchHistDO retrievedHistory = controllerServiceHistorySupport.fetchBatchHistory(batchNr);
		long interval = retrievedHistory.getEndtime().getTime() - retrievedHistory.getStartTime().getTime();
		final long MINIMUM_WAIT = 200;
		
		//TODO Tenkte å ha en wait, men fjernet den. Gir lite mening nå
		assertTrue(interval >= 0);
		
		System.out.println("Runtime: " + interval + " ms");
		
	//initialSave må returnere ID for videre lagring?	
		
	}
	
	//TODO Remove expected exception	
	@Test//(expected = InvalidBatchEntryException.class)
	public void shouldSaveSeveralBatchesAndFetchCorrectly(){

		insertDummyBatches(controllerServiceHistorySupport);
		controllerServiceHistorySupport.fetchBatchHistory("dummyBatch", 1);	
		
	}
	
	public void insertDummyBatches(ControllerServiceHistorySupport controller){

		int finishCode = 6; 
		
		controller.saveAdditionalBatchInformation(
				controller.saveInitialBatchInformation("dummyBatch", 1), finishCode);
		controller.saveAdditionalBatchInformation(
				controller.saveInitialBatchInformation("jummyBatch", 1), finishCode);
		controller.saveAdditionalBatchInformation(
				controller.saveInitialBatchInformation("slummyBatch", 1), finishCode);
		controller.saveAdditionalBatchInformation(
				controller.saveInitialBatchInformation("rummyBatch", 1), finishCode);
		controller.saveAdditionalBatchInformation(
				controller.saveInitialBatchInformation("dummyBatch", 1), finishCode);
		controller.saveAdditionalBatchInformation(
				controller.saveInitialBatchInformation("dummyBatch", 1), finishCode);
		controller.saveAdditionalBatchInformation(
				controller.saveInitialBatchInformation("dummyBatch", 2), finishCode);
		controller.saveAdditionalBatchInformation(
				controller.saveInitialBatchInformation("dummyBatch", 3), finishCode);
	}
	
}
