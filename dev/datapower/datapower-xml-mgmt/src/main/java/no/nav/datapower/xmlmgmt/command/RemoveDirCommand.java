package no.nav.datapower.xmlmgmt.command;

import no.nav.datapower.xmlmgmt.DeviceFileStore;

public class RemoveDirCommand extends AbstractDoActionCommand {

	private final DeviceFileStore location;
	private final String dirPath;
	
	public RemoveDirCommand(final DeviceFileStore location, final String dirPath) {
		this.location = location;
		this.dirPath = dirPath;
	}	
	@Override
	protected void addCommandBody(StringBuffer builder) {
		builder.append("<RemoveDir>\r\n");
		builder.append("<Dir>");
		builder.append(location.getDevicePath(dirPath));
		builder.append("</Dir>\r\n");
		builder.append("</RemoveDir>\r\n");
	}
}
