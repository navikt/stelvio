package no.nav.datapower.xmlmgmt.command;

import no.nav.datapower.xmlmgmt.DeviceFileStore;

public class DeleteFileCommand extends AbstractDoActionCommand {

	private final DeviceFileStore location;
	private final String filename;
	
	public DeleteFileCommand(final DeviceFileStore location, final String filename) {
		this.location = location;
		this.filename = filename;
	}	
	@Override
	protected void addCommandBody(StringBuffer builder) {
		builder.append("<DeleteFile>\r\n");
		builder.append("<File>");
		builder.append(location.getDevicePath(filename));
		builder.append("</File>\r\n");
		builder.append("</DeleteFile>\r\n");
	}
}
