package no.nav.datapower.xmlmgmt;

import java.util.Iterator;
import java.util.List;

import org.jdom.Element;
import org.jdom.Parent;
import org.jdom.filter.ElementFilter;

public class XMLMgmtSOMAResponseValidator implements XMLMgmtDocumentValidator {

	private static final String RESULT_ELEMENT_NAME = "result";
	
	public XMLMgmtSOMAResponseValidator(){
		
	}
	
	public boolean isValid(Element document) {
		
		Iterator decendants = document.getDescendants(new ElementFilter(RESULT_ELEMENT_NAME));
		while(decendants.hasNext()){
			Object node = decendants.next();	
			if(node instanceof Element){
				Element e = (Element)node;
				String text = e.getTextTrim();
				if(!text.equals("OK")){
					return false;
				}
			} 
		}
		return true;
	}

}
