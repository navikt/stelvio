package no.stelvio.batch.controller;


/**
 * Interface for execution of batches that can take a parameter string as input.
 * (Used by the Spring batch framework as job-parameters.)
 *
 */
public interface SpringBatchEnabledBatchControllerServiceBi extends BatchControllerServiceBi, SpringBatchJobOperator {
	

}
