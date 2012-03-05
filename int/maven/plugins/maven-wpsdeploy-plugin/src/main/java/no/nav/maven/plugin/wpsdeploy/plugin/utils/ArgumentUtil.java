package no.nav.maven.plugin.wpsdeploy.plugin.utils;

import java.util.ArrayList;

public class ArgumentUtil {
	public static String arrayListToDelimitedString(ArrayList<String> a, String delimiter) {
	    StringBuffer result = new StringBuffer();
	    if (a.size() > 0) {
	        result.append(a.get(0));
	        for (int i=1; i<a.size(); i++) {
	            result.append(delimiter);
	            result.append(a.get(i));
	        }
	    }
	    return result.toString();
	}
}
