package no.stelvio.batch.support;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import no.stelvio.batch.BatchStatus;
import no.stelvio.batch.StelvioBatchParameterReader;
import no.stelvio.batch.controller.support.DefaultBatchControllerService;
import no.stelvio.batch.domain.BatchHistDO;
import no.stelvio.batch.exception.InvalidBatchEntryException;
import no.stelvio.batch.repository.BatchHistRepository;
import no.stelvio.batch.repository.support.HibernateBatchHistRepository;
import no.stelvio.batch.repository.support.HibernateBatchRepository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ControllerServiceHistorySupportTest {

	private static final String BATCH_TEST_CONTEXT = "btc-testbatch-context.xml";

	private DefaultBatchControllerService batchControllerService;
	private HibernateBatchHistRepository histRepository;
	private ControllerServiceHistorySupport controllerServiceHistorySupport;
	private StelvioBatchParameterReader reader;
	private HibernateBatchRepository batchRepo;
	
	private ApplicationContext ctx;
	
	/**
	 * 
	 */
	public ControllerServiceHistorySupportTest() {
	}

	public void setBatchControllerService(DefaultBatchControllerService batchControllerService) {
		this.batchControllerService = batchControllerService;
	}

	public void setHistRepository(HibernateBatchHistRepository histRepository) {
		this.histRepository = histRepository;
	}
	
	public void setControllerServiceHistorySupport(ControllerServiceHistorySupport controllerServiceHistorySupport) {
		this.controllerServiceHistorySupport = controllerServiceHistorySupport;
	}
	
	public void setReader(StelvioBatchParameterReader reader) {
		this.reader = reader;
	}

	public void setBatchRepo(HibernateBatchRepository batchRepo) {
		this.batchRepo = batchRepo;
	}

	@Before
	public void prepareForTests() {
		
		ctx = new ClassPathXmlApplicationContext(BATCH_TEST_CONTEXT);
		batchControllerService = (DefaultBatchControllerService) ctx.getBean("DefaultBatchControllerService");
		histRepository = (HibernateBatchHistRepository) ctx.getBean("batchHistoryRepository");
		controllerServiceHistorySupport = (ControllerServiceHistorySupport) ctx.getBean("ControllerServiceHistorySupport");
		reader = (StelvioBatchParameterReader) ctx.getBean("stelvioBatchParameterReader");
		batchRepo = (HibernateBatchRepository) ctx.getBean("batchRepository");

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
	
	@Test
	public void shouldSaveBatchHistToDatabaseBatchWithParams() {

		//This "test" need a more complete case
		batchControllerService.executeBatch("btc.testbatch.dummyBatchWithInputParameters", 0);

		BatchHistDO batchHistDO = new BatchHistDO();
		batchHistDO.setBatchname("dummyBatchWithInputParameters");
		batchHistDO.setParameters("timeToRun=1;exitCode=8");

		//TODO This tests the repository, not the implementation class
		long batchID = histRepository.setHist(batchHistDO);
		
		List <BatchHistDO> batchHistory = (List<BatchHistDO>) histRepository.findByNameAndSlice("dummyBatchWithInputParameters", 0);
		assertEquals(batchHistory.get(0).getBatchname(), "dummyBatchWithInputParameters");
		assertEquals(batchHistory.get(0).getParameters(), "timeToRun=1;exitCode=8");
	}

	//TODO rename test
	//TODO Mange av disse testene er veldig ad-hoc og bærer preg av å ville teste database og ikke bare metodesignaturer
	@Test
	public void endToEndTest(){
		String batchName = "btc.testbatch.dummyBatch";
		Date testDay = new Date();
		Date fromDate = new Date();
		//fromDate.setTime(fromDate.getTime()-1500000);
		
		//Providing the controller service with correct setup
		batchControllerService = (DefaultBatchControllerService) ctx.getBean("DefaultBatchControllerService");

		batchControllerService.executeBatch(batchName, 0);

		Collection <BatchHistDO> batchHistory = fetchBatchHistory(batchName, fromDate, new Date());
		Iterator<BatchHistDO> iterator = batchHistory.iterator();
		Assert.assertTrue(iterator.hasNext());
		BatchHistDO batchHistDO = iterator.next();
		Assert.assertEquals(BatchStatus.BATCH_OK, Integer.parseInt(batchHistDO.getStatus()));
		Assert.assertFalse(iterator.hasNext());

	}
	
	@Test
	public void endToEndTestBatchWithParams(){
		String batchName = "btc.testbatch.dummyBatchWithInputParameters";
		Date testDay = new Date();
		Date fromDate = new Date();
		//fromDate.setTime(fromDate.getTime()-1500000);
		
		//Providing the controller service with correct setup
		batchControllerService = (DefaultBatchControllerService) ctx.getBean("DefaultBatchControllerService");
		
		batchControllerService.executeBatch(batchName, 0);
		
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
		fetchBatchHistory("dummyBatch", 1);	
		
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
		
		System.out.println("Runtime: " + interval + " ms");

	}
	
	@Test 
	public void shouldSaveAdditionalDataToExistingBatchHistoryEntryBatchWithParams(){
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
		fetchBatchHistory("dummyBatch", 1);	
		
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
