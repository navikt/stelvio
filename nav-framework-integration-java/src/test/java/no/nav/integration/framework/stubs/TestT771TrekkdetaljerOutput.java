package no.nav.integration.framework.stubs;

import java.io.Serializable;
import java.util.Set;


/**
 * @author HZA2920
 * @version $Id: TestT771TrekkdetaljerOutput.java 1904 2005-01-26 09:40:12Z hza2920 $
 */
public class TestT771TrekkdetaljerOutput implements Serializable {
	
	/**
	 * Skal være 771 for hent trekkdetaljerOutput
	 */
	private String id = "771";

	/** Antall dataforekomster i aktuell (skal være 1) */
	private int antall = 0;

	/** Trekkdetaljer inneholder T770TrekkdetaljerRad*/
	private Set trekkdetaljer = null;

	/** Returmelding (feilkoder etc) */
	private no.nav.integration.framework.stubs.TestK469Message returMelding;
	/**
	 * @return
	 */
	public int getAntall() {
		return antall;
	}

	/**
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return
	 */
	public TestK469Message getReturMelding() {
		return returMelding;
	}

	/**
	 * @return
	 */
	public Set getTrekkdetaljer() {
		return trekkdetaljer;
	}

	/**
	 * @param i
	 */
	public void setAntall(int i) {
		antall = i;
	}

	/**
	 * @param string
	 */
	public void setId(String string) {
		id = string;
	}

	/**
	 * @param message
	 */
	public void setReturMelding(no.nav.integration.framework.stubs.TestK469Message message) {
		returMelding = message;
	}

	/**
	 * @param set
	 */
	public void setTrekkdetaljer(Set set) {
		trekkdetaljer = set;
	}


}
