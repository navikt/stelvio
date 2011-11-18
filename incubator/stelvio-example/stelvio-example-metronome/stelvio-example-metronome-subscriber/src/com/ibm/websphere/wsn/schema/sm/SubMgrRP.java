//
// Generated By:JAX-WS RI IBM 2.1.1 in JDK 6 (JAXB RI IBM JAXB 2.1.3 in JDK 1.6)
//


package com.ibm.websphere.wsn.schema.sm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import org.oasis_open.docs.wsn.b_2.FilterType;
import org.oasis_open.docs.wsn.b_2.SubscriptionPolicyType;
import org.oasis_open.docs.wsrf.rl_2.CurrentTime;
import org.oasis_open.docs.wsrf.rl_2.TerminationTime;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://docs.oasis-open.org/wsrf/rl-2}CurrentTime"/>
 *         &lt;element ref="{http://docs.oasis-open.org/wsrf/rl-2}TerminationTime"/>
 *         &lt;element ref="{http://docs.oasis-open.org/wsn/b-2}ConsumerReference"/>
 *         &lt;element ref="{http://docs.oasis-open.org/wsn/b-2}Filter" minOccurs="0"/>
 *         &lt;element ref="{http://docs.oasis-open.org/wsn/b-2}SubscriptionPolicy" minOccurs="0"/>
 *         &lt;element ref="{http://docs.oasis-open.org/wsn/b-2}CreationTime" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "currentTime",
    "terminationTime",
    "consumerReference",
    "filter",
    "subscriptionPolicy",
    "creationTime"
})
@XmlRootElement(name = "SubMgrRP")
public class SubMgrRP {

    @XmlElement(name = "CurrentTime", namespace = "http://docs.oasis-open.org/wsrf/rl-2", required = true)
    protected CurrentTime currentTime;
    @XmlElement(name = "TerminationTime", namespace = "http://docs.oasis-open.org/wsrf/rl-2", required = true, nillable = true)
    protected TerminationTime terminationTime;
    @XmlElement(name = "ConsumerReference", namespace = "http://docs.oasis-open.org/wsn/b-2", required = true)
    protected W3CEndpointReference consumerReference;
    @XmlElement(name = "Filter", namespace = "http://docs.oasis-open.org/wsn/b-2")
    protected FilterType filter;
    @XmlElement(name = "SubscriptionPolicy", namespace = "http://docs.oasis-open.org/wsn/b-2")
    protected SubscriptionPolicyType subscriptionPolicy;
    @XmlElement(name = "CreationTime", namespace = "http://docs.oasis-open.org/wsn/b-2")
    protected XMLGregorianCalendar creationTime;

    /**
     * Gets the value of the currentTime property.
     * 
     * @return
     *     possible object is
     *     {@link CurrentTime }
     *     
     */
    public CurrentTime getCurrentTime() {
        return currentTime;
    }

    /**
     * Sets the value of the currentTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link CurrentTime }
     *     
     */
    public void setCurrentTime(CurrentTime value) {
        this.currentTime = value;
    }

    /**
     * Gets the value of the terminationTime property.
     * 
     * @return
     *     possible object is
     *     {@link TerminationTime }
     *     
     */
    public TerminationTime getTerminationTime() {
        return terminationTime;
    }

    /**
     * Sets the value of the terminationTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link TerminationTime }
     *     
     */
    public void setTerminationTime(TerminationTime value) {
        this.terminationTime = value;
    }

    /**
     * Gets the value of the consumerReference property.
     * 
     * @return
     *     possible object is
     *     {@link W3CEndpointReference }
     *     
     */
    public W3CEndpointReference getConsumerReference() {
        return consumerReference;
    }

    /**
     * Sets the value of the consumerReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link W3CEndpointReference }
     *     
     */
    public void setConsumerReference(W3CEndpointReference value) {
        this.consumerReference = value;
    }

    /**
     * Gets the value of the filter property.
     * 
     * @return
     *     possible object is
     *     {@link FilterType }
     *     
     */
    public FilterType getFilter() {
        return filter;
    }

    /**
     * Sets the value of the filter property.
     * 
     * @param value
     *     allowed object is
     *     {@link FilterType }
     *     
     */
    public void setFilter(FilterType value) {
        this.filter = value;
    }

    /**
     * Gets the value of the subscriptionPolicy property.
     * 
     * @return
     *     possible object is
     *     {@link SubscriptionPolicyType }
     *     
     */
    public SubscriptionPolicyType getSubscriptionPolicy() {
        return subscriptionPolicy;
    }

    /**
     * Sets the value of the subscriptionPolicy property.
     * 
     * @param value
     *     allowed object is
     *     {@link SubscriptionPolicyType }
     *     
     */
    public void setSubscriptionPolicy(SubscriptionPolicyType value) {
        this.subscriptionPolicy = value;
    }

    /**
     * Gets the value of the creationTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreationTime() {
        return creationTime;
    }

    /**
     * Sets the value of the creationTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreationTime(XMLGregorianCalendar value) {
        this.creationTime = value;
    }

}