package no.nav.datapower.xmlmgmt.command;

import java.io.File;
import java.util.List;

import no.nav.datapower.util.DPFileUtils;
import no.nav.datapower.xmlmgmt.DeviceFileStore;

public class CreateDirCommand extends AbstractDoActionCommand {

	private final DeviceFileStore location;
	private final List<File> dirPaths;
	
	public CreateDirCommand(final DeviceFileStore location, final List<File> dirPaths) {
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
        for(File dirPath : dirPaths) {
            builder.append("<CreateDir>\r\n");
            builder.append("<Dir>");
            builder.append(location.getDevicePath(DPFileUtils.replaceSeparator(dirPath, '/')));
            builder.append("</Dir>\r\n");
            builder.append("</CreateDir>\r\n");
        }
	}
}
