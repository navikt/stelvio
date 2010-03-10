package no.nav.datapower.xmlmgmt.command;

public class AddHostAliasCommand extends AbstractSetConfigCommand {

	private String alias;
	private String ipAddress;
	
	public AddHostAliasCommand(String alias, String ipAddress) {
		this.alias = alias;
		this.ipAddress = ipAddress;
	}
	
	protected void addCommandBody(StringBuffer builder) {
		builder.append("<HostAlias name='" + alias +"'>\r\n");
		builder.append("<IPAddress>" + ipAddress +"</IPAddress>\r\n");
		builder.append("</HostAlias>\r\n");
	}
}
