package nav;

import javax.xml.bind.JAXBElement;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import javax.xml.ws.wsaddressing.W3CEndpointReferenceBuilder;

import org.oasis_open.docs.wsn.b_2.FilterType;
import org.oasis_open.docs.wsn.b_2.ObjectFactory;
import org.oasis_open.docs.wsn.b_2.TopicExpressionType;
import org.oasis_open.docs.wsn.b_2.Subscribe.SubscriptionPolicy;

import com.ibm.websphere.models.config.cmm.SubscriptionDurabilityKind;
import com.ibm.websphere.wsn.notification_broker.InvalidFilterFault;
import com.ibm.websphere.wsn.notification_broker.InvalidMessageContentExpressionFault;
import com.ibm.websphere.wsn.notification_broker.InvalidProducerPropertiesExpressionFault;
import com.ibm.websphere.wsn.notification_broker.InvalidTopicExpressionFault;
import com.ibm.websphere.wsn.notification_broker.NotificationBrokerPortProxy;
import com.ibm.websphere.wsn.notification_broker.NotifyMessageNotSupportedFault;
import com.ibm.websphere.wsn.notification_broker.ResourceUnknownFault;
import com.ibm.websphere.wsn.notification_broker.SubscribeCreationFailedFault;
import com.ibm.websphere.wsn.notification_broker.TopicExpressionDialectUnknownFault;
import com.ibm.websphere.wsn.notification_broker.TopicNotSupportedFault;
import com.ibm.websphere.wsn.notification_broker.UnacceptableInitialTerminationTimeFault;
import com.ibm.websphere.wsn.notification_broker.UnrecognizedPolicyRequestFault;
import com.ibm.websphere.wsn.notification_broker.UnsupportedPolicyRequestFault;

public class Subscribe {

	private static final String CONSUMER_URI = "http://localhost:9080/stelvio-example-metronome-subscriber/PushNotificationConsumer";
		
	public Subscribe() throws InvalidFilterFault, InvalidMessageContentExpressionFault, InvalidProducerPropertiesExpressionFault, InvalidTopicExpressionFault, NotifyMessageNotSupportedFault, ResourceUnknownFault, SubscribeCreationFailedFault, TopicExpressionDialectUnknownFault, TopicNotSupportedFault, UnacceptableInitialTerminationTimeFault, UnrecognizedPolicyRequestFault, UnsupportedPolicyRequestFault {
		if (true) throw new RuntimeException("Skulle ikke vært her!");
		System.out.println("Heidu!");
		org.oasis_open.docs.wsn.b_2.Subscribe subscribeRequest = new org.oasis_open.docs.wsn.b_2.Subscribe();
		W3CEndpointReference consumerReference = new W3CEndpointReferenceBuilder().address(CONSUMER_URI).build();
		subscribeRequest.setConsumerReference(consumerReference);
		ObjectFactory factory = new ObjectFactory();
		TopicExpressionType topicExpressionType = new TopicExpressionType();
		topicExpressionType.getContent().add("Journalfort");
		topicExpressionType.setDialect(com.ibm.websphere.sib.wsn.jaxb.base.TopicExpressionType.DIALECT_SIMPLE_TOPIC_EXPRESSION);
		JAXBElement<TopicExpressionType> topicExpression = factory.createTopicExpression(topicExpressionType);
		FilterType filter = new FilterType();
		filter.getAny().add(topicExpression);
		subscribeRequest.setFilter(filter);
		NotificationBrokerPortProxy port = new NotificationBrokerPortProxy();
//		((BindingProvider)port._getDescriptor().getProxy()).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://localhost:8080/");
		port.subscribe(subscribeRequest);
	}

}
