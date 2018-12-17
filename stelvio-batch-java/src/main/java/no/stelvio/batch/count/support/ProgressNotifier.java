package no.stelvio.batch.count.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;

import no.stelvio.batch.support.CommonBatchInputParameters;
import no.stelvio.batch.support.ProgressListener;
import no.stelvio.batch.support.ProgressLoggerListener;

/**
 * Logs batch progress by use of a {@link BatchCounter} and a optionally configured {@link #progressInterval}, default value is 1.
 * ProgressInterval can be set using {@link #setProgressInterval(Integer)} or by starting batch with a job parameter with 
 * name {@link CommonBatchInputParameters#PROGRESS_INTERVAL_KEY} and a long value.
 *
 * @author person47c121e3ccb5, BEKK
 *
 */
public class ProgressNotifier implements ChunkListener, JobExecutionListener{
    private BatchCounter counter;
    private List<ProgressListener> progressListeners = new ArrayList<>();
    private Integer progressInterval = 1;
    private int chunkCounter;

    @SuppressWarnings("unused")
    private ProgressNotifier() {
    }

    public ProgressNotifier(BatchCounter counter) {
        this.counter = counter;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        counter.stop(CommonBatchEvents.JOB_EVENT);
        notifyListenersAboutFinish();
    }

    /**
     * Stops job event and logs summary.
     */
    public void afterJob() {
        afterJob(null);
    }

    /**
     * Starts job event.
     */
    public void beforeJob() {
        beforeJob(null);
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        setProgressIntervalFromJobParameters(jobExecution);
        counter.resetCounter();
        counter.start(CommonBatchEvents.JOB_EVENT);
        notifyListenersAboutStart();
    }

    private void setProgressIntervalFromJobParameters(JobExecution jobExecution) {
        if (jobExecution != null) {
            Long progressInterval = (Long) jobExecution.getExecutionContext().get(
                    CommonBatchInputParameters.PROGRESS_INTERVAL_KEY);
            if (progressInterval != null) {
                setProgressInterval(progressInterval.intValue());
            }
        }
    }

    private void notifyListenersAboutProgress() {
        for (ProgressListener listener : progressListeners) {
            listener.progressed(counter);
        }
    }

    private void notifyListenersAboutFinish() {
        for (ProgressListener listener : progressListeners) {
            listener.finished(counter);
        }
    }

    private void notifyListenersAboutStart() {
        for (ProgressListener listener : progressListeners) {
            listener.started(counter);
        }
    }

    /**
     * Sets implementation to use for counting.
     * @param counter A batch counter.
     */
    public void setCounter(BatchCounter counter) {
        this.counter = counter;
    }

    /**
     * Number of chunks between each progress logging.
     * @param progressInterval sets the interval to use
     */
    public void setProgressInterval(Integer progressInterval) {
        this.progressInterval = progressInterval;
    }

    public void setProgressListeners(List<ProgressListener> listeners) {
        this.progressListeners = listeners;

    }

    public void addProgressListener(ProgressLoggerListener listener) {
        progressListeners.add(listener);
    }

    @Override
    public void beforeChunk(ChunkContext chunkContext) {

    }

    @Override
    public void afterChunk(ChunkContext chunkContext) {
        chunkCounter++;
        if (chunkCounter % progressInterval == 0) {
            notifyListenersAboutProgress();
            chunkCounter = 0;
        }
    }

    @Override
    public void afterChunkError(ChunkContext chunkContext) {

    }
}
