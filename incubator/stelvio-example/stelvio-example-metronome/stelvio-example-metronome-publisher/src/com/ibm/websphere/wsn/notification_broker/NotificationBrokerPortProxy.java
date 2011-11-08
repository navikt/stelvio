package com.ibm.websphere.wsn.notification_broker;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import org.oasis_open.docs.wsn.b_2.CreatePullPoint;
import org.oasis_open.docs.wsn.b_2.CreatePullPointResponse;
import org.oasis_open.docs.wsn.b_2.DestroyPullPoint;
import org.oasis_open.docs.wsn.b_2.DestroyPullPointResponse;
import org.oasis_open.docs.wsn.b_2.GetCurrentMessage;
import org.oasis_open.docs.wsn.b_2.GetCurrentMessageResponse;
import org.oasis_open.docs.wsn.b_2.GetMessages;
import org.oasis_open.docs.wsn.b_2.GetMessagesResponse;
import org.oasis_open.docs.wsn.b_2.Notify;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import org.oasis_open.docs.wsn.br_2.RegisterPublisher;
import org.oasis_open.docs.wsn.br_2.RegisterPublisherResponse;
import org.oasis_open.docs.wsrf.rp_2.GetResourcePropertyResponse;

public class NotificationBrokerPortProxy{

    protected Descriptor _descriptor;

    public class Descriptor {
        private com.ibm.websphere.wsn.notification_broker.MetronomenMetronomenSPNB _service = null;
        private com.ibm.websphere.wsn.notification_broker.NotificationBroker _proxy = null;
        private Dispatch<Source> _dispatch = null;

        public Descriptor() {
            try
            {
                InitialContext ctx = new InitialContext();
                _service = (com.ibm.websphere.wsn.notification_broker.MetronomenMetronomenSPNB)ctx.lookup("java:comp/env/service/MetronomenMetronomenSPNB");
            }
            catch (NamingException e)
            {
                if ("true".equalsIgnoreCase(System.getProperty("DEBUG_PROXY"))) {
                    System.out.println("NamingException: " + e.getMessage());
                    e.printStackTrace(System.out);
                }
            }

            if (_service == null)
                _service = new com.ibm.websphere.wsn.notification_broker.MetronomenMetronomenSPNB();
            initCommon();
        }

        public Descriptor(URL wsdlLocation, QName serviceName) {
            _service = new com.ibm.websphere.wsn.notification_broker.MetronomenMetronomenSPNB(wsdlLocation, serviceName);
            initCommon();
        }

        private void initCommon() {
            _proxy = _service.getNotificationBrokerPort();
        }

        public com.ibm.websphere.wsn.notification_broker.NotificationBroker getProxy() {
            return _proxy;
        }

        public Dispatch<Source> getDispatch() {
            if (_dispatch == null ) {
                QName portQName = new QName("http://www.ibm.com/websphere/wsn/notification-broker", "NotificationBrokerPort");
                _dispatch = _service.createDispatch(portQName, Source.class, Service.Mode.MESSAGE);

                String proxyEndpointUrl = getEndpoint();
                BindingProvider bp = (BindingProvider) _dispatch;
                String dispatchEndpointUrl = (String) bp.getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
                if (!dispatchEndpointUrl.equals(proxyEndpointUrl))
                    bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, proxyEndpointUrl);
            }
            return _dispatch;
        }

        public String getEndpoint() {
            BindingProvider bp = (BindingProvider) _proxy;
            return (String) bp.getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
        }

        public void setEndpoint(String endpointUrl) {
            BindingProvider bp = (BindingProvider) _proxy;
            bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointUrl);

            if (_dispatch != null ) {
                bp = (BindingProvider) _dispatch;
                bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointUrl);
            }
        }
    }

    public NotificationBrokerPortProxy() {
        _descriptor = new Descriptor();
    }

    public NotificationBrokerPortProxy(URL wsdlLocation, QName serviceName) {
        _descriptor = new Descriptor(wsdlLocation, serviceName);
    }

    public Descriptor _getDescriptor() {
        return _descriptor;
    }

    public GetResourcePropertyResponse getResourceProperty(QName getResourcePropertyRequest) throws InvalidResourcePropertyQNameFault, ResourceUnavailableFault, ResourceUnknownFault {
        return _getDescriptor().getProxy().getResourceProperty(getResourcePropertyRequest);
    }

    public void notify(Notify notify) {
        _getDescriptor().getProxy().notify(notify);
    }

    public SubscribeResponse subscribe(Subscribe subscribeRequest) throws InvalidFilterFault, InvalidMessageContentExpressionFault, InvalidProducerPropertiesExpressionFault, InvalidTopicExpressionFault, NotifyMessageNotSupportedFault, ResourceUnknownFault, SubscribeCreationFailedFault, TopicExpressionDialectUnknownFault, TopicNotSupportedFault, UnacceptableInitialTerminationTimeFault, UnrecognizedPolicyRequestFault, UnsupportedPolicyRequestFault {
        return _getDescriptor().getProxy().subscribe(subscribeRequest);
    }

    public GetCurrentMessageResponse getCurrentMessage(GetCurrentMessage getCurrentMessageRequest) throws InvalidTopicExpressionFault, MultipleTopicsSpecifiedFault, NoCurrentMessageOnTopicFault, ResourceUnknownFault, TopicExpressionDialectUnknownFault, TopicNotSupportedFault {
        return _getDescriptor().getProxy().getCurrentMessage(getCurrentMessageRequest);
    }

    public RegisterPublisherResponse registerPublisher(RegisterPublisher registerPublisherRequest) throws InvalidTopicExpressionFault, PublisherRegistrationFailedFault, PublisherRegistrationRejectedFault, ResourceUnknownFault, TopicNotSupportedFault, UnacceptableInitialTerminationTimeFault {
        return _getDescriptor().getProxy().registerPublisher(registerPublisherRequest);
    }

    public CreatePullPointResponse createPullPoint(CreatePullPoint createPullPointRequest) throws UnableToCreatePullPointFault {
        return _getDescriptor().getProxy().createPullPoint(createPullPointRequest);
    }

    public GetMessagesResponse getMessages(GetMessages getMessagesRequest) throws ResourceUnknownFault, UnableToGetMessagesFault {
        return _getDescriptor().getProxy().getMessages(getMessagesRequest);
    }

    public DestroyPullPointResponse destroyPullPoint(DestroyPullPoint destroyPullPointRequest) throws ResourceUnknownFault, UnableToDestroyPullPointFault {
        return _getDescriptor().getProxy().destroyPullPoint(destroyPullPointRequest);
    }

}