package no.stelvio.batch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.fail;

import no.stelvio.batch.exception.BatchFunctionalException;
import no.stelvio.batch.exception.BatchSystemException;
import no.stelvio.common.context.RequestContext;
import no.stelvio.common.context.RequestContextHolder;

/**
 * AbstractBatch implementation used to test the InitRequestContextInterceptor.
 * 
 *
 */
public class InitRequestContextBatchImpl extends AbstractBatch {

	@Override
	public int executeBatch(int slice) throws BatchSystemException, BatchFunctionalException {
		try {
			RequestContext reqCtx = RequestContextHolder.currentRequestContext();
			assertThat(reqCtx.getUserId(), is(equalTo("test")));

		} catch (IllegalStateException e) {
			fail("RequestContext wasn't initialized by a interceptor");
		}
		return BatchStatus.BATCH_OK;
	}

	@Override
	protected void flushUpdates() {

	}

}
