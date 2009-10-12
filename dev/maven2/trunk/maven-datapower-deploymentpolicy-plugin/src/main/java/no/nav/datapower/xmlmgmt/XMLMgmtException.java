package no.nav.datapower.xmlmgmt;

public class XMLMgmtException extends Exception {

	private static final long serialVersionUID = -6389322198951164949L;

	public XMLMgmtException() {
		super();
	}
	
	public XMLMgmtException(String msg) {
		super(msg);
	}
	
	public XMLMgmtException(Throwable cause) {
		super(cause);
	}
	
	public XMLMgmtException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
