//
// Generated By:JAX-WS RI IBM 2.1.1 in JDK 6 (JAXB RI IBM JAXB 2.1.3 in JDK 1.6)
//


package no.nav.provider.pensjon.sak.ejb.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "hentSakListeResponse", namespace = "http://ejb/sak/pensjon/provider/nav/no")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "hentSakListeResponse", namespace = "http://ejb/sak/pensjon/provider/nav/no")
public class HentSakListeResponse {

    @XmlElement(name = "return", namespace = "")
    private no.nav.dto.pensjon.sak.to.HentSakListeResponseDto _return;

    /**
     * 
     * @return
     *     returns HentSakListeResponseDto
     */
    public no.nav.dto.pensjon.sak.to.HentSakListeResponseDto getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(no.nav.dto.pensjon.sak.to.HentSakListeResponseDto _return) {
        this._return = _return;
    }

}
