//
// Generated By:JAX-WS RI IBM 2.1.1 in JDK 6 (JAXB RI IBM JAXB 2.1.3 in JDK 1.6)
//


package com.ibm.websphere.wsn.subscription_manager;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.ws.Holder;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import org.oasis_open.docs.wsn.b_2.PauseSubscription;
import org.oasis_open.docs.wsn.b_2.PauseSubscriptionResponse;
import org.oasis_open.docs.wsn.b_2.Renew;
import org.oasis_open.docs.wsn.b_2.RenewResponse;
import org.oasis_open.docs.wsn.b_2.ResumeSubscription;
import org.oasis_open.docs.wsn.b_2.ResumeSubscriptionResponse;
import org.oasis_open.docs.wsn.b_2.Unsubscribe;
import org.oasis_open.docs.wsn.b_2.UnsubscribeResponse;
import org.oasis_open.docs.wsrf.rp_2.GetResourcePropertyResponse;

@WebService(name = "SubscriptionManager", targetNamespace = "http://www.ibm.com/websphere/wsn/subscription-manager")
@XmlSeeAlso({
    org.oasis_open.docs.wsrf.rp_2.ObjectFactory.class,
    org.oasis_open.docs.wsrf.r_2.ObjectFactory.class,
    org.oasis_open.docs.wsn.b_2.ObjectFactory.class,
    com.ibm.websphere.wsn.schema.sm.ObjectFactory.class,
    org.oasis_open.docs.wsrf.rl_2.ObjectFactory.class,
    org.oasis_open.docs.wsrf.bf_2.ObjectFactory.class,
    org.oasis_open.docs.wsn.t_1.ObjectFactory.class
})
public interface SubscriptionManager {


    /**
     * 
     * @param getResourcePropertyRequest
     * @return
     *     returns org.oasis_open.docs.wsrf.rp_2.GetResourcePropertyResponse
     * @throws ResourceUnavailableFault
     * @throws ResourceUnknownFault
     * @throws InvalidResourcePropertyQNameFault
     */
    @WebMethod(operationName = "GetResourceProperty")
    @WebResult(name = "GetResourcePropertyResponse", targetNamespace = "http://docs.oasis-open.org/wsrf/rp-2", partName = "GetResourcePropertyResponse")
    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    public GetResourcePropertyResponse getResourceProperty(
        @WebParam(name = "GetResourceProperty", targetNamespace = "http://docs.oasis-open.org/wsrf/rp-2", partName = "GetResourcePropertyRequest")
        QName getResourcePropertyRequest)
        throws InvalidResourcePropertyQNameFault, ResourceUnavailableFault, ResourceUnknownFault
    ;

    /**
     * 
     * @param renewRequest
     * @return
     *     returns org.oasis_open.docs.wsn.b_2.RenewResponse
     * @throws ResourceUnknownFault
     * @throws UnacceptableTerminationTimeFault
     */
    @WebMethod(operationName = "Renew")
    @WebResult(name = "RenewResponse", targetNamespace = "http://docs.oasis-open.org/wsn/b-2", partName = "RenewResponse")
    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    public RenewResponse renew(
        @WebParam(name = "Renew", targetNamespace = "http://docs.oasis-open.org/wsn/b-2", partName = "RenewRequest")
        Renew renewRequest)
        throws ResourceUnknownFault, UnacceptableTerminationTimeFault
    ;

    /**
     * 
     * @param unsubscribeRequest
     * @return
     *     returns org.oasis_open.docs.wsn.b_2.UnsubscribeResponse
     * @throws ResourceUnknownFault
     * @throws UnableToDestroySubscriptionFault
     */
    @WebMethod(operationName = "Unsubscribe")
    @WebResult(name = "UnsubscribeResponse", targetNamespace = "http://docs.oasis-open.org/wsn/b-2", partName = "UnsubscribeResponse")
    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    public UnsubscribeResponse unsubscribe(
        @WebParam(name = "Unsubscribe", targetNamespace = "http://docs.oasis-open.org/wsn/b-2", partName = "UnsubscribeRequest")
        Unsubscribe unsubscribeRequest)
        throws ResourceUnknownFault, UnableToDestroySubscriptionFault
    ;

    /**
     * 
     * @param pauseSubscriptionRequest
     * @return
     *     returns org.oasis_open.docs.wsn.b_2.PauseSubscriptionResponse
     * @throws ResourceUnknownFault
     * @throws PauseFailedFault
     */
    @WebMethod(operationName = "PauseSubscription")
    @WebResult(name = "PauseSubscriptionResponse", targetNamespace = "http://docs.oasis-open.org/wsn/b-2", partName = "PauseSubscriptionResponse")
    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    public PauseSubscriptionResponse pauseSubscription(
        @WebParam(name = "PauseSubscription", targetNamespace = "http://docs.oasis-open.org/wsn/b-2", partName = "PauseSubscriptionRequest")
        PauseSubscription pauseSubscriptionRequest)
        throws PauseFailedFault, ResourceUnknownFault
    ;

    /**
     * 
     * @param resumeSubscriptionRequest
     * @return
     *     returns org.oasis_open.docs.wsn.b_2.ResumeSubscriptionResponse
     * @throws ResourceUnknownFault
     * @throws ResumeFailedFault
     */
    @WebMethod(operationName = "ResumeSubscription")
    @WebResult(name = "ResumeSubscriptionResponse", targetNamespace = "http://docs.oasis-open.org/wsn/b-2", partName = "ResumeSubscriptionResponse")
    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    public ResumeSubscriptionResponse resumeSubscription(
        @WebParam(name = "ResumeSubscription", targetNamespace = "http://docs.oasis-open.org/wsn/b-2", partName = "ResumeSubscriptionRequest")
        ResumeSubscription resumeSubscriptionRequest)
        throws ResourceUnknownFault, ResumeFailedFault
    ;

    /**
     * 
     * @throws ResourceNotDestroyedFault
     * @throws ResourceUnavailableFault
     * @throws ResourceUnknownFault
     */
    @WebMethod(operationName = "Destroy")
    @RequestWrapper(localName = "Destroy", targetNamespace = "http://docs.oasis-open.org/wsrf/rl-2", className = "org.oasis_open.docs.wsrf.rl_2.Destroy")
    @ResponseWrapper(localName = "DestroyResponse", targetNamespace = "http://docs.oasis-open.org/wsrf/rl-2", className = "org.oasis_open.docs.wsrf.rl_2.DestroyResponse")
    public void destroy()
        throws ResourceNotDestroyedFault, ResourceUnavailableFault, ResourceUnknownFault
    ;

    /**
     * 
     * @param newTerminationTime
     * @param requestedTerminationTime
     * @param currentTime
     * @param requestedLifetimeDuration
     * @throws ResourceUnavailableFault
     * @throws ResourceUnknownFault
     * @throws TerminationTimeChangeRejectedFault
     * @throws UnableToSetTerminationTimeFault
     */
    @WebMethod(operationName = "SetTerminationTime")
    @RequestWrapper(localName = "SetTerminationTime", targetNamespace = "http://docs.oasis-open.org/wsrf/rl-2", className = "org.oasis_open.docs.wsrf.rl_2.SetTerminationTime")
    @ResponseWrapper(localName = "SetTerminationTimeResponse", targetNamespace = "http://docs.oasis-open.org/wsrf/rl-2", className = "org.oasis_open.docs.wsrf.rl_2.SetTerminationTimeResponse")
    public void setTerminationTime(
        @WebParam(name = "RequestedTerminationTime", targetNamespace = "http://docs.oasis-open.org/wsrf/rl-2")
        XMLGregorianCalendar requestedTerminationTime,
        @WebParam(name = "RequestedLifetimeDuration", targetNamespace = "http://docs.oasis-open.org/wsrf/rl-2")
        Duration requestedLifetimeDuration,
        @WebParam(name = "NewTerminationTime", targetNamespace = "http://docs.oasis-open.org/wsrf/rl-2", mode = WebParam.Mode.OUT)
        Holder<XMLGregorianCalendar> newTerminationTime,
        @WebParam(name = "CurrentTime", targetNamespace = "http://docs.oasis-open.org/wsrf/rl-2", mode = WebParam.Mode.OUT)
        Holder<XMLGregorianCalendar> currentTime)
        throws ResourceUnavailableFault, ResourceUnknownFault, TerminationTimeChangeRejectedFault, UnableToSetTerminationTimeFault
    ;

}
