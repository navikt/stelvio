package no.stelvio.batch.support;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import no.stelvio.batch.BatchStatus;
import no.stelvio.batch.DummyBatchWithInputParameters;
import no.stelvio.batch.controller.support.DefaultBatchControllerService;
import no.stelvio.batch.domain.BatchHistDO;
import no.stelvio.batch.exception.InvalidBatchEntryException;
import no.stelvio.batch.repository.BatchHistRepository;
import no.stelvio.batch.repository.support.HibernateBatchHistRepository;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.HibernateTemplate;

public class ControllerServiceHistorySupportBatchWithParametersTest {

	private static final String BATCH_TEST_CONTEXT = "btc-testbatch-with-params-context.xml";

	DefaultBatchControllerService batchControllerService;
	HibernateTemplate ht;
	HibernateBatchHistRepository histRepository;
	ControllerServiceHistorySupport controllerServiceHistorySupport;
	
	private SessionFactory sessionFactory;

	/**
	 * 
	 */
	public ControllerServiceHistorySupportBatchWithParametersTest() {
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
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
		batchControllerService.getBatchNameMap().put("btc.testbatch.dummyBatchWithInputParameters", "btc.testbatch.dummyBatchWithInputParameters");

		ClassPathXmlApplicationContext springContext = new ClassPathXmlApplicationContext(BATCH_TEST_CONTEXT);
		sessionFactory = (SessionFactory) springContext.getBean("btc.testbatch.sessionFactory");
		
		ht = new HibernateTemplate(sessionFactory);
		batchControllerService.setApplicationContext(springContext);
		
		histRepository = new HibernateBatchHistRepository();

		histRepository.setHibernateTemplate(ht);
		
		controllerServiceHistorySupport = new ControllerServiceHistorySupport();
		controllerServiceHistorySupport.setBatchHistoryRepository(histRepository);
		
		batchControllerService.setControllerServiceHistorySupport(controllerServiceHistorySupport);

	}

	@Test
	public void shouldPersistBatchHistoryBeforeStartWithMock() {

		batchControllerService.executeBatch("btc.testbatch.dummyBatchWithInputParameters", 0);
		BatchHistRepository mockRepository = Mockito.mock(BatchHistRepository.class);

		BatchHistDO batchHistDO = new BatchHistDO();
		batchHistDO.setBatchname("dummyBatchWithInputParameters");
		
		// Setup canned answer - before execution
		Mockito.when(mockRepository.findLastRunByNameAndSlice("dummyBatchWithInputParameters", 0)).thenReturn(batchHistDO);

		BatchHistDO batchHistory = mockRepository.findLastRunByNameAndSlice("dummyBatchWithInputParameters", 0);
		assertEquals(batchHistory.getBatchname(), "dummyBatchWithInputParameters");
	}

	@Test
	public void shouldSaveBatchHistToDatabase() {

		//This "test" need a more complete case
		batchControllerService.executeBatch("btc.testbatch.dummyBatchWithInputParameters", 0);

		BatchHistDO batchHistDO = new BatchHistDO();
		batchHistDO.setBatchname("dummyBatchWithInputParameters");

		//TODO This tests the repository, not the implementation class
		long batchID = histRepository.setHist(batchHistDO);
		
		List <BatchHistDO> batchHistory = (List<BatchHistDO>) histRepository.findByNameAndSlice("dummyBatchWithInputParameters", 0);
		assertEquals(batchHistory.get(0).getBatchname(), "dummyBatchWithInputParameters");
	}

	//TODO rename test
	//TODO Mange av disse testene er veldig ad-hoc og bærer preg av å ville teste database og ikke bare metodesignaturer
	@Test
	public void endToEndTest(){

		String batchName = "btc.testbatch.dummyBatchWithInputParameters";
		Date testDay = new Date();
		Date fromDate = new Date();
		
		//Providing the controller service with correct setup
		ControllerServiceHistorySupport controller = new ControllerServiceHistorySupport();
		controller.setBatchHistoryRepository(histRepository);
		batchControllerService.setControllerServiceHistorySupport(controller);	


		batchControllerService.executeBatch(batchName, 0);
		
		ControllerServiceHistorySupport testController = batchControllerService.getControllerServiceHistorySupport();

		Collection <BatchHistDO> batchHistory = fetchBatchHistory(batchName, fromDate, new Date());
		Iterator<BatchHistDO> iterator = batchHistory.iterator();
		Assert.assertTrue(iterator.hasNext());
		BatchHistDO batchHistDO = iterator.next();
		Assert.assertEquals(BatchStatus.BATCH_ERROR, Integer.parseInt(batchHistDO.getStatus()));
		Assert.assertFalse(iterator.hasNext());
	}
		
	@Test(expected = InvalidBatchEntryException.class)
	public void shouldThrowExceptionIfNonexistingBatch(){

		histRepository.findByNameAndSlice("non-existing-batch", 1);
		
		insertDummyBatches(controllerServiceHistorySupport);
		fetchBatchHistory("dummyBatchWithInputParameters", 1);	
		
	}
	
	@Test 
	public void shouldSaveAdditionalDataToExistingBatchHistoryEntry(){
		int finishedCode = 1;
		
		long batchNr = controllerServiceHistorySupport.saveInitialBatchInformation("dummyBatchWithInputParameters", 1);
		BatchHistDO historyDO = controllerServiceHistorySupport.fetchBatchHistory(batchNr);
		assertEquals(historyDO.getBatchname(), "dummyBatchWithInputParameters");
		
		controllerServiceHistorySupport.saveAdditionalBatchInformation(batchNr, finishedCode);
				
		BatchHistDO retrievedHistory = controllerServiceHistorySupport.fetchBatchHistory(batchNr);
		long interval = retrievedHistory.getEndtime().getTime() - retrievedHistory.getStartTime().getTime();

	}
	
	@Test
	public void shouldSaveSeveralBatchesAndFetchCorrectly(){

		insertDummyBatches(controllerServiceHistorySupport);
		fetchBatchHistory("dummyBatchWithInputParameters", 1);	
		
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
	
	public Collection <BatchHistDO> fetchBatchHistory(String batchName, int slice) {

		return histRepository.findByNameAndSlice(batchName, slice);
	}


	public Collection<BatchHistDO> fetchBatchHistory(String batchName,
			Date fromDate, Date toDate) {
		return histRepository
				.findByNameAndTimeInterval(batchName, fromDate, toDate);
	}
	
	public Collection <BatchHistDO> fetchBatchHistory(String batchName, Date startDay) {
		return histRepository.findByNameAndDay(batchName, startDay);
	}

}
