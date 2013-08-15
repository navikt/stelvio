package no.stelvio.presentation.security.sso.accessmanager.support;


import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import no.stelvio.presentation.security.sso.ConfigPropertyKeys;
import no.stelvio.presentation.security.sso.accessmanager.AccessManagerConnector;
import no.stelvio.presentation.security.sso.accessmanager.PrincipalNotValidException;
import no.stelvio.presentation.security.sso.accessmanager.StelvioAccessManager;
import no.stelvio.presentation.security.sso.accessmanager.StelvioPrincipal;
import no.stelvio.presentation.security.sso.support.PrincipalRepresentation;

/**
 * A WebSEAL implementation of the StelvioAccessManager which <b>can</b> support a connection to
 * a Tivoli Access Manager through the AccessManagerConnector. If this connector is present the 
 * userprincipal representation must be of type <code>byte[]</code> and the AccessManagerConnector
 * must be able to transform this representation into a StelvioPrincipal.
 * If no AccessManagerConnector is present the userprincipal representation must be of type 
 * <code>StelvioPrincipal</code>.
 * 
 * @author persondab2f89862d3
 * @see StelvioAccessManager
 * @see StelvioPrincipal
 * @see AccessManagerConnector
 */
public class WebSealAccessManager implements StelvioAccessManager {

	private AccessManagerConnector accessManagerConnector;
	private StelvioPrincipal principal;
	private Logger log = Logger.getLogger("no.stelvio.presentation.security.sso.accessmanager.WebSealAccessManager");
	
	private Properties groupMap;
	
	/**
	 * Gets the mapping of authentication levels to ldap groups.
	 * 
	 * @return the mapping of authentication levels to ldap groups
	 */
	public Properties getGroupMap() {
		return groupMap;
	}
	/**
	 * Sets the mapping of authentication levels to ldap groups.
	 * 
	 * @param groupMap the group-map
	 */
	public void setGroupMap(Properties groupMap) {
		this.groupMap = groupMap;
	}
	/**
	 * Helper method which gets the groups from the group-map that match the supplied key. 
	 * 
	 * @param key the key 
	 * @return an array of groups mapped to the supplied key.
	 */
	protected String[] getGroupsFromMap(String key){
		String commaseparated = groupMap.getProperty(key);
		String[] props = commaseparated != null ? commaseparated.split(",") : null;
		return props;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public StelvioPrincipal getPrincipal() {
		return this.principal;
	}	
	
	/**
	 * Sets the representation of a userprincipal and converts it to a StelvioPrincipal. 
	 * This representation must either be of type <code>StelvioPrincipal</code> or if there
	 * is a <code>AccessManagerConnector</code> present be of type
	 * <code>byte[]</code>.
	 * 
	 * @param representation the principal representation to set
	 * @throws PrincipalNotValidException if the representation cannot be converted to a StelvioPrincipal.
	 */
	public void setPrincipalRepresentation(Object representation) 
	throws PrincipalNotValidException {
		
		if(this.accessManagerConnector != null){
			if(representation instanceof byte[]){
				byte[] pac = (byte[])representation;
				this.principal = this.accessManagerConnector.getTransformedPrincipal(pac);
			}
		} else {
			if(representation instanceof PrincipalRepresentation){
				this.principal = createStelvioPrincipal((PrincipalRepresentation)representation);
			}	
		}
	}
	/**
	 * Creates a StelvioPrincipal object from the PrincipalRepresentation parameter.
	 * If the the user in the PrincipalRepresentation is authorized as another user, the
	 * authorization type and username of the other user will be included in the
	 * StelvioPrincipal object.
	 * 
	 * @param representation the representation of a user from which to create a StelvioPrincipal
	 * @return the StelvioPrincipal
	 * @throws PrincipalNotValidException if the principalrepresentation received cannot be converted 
	 * into a valid StelvioPrincipal
	 */
	private StelvioPrincipal createStelvioPrincipal(PrincipalRepresentation representation)
	throws PrincipalNotValidException {
		
		StelvioPrincipal principal;
		String userId = representation.getOriginalUserName();
		String authorizedAs = userId;
		
		List<String> authLevelGroupIds = getGroupIdsFromAuthenticationLevel(representation);
		List<String> authzTypeGroups = getGroupIdsFromAuthorizationType(representation);
		List<String> selfGroups = null;
		
		if(isAuthorizedAsSelf(representation)){
			selfGroups = 
				getGroupIdsFromKey(ConfigPropertyKeys.AUTHORIZED_AS_SELF_GROUPS_KEY);
			authorizedAs = userId;
		} else {			
			authorizedAs = representation.getAuthorizedAs();
			if(log.isLoggable(Level.FINE)){		
				log.fine("The current user is acting on behalf of the user:" + authorizedAs);
			}
		}
		List<String> groupIds = mergeLists(authLevelGroupIds, authzTypeGroups, selfGroups);
		
		if(log.isLoggable(Level.FINE)){		
			log.fine("A StelvioPrincipal will be created with the following values: " 
					+ userId + ", " + authorizedAs + ", " + groupIds );
		}	
		principal = new DefaultStelvioPrincipal(userId, 
												authorizedAs,
												groupIds);
		return principal;
	}
	
	private List<String> mergeLists(List<String>... lists){
		List<String> merged = new ArrayList<String>();
		for (List<String> list : lists) {
			if(list != null){
				merged.addAll(list);
			}
		}
		return merged;
	}
	
	protected List<String> getGroupIdsFromAuthorizationType(PrincipalRepresentation representation)
	throws PrincipalNotValidException {
		if(log.isLoggable(Level.FINE)){		
			log.fine("Getting group ids from the mapping table based on the authorization type.");
		}
		String authzType = representation.getAuthorizationType();
		List<String> groups = null;			
		if(authzType != null && !authzType.equals("") && !authzType.equals("NOT_FOUND")){
			if(authzType.contains(",")){
				String[] types =  authzType.split(",");
				for (String string : types) {
					String type = string.trim();
					List<String> list = getGroupIdsFromKey(type);
					if(list != null){
						groups = groups == null ? new ArrayList<String>() : groups;
						groups.addAll(list);
					}
				}
			} else {
				groups = getGroupIdsFromKey(authzType);
			}
			
			if(groups == null){
				if(log.isLoggable(Level.FINE)){		
					log.fine("The authorization-type '" + representation.getAuthorizationType()  
							+ "' derived from the current request" 
							+ " could not be found in the group-mapping properties.");
				}
				throw new PrincipalNotValidException("The authorization-type '" + representation.getAuthorizationType()  
						+ "' derived from the current request" 
						+ " could not be found in the group-mapping properties.");
			}
		} else {
			if(log.isLoggable(Level.FINE)){		
				log.fine("The authorization type was not found in the request, no group ids added.");
			}
		}
		return groups;
	}
	
	protected List<String> getGroupIdsFromAuthenticationLevel(PrincipalRepresentation representation){
		if(log.isLoggable(Level.FINE)){		
			log.fine("Getting group ids from the mapping table based on authentication level.");
		}
		return getGroupIdsFromKey(representation.getAuthenticationLevel());
	} 
	
	/**
	 * 
	 * @param representation
	 * @return
	 */
	private boolean isAuthorizedAsSelf(PrincipalRepresentation representation){
		String userId = representation.getOriginalUserName();
		String authorizedAs = representation.getAuthorizedAs();
		return (authorizedAs.equals("") || authorizedAs.equals("NOT_FOUND") 
				|| userId.equals(authorizedAs));
	}
	/**
	 * 
	 * @param key
	 * @return
	 */
	protected List<String> getGroupIdsFromKey(String key){
		
		List<String> groups = new ArrayList<String>();
		
		String[] groupsFromGroupsKey = getGroupsFromMap(key);
		if(log.isLoggable(Level.FINE)){
			log.fine("Getting the groupnames which correspond to the groupskey from the mapping table:");
			log.fine("Groupskey - " + key);
		}
		if(groupsFromGroupsKey != null){	
			for (String string : groupsFromGroupsKey) {
				groups.add(string);
				if(log.isLoggable(Level.FINE)){
					log.fine("Corresponding group - " + string);
				}
			}
			return groups;
		} else {
			return null;
		}
	}
	
	
	/**
	 * Gets the AccessManagerConnector.
	 * @return the AccessManagerConnector.
	 */
	public AccessManagerConnector getAccessManagerConnector() {
		return accessManagerConnector;
	}
	/**
	 * Sets the AccessManagerConnector.
	 * @param accessManagerConnector the accessManagerConnector to set.
	 */
	public void setAccessManagerConnector(AccessManagerConnector accessManagerConnector) {
		this.accessManagerConnector = accessManagerConnector;
	}
}
