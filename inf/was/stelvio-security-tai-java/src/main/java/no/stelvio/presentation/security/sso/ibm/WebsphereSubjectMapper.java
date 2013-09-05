package no.stelvio.presentation.security.sso.ibm;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.Subject;

import no.stelvio.presentation.security.sso.accessmanager.StelvioPrincipal;
import no.stelvio.presentation.security.sso.accessmanager.SubjectMapper;

import com.ibm.websphere.security.CustomRegistryException;
import com.ibm.websphere.security.EntryNotFoundException;
import com.ibm.websphere.security.UserRegistry;
import com.ibm.wsspi.security.token.AttributeNameConstants;

/**
 * A WebSphere-specific implementation of a SubjectMapper which can create a <code>Subject</code> and populate it with
 * group-memberships from a custom user-registry. A group-map with mappings from authentication levels to ldap groups determines
 * which groups that will be added to the <code>Subject</code>.
 * 
 * @author persondab2f89862d3
 * @see SubjectMapper
 * @see Subject
 */
public class WebsphereSubjectMapper implements SubjectMapper {

    private Logger log = Logger.getLogger("no.stelvio.presentation.security.sso.ibm.WebsphereSubjectMapper");
    private static final String AUTHORIZED_AS = "no.stelvio.presentation.security.sso.ibm.WebsphereSubjectMapper.AUTHORIZED_AS";
    private static final String SSOTOKEN = "no.stelvio.presentation.security.sso.ibm.WebsphereSubjectMapper.SSOTOKEN";

    /**
     * Creates a <code>Subject</code> from the supplied StelvioPrincipal. Groups from the user-registry corresponding to the
     * principal's authentication level will be added to the Subject using the mappings in the group-map.
     * 
     * @see {@link #buildSubject(String, List)}
     * @see {@link #getLdapGroups(String[])}
     * @see {@link #getGroupsFromMap(String)}
     * 
     * @param principal
     *            the StelvioPrincipal to create to Subject from
     * @return a Subject containining the userid of the StelvioPrincipal and groups corresponding to the principal's
     *         authentication level.
     * @throws <b>EntryNotFoundException</b> if a userregistry operation fails
     * @throws <b>NamingException</b> if a userregistry operation fails
     * @throws <b>CustomRegistryException</b> if a userregistry operation fails
     * @throws <b>RemoteException</b> if a userregistry operation fails
     */
    public Subject createSubject(StelvioPrincipal principal)
            throws EntryNotFoundException, NamingException, CustomRegistryException, RemoteException {
        if (log.isLoggable(Level.FINE)) {
            log.fine("Creating a subject for the StelvioPrincipal " + principal.getUserId() + ".");
            log.fine("The following groupIds were found: " + principal.getGroupIds() + ".");
        }
        List<String> ldapGroups = getLdapGroups(principal.getGroupIds());
        return buildSubject(principal, ldapGroups);
    }

    /**
     * Builds a Subject using the supplied userId and ldap-groups.
     * 
     * @param userId
     *            the userId
     * @param ldapGroups
     *            the ldapGroups
     * @return a Subject for the supplied user.
     */
    protected Subject buildSubject(StelvioPrincipal principal, List<String> ldapGroups) {
        Subject subject = new Subject();
        String uniqueId = principal.getUserId();
        String userId = principal.getUserId();
        String authorizedAs = principal.getAuthorizedAs();
        
        // Build Hashtable with the new information and add to the Subject.
        Hashtable<String, Object> hashtable = new Hashtable<String, Object>();
        hashtable.put(AttributeNameConstants.WSCREDENTIAL_UNIQUEID, uniqueId);
        hashtable.put(AttributeNameConstants.WSCREDENTIAL_SECURITYNAME, userId);

        // Add custom attribute
        hashtable.put(AUTHORIZED_AS, authorizedAs);
        if (principal.getSsoToken() != null) {
            hashtable.put(SSOTOKEN, principal.getSsoToken());
            hashtable.put(AttributeNameConstants.WSCREDENTIAL_CACHE_KEY, principal.getSsoToken());
        } else {
            String key = uniqueId + "_" + principal.getGroupIds() + "_" + authorizedAs;
            hashtable.put(AttributeNameConstants.WSCREDENTIAL_CACHE_KEY, key);            
        }
        if (ldapGroups != null) {
            hashtable.put(AttributeNameConstants.WSCREDENTIAL_GROUPS, ldapGroups);
        }
        

        // AuthorizationToken tok;
        subject.getPublicCredentials().add(hashtable);

        return subject;
    }

    /**
     * Makes a lookup in the userregistry for the supplied groups and returns the unique groupId for each group as a List.
     * 
     * @param groups
     *            the groups to lookup.
     * @return the unique groupId for each of the groups found in the userregistry.
     * @throws EntryNotFoundException
     *             if a userregistry operation fails
     * @throws NamingException
     *             if a userregistry operation fails
     * @throws CustomRegistryException
     *             if a userregistry operation fails
     * @throws RemoteException
     *             if a userregistry operation fails
     */
    protected List<String> getLdapGroups(List<String> groups)
            throws EntryNotFoundException, NamingException, CustomRegistryException, RemoteException {
        
        if (groups == null) {
            return null;
        }
        
        // Gets the groups from the userregistry. Use the UniqueGroupId.
        InitialContext ctx = new InitialContext();
        List<String> ldapGroups = new ArrayList<String>();
        // Do a lookup in the user-registry
        UserRegistry adReg = (UserRegistry) ctx.lookup("UserRegistry");
        for (String group : groups) {
            String ldapGroup = adReg.getUniqueGroupId(group.trim());
            ldapGroups.add(ldapGroup);
            if (log.isLoggable(Level.FINE)) {
                log.fine("Adding group(s) from ldap - " + ldapGroup);
            }
        }
        ctx.close();

        return ldapGroups;
    }

}
