package no.stelvio.consumer.star.example.henvendelse.support;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import no.stelvio.consumer.star.example.henvendelse.SearchHenvendelseServiceBi;
import no.stelvio.consumer.star.example.henvendelse.to.SearchHenvendelseRequest;
import no.stelvio.consumer.star.example.henvendelse.to.SearchHenvendelseResponse;
import no.stelvio.domain.star.example.henvendelse.Henvendelse;
import no.stelvio.domain.star.example.henvendelse.HenvendelseStatistics;
import no.stelvio.domain.star.example.henvendelse.HenvendelseStatisticsCriteria;

/**
 * Mock impl av SokHenvendelseService
 *
 * @author personff564022aedd
 */
public class SimpleSearchHenvendelseService implements SearchHenvendelseServiceBi {

	private static List<Henvendelse> ola = new ArrayList<Henvendelse>();
	private static List<Henvendelse> ole = new ArrayList<Henvendelse>();
	private static List<Henvendelse> kirsten = new ArrayList<Henvendelse>();
	private static ArrayList<List<Henvendelse>> mockLists;

	static {
		Henvendelse h1 = new Henvendelse( "01327200336" );
		h1.setFagomrKode( "Pensjon" );
		h1.setOpprettetDato( Calendar.getInstance() );
		h1.setOpprettetEnhet( "0129 Oslo" );
		h1.setSakId( "974816723" );
		h1.setHenvtype( "Regelverk" );
		h1.setStonadstype( "Alderspensjon" );

		ola.add( h1 );

		Henvendelse h2 = new Henvendelse( "04250100286" );
		h2.setFagomrKode( "Bidrag" );
		h2.setOpprettetDato( Calendar.getInstance() );
		h2.setOpprettetEnhet( "1357 Bergen" );
		h2.setSakId( "923665" );
		h2.setHenvtype( "Skjema" );
		h2.setStonadstype( "Uførepensjon" );
		ola.add( h2 );

		addHenvendelser(ola,1000, "12345678901");

		Henvendelse h3 = new Henvendelse( "12345678901" );
		h3.setFagomrKode( "Bidrag" );
		h3.setOpprettetDato( Calendar.getInstance() );
		h3.setOpprettetEnhet( "1357 Bergen" );
		h3.setSakId( "923665" );
		h3.setHenvtype( "Skjema" );

		ole.add( h3 );

		Henvendelse h4 = new Henvendelse( "04250100286" );
		h4.setFagomrKode( "Pensjon" );
		h4.setOpprettetDato( Calendar.getInstance() );
		h4.setOpprettetEnhet( "1357 Bergen" );
		h4.setSakId( "923665" );
		h4.setHenvtype( "Skjema" );

		ole.add( h4 );

		mockLists = new ArrayList<List<Henvendelse>>();
		mockLists.add(ola);
		mockLists.add(ole);
		mockLists.add(kirsten);
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

	public SearchHenvendelseResponse genererHenvendelseStatistikk(SearchHenvendelseRequest searchHenvendelseRequest) {

		HenvendelseStatistics henvendelseStatistics;

		HenvendelseStatisticsCriteria crit = searchHenvendelseRequest.getCriteria();
		if (crit.getTidsperiode().equals("Siste 5 dager")) {
			henvendelseStatistics = new HenvendelseStatistics(7, HenvendelseStatistics.TIDSENHET_DAG);

			henvendelseStatistics.addRow(new String[]{"Antall", "246", "302", "198", "265" ,"10", "1021"});
			henvendelseStatistics.addRow(new String[]{"Krigspensjon", "0", "0", "0", "1", "99", "100"});
			henvendelseStatistics.addRow(new String[]{"Ikke angitt", "5", "8", "4", "2", "24", "43"});

		} else {

			henvendelseStatistics = new HenvendelseStatistics(6, HenvendelseStatistics.TIDSENHET_UKE);

			henvendelseStatistics.addRow(new String[]{"Antall", "246", "302", "198", "265", "1011"});
			henvendelseStatistics.addRow(new String[]{"Krigspensjon", "0", "0", "0", "1", "1"});
			henvendelseStatistics.addRow(new String[]{"Ikke angitt", "5", "8", "4", "2", "19"});
		}

		return new SearchHenvendelseResponse(henvendelseStatistics);
	}

}
