package no.stelvio.batch.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;

import no.stelvio.batch.BatchParameterReader;
import no.stelvio.batch.BatchStatus;
import no.stelvio.batch.controller.SpringBatchJobOperator;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.support.PropertiesConverter;

/**
 * @author person47c121e3ccb5, BEKK
 *
 */
public class SpringBatchJobOperatorTest {
	private SpringBatchJobOperator jobOperator;
	private JobOperator springJobOperatorMock;
	private JobExplorer jobExplorerMock;
	private JobRepository jobRepositoryMock;
	private JobParametersConverter jobParametersConverterMock;
	private BatchParameterReader parameterReaderMock;
	private String jobName = "BPOPP014";
	private String parameters= "batchParam1=param1,batchParam2=param2";
	private Long jobExecutionId = 13L;
	private JobExecution jobEx = new JobExecution(jobExecutionId);
	
	@Before
	public void setUp() {
		DefaultSpringBatchJobOperator jobOperatorImpl = new DefaultSpringBatchJobOperator();
		springJobOperatorMock = mock(JobOperator.class);
		jobOperatorImpl.setJobOperator(springJobOperatorMock);
		jobExplorerMock = mock(JobExplorer.class);
		jobOperatorImpl.setJobExplorer(jobExplorerMock);
		jobRepositoryMock = mock(JobRepository.class);
		jobOperatorImpl.setJobRepository(jobRepositoryMock);
		jobParametersConverterMock = mock(JobParametersConverter.class);
		jobOperatorImpl.setJobParametersConverter(jobParametersConverterMock);
		jobOperator = jobOperatorImpl;
	}
	
	@Test
	public void shouldStartBatch() throws Exception {
		when(springJobOperatorMock.start(jobName, parameters)).thenReturn(jobExecutionId);
		jobEx.setExitStatus(ExitStatus.COMPLETED);
		when(jobExplorerMock.getJobExecution(jobExecutionId)).thenReturn(jobEx);
		assertEquals(BatchStatus.BATCH_OK, jobOperator.executeBatch(jobName, parameters));
		
	}
	
	@Test
	public void shouldRestartBatch() throws Exception {
		when(springJobOperatorMock.start(jobName, parameters)).thenThrow(new JobInstanceAlreadyExistsException(jobName));
		JobParameters params = new JobParameters();
		when(jobParametersConverterMock.getJobParameters(PropertiesConverter
				.stringToProperties(parameters))).thenReturn(params);
		when(jobRepositoryMock.getLastJobExecution(jobName, params)).thenReturn(jobEx);
		Long restartExecutionId = 21L;
		when(springJobOperatorMock.restart(jobEx.getId())).thenReturn(restartExecutionId);
		JobExecution restartJobEx = new JobExecution(restartExecutionId);
		when(jobExplorerMock.getJobExecution(restartExecutionId)).thenReturn(restartJobEx);
		restartJobEx.setExitStatus(ExitStatus.COMPLETED);

		assertEquals(BatchStatus.BATCH_OK, jobOperator.executeBatch(jobName, parameters));		
	}	

	@Test
	public void shouldStopBatch() throws Exception {
		when(springJobOperatorMock.getRunningExecutions(jobName)).thenReturn(new HashSet<Long>(Arrays.asList(jobExecutionId)));
		when(springJobOperatorMock.stop(jobExecutionId)).thenReturn(true);
		assertTrue(jobOperator.stopBatch(jobName));
	
	}
	
	@Test
	public void shouldAssembleParameters() throws Exception {
		parameterReaderMock = mock(BatchParameterReader.class);
		((DefaultSpringBatchJobOperator) jobOperator).setParameterReader(parameterReaderMock);
		String tBatchParams = "batchParam3=param3,batchParam4=param4";
		when(parameterReaderMock.getBatchParameters(jobName)).thenReturn(tBatchParams);
		String assembledParameters = parameters + "," + tBatchParams;
		when(springJobOperatorMock.start(jobName, assembledParameters)).thenReturn(jobExecutionId);
		when(jobExplorerMock.getJobExecution(jobExecutionId)).thenReturn(jobEx);
		
		jobOperator.executeBatch(jobName, parameters);
	}
	
	@Test
	public void shouldWrapAndReThrowExceptions() throws Exception {
		Throwable cause = new NoSuchJobException(jobName);
		try {
			when(springJobOperatorMock.start(jobName, parameters)).thenThrow(cause);
			jobOperator.executeBatch(jobName, parameters);
			fail();
		} catch (RuntimeException expected) {
			assertEquals(cause, expected.getCause());
		}
	}

}
