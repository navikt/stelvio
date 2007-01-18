/**
 * 
 */
package no.nav.presentation.pensjon.saksbehandling.stelvio.service.impl;

import no.nav.domain.pensjon.saksbehandling.SaksbehandlerDO;
import no.nav.service.pensjon.saksbehandling.service.SaksbehandlerService;

/**
 * @author person4f9bc5bd17cc
 *
 */
public class SaksbehandlerServiceMock implements SaksbehandlerService {
	private static SaksbehandlerDO ola = new SaksbehandlerDO();
	private static SaksbehandlerDO ole = new SaksbehandlerDO();
	private static SaksbehandlerDO kirsten = new SaksbehandlerDO();
	private static SaksbehandlerDO per = new SaksbehandlerDO();
	
	static {
		ola.setSaksbehandlernr(1l);
		ola.setFornavn( "Ola" );
		ola.setEtternavn( "Normann" );
		ola.setEnhet( "0026 Oslo" );
		
		ole.setSaksbehandlernr(2l);
		ole.setFornavn( "Ole" );
		ole.setEtternavn( "Petter" );
		ole.setEnhet( "4005 person21eaa6ad9a5aanger" );
		
		kirsten.setSaksbehandlernr(3l);
		kirsten.setFornavn( "Kirsten" );
		kirsten.setEtternavn( "person7b6db0c03aa3" );
		kirsten.setEnhet( "4604 Kristiansand" );

		per.setSaksbehandlernr(4l);
		per.setFornavn( "Per" );
		per.setEtternavn( "Karl" );
		per.setEnhet( "4604 Kristiansand" );
	}
	
	/**
	 * TODO: Document me
	 */
	public SaksbehandlerDO readSaksbehandler(Long nr) {
		SaksbehandlerDO saksbehandler = new SaksbehandlerDO();
		
		if (nr == 1) {
			saksbehandler = ola;
		}
		else if (nr == 2) {
			saksbehandler = ole;
		}
		else if (nr == 3) {
			saksbehandler = kirsten;
		}
		else if (nr == 4) {
			saksbehandler = per;
		}
		
		return saksbehandler;
	}

}