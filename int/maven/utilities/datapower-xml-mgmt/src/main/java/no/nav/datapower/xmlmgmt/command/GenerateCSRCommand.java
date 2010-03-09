package no.nav.datapower.xmlmgmt.command;

public class GenerateCSRCommand extends AbstractDoActionCommand {

	private String country;
	private String state;
	private String locality;
	private String organization;
	private String organizationalUnit;
	private String commonName;

	public static class Builder {
		private String country;
		private String state;
		private String locality;
		private String organization;
		private String organizationalUnit;
		private String commonName;
		
		public Builder(String country, String organization, String organizationalUnit, String commonName) {
			this.country = country;
			this.organization = organization;
			this.organizationalUnit = organizationalUnit;
			this.commonName = commonName;
		}		
		public Builder state(String state) { this.state = state; return this; }
		public Builder locality(String locality) { this.locality = locality; return this; }
	}

	public GenerateCSRCommand(Builder builder) {
		this.country = builder.country;
		this.state = builder.state;
		this.locality = builder.locality;
		this.organization = builder.organization;
		this.organizationalUnit = builder.organizationalUnit;
		this.commonName = builder.commonName;
	}
	
	@Override
	protected void addCommandBody(StringBuffer builder) {
		builder.append("<Keygen>\r\n");
        builder.append("<LDAPOrder>off</LDAPOrder>\r\n");
        builder.append("<C>" + country + "</C>\r\n");
        if (state != null && !state.equals(""))
        	builder.append("<ST>" + state + "</ST>\r\n");
        if (locality != null && !locality.equals(""))
        	builder.append("<L>" + locality + "</L>\r\n");
        builder.append("<O>" + organization + "</O>\r\n");
        builder.append("<OU>" + organizationalUnit + "</OU>\r\n");
        builder.append("<CN>" + commonName + "</CN>\r\n");
        builder.append("<KeyLength>1024</KeyLength>\r\n");
        builder.append("<Days>1095</Days>\r\n");
        builder.append("<ExportKey>on</ExportKey>\r\n");
        builder.append("<GenSSCert>on</GenSSCert>\r\n");
        builder.append("<ExportSSCert>on</ExportSSCert>\r\n");
        builder.append("<GenObject>on</GenObject>\r\n");
        builder.append("</Keygen>\r\n");
	}
}
