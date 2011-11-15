//
// Generated By:JAX-WS RI IBM 2.1.1 in JDK 6 (JAXB RI IBM JAXB 2.1.3 in JDK 1.6)
//


package com.ibm.websphere.wsn.subscription_manager;

import javax.xml.ws.WebFault;
import org.oasis_open.docs.wsrf.rl_2.ResourceNotDestroyedFaultType;

@WebFault(name = "ResourceNotDestroyedFault", targetNamespace = "http://docs.oasis-open.org/wsrf/rl-2")
public class ResourceNotDestroyedFault
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private ResourceNotDestroyedFaultType faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public ResourceNotDestroyedFault(String message, ResourceNotDestroyedFaultType faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param message
     * @param cause
     */
    public ResourceNotDestroyedFault(String message, ResourceNotDestroyedFaultType faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: org.oasis_open.docs.wsrf.rl_2.ResourceNotDestroyedFaultType
     */
    public ResourceNotDestroyedFaultType getFaultInfo() {
        return faultInfo;
    }

}
