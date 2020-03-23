package no.stelvio.batch.support;

import no.stelvio.batch.count.support.BatchCounter;

/**
 *
 */
public interface ProgressListener {

	void progressed(BatchCounter counter);

	void finished(BatchCounter counter);

	void started(BatchCounter counter);

}
