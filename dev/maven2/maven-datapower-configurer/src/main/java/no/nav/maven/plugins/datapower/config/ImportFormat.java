package no.nav.maven.plugins.datapower.config;

public class ImportFormat {

	private final String format;
	
	private ImportFormat(final String format) {
		this.format = format;
	}
	
	public static final ImportFormat XML = new ImportFormat("XML"); 
	public static final ImportFormat ZIP = new ImportFormat("ZIP");
	
	public String toString() {
		return format;
	}
}
