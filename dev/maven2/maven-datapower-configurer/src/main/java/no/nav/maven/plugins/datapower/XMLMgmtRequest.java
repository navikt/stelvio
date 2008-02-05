package no.nav.maven.plugins.datapower;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import no.nav.maven.plugins.datapower.command.*;

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
	
	private List commands = new LinkedList();
	
	public XMLMgmtRequest(String domain) {
		this.domain = domain;
	}
	
	public void addCreateDirCommands(DeviceFileStore location, List dirs) {
		Iterator dirIter = dirs.iterator();
		while (dirIter.hasNext()) {
			addCommand(new CreateDirCommand(location, (String)dirIter.next()));
		}
	}
	
	public void addSetFileCommands(DeviceFileStore location, Map files) {
		Iterator fileIter = files.entrySet().iterator();
		while (fileIter.hasNext()) {
			Map.Entry file = (Map.Entry)fileIter.next();
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
		Iterator cmdIter = commands.iterator();
		while (cmdIter.hasNext()) {
			XMLMgmtCommand cmd = (XMLMgmtCommand) cmdIter.next();
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
