package com.ibm.websphere.wsn.subscription_manager;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Holder;
import org.oasis_open.docs.wsn.b_2.PauseSubscription;
import org.oasis_open.docs.wsn.b_2.PauseSubscriptionResponse;
import org.oasis_open.docs.wsn.b_2.Renew;
import org.oasis_open.docs.wsn.b_2.RenewResponse;
import org.oasis_open.docs.wsn.b_2.ResumeSubscription;
import org.oasis_open.docs.wsn.b_2.ResumeSubscriptionResponse;
import org.oasis_open.docs.wsn.b_2.Unsubscribe;
import org.oasis_open.docs.wsn.b_2.UnsubscribeResponse;
import org.oasis_open.docs.wsrf.rp_2.GetResourcePropertyResponse;

public class SubscriptionManagerPortProxy{

    protected Descriptor _descriptor;

    public class Descriptor {
        private com.ibm.websphere.wsn.subscription_manager.MetronomenMetronomenSPSM _service = null;
        private com.ibm.websphere.wsn.subscription_manager.SubscriptionManager _proxy = null;
        private Dispatch<Source> _dispatch = null;

        public Descriptor() {
            try
            {
                InitialContext ctx = new InitialContext();
                _service = (com.ibm.websphere.wsn.subscription_manager.MetronomenMetronomenSPSM)ctx.lookup("java:comp/env/service/MetronomenMetronomenSPSM");
            }
            catch (NamingException e)
            {
                if ("true".equalsIgnoreCase(System.getProperty("DEBUG_PROXY"))) {
                    System.out.println("NamingException: " + e.getMessage());
                    e.printStackTrace(System.out);
                }
            }

            if (_service == null)
                _service = new com.ibm.websphere.wsn.subscription_manager.MetronomenMetronomenSPSM();
            initCommon();
        }

        public Descriptor(URL wsdlLocation, QName serviceName) {
            _service = new com.ibm.websphere.wsn.subscription_manager.MetronomenMetronomenSPSM(wsdlLocation, serviceName);
            initCommon();
        }

        private void initCommon() {
            _proxy = _service.getSubscriptionManagerPort();
        }

        public com.ibm.websphere.wsn.subscription_manager.SubscriptionManager getProxy() {
            return _proxy;
        }

        public Dispatch<Source> getDispatch() {
            if (_dispatch == null ) {
                QName portQName = new QName("http://www.ibm.com/websphere/wsn/subscription-manager", "SubscriptionManagerPort");
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

    public SubscriptionManagerPortProxy() {
        _descriptor = new Descriptor();
    }

    public SubscriptionManagerPortProxy(URL wsdlLocation, QName serviceName) {
        _descriptor = new Descriptor(wsdlLocation, serviceName);
    }

    public Descriptor _getDescriptor() {
        return _descriptor;
    }

    public GetResourcePropertyResponse getResourceProperty(QName getResourcePropertyRequest) throws InvalidResourcePropertyQNameFault, ResourceUnavailableFault, ResourceUnknownFault {
        return _getDescriptor().getProxy().getResourceProperty(getResourcePropertyRequest);
    }

    public RenewResponse renew(Renew renewRequest) throws ResourceUnknownFault, UnacceptableTerminationTimeFault {
        return _getDescriptor().getProxy().renew(renewRequest);
    }

    public UnsubscribeResponse unsubscribe(Unsubscribe unsubscribeRequest) throws ResourceUnknownFault, UnableToDestroySubscriptionFault {
        return _getDescriptor().getProxy().unsubscribe(unsubscribeRequest);
    }

    public PauseSubscriptionResponse pauseSubscription(PauseSubscription pauseSubscriptionRequest) throws PauseFailedFault, ResourceUnknownFault {
        return _getDescriptor().getProxy().pauseSubscription(pauseSubscriptionRequest);
    }

    public ResumeSubscriptionResponse resumeSubscription(ResumeSubscription resumeSubscriptionRequest) throws ResourceUnknownFault, ResumeFailedFault {
        return _getDescriptor().getProxy().resumeSubscription(resumeSubscriptionRequest);
    }

    public void destroy() throws ResourceNotDestroyedFault, ResourceUnavailableFault, ResourceUnknownFault {
        _getDescriptor().getProxy().destroy();
    }

    public void setTerminationTime(XMLGregorianCalendar requestedTerminationTime, Duration requestedLifetimeDuration, Holder<XMLGregorianCalendar> newTerminationTime, Holder<XMLGregorianCalendar> currentTime) throws ResourceUnavailableFault, ResourceUnknownFault, TerminationTimeChangeRejectedFault, UnableToSetTerminationTimeFault {
        _getDescriptor().getProxy().setTerminationTime(requestedTerminationTime,requestedLifetimeDuration,newTerminationTime,currentTime);
    }

}