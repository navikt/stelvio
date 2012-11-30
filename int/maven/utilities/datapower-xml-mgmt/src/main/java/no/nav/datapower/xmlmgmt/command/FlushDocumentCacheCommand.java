package no.nav.datapower.xmlmgmt.command;

public class FlushDocumentCacheCommand extends AbstractDoActionCommand {

	private final String XMLManager;
	
	public FlushDocumentCacheCommand( final String XMLManager) {
		this.XMLManager = XMLManager;
	}

	protected void addCommandBody(StringBuffer builder) {
		builder.append("<FlushDocumentCache>\r\n");
		builder.append("<XMLManager>");
		builder.append(XMLManager);
		builder.append("</XMLManager>\r\n");
		builder.append("</FlushDocumentCache>\r\n");
	}
}