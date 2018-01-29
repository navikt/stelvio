package no.stelvio.batch.controller.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import no.stelvio.batch.BatchStatus;
import no.stelvio.batch.controller.SpringBatchEnabledBatchControllerServiceBi;
import no.stelvio.batch.controller.client.BatchControllerClient.SpringBatchLauncher;
import no.stelvio.batch.controller.client.support.SpringBatchEnabledBatchRunner;

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
	private SpringBatchEnabledBatchRunner batchRunner;
	private String[] userNameAndPassword = {"-username <username>", "-password <password>"};
	private String[] stelvioInput = {"BPOPP004", "1"};
	private String jobName = "BPOPP014";
	private String parameters = "runDate=14.05.2009,stopOnStep=step1";	
	private String[] springBatchInput = {jobName, parameters};
	private String stop = "stop";
	@Mock
	private SpringBatchEnabledBatchControllerServiceBi controllerServiceMock;

	
	/**
	 * Set up spring batch launcher and runner.
	 */
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		launcher = new SpringBatchLauncher();
		batchRunner = new SpringBatchEnabledBatchRunner();
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
		assertFalse(launcher.isSpringBatch(ArrayUtils.addAll(stelvioInput, userNameAndPassword)));
	}	
	
	/**
	 * Test if launcher should recognize stelvio batch stop input.
	 */
	@Test
	public void shouldRecognizeStelvioBatchStopInput() {
		assertFalse(launcher.isSpringBatch(ArrayUtils.add(stelvioInput, stop)));
	}	
	
	/**
	 * Test if launcher should recognize stelvio batch stop input with security.
	 */
	@Test
	public void shouldRecognizeStelvioBatchStopInputWithSecurity() {
		assertFalse(launcher.isSpringBatch(ArrayUtils.addAll(
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
		assertTrue(launcher.isSpringBatch(ArrayUtils.addAll(
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
		assertTrue(launcher.isSpringBatch(ArrayUtils.add(ArrayUtils.add(
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
		executeBatch(ArrayUtils.addAll(springBatchInput, userNameAndPassword));
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
		stopBatch(ArrayUtils.add(ArrayUtils.add(
				new String[] {jobName}, "-username test"), stop));
	}		
	
	/**
	 * Test that should return fatal status if stop call fails.
	 */
	@Test
	public void shouldReturnFatalStatusIfStopCallFails() {
		when(controllerServiceMock.stopBatch(isA(String.class))).thenReturn(false);

		int returnStatus = launcher.runBatch(new String[] {jobName, stop}, batchRunner);
		verify(controllerServiceMock).stopBatch(isA(String.class));
		assertEquals(BatchStatus.BATCH_FATAL, returnStatus);
	}		

	/**
	 * Execute batch with input parameters.
	 * 
	 * @param inputParameters input
	 */
	private void executeBatch(String[] inputParameters) {
		int batchStatus = BatchStatus.BATCH_OK;
		when(controllerServiceMock.executeBatch(inputParameters[0], inputParameters[1])).thenReturn(batchStatus);
		int returnStatus = launcher.runBatch(inputParameters, batchRunner);
		verify(controllerServiceMock).executeBatch(inputParameters[0], inputParameters[1]);
		assertEquals(batchStatus, returnStatus);
	}		
	
	/**
	 * Stop batch with input parameters.
	 * 
	 * @param inputParameters input
	 */
	private void stopBatch(String[] inputParameters) {
		when(controllerServiceMock.stopBatch(inputParameters[0])).thenReturn(true);

		int returnStatus = launcher.runBatch(inputParameters, batchRunner);
		verify(controllerServiceMock).stopBatch(inputParameters[0]);
		assertEquals(BatchStatus.BATCH_OK, returnStatus);
	}		
}
