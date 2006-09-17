package no.stelvio.integration.framework.stubs;

import java.io.Serializable;

/**
 * Enten trekkvedtak-id, kreditor-offnr elelr kreditorIdTSS må være fylt ut.
 * 
 * @author HZA2920
 * @version $Id: TestT770TrekkdetaljerInput.java 2868 2006-04-25 11:22:51Z psa2920 $
 */
public class TestT770TrekkdetaljerInput implements Serializable {

	/**
	 * Skal være 770 for hent trekkdetaljer.
	 */
	private String id="770";
	
	/** Unik identifikasjon av trekkvedtaktet (intern id) */
	private long trekkvedtakid = 0;
	
	/** fnr/orgnr/samhandlerid til kreditoren */
	private String kreditorOffnr = null;
	
	/** Intern identifikasjon av kreditoren i samhandlerdatabaseb */
	private String kreditorIdTSS = null;
	
	/** Behandlene TKnr i UR */
	private String tknr = null;

	/**
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return
	 */
	public String getKreditorIdTSS() {
		return kreditorIdTSS;
	}

	/**
	 * @return
	 */
	public String getKreditorOffnr() {
		return kreditorOffnr;
	}

	/**
	 * @return
	 */
	public String getTknr() {
		return tknr;
	}

	/**
	 * @return
	 */
	public long getTrekkvedtakid() {
		return trekkvedtakid;
	}

	/**
	 * @param string
	 */
	public void setId(String string) {
		id = string;
	}

	/**
	 * @param string
	 */
	public void setKreditorIdTSS(String string) {
		kreditorIdTSS = string;
	}

	/**
	 * @param string
	 */
	public void setKreditorOffnr(String string) {
		kreditorOffnr = string;
	}

	/**
	 * @param string
	 */
	public void setTknr(String string) {
		tknr = string;
	}

	/**
	 * @param l
	 */
	public void setTrekkvedtakid(long l) {
		trekkvedtakid = l;
	}

}
