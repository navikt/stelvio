//
// Generated By:JAX-WS RI IBM 2.1.1 in JDK 6 (JAXB RI IBM JAXB 2.1.3 in JDK 1.6)
//


package org.oasis_open.docs.wsrf.rl_2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


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
 *         &lt;element name="TerminationTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="TerminationReason" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
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
    "terminationTime",
    "terminationReason"
})
@XmlRootElement(name = "TerminationNotification")
public class TerminationNotification {

    @XmlElement(name = "TerminationTime", required = true, nillable = true)
    protected XMLGregorianCalendar terminationTime;
    @XmlElement(name = "TerminationReason")
    protected Object terminationReason;

    /**
     * Gets the value of the terminationTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTerminationTime() {
        return terminationTime;
    }

    /**
     * Sets the value of the terminationTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTerminationTime(XMLGregorianCalendar value) {
        this.terminationTime = value;
    }

    /**
     * Gets the value of the terminationReason property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getTerminationReason() {
        return terminationReason;
    }

    /**
     * Sets the value of the terminationReason property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setTerminationReason(Object value) {
        this.terminationReason = value;
    }

}
