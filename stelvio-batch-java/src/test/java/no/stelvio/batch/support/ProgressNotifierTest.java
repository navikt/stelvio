package no.stelvio.batch.support;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import no.stelvio.batch.count.support.BatchCounter;
import no.stelvio.batch.count.support.ProgressNotifier;
import no.stelvio.batch.count.support.SimpleBatchCounter;


import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.MetaDataInstanceFactory;

/**
 *
 */
public class ProgressNotifierTest {
	private BatchCounter counter = new SimpleBatchCounter();
	private ProgressNotifier progressNotifier = new ProgressNotifier(counter);
	private ProgressListener progressListener;
	
	@Before
	public void setUp() {
		progressListener = mock(ProgressListener.class);
		progressNotifier.setProgressListeners(Arrays.asList(progressListener));
	}
	
	@Test
	public void shouldNotifyAboutProgressAfterChunkCompletion() {
		progressNotifier.afterChunk(null);
		verify(progressListener).progressed(counter);
	}
	
	@Test
	public void shouldNotifyAboutProgressAfterConfiguredNumberOfChunkCompletions() {
		progressNotifier.setProgressInterval(3);
		progressNotifier.afterChunk(null);
		progressNotifier.afterChunk(null);
		progressNotifier.afterChunk(null);
		progressNotifier.afterChunk(null);
		progressNotifier.afterChunk(null);
		verify(progressListener).progressed(counter);
	}
	
	@Test
	public void shouldGetConfiguredProgressIntervalFromExecutionContextAndNotifyAboutProgress() {
		JobExecution jobExecution = MetaDataInstanceFactory.createJobExecution();
		jobExecution.getExecutionContext().put(CommonBatchInputParameters.PROGRESS_INTERVAL_KEY, 2L);
		progressNotifier.beforeJob(jobExecution);
		progressNotifier.afterChunk(null);
		progressNotifier.afterChunk(null);
		progressNotifier.afterChunk(null);
		progressNotifier.afterChunk(null);
		progressNotifier.afterChunk(null);
		verify(progressListener, times(2)).progressed(counter);
	}		
	
	@Test
	public void shouldNotifyAboutBatchStart() {
		progressNotifier.beforeJob();
		verify(progressListener).started(counter);
	}
	
	@Test
	public void shouldNotifyAboutBatchCompletion() {
		progressNotifier.afterJob();
		verify(progressListener).finished(counter);
	}

}
