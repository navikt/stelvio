package no.nav.j2ca.adldap.exception;

import javax.resource.ResourceException;

public class ADLDAPAdapterConnectionFailedException extends ResourceException
{
    /**
     * @param message
     * @param ex
     */
    public ADLDAPAdapterConnectionFailedException(String message, Exception ex)
    {
        super(message,ex);
    }
    
    /**
     * @param message
     */
    public ADLDAPAdapterConnectionFailedException(String message)
    {
        super(message);
    }

}