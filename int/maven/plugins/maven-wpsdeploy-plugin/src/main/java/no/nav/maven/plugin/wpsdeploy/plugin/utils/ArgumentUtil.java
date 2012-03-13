package no.nav.maven.plugin.wpsdeploy.plugin.utils;

import java.util.List;

public class ArgumentUtil {
	public static String listToDelimitedString(List<String> variables, String delimiter) {
	    StringBuffer result = new StringBuffer();
	    if (variables.size() > 0) {
	        result.append(variables.get(0));
	        for (int i=1; i<variables.size(); i++) {
	            result.append(delimiter);
	            result.append(variables.get(i));
	        }
	    }
	    return result.toString();
	}
}
