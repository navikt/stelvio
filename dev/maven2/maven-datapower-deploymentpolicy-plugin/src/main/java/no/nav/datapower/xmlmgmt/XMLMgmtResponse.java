package no.nav.datapower.xmlmgmt;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;

import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import no.nav.datapower.xmlmgmt.net.DPHttpResponse;

public class XMLMgmtResponse extends XMLMgmtDocument {
	
	private XMLMgmtDocumentValidator validator;
	private DPHttpResponse response;
	public XMLMgmtResponse(DPHttpResponse response, XMLMgmtDocumentValidator validator){
		try {
			this.response = response;
			this.document = buildDocument(response);
			this.validator = validator;
		} catch (IOException e) {
			throw new RuntimeException("Failed to create xml document from response." ,e);
		} catch (JDOMException e) {
			throw new RuntimeException("Failed to create xml document from response." ,e);
		}
	}
	
	public boolean isSuccessful(){
		if(response.getHttpResponseCode() == 200){
			return validator.isValid(this.document);
		} else {
			return false;
		}
	}
	
	protected Element buildDocument(DPHttpResponse response) throws JDOMException, IOException {
		SAXBuilder sax = new SAXBuilder();
		Document config = sax.build(new StringReader(response.getResponseBody()));
		return config.getRootElement(); 
	}

}
