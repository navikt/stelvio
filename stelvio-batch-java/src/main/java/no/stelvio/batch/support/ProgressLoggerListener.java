package no.stelvio.batch.support;

import no.stelvio.batch.count.support.BatchCounter;
import no.stelvio.batch.count.support.EventReportFormatter;
import no.stelvio.common.log.InfoLogger;

/**
 *
 */
public class ProgressLoggerListener implements ProgressListener {
	private InfoLogger logger;
	private EventReportFormatter formatter;
	
	private ProgressLoggerListener() {
	}

	public ProgressLoggerListener(InfoLogger logger, EventReportFormatter formatter) {
		this.logger = logger;
		this.formatter = formatter;
	}

	@Override
	public void finished(BatchCounter counter) {
		logger.info("Batch ended with progress counter status:");
		logCounters(counter);
	}

	@Override
	public void progressed(BatchCounter counter) {
		logger.info("Batch progress status:");
		logCounters(counter);
		
	}

	@Override
	public void started(BatchCounter counter) {
		logger.info("Batch started with following progress counters active:");
		logCounters(counter);
	}

	private void logCounters(BatchCounter counter) {
		logger.info(formatter.format(counter.getEventReport()));
	}

	public void setLogger(InfoLogger logger) {
		this.logger = logger;
	}

	public void setFormatter(EventReportFormatter formatter) {
		this.formatter = formatter;
	}
	
	
}
