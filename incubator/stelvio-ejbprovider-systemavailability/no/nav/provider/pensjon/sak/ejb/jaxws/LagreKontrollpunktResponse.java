//
// Generated By:JAX-WS RI IBM 2.1.1 in JDK 6 (JAXB RI IBM JAXB 2.1.3 in JDK 1.6)
//


package no.nav.provider.pensjon.sak.ejb.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "lagreKontrollpunktResponse", namespace = "http://ejb/sak/pensjon/provider/nav/no")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lagreKontrollpunktResponse", namespace = "http://ejb/sak/pensjon/provider/nav/no")
public class LagreKontrollpunktResponse {

    @XmlElement(name = "return", namespace = "")
    private no.nav.dto.pensjon.sak.to.LagreKontrollpunktResponseDto _return;

    /**
     * 
     * @return
     *     returns LagreKontrollpunktResponseDto
     */
    public no.nav.dto.pensjon.sak.to.LagreKontrollpunktResponseDto getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(no.nav.dto.pensjon.sak.to.LagreKontrollpunktResponseDto _return) {
        this._return = _return;
    }

}