package no.stelvio.batch.support;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.launch.support.ExitCodeMapper;

import no.stelvio.batch.BatchStatus;

/**
 * Maps Stelvio Batch exit codes to Stelvio {@link BatchStatus} codes.
 * 
 */
public class TWSExitCodeMapper implements ExitCodeMapper {
	protected Log logger = LogFactory.getLog(getClass());

	private Map<String, Integer> mapping;

	/**
	 * Constructs a new mapper.
	 */
	public TWSExitCodeMapper() {
		mapping = new HashMap<>();
		mapping.put(ExitStatus.COMPLETED.getExitCode(), BatchStatus.BATCH_OK);
		mapping.put(ExitStatus.STOPPED.getExitCode(), BatchStatus.BATCH_STOPPED);
		mapping.put(ExitStatus.FAILED.getExitCode(), BatchStatus.BATCH_FATAL);
		mapping.put(NavExitStatus.ERROR.getExitCode(), BatchStatus.BATCH_ERROR);
		mapping.put(NavExitStatus.WARNING.getExitCode(), BatchStatus.BATCH_WARNING);
	}

	@Override
	public int intValue(String exitCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("Mapping exitcode:" + exitCode);
		}
		Integer twsCode = null;
		try {
			twsCode = mapping.get(exitCode);
			if (logger.isDebugEnabled()) {
				logger.debug("twsCode: " + twsCode);
			}
		} catch (Throwable ex) {
			if (logger.isErrorEnabled()) {
				logger.error("Error mapping exit code, error exit status returned.", ex);
			}
		}
		return (twsCode != null) ? twsCode.intValue() : BatchStatus.BATCH_ERROR;
	}
}
