package no.stelvio.batch.framework;

import javax.jms.Message;

/**
 * Superclass for all message driven batches. This extends the AbstractBatch
 * by allowing the caller to set the message to be worked on since this will
 * come from an MDB.
 *  
 * @author person356941106810, Accenture
 */
public abstract class AbstractMessageDrivenBatch extends AbstractBatch {
	/** The message to process. */
	private Message message = null;

	/**
	 * Initializes the batch with its name.
	 *
	 * @param batchName the batch's name.
	 */
	protected AbstractMessageDrivenBatch(String batchName) {
		super(batchName);
	}

	/**
	 * Sets the message the batch will work on.
	 *
	 * @param message the message the batch will work on.
	 */
	public void setMessage(javax.jms.Message message) {
		this.message = message;
	}

	/**
	 * Returns the message to work on.
	 *
	 * @return the message to work on.
	 */
	public Message getMessage() {
		return message;
	}
}
