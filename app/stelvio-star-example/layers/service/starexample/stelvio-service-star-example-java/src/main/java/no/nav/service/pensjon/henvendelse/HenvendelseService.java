package no.nav.service.pensjon.henvendelse;

import java.util.List;

import no.nav.domain.pensjon.henvendelse.Henvendelse;
import no.nav.domain.pensjon.person.Fodselsnummer;
import no.nav.service.pensjon.henvendelse.to.HenvendelseStatistikkRequest;
import no.nav.service.pensjon.henvendelse.to.HenvendelseStatistikkResponse;


/**
 * Defines an interface for accessing data and functionality related to henvendelse. 
 * 
 * @author personff564022aedd
 */
public interface HenvendelseService {

	/**
	 * @return Response object containing HenvendelseStatistikk
	 */
	HenvendelseStatistikkResponse genererHenvendelseStatistikk(HenvendelseStatistikkRequest henvendelseStatistikkRequest);

	List<Henvendelse> hentHenvendelseList(Fodselsnummer fodselsnummer) throws Exception;
}
