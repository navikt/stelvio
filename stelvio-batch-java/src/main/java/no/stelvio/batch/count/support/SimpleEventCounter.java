package no.stelvio.batch.count.support;

/**
 * @author person47c121e3ccb5, BEKK
 * 
 */
public class SimpleEventCounter implements EventCounter {
	private long count;
	private long time;
	private ThreadLocal<Long> startTime = ThreadLocal.withInitial(() -> 0L);

	/**
	 * Creates a new counter
	 * 
	 * @param count
	 *            initial count value
	 * @param time
	 *            initial time value
	 */
	public SimpleEventCounter(long count, long time) {
		this.count = count;
		this.time = time;
	}

	public synchronized void addCount(long numberOfCounts) {
		count += numberOfCounts;
	}

	public synchronized void addTime(long ms) {
		time += ms;
	}

	public synchronized void setStartTime(long time) {
		startTime.set(time);
	}

	public synchronized long getStartTime() {
		return startTime.get();
	}

	@Override
	public synchronized long getCount() {
		return count;
	}

	@Override
	public synchronized long getTime() {
		return time;
	}

	@Override
	public synchronized long getAvg() {
		if (count == 0) {
			return 0;
		}
		return time / count;
	}

	@Override
	public String toString() {
		return "count=" + count + ", time=" + time + ", avg=" + getAvg();
	}

}

