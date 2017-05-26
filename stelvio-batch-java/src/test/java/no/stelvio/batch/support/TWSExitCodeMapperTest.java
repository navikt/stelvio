package no.stelvio.batch.support;

import static org.junit.Assert.assertEquals;
import no.stelvio.batch.BatchStatus;
import no.stelvio.batch.support.NavExitStatus;
import no.stelvio.batch.support.TWSExitCodeMapper;

import org.junit.Test;
import org.springframework.batch.core.ExitStatus;

/**
 * Test for testing the mapping between Spring Batch exit codes and Stelvio Batch status
 * codes.
 * @author person47c121e3ccb5, BEKK
 *
 */
public class TWSExitCodeMapperTest {
	private TWSExitCodeMapper exitCodeMapper = new TWSExitCodeMapper();
	
	/**
	 * Spring Batch exit code ExitStatus.COMPLETED should map to Stelvio BatchStatus.BATCH_OK.
	 *
	 */
	@Test
	public void shouldMapCompletedCode() {
		assertEquals(BatchStatus.BATCH_OK, 
				exitCodeMapper.intValue(ExitStatus.COMPLETED.getExitCode()));
	}
	
	/**
	 * Spring Batch exit code ExitStatus.FAILED should map to Stelvio BatchStatus.BATCH_FATAL.
	 *
	 */
	@Test
	public void shouldMapFailedCode() {
		assertEquals(BatchStatus.BATCH_FATAL, 
				exitCodeMapper.intValue(ExitStatus.FAILED.getExitCode()));
	}	
	
	/**
	 * Spring Batch exit code ExitStatus.STOPPED should map to Stelvio BatchStatus.BATCH_STOPPED.
	 *
	 */
	@Test
	public void shouldMapStopCode() {
		assertEquals(BatchStatus.BATCH_STOPPED, 
				exitCodeMapper.intValue(ExitStatus.STOPPED.getExitCode()));
	}
	
	@Test
	public void shouldMapWarningCode() {
		assertEquals(BatchStatus.BATCH_WARNING, 
				exitCodeMapper.intValue(NavExitStatus.WARNING.getExitCode()));
	}
	
	@Test
	public void shouldMapErrorCode() {
		assertEquals(BatchStatus.BATCH_ERROR, 
				exitCodeMapper.intValue(NavExitStatus.ERROR.getExitCode()));
	}
	
	/**
	 * Spring Batch exit code ExitStatus.UNKNOWN should map to Stelvio BatchStatus.BATCH_ERROR.
	 *
	 */
	@Test
	public void shouldReturnErrorForMissingMapping() {
		assertEquals(BatchStatus.BATCH_ERROR, 
				exitCodeMapper.intValue(ExitStatus.UNKNOWN.getExitCode()));
	}

}
