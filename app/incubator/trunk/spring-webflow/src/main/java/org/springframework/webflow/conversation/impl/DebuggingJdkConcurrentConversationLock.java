package org.springframework.webflow.conversation.impl;

import java.io.Serializable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DebuggingJdkConcurrentConversationLock implements ConversationLock, Serializable {

	private Lock lock = new ReentrantLock();
	private Thread lockingThread;
	private StackTraceElement[] stackTraceForLockingThread = null;
	private long lockTime;

	public void lock() {
		Exception e = new Exception();
		e.fillInStackTrace();
		System.err.println("> trying to lock: lock=" + this + ", thread=" + Thread.currentThread());
		e.printStackTrace(System.err);
		lock.lock();
		lockTime = System.currentTimeMillis();
		lockingThread = Thread.currentThread();
		stackTraceForLockingThread = e.getStackTrace();
		System.err.println("==> LOCKED: lock=" + this + ", thread=" + lockingThread);
	}

	public void unlock() {
		Thread prevOwner = lockingThread;
		lockingThread = null;
		stackTraceForLockingThread = null;
		System.err.println("<== UNLOCKED: lock=" + this + ", previous owner thread=" + prevOwner);
		lock.unlock();
	}

	/**
	 * @return state of lock
	 */
	public boolean isLocked() {
		boolean gotLock = lock.tryLock();
		if (gotLock) {
			lock.unlock();
		}
		return !gotLock;
	}

	public Thread getLockingThread() {
		return lockingThread;
	}

	public StackTraceElement[] getStackTraceForLockingThread() {
		return stackTraceForLockingThread;
	}

	public long getLockTime() {
		return lockTime;
	}
}
