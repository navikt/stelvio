package no.nav.datapower.util;

import org.apache.commons.lang.StringUtils;

public class DPFilenameUtils extends org.apache.commons.io.FilenameUtils {

	private DPFilenameUtils() {}
	
	public static String getRelativePath(String filename, String relativeTo) {
		return DPFilenameUtils.getPathNoEndSeparator(StringUtils.substringAfter(filename, relativeTo));
	}
}
