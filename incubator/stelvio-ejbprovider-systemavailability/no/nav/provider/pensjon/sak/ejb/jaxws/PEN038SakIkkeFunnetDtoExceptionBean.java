//
// Generated By:JAX-WS RI IBM 2.1.1 in JDK 6 (JAXB RI IBM JAXB 2.1.3 in JDK 1.6)
//


package no.nav.provider.pensjon.sak.ejb.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI IBM 2.1.1 in JDK 6
 * Generated source version: 2.1.1
 * 
 */
@XmlRootElement(name = "PEN038SakIkkeFunnetDtoException", namespace = "http://ejb/sak/pensjon/provider/nav/no")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PEN038SakIkkeFunnetDtoException", namespace = "http://ejb/sak/pensjon/provider/nav/no", propOrder = {
    "errorId",
    "message"
})
public class PEN038SakIkkeFunnetDtoExceptionBean {

    private long errorId;
    private String message;

    /**
     * 
     * @return
     *     returns long
     */
    public long getErrorId() {
        return this.errorId;
    }

    /**
     * 
     * @param errorId
     *     the value for the errorId property
     */
    public void setErrorId(long errorId) {
        this.errorId = errorId;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * 
     * @param message
     *     the value for the message property
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
