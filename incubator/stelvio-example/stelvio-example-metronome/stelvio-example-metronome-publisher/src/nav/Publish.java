package nav;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.GregorianCalendar;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.ws.Service;
import javax.xml.ws.soap.AddressingFeature;

import org.oasis_open.docs.wsn.b_2.NotificationMessageHolderType;
import org.oasis_open.docs.wsn.b_2.Notify;
import org.oasis_open.docs.wsn.b_2.NotificationMessageHolderType.Message;

import com.ibm.websphere.wsn.notification_broker.NotificationBroker;

public class Publish {

	private static final String BROKER_WSDL_TARGET_NS = "http://www.ibm.com/websphere/wsn/notification-broker";
	private static final String BROKER_PORTTYPE_NAME = "NotificationBrokerPort";
	private static final String BROKER_SERVICE_NAME = "MetronomenMetronomenSPNB";
	private static final QName BROKER_PORTTYPE_QNAME = new QName(BROKER_WSDL_TARGET_NS, BROKER_PORTTYPE_NAME);
	private static final QName BROKER_SERVICE_QNAME = new QName(BROKER_WSDL_TARGET_NS, BROKER_SERVICE_NAME);

	public void publish(GregorianCalendar calendar) throws MalformedURLException, SOAPException {
		Service broker = Service.create(new URL("http://localhost:9080/MetronomenMetronomenSPNB/Service" + "?wsdl"), BROKER_SERVICE_QNAME);
		NotificationBroker port = broker.getPort(BROKER_PORTTYPE_QNAME, NotificationBroker.class, new AddressingFeature());
		Message message = new Message();
		SOAPFactory soapFactory = SOAPFactory.newInstance();
		SOAPElement messageContents = soapFactory.createElement("test");
		message.setAny(messageContents);
		NotificationMessageHolderType notificationHolder = new NotificationMessageHolderType();
		notificationHolder.setMessage(message);
//		TopicExpressionType topicExpression = new TopicExpressionType();
//		topicExpression.setExpression("jeje:melding");
//		topicExpression.setDialect(TopicExpressionType.DIALECT_SIMPLE_TOPIC_EXPRESSION);
//		topicExpression.addPrefixMapping("jeje", "http://nav/jeje");
//		notificationHolder.setTopic(topicExpression);
		Notify notify = new Notify();
		notify.getNotificationMessage().add(notificationHolder);
		port.notify(notify);
	}
}
