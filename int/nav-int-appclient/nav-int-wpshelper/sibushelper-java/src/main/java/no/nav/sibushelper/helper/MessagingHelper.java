/**
 * 
 */
package no.nav.sibushelper.helper;

import java.util.List;

/**
 * @author persona2c5e3b49756 Schnell
 *
 */
public interface MessagingHelper {

	/**
	 * @param busName
	 * @param meName
	 * @param queueName
	 * @return
	 * @throws MessagingOperationFailedException
	 */
	public List<MessageInfo> browseQueue(String busName, String meName, String queueName) throws MessagingOperationFailedException;

	/**
	 * @param meName
	 * @param busName
	 * @param queueName
	 * @param msgId
	 * @return
	 * @throws MessagingOperationFailedException
	 */
	public MessageInfo browseSingleMessage(String busName, String meName, String queueName, String msgId) throws MessagingOperationFailedException;

	/**
	 * @param busName
	 * @param meName
	 * @param seQueue
	 * @param msgSelector
	 * @param maxMesg
	 * @return
	 */
	public List<MessageInfo> moveExceptionToDestination(String busName, String meName, String seQueue, String msgSelector, long maxMesg);
	

	/**
	 * @param busName
	 * @param meName
	 * @param srcQueue
	 * @param trgQueue
	 * @param msgSelector
	 * @param maxMesg
	 * @return
	 */
	public long moveMessages(String busName, String meName, String srcQueue, String trgQueue, String msgSelector, long maxMesg);

	/**
	 * @param busName
	 * @param meName
	 * @param queueName
	 * @param msgSelector
	 * @param maxMesg
	 * @return
	 * @throws MessagingOperationFailedException
	 * @throws DestinationNotFoundException
	 */
	public long clearQueue(String busName, String meName, String queueName, String msgSelector, long maxMesg) throws MessagingOperationFailedException, DestinationNotFoundException;



}
