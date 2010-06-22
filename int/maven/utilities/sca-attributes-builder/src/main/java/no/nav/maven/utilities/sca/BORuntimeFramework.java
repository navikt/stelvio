package no.nav.maven.utilities.sca;

public enum BORuntimeFramework {
	VERSION6("emf"), VERSION7("xci");

	private String elementName;

	private BORuntimeFramework(String elementName) {
		this.elementName = elementName;
	}

	protected String getElementName() {
		return elementName;
	}
}
