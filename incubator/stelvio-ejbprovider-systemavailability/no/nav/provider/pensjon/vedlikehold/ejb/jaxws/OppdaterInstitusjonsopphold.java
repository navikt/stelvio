//
// Generated By:JAX-WS RI IBM 2.1.1 in JDK 6 (JAXB RI IBM JAXB 2.1.3 in JDK 1.6)
//


package no.nav.provider.pensjon.vedlikehold.ejb.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "oppdaterInstitusjonsopphold", namespace = "http://ejb/vedlikehold/pensjon/provider/nav/no")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "oppdaterInstitusjonsopphold", namespace = "http://ejb/vedlikehold/pensjon/provider/nav/no")
public class OppdaterInstitusjonsopphold {

    @XmlElement(name = "arg0", namespace = "")
    private no.nav.dto.pensjon.vedlikehold.to.OppdaterInstitusjonsoppholdRequestDto arg0;

    /**
     * 
     * @return
     *     returns OppdaterInstitusjonsoppholdRequestDto
     */
    public no.nav.dto.pensjon.vedlikehold.to.OppdaterInstitusjonsoppholdRequestDto getArg0() {
        return this.arg0;
    }

    /**
     * 
     * @param arg0
     *     the value for the arg0 property
     */
    public void setArg0(no.nav.dto.pensjon.vedlikehold.to.OppdaterInstitusjonsoppholdRequestDto arg0) {
        this.arg0 = arg0;
    }

}
