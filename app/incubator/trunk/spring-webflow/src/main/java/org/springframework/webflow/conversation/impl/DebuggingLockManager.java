package org.springframework.webflow.conversation.impl;

import java.util.Map;
import java.util.WeakHashMap;

public class DebuggingLockManager {
	private static final DebuggingLockManager instance = new DebuggingLockManager();
	private Map<DebuggingJdkConcurrentConversationLock, Object> locks = new WeakHashMap<DebuggingJdkConcurrentConversationLock, Object>();
	private Thread reporterThread;

	private class LockReporter implements Runnable {
		public void run() {
			while (true) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// NOOP
				}

				System.err.println("== Conversation locks ==");
				for (DebuggingJdkConcurrentConversationLock lock : locks.keySet()) {
					if (lock == null)
						continue; // weak hashmap
					if (lock.isLocked()) {
						long lockTime = System.currentTimeMillis() - lock.getLockTime();
						System.err.println("Lock: " + lock + ", locked for " + lockTime + " ms by thread="
								+ lock.getLockingThread());

						if (lockTime > 120000L) {
							StringBuilder builder = new StringBuilder();
							for( StackTraceElement elem : lock.getStackTraceForLockingThread() ) {
								builder.append(elem.toString() + "\n");
							}
							System.err.println("Lock: " + lock + ", locked for " + lockTime + " ms by thread="
									+ lock.getLockingThread()+ " stack:" + builder.toString());
						}

					}
				}
				System.err.println("== End of conversation locks ==");
			}
		}
	}

	/**
	 * Locked
	 */
	private DebuggingLockManager() {
		reporterThread = new Thread(new LockReporter());
		reporterThread.start();
	}

	/**
	 * @return singleton instance
	 */
	public static DebuggingLockManager getInstance() {
		return instance;
	}

	/**
	 * Register lock
	 * 
	 * @param lock
	 */
	public void registerLock(DebuggingJdkConcurrentConversationLock lock) {
		locks.put(lock, null);
	}
}
