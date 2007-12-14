package nav.pp.mqmovefix;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MQMoveFix {

	public static void main(String[] args) {

		if (args.length != 6) {
			System.err.println("Usage: MQMoveFix <qm host> <qm port> <qm name> <qm channel> <source queue> <dest queue>");
			System.exit(1);
		}

		String qmHost = args[0];
		int qmPort = Integer.parseInt(args[1]);
		String qmName = args[2];
		String qmChannel = args[3];
		String qmSourceQueue = args[4];
		String qmDestQueue = args[5];

		WSMQQueueAccessor sourceAccessor = new WSMQQueueAccessor(qmName, qmName, qmHost, qmPort, qmChannel, qmSourceQueue);
		WSMQQueueAccessor destAccessor = new WSMQQueueAccessor(qmName, qmName, qmHost, qmPort, qmChannel, qmDestQueue);

		try {
			// Create message listener instance
			FixingMessageListener listener = new MQMoveFix.FixingMessageListener(destAccessor);

			// Create listener session
			Connection listenerConn = sourceAccessor.getConnection();
			Session listenerSess = listenerConn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageConsumer consumer = listenerSess.createConsumer(sourceAccessor.getDestination());
			consumer.setMessageListener(listener);
			listenerConn.start();

			System.out.println("Startet..");

		} catch (Exception e) {
			System.err.println("FEIL: " + e);
		}

		try {
			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException e) {
			System.err.println("FEIL: Våknet fra evig dvale..");
		}
	}

	/**
	 * @author kristian
	 */
	public static class FixingMessageListener implements MessageListener {
		private WSMQQueueAccessor queueAccessor;

		private Connection conn;
		private Session sess;
		private MessageProducer producer;

		private int numReceived = 0, numSent = 0, numChanged = 0;

		public FixingMessageListener(WSMQQueueAccessor accessor) throws Exception {
			this.queueAccessor = accessor;

			// Dirty, not cleaning up resources.. :-D
			conn = queueAccessor.getConnection();
			sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			producer = sess.createProducer(accessor.getDestination());
		}

		public void onMessage(Message message) {
			try {
				numReceived++;
				if (message instanceof TextMessage) {
					TextMessage msg = (TextMessage) message;

					String msgText = msg.getText();

					// LinkingDnrFnr
					if (msgText != null && msgText.indexOf("linkingDnrFnr") > 0) {
						Document doc = XMLUtils.parseXML(msgText);
						fiksLinkingDnrFnrMelding(doc);
						msgText = XMLUtils.encodeXML(doc);
						numChanged++;
					}

					// Rute melding videre
					TextMessage routemsg = sess.createTextMessage();
					routemsg.setText(msgText);
					producer.send(routemsg);
					numSent++;

					if (numReceived % 10 == 0) {
						System.out.println("Received: " + numReceived + ", sent: " + numSent + ", changed: " + numChanged);
					}

				} else {
					System.err.println("FEIL: Mottatt melding av feil type: " + message.getClass().getName());
				}
			} catch (JMSException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void fiksLinkingDnrFnrMelding(Document doc) {
			Element root = doc.getDocumentElement();

			Element fnrNode = XMLUtils.findNamedNode(root, "fnr");

			Element dnrNode = XMLUtils.findNamedNode(root, "dnr");
			String dnrOldVal = dnrNode.getFirstChild().getNodeValue();

			Element nyttFnrNode = XMLUtils.findNamedNode(root, "nyttFnr");
			String nyttFnrOldVal = nyttFnrNode.getFirstChild().getNodeValue();

			fnrNode.getFirstChild().setNodeValue(nyttFnrOldVal);
			dnrNode.getFirstChild().setNodeValue(nyttFnrOldVal);
			nyttFnrNode.getFirstChild().setNodeValue(dnrOldVal);
		}
	}

}
