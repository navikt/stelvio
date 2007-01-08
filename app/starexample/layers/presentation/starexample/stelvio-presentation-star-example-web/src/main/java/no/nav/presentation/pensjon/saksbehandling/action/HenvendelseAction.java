package no.nav.presentation.pensjon.saksbehandling.action;

import no.nav.domain.pensjon.person.Fodselsnummer;
import no.nav.presentation.pensjon.saksbehandling.stelvio.service.HenvendelseService;
import no.nav.presentation.pensjon.saksbehandling.util.PagedSortableList;

import no.nav.domain.pensjon.henvendelse.Henvendelse;


public class HenvendelseAction {
	
	private HenvendelseService henvendelsesService;
	
	
	public Henvendelse getHenvendelse()
	{
		Henvendelse henvendelse = null;
		
		return henvendelse;
	}
	
	public void updateHenvendelse( Henvendelse henvendelse )
	{
		henvendelsesService.updateHenvendelse(henvendelse);
	}
	
	public Henvendelse createHenvendelse( Henvendelse henvendelse )
	{
		return henvendelsesService.createHenvendelse(henvendelse);
	}
	
	

	public PagedSortableList<Henvendelse> hentHenvendelser( Fodselsnummer fodselsnummer )
	{
		PagedSortableList<Henvendelse> sortableList = new PagedSortableList<Henvendelse>(
							henvendelsesService.readHenvendelseList(fodselsnummer), 10, "fagomrode" );
		
		return sortableList;
	}


	/**
	 * @param henvendelsesService the henvendelsesService to set
	 */
	public void setHenvendelsesService(HenvendelseService henvendelsesService) {
		this.henvendelsesService = henvendelsesService;
	}
	
	
	
	
	
}
