package no.stelvio.batch.support;

import no.stelvio.batch.count.support.BatchCounter;

/**
 * @author person47c121e3ccb5, BEKK
 *
 */
public interface ProgressListener {

	void progressed(BatchCounter counter);

	void finished(BatchCounter counter);

	void started(BatchCounter counter);

}
