package no.nav.datapower.xmlmgmt.command;

import java.util.ArrayList;

import no.nav.datapower.xmlmgmt.DeviceFileStore;

public class CreateDirCommand extends AbstractDoActionCommand {

	private final DeviceFileStore location;
	private final ArrayList<String> dirPaths;
	
	public CreateDirCommand(final DeviceFileStore location, final ArrayList<String> dirPaths) {
		this.location = location;
		this.dirPaths = dirPaths;
	}
/*	
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
*/
	protected void addCommandBody(StringBuffer builder) {
		builder.append("<CreateDir>\r\n");
        for(String dirPath : dirPaths) {
            builder.append("<Dir>");
            builder.append(location.getDevicePath(dirPath));
            builder.append("</Dir>\r\n");
        }
		builder.append("</CreateDir>\r\n");
	}
}
