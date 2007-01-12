package no.nav.service.pensjon.henvendelse;

import java.util.List;

import no.nav.domain.pensjon.henvendelse.Henvendelse;
import no.nav.domain.pensjon.henvendelse.HenvendelseStatistikk;
import no.nav.domain.pensjon.person.Fodselsnummer;
import no.nav.service.pensjon.henvendelse.to.HenvendelseStatistikkRequest;
import no.stelvio.common.transferobject.EntityResponse;


/**
 * Defines an interface for accessing data and functionality related to henvendelse. 
 * 
 * @author personff564022aedd
 */
public interface HenvendelseService {

	/**
	 * @return Response object containing HenvendelseStatistikk
	 */
	EntityResponse<HenvendelseStatistikk> genererHenvendelseStatistikk(HenvendelseStatistikkRequest henvendelseStatistikkRequest);
	
	
	public List<Henvendelse> hentHenvendelseList(Fodselsnummer fodselsnummer) throws Exception;
}
