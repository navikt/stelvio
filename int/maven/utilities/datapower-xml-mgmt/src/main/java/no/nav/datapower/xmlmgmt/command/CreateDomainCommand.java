package no.nav.datapower.xmlmgmt.command;


public class CreateDomainCommand extends AbstractSetConfigCommand {

	private String domain;
	
	public CreateDomainCommand(final String domain) {
		this.domain = domain;
	}
/*	
	public String format() {
		StringBuffer req = new StringBuffer();
		req.append("<dp:set-config>\r\n");
		req.append("<Domain name='");
		req.append(domain);
		req.append("'/>\r\n");
		req.append("</dp:set-config>\r\n");
		return req.toString();
	}
*/
	protected void addCommandBody(StringBuffer builder) {
		builder.append("<Domain name='");
		builder.append(domain);
		builder.append("'>\r\n");
        builder.append("<NeighborDomain class='Domain'>default</NeighborDomain>\r\n");
		builder.append("</Domain>\r\n");
	}
}
