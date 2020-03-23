/**
 * 
 */
package no.stelvio.batch.count.support;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
public class ConcurrentCounterTestUtil {

	public static void executeConcurrentStartAndStop(final BatchCounter counter, final CounterEvent concurrentEvent, final int sleeptime) throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		final CountDownLatch ready = new CountDownLatch(2);
		final CountDownLatch done = new CountDownLatch(2);
		final CountDownLatch start = new CountDownLatch(1);
		final CountDownLatch startSecondThread = new CountDownLatch(1);
		final CountDownLatch stopSecondThread = new CountDownLatch(1);
		executor.execute(new Runnable() {
			public void run() {
				ready.countDown();
				try {
					start.await();
					counter.start(concurrentEvent);
					Thread.sleep(sleeptime);
					startSecondThread.countDown();
					stopSecondThread.await();
					counter.stop(concurrentEvent);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				} finally {
					done.countDown();
				}
			}
		});
		executor.execute(new Runnable() {
			public void run() {
				ready.countDown();
				try {
					start.await();
					startSecondThread.await();
					counter.start(concurrentEvent);
					counter.stop(concurrentEvent);
					stopSecondThread.countDown();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				} finally {
					done.countDown();
				}
			}
		});		
		ready.await();
		start.countDown();
		done.await();
	}
}
