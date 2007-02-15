package no.stelvio.consumer.star.example.henvendelse;

import java.util.List;

import no.nav.domain.pensjon.henvendelse.Henvendelse;
import no.nav.domain.pensjon.person.Fodselsnummer;
import no.stelvio.consumer.star.example.henvendelse.to.SokHenvendelseRequest;
import no.stelvio.consumer.star.example.henvendelse.to.SokHenvendelseResponse;

/**
 * Defines an interface for accessing data and functionality related to henvendelse.
 *
 * @author personff564022aedd
 */
public interface SokHenvendelse {

	/**
	 * @return Response object containing HenvendelseStatistikk
	 */
	SokHenvendelseResponse genererHenvendelseStatistikk(SokHenvendelseRequest sokHenvendelseRequest);

	List<Henvendelse> hentHenvendelseList(Fodselsnummer fodselsnummer) throws Exception;
}
