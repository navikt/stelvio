package no.nav.maven.plugins.utils;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ErrorHandlerImpl implements ErrorHandler {

	public void error(SAXParseException saxparseexception) throws SAXException {
		System.out.println((new StringBuilder()).append("[ERROR] Line No.: ").append(saxparseexception.getLineNumber()).append(", ERROR: ").append(saxparseexception.toString()).toString());
		printFailureMessage();
		System.exit(1);
	}

	public void fatalError(SAXParseException saxparseexception) throws SAXException {
		System.out.println((new StringBuilder()).append("[ERROR] Line No.: ").append(saxparseexception.getLineNumber()).append(", FATAL ERROR: ").append(saxparseexception.toString()).toString());
		printFailureMessage();
		System.exit(1);
	}

	public void warning(SAXParseException saxparseexception) throws SAXException {
		System.out.println((new StringBuilder()).append("[WARN] Line No.: ").append(saxparseexception.getLineNumber()).append(", WARNING: ").append(saxparseexception.toString()).toString());
	}
	
	private void printFailureMessage(){
		System.out.println("[ERROR] ##################################################################");
		System.out.println("[ERROR] ### XML Validation failed. Please refer to the messages above. ###");
		System.out.println("[ERROR] ##################################################################");
	}
}
