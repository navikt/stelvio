/*
 * Copyright 2004-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.webflow.execution.repository.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.webflow.conversation.Conversation;
import org.springframework.webflow.conversation.ConversationManager;
import org.springframework.webflow.execution.repository.FlowExecutionLock;

/**
 * A flow execution lock that locks a conversation managed by a
 * {@link ConversationManager}.
 * <p>
 * This implementation ensures multiple threads cannot manipulate the same
 * conversation at the same time. The locked conversation is the sole gateway to
 * a flow execution, and a lock on it prevents access to any associated
 * execution.
 * 
 * @see ConversationManager
 * @see Conversation
 * @see Conversation#lock()
 * @see Conversation#unlock()
 * 
 * @author Keith Donald
 */
class ConversationBackedFlowExecutionLock implements FlowExecutionLock {

	protected final Log logger = LogFactory.getLog(ConversationBackedFlowExecutionLock.class);

	
	/**
	 * The conversation to lock.
	 */
	private Conversation conversation;

	/**
	 * Creates a new conversation-backed flow execution lock.
	 * @param conversation the conversation to lock
	 */
	public ConversationBackedFlowExecutionLock(Conversation conversation) {
		this.conversation = conversation;
	}

	public void lock() {
		conversation.lock();
//		logger.debug("Thread " + Thread.currentThread().getId() + " unlocked conversation: " 
//				+ conversation.getId() + " by " + logCaller());
		
	}

	public void unlock() {
//		logger.debug("Thread " + Thread.currentThread().getId() + " unlocked conversation: " 
//				+ conversation.getId() + " by " + logCaller());
		conversation.unlock();
	}
	
	
//	private String logCaller() {
//		// Hack (?) to get the stack trace.
//		Throwable dummyException=new Throwable();
//		StackTraceElement
//		locations[]=dummyException.getStackTrace();
//		// Caller will be the third element
//		String cname="unknown";
//		String method="unknown";
//		if( locations!=null && locations.length >2 ) {
//			StackTraceElement caller=locations[2];
//			cname=caller.getClassName();
//			method=caller.getMethodName();
//		}		
//		return "class: " + cname + " method: " + method;
//	}
	
}