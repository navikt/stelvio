package no.stelvio.common.codestable.support;

import java.util.List;
import java.util.Locale;

import no.stelvio.common.codestable.CodesTable;
import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.codestable.DecodeNotFoundException;
import no.stelvio.common.codestable.ItemNotFoundException;

/**
 * Implementation of CodesTable used to handle a codestable, its belonging values and predicates for filtering the items
 * in the codestable.
 *
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 * @todo use Commons Collection's LazyMap to initialize the filtered codes table map
 * @todo add safe copying of input/output, that is, constructor and getItems()
 */
public class DefaultCodesTable<T extends CodesTableItem> extends AbstractCodesTable<T> implements CodesTable<T> {
	private static final long serialVersionUID = -1882474781384872740L;

	/**
	 * Creates a <code>DefaultCodesTable</code> with a list of <code>CodesTableItem</code>s.
	 *
	 * @param codesTableItems list of <code>CodesTableItem</code>s this <code>DefaultCodesTable</code> consists of.
	 */
	public DefaultCodesTable(List<T> codesTableItems) {
		init(codesTableItems);
	}

	/** {@inheritDoc} */
	public String getDecode(Enum code) throws ItemNotFoundException, DecodeNotFoundException {
		return decode(code.name());
	}

	/** {@inheritDoc} */
	public String getDecode(Enum code, Locale locale) throws DecodeNotFoundException {
		return decode(code.name(), locale);
	}

	/** {@inheritDoc} */
	public String getDecode(Object code) throws ItemNotFoundException, DecodeNotFoundException {
		return decode(code);
	}

	/** {@inheritDoc} */
	public String getDecode(Object code, Locale locale) {
		return decode(code, locale);
	}

	/** {@inheritDoc} */
	public boolean validateCode(String code) {
		return super.validateCode(code);
	}
}==== ORIGINAL VERSION app/stelvio-common-java/src/main/java/no/stelvio/common/codestable/support/DefaultCodesTable.java 117153772439361
}==== THEIR VERSION app/stelvio-common-java/src/main/java/no/stelvio/common/codestable/support/DefaultCodesTable.java (/mirror/ud/stelvio) 117153772439361
	/** {@inheritDoc} */
	public boolean validateCode(String code) {
		return super.validateCode(code);
	}
}
