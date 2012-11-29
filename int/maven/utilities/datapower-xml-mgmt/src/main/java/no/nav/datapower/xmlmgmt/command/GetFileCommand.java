package no.nav.datapower.xmlmgmt.command;

import no.nav.datapower.xmlmgmt.DeviceFileStore;


public class GetFileCommand implements XMLMgmtCommand {
	
	private final DeviceFileStore location;
	private final String fileName;
	
	public GetFileCommand(final DeviceFileStore location, final String fileName) {
		this.location = location;
		this.fileName = fileName;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public String format() {
		StringBuffer str = new StringBuffer();
		str.append("<dp:get-file name='");
		str.append(location.getDevicePath(fileName));
		str.append("'>");
		str.append("</dp:get-file>\r\n");
		return str.toString();
	}
}