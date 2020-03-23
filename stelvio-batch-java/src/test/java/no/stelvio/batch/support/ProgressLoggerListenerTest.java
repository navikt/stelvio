package no.stelvio.batch.support;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import no.stelvio.batch.count.support.BatchCounter;
import no.stelvio.batch.count.support.EventReportFormatter;
import no.stelvio.batch.count.support.SimpleBatchCounter;
import no.stelvio.common.log.InfoLogger;

import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class ProgressLoggerListenerTest {
	private ProgressListener progressLogger;
	private InfoLogger logger;
	private BatchCounter counter = new SimpleBatchCounter();
	private EventReportFormatter formatter;
	
	@Before
	public void setUp() {
		logger = mock(InfoLogger.class);
		formatter = mock(EventReportFormatter.class);
		progressLogger = new ProgressLoggerListener(logger, formatter);
	}
	
	@Test
	public void shouldLogBatchStart() {
		progressLogger.started(counter);
		verifyLogging();
	}
	
	@Test
	public void shouldLogBatchFinish() {
		progressLogger.finished(counter);
		verifyLogging();
	}
	
	@Test
	public void shouldLogBatchProgress() {
		progressLogger.progressed(counter);
		verifyLogging();
	}	
	
	private void verifyLogging() {
		
		verify(logger, atLeastOnce()).info(isA(String.class));
		verify(formatter).format(counter.getEventReport());
	}	
}
