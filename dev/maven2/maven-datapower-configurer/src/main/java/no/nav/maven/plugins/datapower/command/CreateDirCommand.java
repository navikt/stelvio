package no.nav.maven.plugins.datapower.command;

import no.nav.maven.plugins.datapower.DeviceFileStore;

public class CreateDirCommand implements XMLMgmtCommand {

	private final DeviceFileStore location;
	private final String dirPath;
	
	public CreateDirCommand(final DeviceFileStore location, final String dirPath) {
		this.location = location;
		this.dirPath = dirPath;
	}
	
	public String format() {
		StringBuffer req = new StringBuffer();
		req.append("<dp:do-action>\r\n");
		req.append("<CreateDir>\r\n");
		req.append("<Dir>");
		req.append(location.getDevicePath(dirPath));
		req.append("</Dir>\r\n");
		req.append("</CreateDir>\r\n");
		req.append("</dp:do-action>\r\n");
		return req.toString();
	}
}
