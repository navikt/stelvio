package no.nav.maven.plugins.datapower.config;

import java.util.Enumeration;
import java.util.Properties;

public class XCFGFormatter {

	private String template;
	
	public XCFGFormatter(String template) {
		this.template = template;
	}
	
	public String format(Properties envProps) /*throws DocumentException*/ {
		String key = null;
		Enumeration envVar = null;
		String myTemplate = new String(template);

		envVar = envProps.keys();
		while (envVar.hasMoreElements()) {
			key = envVar.nextElement().toString();
			myTemplate = replace(myTemplate, "${" + key + "}", envProps.get(key).toString());
		}
		return myTemplate;
	}
	
	private String replace(String source, String pattern, String replace) {
		String retval = "";
		int startIndex, endIndex;
		if (source.indexOf(pattern) >= 0) {
			startIndex = source.indexOf(pattern);
			endIndex = startIndex + pattern.length();
			retval += source.substring(0, startIndex);
			retval += replace;
			retval += source.substring(endIndex);
			return retval;
		}
		return source;
	}

}
