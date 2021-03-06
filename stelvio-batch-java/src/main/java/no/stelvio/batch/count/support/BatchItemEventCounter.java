package no.stelvio.batch.count.support;

import java.util.List;

import org.springframework.batch.core.listener.ItemListenerSupport;

/**
 * A Spring Batch item listener that counts the events with a {@link BatchCounter}.
 * 
 *
 * @param <I> Type read.
 * @param <O> Type written.
 */
public class BatchItemEventCounter<I, O> extends ItemListenerSupport<I, O>{

	private BatchCounter counter;
	private CounterEvent readEvent;
	private CounterEvent processEvent;
	private CounterEvent writeEvent;

	@Override
	public void afterRead(I item) {
		if(readEvent != null){
			counter.stop(readEvent);
		}
	}

	@Override
	public void beforeRead() {
		if (readEvent != null) {
			counter.start(readEvent);
		}
	}

	@Override
	public void afterProcess(I item, O result) {
		if(processEvent != null){
			counter.stop(processEvent);
		}
	}

	@Override
	public void beforeProcess(I item) {
		if(processEvent != null){
			counter.start(processEvent);
		}
	}

	@Override
	public void afterWrite(List<? extends O> item) {
		if(writeEvent != null){
			counter.stop(writeEvent, item.size());
		}
	}

	@Override
	public void beforeWrite(List<? extends O> item) {
		if(writeEvent != null){
			counter.start(writeEvent);
		}
	}	

	/**
	 * Sets the implementation to use.
	 * @param counter the counter to set
	 */
	public void setCounter(BatchCounter counter) {
		this.counter = counter;
	}


	public void setReadEvent(CounterEvent readEvent) {
		this.readEvent = readEvent;
	}


	public void setProcessEvent(CounterEvent processEvent) {
		this.processEvent = processEvent;
	}


	public void setWriteEvent(CounterEvent writeEvent) {
		this.writeEvent = writeEvent;
	}	
	
}
