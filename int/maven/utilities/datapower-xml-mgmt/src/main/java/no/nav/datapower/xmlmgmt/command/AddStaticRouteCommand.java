package no.nav.datapower.xmlmgmt.command;

public class AddStaticRouteCommand extends AbstractModifyConfigCommand {

	private String ethInterface;
	private String destination;
	private String gateway;

	public AddStaticRouteCommand(String ethInterface, String destination, String gateway) {
		this.ethInterface = ethInterface;
		this.destination = destination;
		this.gateway = gateway;
	}
	
	/*public String format() {
		StringBuffer req = new StringBuffer();
		req.append("<dp:modify-config>\r\n");
		req.append("</dp:modify-config>\r\n");
  		return null;
	}*/

	protected void addCommandBody(StringBuffer builder) {
		builder.append("<EthernetInterface name='" + ethInterface + "'>\r\n");
		builder.append("<StaticRoutes>\r\n");
		builder.append("<Destination>" + destination + "</Destination>\r\n");
		builder.append("<Gateway>" + gateway + "</Gateway>\r\n");
		builder.append("<Metric>0</Metric>\r\n");
		builder.append("</StaticRoutes>\r\n");
		builder.append("</EthernetInterface>\r\n");
	}
}
