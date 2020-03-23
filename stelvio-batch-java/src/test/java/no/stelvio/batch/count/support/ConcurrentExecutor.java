package no.stelvio.batch.count.support;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
public class ConcurrentExecutor {

	private static class MultipleExecutions implements Runnable {
		private final int numberOfExecutions;
		private final Runnable action;

		public MultipleExecutions(int numberOfExecutions, Runnable action) {
			this.numberOfExecutions = numberOfExecutions;
			this.action = action;
		}
		
		public void run() {
			int count = 0;
			while (count < numberOfExecutions) {
				action.run();
				count++;
			}
		}		
	}
	
	public static void execute(final int concurrency, final int numberOfExecutionsPerThread, final Runnable action) throws InterruptedException {
		ExecutorService executor = Executors.newFixedThreadPool(concurrency);
		final CountDownLatch ready = new CountDownLatch(concurrency);
		final CountDownLatch start = new CountDownLatch(1);
		final CountDownLatch done = new CountDownLatch(concurrency);
		for (int i = 0; i < concurrency; i++) {
			executor.execute(new Runnable() {
				public void run() {
					ready.countDown();
					try {
						start.await();
						new MultipleExecutions(numberOfExecutionsPerThread, action).run();
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					} finally {
						done.countDown();
					}
				}
			});
		}
		ready.await();
		start.countDown();
		done.await();
	}

}
