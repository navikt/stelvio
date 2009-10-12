package no.nav.datapower.util;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

public class WildcardPathFilter implements IOFileFilter {

	private String filter;
	
	public WildcardPathFilter(String filter) {
		this.filter = filter;
	}
	public boolean accept(File file) {
		return FilenameUtils.wildcardMatch(file.getPath(), filter);
	}

	public boolean accept(File file, String name) {
		return false;
	}

	@Override
	public String toString() {
		return getClass().getName() + "('" + filter + "')";
	}

}
