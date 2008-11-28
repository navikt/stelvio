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
	 * @param queueName
	 * @return
	 * @throws MessagingOperationFailedException
	 */
	public int queueMessages(String busName, String meName, String queueName) throws MessagingOperationFailedException;
	
	/**
	 * @param busName
	 * @param meName
	 * @param queueName
	 * @return
	 * @throws MessagingOperationFailedException
	 * @throws DestinationNotFoundException
	 */
	public int clearQueue(String busName, String meName, String queueName) throws MessagingOperationFailedException, DestinationNotFoundException;
}
