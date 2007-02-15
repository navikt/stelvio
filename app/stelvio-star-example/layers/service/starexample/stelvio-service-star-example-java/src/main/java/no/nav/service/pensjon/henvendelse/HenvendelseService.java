package no.nav.service.pensjon.henvendelse;

import java.util.List;

import no.nav.service.pensjon.henvendelse.to.HenvendelseStatistikkRequest;
import no.nav.service.pensjon.henvendelse.to.HenvendelseStatistikkResponse;
import no.stelvio.star.example.henvendelse.Henvendelse;
import no.stelvio.star.example.person.Fodselsnummer;


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
