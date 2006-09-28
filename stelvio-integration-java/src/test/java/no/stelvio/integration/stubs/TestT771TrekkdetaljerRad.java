package no.stelvio.integration.stubs;

import java.io.Serializable;
import java.util.Date;


/**
 * @author HZA2920
 * @version $Id: TestT771TrekkdetaljerRad.java 1904 2005-01-26 09:40:12Z hza2920 $
 */
public class TestT771TrekkdetaljerRad implements Serializable {

	/** Fnr/orgnr til den som det skal trekkes fra */
	private String debitorOffnr = null;

	/** Fnr/orgnr/samhandlernr for kreditor */
	private String kreditorOffnr = null;

	/** navnet til kreditor */
	private String kreditornavn = null;

	/** Hvilken type trekk 4 tegn*/
	private String kodeTrekktype = null;

	/** beskriver nærmere hvilken trekktype */
	private String tekstTrekktype = null;

	/** dato trekk gjelder fra */
	private Date datoTrekkperFom = null;

	/** Kreditors referanse (30 tegn)*/
	private String kreditorsRef = null;

	/** Angir om trekket er et saldotrekk, fast beløp eller dagsats */
	private String kodeTrekkalt = null;

	/** tekst for trekkalt*/
	private String tekstTrekkalt = null;

	/** trekkbeløpet evt trekkprosend */
	private double sats = 0.0;

	/** Når trekket ble foretatt */
	private Date datoTrukket = null;

	/** angir hvilket fagområde trekket ble utført i */
	private String kodeFagomrade = null;

	/** Tekst for kodefagområde */
	private String tekstFagomrade = null;

	/** Angir hvor mye som ble trukket */
	private double belopTrukket = 0.0;

	/** Behandlende TKNR */
	private String tknr = null;

	/**
	 * @return
	 */
	public double getBelopTrukket() {
		return belopTrukket;
	}

	/**
	 * @return
	 */
	public Date getDatoTrekkperFom() {
		return datoTrekkperFom;
	}

	/**
	 * @return
	 */
	public Date getDatoTrukket() {
		return datoTrukket;
	}

	/**
	 * @return
	 */
	public String getDebitorOffnr() {
		return debitorOffnr;
	}

	/**
	 * @return
	 */
	public String getKodeFagomrade() {
		return kodeFagomrade;
	}

	/**
	 * @return
	 */
	public String getKodeTrekkalt() {
		return kodeTrekkalt;
	}

	/**
	 * @return
	 */
	public String getKodeTrekktype() {
		return kodeTrekktype;
	}

	/**
	 * @return
	 */
	public String getKreditornavn() {
		return kreditornavn;
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
	public String getKreditorsRef() {
		return kreditorsRef;
	}

	/**
	 * @return
	 */
	public double getSats() {
		return sats;
	}

	/**
	 * @return
	 */
	public String getTekstFagomrade() {
		return tekstFagomrade;
	}

	/**
	 * @return
	 */
	public String getTekstTrekkalt() {
		return tekstTrekkalt;
	}

	/**
	 * @return
	 */
	public String getTekstTrekktype() {
		return tekstTrekktype;
	}

	/**
	 * @return
	 */
	public String getTknr() {
		return tknr;
	}

	/**
	 * @param d
	 */
	public void setBelopTrukket(double d) {
		belopTrukket = d;
	}

	/**
	 * @param date
	 */
	public void setDatoTrekkperFom(Date date) {
		datoTrekkperFom = date;
	}

	/**
	 * @param date
	 */
	public void setDatoTrukket(Date date) {
		datoTrukket = date;
	}

	/**
	 * @param string
	 */
	public void setDebitorOffnr(String string) {
		debitorOffnr = string;
	}

	/**
	 * @param string
	 */
	public void setKodeFagomrade(String string) {
		kodeFagomrade = string;
	}

	/**
	 * @param string
	 */
	public void setKodeTrekkalt(String string) {
		kodeTrekkalt = string;
	}

	/**
	 * @param string
	 */
	public void setKodeTrekktype(String string) {
		kodeTrekktype = string;
	}

	/**
	 * @param string
	 */
	public void setKreditornavn(String string) {
		kreditornavn = string;
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
	public void setKreditorsRef(String string) {
		kreditorsRef = string;
	}

	/**
	 * @param d
	 */
	public void setSats(double d) {
		sats = d;
	}

	/**
	 * @param string
	 */
	public void setTekstFagomrade(String string) {
		tekstFagomrade = string;
	}

	/**
	 * @param string
	 */
	public void setTekstTrekkalt(String string) {
		tekstTrekkalt = string;
	}

	/**
	 * @param string
	 */
	public void setTekstTrekktype(String string) {
		tekstTrekktype = string;
	}

	/**
	 * @param string
	 */
	public void setTknr(String string) {
		tknr = string;
	}


}
