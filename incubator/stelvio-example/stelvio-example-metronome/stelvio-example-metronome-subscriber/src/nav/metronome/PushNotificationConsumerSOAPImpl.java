package nav.metronome;

import java.util.List;

import org.oasis_open.docs.wsn.b_2.NotificationMessageHolderType;
import org.oasis_open.docs.wsn.b_2.Notify;


@javax.jws.WebService(endpointInterface="nav.metronome.NotificationConsumer", targetNamespace="http://nav/metronome", serviceName="PushNotificationConsumer", portName="PushNotificationConsumerSOAP", wsdlLocation="WEB-INF/wsdl/notify.wsdl")
public class PushNotificationConsumerSOAPImpl{

    public void notify(Notify notify) {
    	List<NotificationMessageHolderType> notificationmessages = notify.getNotificationMessage();
    	for (NotificationMessageHolderType notification : notificationmessages) {
			System.out.println(notification.getMessage().getAny());
		}
    }

}