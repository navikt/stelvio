//
// Generated By:JAX-WS RI IBM 2.1.1 in JDK 6 (JAXB RI IBM JAXB 2.1.3 in JDK 1.6)
//


package com.ibm.websphere.wsn.subscription_manager;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

@WebServiceClient(name = "MetronomenMetronomenSPSM", targetNamespace = "http://www.ibm.com/websphere/wsn/subscription-manager", wsdlLocation = "WEB-INF/wsdl/SubscriptionManager.wsdl")
public class MetronomenMetronomenSPSM
    extends Service
{

    private final static URL METRONOMENMETRONOMENSPSM_WSDL_LOCATION;

    static {
        URL url = null;
        try {
            url = com.ibm.websphere.wsn.subscription_manager.MetronomenMetronomenSPSM.class.getResource("/WEB-INF/wsdl/SubscriptionManager.wsdl");
            if (url == null) throw new MalformedURLException("/WEB-INF/wsdl/SubscriptionManager.wsdl does not exist in the module.");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        METRONOMENMETRONOMENSPSM_WSDL_LOCATION = url;
    }

    public MetronomenMetronomenSPSM(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public MetronomenMetronomenSPSM() {
        super(METRONOMENMETRONOMENSPSM_WSDL_LOCATION, new QName("http://www.ibm.com/websphere/wsn/subscription-manager", "MetronomenMetronomenSPSM"));
    }

    /**
     * 
     * @return
     *     returns SubscriptionManager
     */
    @WebEndpoint(name = "SubscriptionManagerPort")
    public SubscriptionManager getSubscriptionManagerPort() {
        return (SubscriptionManager)super.getPort(new QName("http://www.ibm.com/websphere/wsn/subscription-manager", "SubscriptionManagerPort"), SubscriptionManager.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SubscriptionManager
     */
    @WebEndpoint(name = "SubscriptionManagerPort")
    public SubscriptionManager getSubscriptionManagerPort(WebServiceFeature... features) {
        return (SubscriptionManager)super.getPort(new QName("http://www.ibm.com/websphere/wsn/subscription-manager", "SubscriptionManagerPort"), SubscriptionManager.class, features);
    }

}
