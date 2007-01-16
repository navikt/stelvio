package no.stelvio.repository.codestable.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.repository.codestable.CodesTableRepository;

public class HibernateCodesTableRepository implements CodesTableRepository {

	public List fetchCodesTable(Class codestable) {
		List list = new ArrayList();
		list.add(new Cti("code", "decode", Locale.ENGLISH, true));
		
		return list;
	}
	
	public static class Cti extends CodesTableItem {
		public Cti(String code, String decode, Locale locale, boolean isValid) {
			super(code, decode, locale, isValid);
		}
	}
}
