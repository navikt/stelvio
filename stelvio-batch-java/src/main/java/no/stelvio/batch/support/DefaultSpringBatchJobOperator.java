package no.stelvio.batch.support;

import java.util.Arrays;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.launch.support.ExitCodeMapper;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.support.PropertiesConverter;

import no.stelvio.batch.BatchParameterReader;
import no.stelvio.batch.controller.SpringBatchJobOperator;

/**
 * Class that acts as glue between Spring Batch and Stelvio Batch execution semantics. Uses {@link JobOperator}, {@link JobExplorer} and {@link JobRepository} to interact with
 * Spring Batch framework.
 * 
 */
public class DefaultSpringBatchJobOperator implements SpringBatchJobOperator {
    private final Log logger = LogFactory.getLog(getClass());
    private JobOperator jobOperator;
    private JobExplorer jobExplorer;
    private JobRepository jobRepository;
    private ExitCodeMapper exitCodeMapper = new TWSExitCodeMapper();
    private BatchParameterReader parameterReader;
    private JobParametersConverter jobParametersConverter;

    @Override
    public int executeBatch(String jobName, String parameters) {
        try {
            JobExecution jobExecution = execute(jobName, parameters);
            return exitCodeMapper.intValue(jobExecution.getExitStatus().getExitCode());
        } catch (Exception e) {
            throw logAndWrapException("Failed to start batch=" + jobName, e);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Stop batch has also been extended, now handling multiple concurrent instances.
     * 
     * @param jobName name of the batch
     * @return true if a stop request is sent successfully to all executions, or false if one or more fails.
     *         Note that this does not mean that the batch was successfully stopped. See {@link JobOperator#stop(long)}
     */
    @Override
    public boolean stopBatch(String jobName) {
        boolean result = true;

        try {
            for (Long execution : getRunningExecutions(jobName)) {
                boolean currentStopStatus = stopExecution(execution);
                result = result && currentStopStatus;
            }
        } catch (Exception e) {
            throw logAndWrapException("Failed to stop batch = " + jobName, e);
        }

        return result;
    }

    private Set<Long> getRunningExecutions(String jobName) throws NoSuchJobException {
        Set<Long> executions = jobOperator.getRunningExecutions(jobName);

        if (executions.isEmpty()) {
            throw new RuntimeException("No running batch with jobName = " + jobName);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Found following executions: " + Arrays.toString(executions.toArray()));
        }

        return executions;
    }

    private boolean stopExecution(Long executionId) {
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("Sending stop request to batch with executionId = " + executionId);
            }
            return jobOperator.stop(executionId);
        } catch (Exception e) {
            throw logAndWrapException("Failed to stop batch with executionId = " + executionId, e);
        }
    }

    /**
     * Wrap a throwable with a runtimeexception and log it.
     * 
     * @param message message
     * @param e throwable
     * @return runtime exception
     */
    private RuntimeException logAndWrapException(String message, Throwable e) {
        if (logger.isErrorEnabled()) {
            logger.error(message, e);
        }
        return new RuntimeException(message + ", message from cause: " + e.getMessage(), e);
    }

    /**
     * Execute job.
     * 
     * @param jobName
     *            job name
     * @param parameters
     *            parameters
     * @return a job execution
     * @throws NoSuchJobException
     *             no such job
     * @throws JobInstanceAlreadyCompleteException
     *             job instance already complete
     * @throws NoSuchJobExecutionException
     *             no such job execution
     * @throws JobRestartException
     *             job restart
     * @throws JobParametersInvalidException
     *             job parameters invalid
     */
    private JobExecution execute(String jobName, String parameters) throws NoSuchJobException,
            JobInstanceAlreadyCompleteException, NoSuchJobExecutionException, JobRestartException,
            JobParametersInvalidException {

        Long jobExecutionId = start(jobName, parameters);
        JobExecution jobExecution = getJobExecution(jobName, parameters, jobExecutionId);
        if (logger.isDebugEnabled()) {
            logger.debug("Batch done executing, jobExecution:" + jobExecution);
        }
        return jobExecution;
    }

    /**
     * Get job execution.
     * 
     * @param jobName
     *            job name
     * @param parameters
     *            parameters
     * @param jobExecutionId
     *            job execution id
     * @return job execution
     */
    private JobExecution getJobExecution(String jobName, String parameters, Long jobExecutionId) {
        JobExecution jobExecution = jobExplorer.getJobExecution(jobExecutionId);
        if (jobExecution == null) {
            throw new RuntimeException("Failed to start batch, no JobExecution found for jobName=" + jobName
                                       + ", and parameters=" + parameters);
        }
        return jobExecution;
    }

    /**
     * Start job.
     * 
     * @param jobName
     *            job name
     * @param parameters
     *            parameters
     * @return id
     * @throws NoSuchJobException
     *             no such job
     * @throws JobInstanceAlreadyCompleteException
     *             job instance already complete
     * @throws NoSuchJobExecutionException
     *             no such job execution
     * @throws JobRestartException
     *             job restart
     * @throws JobParametersInvalidException
     *             job parameters invalid
     */
    private Long start(String jobName, String parameters) throws NoSuchJobException, JobInstanceAlreadyCompleteException,
            NoSuchJobExecutionException, JobRestartException, JobParametersInvalidException {

        Long jobExecutionId;
        try {
            String jobParameters = assembleParameters(jobName, parameters);
            jobExecutionId = jobOperator.start(jobName, jobParameters); // synkron ex.
        } catch (JobInstanceAlreadyExistsException e) {
            if (logger.isInfoEnabled()) {
                logger.info("Job instance exists, trying restart", e);
            }
            jobExecutionId = restart(jobName, parameters);
        }
        return jobExecutionId;
    }

    /**
     * Job restart.
     * 
     * @param jobName
     *            job name
     * @param parameters
     *            parameters
     * @return id
     * @throws JobInstanceAlreadyCompleteException
     *             job instance already complete
     * @throws NoSuchJobExecutionException
     *             no such execution
     * @throws NoSuchJobException
     *             no such job
     * @throws JobRestartException
     *             jo restart
     * @throws JobParametersInvalidException
     *             jo parameters invalid
     */
    private Long restart(String jobName, String parameters) throws JobInstanceAlreadyCompleteException,
            NoSuchJobExecutionException, NoSuchJobException, JobRestartException, JobParametersInvalidException {
        JobParameters jobParameters = jobParametersConverter.getJobParameters(PropertiesConverter
                .stringToProperties(parameters));
        JobExecution jobExecution = jobRepository.getLastJobExecution(jobName, jobParameters);
        if (jobExecution == null) {
            throw new NoSuchJobExecutionException("No JobExecution found for jobName=" + jobName + ", and jobParameters="
                                                  + jobParameters);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("restarting jobExecution:" + jobExecution);
        }
        return jobOperator.restart(jobExecution.getId());

    }

    /**
     * Concatenates given parameters with (optional) parameters from another source.
     * 
     * @param jobName
     *            Name of batch to get additional parameters for
     * @param parameters
     *            Parameters from batch start input.
     * @return Concatenated parameter string.
     */
    private String assembleParameters(String jobName, String parameters) {
        String tBatchParameters = parameterReader == null ? null : parameterReader.getBatchParameters(jobName);
        if (parameters == null) {
            return tBatchParameters;
        } else if (tBatchParameters == null) {
            return parameters;
        } else {
            return parameters + "," + tBatchParameters;
        }
    }

    /**
     * Sets implementation to use.
     * 
     * @param jobOperator
     *            jobOperator to set.
     */
    public void setJobOperator(JobOperator jobOperator) {
        this.jobOperator = jobOperator;
    }

    /**
     * Sets implementation to use.
     * 
     * @param jobExplorer
     *            jobExplorer to set.
     */
    public void setJobExplorer(JobExplorer jobExplorer) {
        this.jobExplorer = jobExplorer;
    }

    /**
     * Sets implementation to use.
     * 
     * @param parameterReader
     *            parameterReader to set.
     */
    public void setParameterReader(BatchParameterReader parameterReader) {
        this.parameterReader = parameterReader;
    }

    /**
     * Sets implementation to use.
     * 
     * @param jobParametersConverter
     *            jobParametersConverter to set.
     */
    public void setJobParametersConverter(JobParametersConverter jobParametersConverter) {
        this.jobParametersConverter = jobParametersConverter;
    }

    /**
     * Sets implementation to use.
     * 
     * @param jobRepository
     *            jobRepository to set.
     */
    public void setJobRepository(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

}
