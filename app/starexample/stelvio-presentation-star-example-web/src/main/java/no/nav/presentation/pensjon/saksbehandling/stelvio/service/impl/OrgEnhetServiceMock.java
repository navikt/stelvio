package no.nav.presentation.pensjon.saksbehandling.stelvio.service.impl;

import no.nav.presentation.pensjon.saksbehandling.stelvio.exceptions.DatabaseNotFoundException;
import no.nav.presentation.pensjon.saksbehandling.stelvio.service.OrgEnhetService;

/**
 * 
 * @author personb66fa0b5ff6e
 *
 */
public class OrgEnhetServiceMock implements OrgEnhetService{

	public String getOrgEnhetNavn(String regenhetnr)throws DatabaseNotFoundException{
		
		String enhetnavn = "";
						
		if(regenhetnr.equals("0026")){
			enhetnavn= "0026 Oslo";
		}
		else if(regenhetnr.equals("4005")){
			enhetnavn= "4005 person21eaa6ad9a5aanger";
		}
		else if(regenhetnr.equals("4604")){
			enhetnavn= "4604 Kristiansand";
		}
		
		return enhetnavn;
	}
}
