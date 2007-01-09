/**
 * 
 */
package no.nav.service.pensjon.person.impl;

import java.util.ArrayList;

import no.nav.domain.pensjon.person.Brukerprofil;
import no.nav.service.pensjon.person.BrukerprofilService;

/** 
 * @author persond3cb2ee15f42
 */
public class BrukerprofilServiceMock implements BrukerprofilService {
	private static Brukerprofil ola = new Brukerprofil( "12345678901" );
	private static Brukerprofil ole = new Brukerprofil( "22222222222" );
	private static Brukerprofil kirsten = new Brukerprofil( "33333333333" );
	private static ArrayList<Brukerprofil> mockProfiles;
	
	static {
		ola.setSpesielleBehovKode( "kaffe" );
		ola.setTilrettelagtKommunikasjon( "skriftlig" );

		ole.setSpesielleBehovKode( "Te"  );
		ole.setTilrettelagtKommunikasjon( "muntlig" );

		kirsten.setSpesielleBehovKode( "nei"  );
	
		mockProfiles = new ArrayList<Brukerprofil>();
		mockProfiles.add(ola);
		mockProfiles.add(ole);
		mockProfiles.add(kirsten);
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see no.nav.service.pensjon.person.BrukerprofilService#hentBrukerprofil(java.lang.String)
	 */
	public Brukerprofil hentBrukerprofil( String fodselsnummer )
		throws Exception
	{
		for( int i = 0 ; i < mockProfiles.size() ; i++ )
		{
			if( mockProfiles.get( i ).getFodselsnummer().equals( fodselsnummer ) )
			{
				return mockProfiles.get( i );
			}
		}
		throw new Exception();
	}
	
}