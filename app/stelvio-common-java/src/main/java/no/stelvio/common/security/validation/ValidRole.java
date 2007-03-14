package no.stelvio.common.security.validation;

import no.stelvio.common.security.definition.Role;
/**
 * Interface for defining a valid security-role which can be used for rolename validation.
 * This interface is intended to be implemented by application-specific <b>enums</b> containing the valid rolenames.
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 * @see Role
 */
public interface ValidRole extends Role {	
	
}
