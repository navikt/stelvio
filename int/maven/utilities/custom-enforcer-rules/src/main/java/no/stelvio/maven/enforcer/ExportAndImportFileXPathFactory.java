package no.stelvio.maven.enforcer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class ExportAndImportFileXPathFactory {
	private static XPath xPath;

	public static XPathExpression getXPathExpression(String expression) throws XPathExpressionException {
		return getXPath().compile(expression);
	}

	private static XPath getXPath() {
		if (xPath == null) {
			XPathFactory xPathFactory = XPathFactory.newInstance();
			xPath = xPathFactory.newXPath();
			xPath.setNamespaceContext(new ExportAndImportFileNamespaceContext());
		}
		return xPath;
	}

	private static final class ExportAndImportFileNamespaceContext implements NamespaceContext {
		private Map<String, String> namespaceMap = new HashMap<String, String>();

		public ExportAndImportFileNamespaceContext() {
			namespaceMap.put("jaxws", "http://www.ibm.com/xmlns/prod/websphere/scdl/jaxws/6.0.0");
			namespaceMap.put("xsi", "http://www.w3.org/2001/XMLSchema-instance");
		}

		public String getNamespaceURI(String prefix) {
			return namespaceMap.get(prefix);
		}

		public String getPrefix(String namespaceURI) {
			for (Map.Entry<String, String> namespaceMapEntry : namespaceMap.entrySet()) {
				if (namespaceMapEntry.getValue().equals(namespaceURI)) {
					return namespaceMapEntry.getKey();
				}
			}
			return null;
		}

		public Iterator getPrefixes(String namespaceURI) {
			return namespaceMap.keySet().iterator();
		}
	}
}
