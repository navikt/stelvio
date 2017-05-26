package no.stelvio.batch.controller.support;

import org.junit.Assert;

import no.stelvio.batch.BatchStatus;
import no.stelvio.batch.controller.SpringBatchJobOperator;
import no.stelvio.common.context.RequestContext;
import no.stelvio.common.context.RequestContextHolder;

/**
 * Simple JobOperator used by SpringBatchEnabledBatchControllerServiceTest.java to 
 * test that the RequestContext contains the expected values  
 * 
 * @author persone0853a2f3fda (Accenture)
 *
 */
class SpringBatchJobOperatorHelper implements SpringBatchJobOperator {

	@Override
	public int executeBatch(String jobName, String parameters) {
		RequestContext rctx =  RequestContextHolder.currentRequestContext();
		
		Assert.assertNotNull(rctx.getUserId());
		Assert.assertTrue(rctx.getUserId().equals("SpringBatchEnabledBatchControllerService-testJob"));
		
		Assert.assertNotNull(rctx.getComponentId());
		Assert.assertTrue(rctx.getComponentId().equals("SpringBatchEnabledBatchControllerService-testComponentId"));
		
		Assert.assertNotNull(rctx.getTransactionId());

		return BatchStatus.BATCH_STOPPED;
	}

	@Override
	public boolean stopBatch(String jobName) {
		return true;	
	}
}