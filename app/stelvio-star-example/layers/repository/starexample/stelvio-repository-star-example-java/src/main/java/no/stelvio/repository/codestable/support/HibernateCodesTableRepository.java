package no.stelvio.repository.codestable.support;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import no.nav.domain.pensjon.codestable.HenvendelseTypeCti;
import no.stelvio.repository.codestable.CodesTableRepository;

public class HibernateCodesTableRepository implements CodesTableRepository {

	public List fetchCodesTable(Class codestable) {
		List list = new ArrayList();
		list.add(new HenvendelseTypeCti("code", "decode", new Date(), new Date(), Locale.ENGLISH, true));
		
		return list;
	}
}
