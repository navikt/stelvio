package no.nav.datapower.xmlmgmt;

import no.nav.datapower.xmlmgmt.net.DPHttpResponse;

public class XMLMgmtResponseFactory {

	public static XMLMgmtResponse createXMLMgmtResponse(DPHttpResponse response){
		XMLMgmtResponse xmlResponse = new XMLMgmtResponse(response, new XMLMgmtSOMAResponseValidator());
		return xmlResponse;
	}
}
