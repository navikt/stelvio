
package no.nav.j2ca.adldap.exception;

import javax.resource.ResourceException;

public class ADLDAPAdapterException extends ResourceException
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -3502378000448387480L;
	private Exception rootCause;
    private boolean isCommException;

	public ADLDAPAdapterException(String message)
    {
        super(message);
    }

    public ADLDAPAdapterException(String message, Exception e)
    {
        super(message);
        rootCause = e;
    }

    public Exception getRootCause()
    {
        return rootCause;
    }

    public void setRootCause(Exception rootCause)
    {
        this.rootCause = rootCause;
    }

    static String copyright()
    {
        return "\n\n(C) Copyright NAV 2007.\n\n";
    }

    public boolean isCommException()
    {
        return isCommException;
    }

    public void setCommException(boolean isCommException)
    {
        this.isCommException = isCommException;
    }
}