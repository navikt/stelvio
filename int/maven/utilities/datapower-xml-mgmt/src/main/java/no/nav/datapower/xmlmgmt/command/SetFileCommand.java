package no.nav.datapower.xmlmgmt.command;

import no.nav.datapower.xmlmgmt.DeviceFileStore;


public class SetFileCommand implements XMLMgmtCommand {
	
	private final DeviceFileStore location;
	private final String fileName;
	private final String fileData;
	
	public SetFileCommand(final DeviceFileStore location, final String fileName, final String base64FileData) {
		this.location = location;
		this.fileName = fileName;
		this.fileData = base64FileData;
	}
	
	public String getFileName() {
		return fileName;
	}

	public String getFileData() {
		return fileData;
	}
	
	public String format() {
		StringBuffer str = new StringBuffer();
		str.append("<dp:set-file name='");
		str.append(location.getDevicePath(fileName));
		str.append("'>");
		str.append(fileData);
		str.append("</dp:set-file>\r\n");
		return str.toString();
	}
}
