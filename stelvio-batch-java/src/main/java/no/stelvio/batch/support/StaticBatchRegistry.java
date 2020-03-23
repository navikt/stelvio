package no.stelvio.batch.support;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import no.stelvio.batch.BatchBi;
import no.stelvio.batch.BatchRegistry;
import no.stelvio.batch.exception.InvalidBatchEntryException;

/**
 * Thread safe BatchRegistry using a final HashMap to hold running batch instances. The HashMap may only be accessed by
 * synchronized methods, hence all threads should be able to read up-to-date data.
 * 
 *
 */
public class StaticBatchRegistry implements BatchRegistry {

	private final Map<String, BatchBi> batches = new HashMap<>();

	@Override
	public synchronized void registerBatch(String batchName, int slice, BatchBi batch) {
		if (isBatchRegistered(batchName, slice)) {
			throw new InvalidBatchEntryException("The batch " + batchName + " with slice " + slice + " is already running!");
		}
		batches.put(getHashCode(batchName, slice), batch);
	}

	@Override
	public synchronized boolean unregisterBatch(String batchName, int slice) {
		return (batches.remove(getHashCode(batchName, slice))) != null;
	}

	@Override
	public synchronized boolean isBatchRegistered(String batchName, int slice) {
		return batches.containsKey(getHashCode(batchName, slice));
	}

	@Override
	public synchronized BatchBi getBatch(String batchName, int slice) {
		return batches.get(getHashCode(batchName, slice));
	}

	/**
	 * Calculates a hash code based on the batch name and the slice Used as key when registering a batch with a batch name and a
	 * slice.
	 * 
	 * @param batchName batch name
	 * @param slice slice
	 * @return hash code
	 */
	private String getHashCode(String batchName, int slice) {
		HashCodeBuilder builder = new HashCodeBuilder();
		builder.append(batchName);
		builder.append(slice);
		return String.valueOf(builder.toHashCode());
	}

}
