package no.nav.datapower.xmlmgmt;

import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

public class DatapowerManagementNamespaceContext implements NamespaceContext {

	public static final String DEFAULT_PREFIX = "dp";
	public static final String DEFAULT_NAMESPACE = "http://www.datapower.com/schemas/management";

	public String getNamespaceURI(String prefix) {
		if (prefix == null) {
			throw new NullPointerException("Null prefix");
		} else if (DEFAULT_PREFIX.equals(prefix)) {
			return DEFAULT_NAMESPACE;
		}
		return XMLConstants.NULL_NS_URI;
	}

	public String getPrefix(String uri) {
		throw new UnsupportedOperationException();
	}

	public Iterator getPrefixes(String uri) {
		throw new UnsupportedOperationException();
	}

}
