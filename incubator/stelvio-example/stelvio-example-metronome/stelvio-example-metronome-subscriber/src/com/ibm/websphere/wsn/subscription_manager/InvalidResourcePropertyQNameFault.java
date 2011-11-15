//
// Generated By:JAX-WS RI IBM 2.1.1 in JDK 6 (JAXB RI IBM JAXB 2.1.3 in JDK 1.6)
//


package com.ibm.websphere.wsn.subscription_manager;

import javax.xml.ws.WebFault;
import org.oasis_open.docs.wsrf.rp_2.InvalidResourcePropertyQNameFaultType;

@WebFault(name = "InvalidResourcePropertyQNameFault", targetNamespace = "http://docs.oasis-open.org/wsrf/rp-2")
public class InvalidResourcePropertyQNameFault
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private InvalidResourcePropertyQNameFaultType faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public InvalidResourcePropertyQNameFault(String message, InvalidResourcePropertyQNameFaultType faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param message
     * @param cause
     */
    public InvalidResourcePropertyQNameFault(String message, InvalidResourcePropertyQNameFaultType faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: org.oasis_open.docs.wsrf.rp_2.InvalidResourcePropertyQNameFaultType
     */
    public InvalidResourcePropertyQNameFaultType getFaultInfo() {
        return faultInfo;
    }

}
