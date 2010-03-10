package no.nav.datapower.xmlmgmt;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import no.nav.datapower.util.DPCollectionUtils;
import no.nav.datapower.xmlmgmt.command.CreateDirCommand;
import no.nav.datapower.xmlmgmt.command.SetFileCommand;
import no.nav.datapower.xmlmgmt.command.XMLMgmtCommand;

public class XMLMgmtRequest {

	private static final String ENCODING = "UTF-8";
	
	private static final String SOAPENV_START =
		"<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:dp='http://www.datapower.com/schemas/management'>\r\n" +
		"<soapenv:Header/>\r\n" +
		"<soapenv:Body>\r\n";

	private static final String SOAPENV_CLOSE =
	   	"</soapenv:Body>\r\n" +
	   	"</soapenv:Envelope>";
	
		
	private final String domain;
	
	private List<XMLMgmtCommand> commands = DPCollectionUtils.newLinkedList();
	
	public XMLMgmtRequest(String domain) {
		this.domain = domain;
	}
		
	public void addCreateDirCommands(DeviceFileStore location, List<String> dirs) {
		for (String dir : dirs) {
			addCommand(new CreateDirCommand(location, dir));
		}
	}
	
	public void addSetFileCommands(DeviceFileStore location, Map<String, String> files) {
		for (Map.Entry file : files.entrySet()) {
			addCommand(new SetFileCommand(location, (String)file.getKey(), (String)file.getValue()));
		}
	}
	
	public void addCommand(XMLMgmtCommand command) {
		commands.add(command);
	}
	
	
	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append(SOAPENV_START);
		str.append("<dp:request domain='" + domain + "'>\r\n");
		for (XMLMgmtCommand cmd : commands) {
			str.append(cmd.format());
		}
		str.append("</dp:request>\r\n");
		str.append(SOAPENV_CLOSE);
		return str.toString();
	}
	
	public byte[] getBytes() throws UnsupportedEncodingException {
		return toString().getBytes(ENCODING);
	}
}
