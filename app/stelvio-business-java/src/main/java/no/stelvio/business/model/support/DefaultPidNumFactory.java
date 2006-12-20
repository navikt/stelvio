package no.stelvio.business.model.support;

import no.stelvio.business.model.PidNumFactory;
import no.stelvio.business.model.PidNum;
import no.stelvio.business.model.Fodselsnummer;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 */
public class DefaultPidNumFactory implements PidNumFactory {
    public PidNum createPidNum(String pidNum) {
        return new Fodselsnummer(pidNum);
    }
}
