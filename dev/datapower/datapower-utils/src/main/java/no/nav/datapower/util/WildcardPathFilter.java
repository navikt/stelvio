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
//		System.out.println("WildcardPathFilter.accept(File), file = " + file.getPath());
//		System.out.println("WildcardPathFilter.accept(File), filter = " + filter);
		boolean returnValue = FilenameUtils.wildcardMatch(file.getPath(), filter);
//		System.out.println("WildcardPathFilter.accept(File), returnValue = " + returnValue);
		return returnValue;
	}

	public boolean accept(File file, String name) {
		System.out.println("WildcardPathFilter.accept(File, String), file = " + file + ", name = " + name);
		return false;
	}

	@Override
	public String toString() {
		return getClass().getName() + "('" + filter + "')";
	}

}
