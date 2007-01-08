package no.nav.domain.pensjon.person;

public class Utenlandskonto  {
	
	private String banknavn;
	private String bankadresse1;
	private String bankadresse2;
	private String bankadresse3;
	private String swiftkode;
	private String kontonummer;
	private String valuta;
	private String land;
	private String bankkode;
	
	public Utenlandskonto(){
	}

	public Utenlandskonto(String banknavn, String bankadresse1, String bankadresse2, String bankadresse3, String swiftkode, String kontonummer, String valuta, String land, String bankkode) {
		super();
		this.banknavn = banknavn;
		this.bankadresse1 = bankadresse1;
		this.bankadresse2 = bankadresse2;
		this.bankadresse3 = bankadresse3;
		this.swiftkode = swiftkode;
		this.kontonummer = kontonummer;
		this.valuta = valuta;
		this.land = land;
		this.bankkode = bankkode;
	}

	public String getBankadresse1() {
		return bankadresse1;
	}

	public void setBankadresse1(String bankadresse1) {
		this.bankadresse1 = bankadresse1;
	}

	public String getBankadresse2() {
		return bankadresse2;
	}

	public void setBankadresse2(String bankadresse2) {
		this.bankadresse2 = bankadresse2;
	}

	public String getBankadresse3() {
		return bankadresse3;
	}

	public void setBankadresse3(String bankadresse3) {
		this.bankadresse3 = bankadresse3;
	}

	public String getBankkode() {
		return bankkode;
	}

	public void setBankkode(String bankkode) {
		this.bankkode = bankkode;
	}

	public String getBanknavn() {
		return banknavn;
	}

	public void setBanknavn(String banknavn) {
		this.banknavn = banknavn;
	}

	public String getKontonummer() {
		return kontonummer;
	}

	public void setKontonummer(String kontonummer) {
		this.kontonummer = kontonummer;
	}

	public String getLand() {
		return land;
	}

	public void setLand(String land) {
		this.land = land;
	}

	public String getSwiftkode() {
		return swiftkode;
	}

	public void setSwiftkode(String swiftkode) {
		this.swiftkode = swiftkode;
	}

	public String getValuta() {
		return valuta;
	}

	public void setValuta(String valuta) {
		this.valuta = valuta;
	}
}