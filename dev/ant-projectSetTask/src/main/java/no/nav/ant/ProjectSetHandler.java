package no.nav.ant;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ProjectSetHandler extends DefaultHandler {
	
	private List result = new ArrayList();  //  @jve:decl-index=0:
	
	

	public List getResult() {
		return result;
	}



	public void startElement(String namespaceURI, String sName, String qName,
			Attributes attrs) throws SAXException {

		if ("project".equals(qName)) {

			ProjectSetData module = new ProjectSetData();
			StringTokenizer tokens = new StringTokenizer(attrs
					.getValue("reference"), ",");
			// skipper den første som er versjonsnummer
			tokens.nextToken();
			module.setSvnUrl(tokens.nextToken());
			module.setModuleName(tokens.nextToken());
			
			result.add(module);

		}

	}

}
