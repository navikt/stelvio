package no.nav.datapower.xmlmgmt.command;

public class DeleteDomainCommand extends AbstractDeleteConfigCommand {

	private String domain;
	
	public DeleteDomainCommand(final String domain) {
		this.domain = domain;
	}
	
	/*public String format() {
		StringBuffer req = new StringBuffer();
		req.append("<dp:del-config>\r\n");
		req.append("<Domain name='");
		req.append(domain);
		req.append("'/>\r\n");
		req.append("</dp:del-config>\r\n");
		return req.toString();
	}*/

	protected void addCommandBody(StringBuffer builder) {
		builder.append("<Domain name='");
		builder.append(domain);
		builder.append("'/>\r\n");
	}
}
