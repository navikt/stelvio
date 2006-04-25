package no.trygdeetaten.common.framework.ejb;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

/**
 * Implementation of InitialContext that won't cause unit tests to delay.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 1222 $ $Author: tsb2920 $ $Date: 2004-08-31 10:51:01 +0200 (Tue, 31 Aug 2004) $
 */
public class TestContext implements Context {

	private boolean failOnClose = false;

	public TestContext() {
		this(false);
	}

	public TestContext(boolean failOnClose) {
		this.failOnClose = failOnClose;
	}

	public Object lookup(String name) throws NamingException {
		if ("TestEJBHome".equals(name)) {
			return new TestEJBHome();
		} else if ("NamingException".equals(name)) {
			throw new NamingException(name);
		} else if("Resource".equals(name)) {
			return new Byte((byte)1); 
		} else {
			return name;
		}
	}

	public void close() throws NamingException {
		if (failOnClose) {
			throw new NamingException("Failed to close context");
		}
	}

	// ----- NO NEED TO IMPLEMENT THE REMAINING METHODS
	public Object lookup(Name name) throws NamingException {
		return null;
	}
	public void bind(Name name, Object obj) throws NamingException {
	}
	public void bind(String name, Object obj) throws NamingException {
	}
	public void rebind(Name name, Object obj) throws NamingException {
	}
	public void rebind(String name, Object obj) throws NamingException {
	}
	public void unbind(Name name) throws NamingException {
	}
	public void unbind(String name) throws NamingException {
	}
	public void rename(Name oldName, Name newName) throws NamingException {
	}
	public void rename(String oldName, String newName) throws NamingException {
	}
	public NamingEnumeration list(Name name) throws NamingException {
		return null;
	}
	public NamingEnumeration list(String name) throws NamingException {
		return null;
	}
	public NamingEnumeration listBindings(Name name) throws NamingException {
		return null;
	}
	public NamingEnumeration listBindings(String name) throws NamingException {
		return null;
	}
	public void destroySubcontext(Name name) throws NamingException {
	}
	public void destroySubcontext(String name) throws NamingException {
	}
	public Context createSubcontext(Name name) throws NamingException {
		return null;
	}
	public Context createSubcontext(String name) throws NamingException {
		return null;
	}
	public Object lookupLink(Name name) throws NamingException {
		return null;
	}
	public Object lookupLink(String name) throws NamingException {
		return null;
	}
	public NameParser getNameParser(Name name) throws NamingException {
		return null;
	}
	public NameParser getNameParser(String name) throws NamingException {
		return null;
	}
	public Name composeName(Name name, Name prefix) throws NamingException {
		return null;
	}
	public String composeName(String name, String prefix) throws NamingException {
		return null;
	}
	public Object addToEnvironment(String propName, Object propVal) throws NamingException {
		return null;
	}
	public Object removeFromEnvironment(String propName) throws NamingException {
		return null;
	}
	public Hashtable getEnvironment() throws NamingException {
		return null;
	}
	public String getNameInNamespace() throws NamingException {
		return null;
	}
}
