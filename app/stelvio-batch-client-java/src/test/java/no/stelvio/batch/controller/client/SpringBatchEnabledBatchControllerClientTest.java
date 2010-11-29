package no.stelvio.batch.controller.client;


import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import no.stelvio.batch.BatchStatus;
import no.stelvio.batch.controller.SpringBatchEnabledBatchControllerServiceBi;
import no.stelvio.batch.controller.client.BatchControllerClient.SpringBatchLauncher;
import no.stelvio.batch.controller.client.support.SpringBatchEnabledBatchRunner;


import org.apache.commons.lang.ArrayUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for spring batch.
 * 
 * startBatch.sh - no.stelvio.batch.controller.client.BatchControllerClient $*
 * stopBatch.sh -  no.stelvio.batch.controller.client.BatchControllerClient $1 $2 stop
 * @author person47c121e3ccb5, BEKK
 *
 */
public class SpringBatchEnabledBatchControllerClientTest {
	private SpringBatchLauncher launcher;
	private SpringBatchEnabledBatchControllerServiceBi controllerServiceMock;
	private SpringBatchEnabledBatchRunner batchRunner;
	private String[] userNameAndPassword = {"-username <username>", "-password <password>"};
	private String[] stelvioInput = {"BPOPP004", "1"};
	private String jobName = "BPOPP014";
	private String parameters = "runDate=14.05.2009,stopOnStep=step1";	
	private String[] springBatchInput = {jobName, parameters};
	private String stop = "stop";

	
	/**
	 * Set up spring batch launcher and runner.
	 */
	@Before
	public void setUp() {
		launcher = new SpringBatchLauncher();
		batchRunner = new SpringBatchEnabledBatchRunner();
		controllerServiceMock = createMock(SpringBatchEnabledBatchControllerServiceBi.class);
		batchRunner.setControllerService(controllerServiceMock);
	}
	
	/**
	 * Test if launcher should recognize illegal input.
	 */
	@Test
	public void shouldRecognizeIllegalInput() {
		assertFalse(launcher.isSpringBatch(new String[] {"x"}));
	}
	
	/**
	 * Test if launcher should recognize stelvio batch execution input.
	 */
	@Test
	public void shouldRecognizeStelvioBatchExecutionInput() {
		assertFalse(launcher.isSpringBatch(stelvioInput));
	}
	
	/**
	 * Test if launcher should recognize stelvio batch execution input with security.
	 */
	@Test
	public void shouldRecognizeStelvioBatchExecutionInputWithSecurity() {
		assertFalse(launcher.isSpringBatch((String[]) ArrayUtils.addAll(stelvioInput, userNameAndPassword)));
	}	
	
	/**
	 * Test if launcher should recognize stelvio batch stop input.
	 */
	@Test
	public void shouldRecognizeStelvioBatchStopInput() {
		assertFalse(launcher.isSpringBatch((String[]) ArrayUtils.add(stelvioInput, stop)));
	}	
	
	/**
	 * Test if launcher should recognize stelvio batch stop input with security.
	 */
	@Test
	public void shouldRecognizeStelvioBatchStopInputWithSecurity() {
		assertFalse(launcher.isSpringBatch((String[]) ArrayUtils.addAll(
				ArrayUtils.add(stelvioInput, stop), userNameAndPassword)));
	}	
	
	/**
	 * Test if launcher should recognize spring batch execution input.
	 */
	@Test
	public void shouldRecognizeSpringBatchExecutionInput() {
		assertTrue(launcher.isSpringBatch(springBatchInput));
	}
	
	/**
	 * Test if launcher should recognize spring batch execution input with security.
	 */
	@Test
	public void shouldRecognizeSpringBatchExecutionInputWithSecurity() {
		assertTrue(launcher.isSpringBatch((String[]) ArrayUtils.addAll(
				springBatchInput, userNameAndPassword)));
	}	
	
	/**
	 * Test if launcher should recognize spring batch stop input.
	 */
	@Test
	public void shouldRecognizeSpringBatchStopInput() {
		assertTrue(launcher.isSpringBatch(new String[] {jobName, stop}));
	}	
	
	/**
	 * Test if launcher should recognize spring batch stop input with security.
	 */
	@Test
	public void shouldRecognizeSpringBatchStopInputWithSecurity() {
		assertTrue(launcher.isSpringBatch((String[]) ArrayUtils.add(ArrayUtils.add(
				new String[] {jobName}, "-username test"), stop)));
	}		
	
	/**
	 * Test that should call run batch with job name and parameters.
	 */
	@Test
	public void shouldCallRunBatchWithJobNameAndParameters() {
		executeBatch(springBatchInput);
	}
	
	/**
	 * Test that should call run batch with job name and parameters when security is enabled.
	 */
	@Test
	public void shouldCallRunBatchWithJobNameAndParametersWhenSecurityIsEnabled() {
		executeBatch((String[]) ArrayUtils.addAll(springBatchInput, userNameAndPassword));
	}	

	/**
	 * Test that should call stop batch with job name.
	 */
	@Test
	public void shouldCallStopBatchWithJobName() {
		stopBatch(new String[] {jobName, stop});
	}
	
	/**
	 * Test that should call stop batch with job name when security is enabled.
	 */
	@Test
	public void shouldCallStopBatchWithJobNameWhenSecurityIsEnabled() {
		stopBatch((String[]) ArrayUtils.add(ArrayUtils.add(
				new String[] {jobName}, "-username test"), stop));
	}		
	
	/**
	 * Test that should return fatal status if stop call fails.
	 */
	@Test
	public void shouldReturnFatalStatusIfStopCallFails() {
		expect(controllerServiceMock.stopBatch(isA(String.class))).andReturn(false);
		replay(controllerServiceMock);
		int returnStatus = launcher.runBatch(new String[] {jobName, stop}, batchRunner);
		verify(controllerServiceMock);
		assertEquals(BatchStatus.BATCH_FATAL, returnStatus);
	}		

	/**
	 * Execute batch with input parameters.
	 * 
	 * @param inputParameters input
	 */
	private void executeBatch(String[] inputParameters) {
		int batchStatus = BatchStatus.BATCH_OK;
		expect(controllerServiceMock.executeBatch(jobName, parameters)).andReturn(batchStatus);
		replay(controllerServiceMock);
		int returnStatus = launcher.runBatch(inputParameters, batchRunner);
		verify(controllerServiceMock);
		assertEquals(batchStatus, returnStatus);
	}		
	
	/**
	 * Stop batch with input parameters.
	 * 
	 * @param inputParameters input
	 */
	private void stopBatch(String[] inputParameters) {
		expect(controllerServiceMock.stopBatch(jobName)).andReturn(true);
		replay(controllerServiceMock);
		int returnStatus = launcher.runBatch(inputParameters, batchRunner);
		verify(controllerServiceMock);
		assertEquals(BatchStatus.BATCH_OK, returnStatus);
	}		
}
