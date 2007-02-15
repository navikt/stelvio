package no.stelvio.consumer.star.example.henvendelse.support;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import no.nav.domain.pensjon.henvendelse.Henvendelse;
import no.nav.domain.pensjon.henvendelse.HenvendelseStatistikk;
import no.nav.domain.pensjon.henvendelse.HenvendelseStatistikkCriteria;
import no.nav.domain.pensjon.person.Fodselsnummer;
import no.stelvio.consumer.star.example.henvendelse.HenvendelseService;
import no.stelvio.consumer.star.example.henvendelse.to.HenvendelseStatistikkRequest;
import no.stelvio.consumer.star.example.henvendelse.to.HenvendelseStatistikkResponse;

/**
 * Mock impl av HenvendelseService
 *
 * @author personff564022aedd
 */
public class SimpleHenvendelseService implements HenvendelseService {

	private static List<Henvendelse> ola = new ArrayList<Henvendelse>();
	private static List<Henvendelse> ole = new ArrayList<Henvendelse>();
	private static List<Henvendelse> kirsten = new ArrayList<Henvendelse>();
	private static ArrayList<List<Henvendelse>> mockLists;

	static {
		Henvendelse h1 = new Henvendelse( "12345678901" );
		h1.setFagomrKode( "Pensjon" );
		h1.setOpprettetDato( Calendar.getInstance() );
		h1.setOpprettetEnhet( "0129 Oslo" );
		h1.setSakId( "974816723" );
		h1.setHenvtype( "Regelverk" );
		h1.setStonadstype( "Alderspensjon" );

		SimpleHenvendelseService.ola.add( h1 );

		Henvendelse h2 = new Henvendelse( "12345678901" );
		h2.setFagomrKode( "Bidrag" );
		h2.setOpprettetDato( Calendar.getInstance() );
		h2.setOpprettetEnhet( "1357 Bergen" );
		h2.setSakId( "923665" );
		h2.setHenvtype( "Skjema" );
		h2.setStonadstype( "Uførepensjon" );
		SimpleHenvendelseService.ola.add( h2 );

		SimpleHenvendelseService.addHenvendelser(SimpleHenvendelseService.ola,1000, "12345678901");

		Henvendelse h3 = new Henvendelse( "22222222222" );
		h3.setFagomrKode( "Bidrag" );
		h3.setOpprettetDato( Calendar.getInstance() );
		h3.setOpprettetEnhet( "1357 Bergen" );
		h3.setSakId( "923665" );
		h3.setHenvtype( "Skjema" );

		SimpleHenvendelseService.ole.add( h3 );

		Henvendelse h4 = new Henvendelse( "22222222222" );
		h4.setFagomrKode( "Pensjon" );
		h4.setOpprettetDato( Calendar.getInstance() );
		h4.setOpprettetEnhet( "1357 Bergen" );
		h4.setSakId( "923665" );
		h4.setHenvtype( "Skjema" );

		SimpleHenvendelseService.ole.add( h4 );

		SimpleHenvendelseService.mockLists = new ArrayList<List<Henvendelse>>();
		SimpleHenvendelseService.mockLists.add(SimpleHenvendelseService.ola);
		SimpleHenvendelseService.mockLists.add(SimpleHenvendelseService.ole);
		SimpleHenvendelseService.mockLists.add(SimpleHenvendelseService.kirsten);
	}

	private static void addHenvendelser( List<Henvendelse> list, int count, String fodselsnummer )
	{
		for( int i = 0 ; i < count ; i++ )
		{
			Henvendelse h = new Henvendelse( fodselsnummer );
			h.setFagomrKode( "Bidrag " + i );
			h.setOpprettetDato( Calendar.getInstance() );
			h.setOpprettetEnhet( "1357 Bergen " + i );
			h.setSakId( "923665 " + i );
			h.setHenvtype( "Skjema " + i );
			h.setStonadstype( "Alderspensjon " + i );
			list.add( h );

		}
	}



	public List<Henvendelse> hentHenvendelseList( Fodselsnummer fodselsnummer )
		throws Exception

	{
		System.out.println( "mockLists.size() = " + SimpleHenvendelseService.mockLists.size() );
		for( int i = 0 ; i < SimpleHenvendelseService.mockLists.size() ; i ++ )
		{
			System.out.println( "i = " + i );

			if( SimpleHenvendelseService.mockLists.get( i ).size() == 0 )
			{
				System.out.println( "returning null" );
				return null;
			}
			else if( SimpleHenvendelseService.mockLists.get( i ).get( 0 ).getFodselsnummer().getFodselsnummer().equals( fodselsnummer.getFodselsnummer() ) )
			{
				System.out.println( "returning list" );
				return SimpleHenvendelseService.mockLists.get( i );
			}

			System.out.println( "i = " + i );
		}

		return null;
	}

	public HenvendelseStatistikkResponse genererHenvendelseStatistikk(HenvendelseStatistikkRequest henvendelseStatistikkRequest) {

		HenvendelseStatistikk henvendelseStatistikk;

		HenvendelseStatistikkCriteria crit = henvendelseStatistikkRequest.getCriteria();
		if (crit.getTidsperiode().equals("Siste 5 dager")) {
			henvendelseStatistikk = new HenvendelseStatistikk(7, HenvendelseStatistikk.TIDSENHET_DAG);

			henvendelseStatistikk.addRow(new String[]{"Antall", "246", "302", "198", "265" ,"10", "1021"});
			henvendelseStatistikk.addRow(new String[]{"Krigspensjon", "0", "0", "0", "1", "99", "100"});
			henvendelseStatistikk.addRow(new String[]{"Ikke angitt", "5", "8", "4", "2", "24", "43"});

		} else {

			henvendelseStatistikk = new HenvendelseStatistikk(6, HenvendelseStatistikk.TIDSENHET_UKE);

			henvendelseStatistikk.addRow(new String[]{"Antall", "246", "302", "198", "265", "1011"});
			henvendelseStatistikk.addRow(new String[]{"Krigspensjon", "0", "0", "0", "1", "1"});
			henvendelseStatistikk.addRow(new String[]{"Ikke angitt", "5", "8", "4", "2", "19"});
		}

		return new HenvendelseStatistikkResponse(henvendelseStatistikk);
	}

}
