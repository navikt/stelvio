package no.stelvio.presentation.security.sso.accessmanager.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import no.stelvio.presentation.security.sso.accessmanager.PrincipalNotValidException;
import no.stelvio.presentation.security.sso.accessmanager.StelvioAccessManager;
import no.stelvio.presentation.security.sso.accessmanager.StelvioPrincipal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A openAm implementation of the StelvioAccessManager The userprincipal representation must be of type
 * <code>StelvioPrincipal</code>.
 * 
 * @author person0a5e006fe6fb Hilstad
 * @see StelvioAccessManager
 * @see StelvioPrincipal
 */
public class OpenAmAccessManager implements StelvioAccessManager {

    // Parameters exposed in JSON structure returned from OpenAM
    private static final String PARAMETER_NAME = "name";
    private static final String PARAMETER_VALUES = "values";
    private static final String PARAMETER_ATTRIBUTES = "attributes";
    private static final String PARAMETER_UID = "uid";
    private static final String PARAMETER_SECURITY_LEVEL = "SecurityLevel";
    private static final String PARAMETER_AUTH_TYPE = "AuthType";
    private static final String PARAMETER_AUTH_METHOD = "AuthMethod";
    private static final String VALID_AUTH_METHOD_ONEDAYPW = "NAV-OneDayPw";
    private static final String VALID_AUTH_TYPE_FEDERATION = "Federation";
    private static final String VALID_AUTH_TYPE_LDAP = "LDAP";

	private HashSet<String> validAuthTypes = new HashSet<String>(Arrays.asList(VALID_AUTH_TYPE_FEDERATION, VALID_AUTH_TYPE_LDAP));
    private HashSet<String> validAuthMethods = new HashSet<String>(Arrays.asList(VALID_AUTH_METHOD_ONEDAYPW));
    
    private StelvioPrincipal principal;
    private Logger log = Logger.getLogger("no.stelvio.presentation.security.sso.accessmanager.OpenAmAccessManager");

    private Properties groupMap;
    private String openAMAddress;
    private String openAMQueryTemplate;
    private boolean authMethodauthTypeCheckDisabled = false;

	public void setOpenAMRestAPI(OpenAMRestAPIImpl openAMRestAPI) {
        this.openAMRestAPI = openAMRestAPI;
    }

    private OpenAMRestAPIImpl openAMRestAPI;

    public OpenAmAccessManager() {
        openAMRestAPI = new OpenAMRestAPIImpl();
    }

    /**
     * Gets the open AM address
     * 
     * @return the open AM address
     */
    public String getOpenAMAddress() {
        return openAMAddress;
    }

    /**
     * Sets the open AM address
     * 
     * @param openAMAddress
     *            the open AM address
     */
    public void setOpenAMAddress(String openAMAddress) {
        this.openAMAddress = openAMAddress;
    }

    /**
     * Gets the open AM query template
     * 
     * @return the open AM query template
     */
    public String getOpenAMQueryTemplate() {
        return openAMQueryTemplate;
    }

    /**
     * Sets the open AM query template
     * 
     * @param openAMQueryTemplate
     *            the open AM query template
     */
    public void setOpenAMQueryTemplate(String openAMQueryTemplate) {
        this.openAMQueryTemplate = openAMQueryTemplate;
    }

    /**
     * Allows disabling of checking AuthMethod and AuthType in OpenAM response 
     * @return
     */
    public boolean isAuthMethodauthTypeDisabled() {
		return authMethodauthTypeCheckDisabled;
	}

	/**
	 * Allows disabling of checking AuthMethod and AuthType in OpenAM response.
	 * Default is false which will perform the check.
	 * @param authMethodauthTypeCheckDisabled
	 */
    public void setAuthMethodauthTypeDisabled(boolean authMethodauthTypeCheckDisabled) {
		this.authMethodauthTypeCheckDisabled = authMethodauthTypeCheckDisabled;
	}

    public HashSet<String> getValidAuthTypes() {
		return validAuthTypes;
	}

	public void setValidAuthTypes(HashSet<String> validAuthTypes) {
		this.validAuthTypes = validAuthTypes;
	}

	public  HashSet<String> getValidAuthMethods() {
		return validAuthMethods;
	}

	public void setValidAuthMethods(HashSet<String> validAuthMethods) {
		this.validAuthMethods = validAuthMethods;
	}

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
     * @param groupMap
     *            the group-map
     */
    public void setGroupMap(Properties groupMap) {
        this.groupMap = groupMap;
    }

    /**
     * Helper method which gets the groups from the group-map that match the supplied key.
     * 
     * @param key
     *            the key
     * @return an array of groups mapped to the supplied key.
     */
    private String[] getGroupsFromMap(String key) {
        String commaseparated = getGroupMap().getProperty(key);
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
     * Sets the representation of a userprincipal and converts it to a StelvioPrincipal. This representation must either be of
     * type <code>StelvioPrincipal</code> or if there is a <code>AccessManagerConnector</code> present be of type
     * <code>byte[]</code>.
     * 
     * @param representation
     *            the principal representation to set
     * @throws PrincipalNotValidException
     *             if the representation cannot be converted to a StelvioPrincipal.
     */
    public void setPrincipalRepresentation(Object representation)
            throws PrincipalNotValidException {
        if (representation instanceof String) {
            this.principal = createStelvioPrincipal((String) representation);
        } else {
            throw new PrincipalNotValidException("Invalid principalrepresentation: " + representation);
        }
    }

    /**
     * Creates a StelvioPrincipal object from the PrincipalRepresentation parameter. If the the user in the
     * PrincipalRepresentation is authorized as another user, the authorization type and username of the other user will be
     * included in the StelvioPrincipal object.
     * 
     * @param representation
     *            the representation of a user from which to create a StelvioPrincipal
     * @return the StelvioPrincipal
     * @throws PrincipalNotValidException
     *             if the principalrepresentation received cannot be converted into a valid StelvioPrincipal
     */
    private StelvioPrincipal createStelvioPrincipal(String representation)
            throws PrincipalNotValidException {

        StelvioPrincipal principal;
        // populate representation with values from openAM
        String openAMQueryTemplateWithAddress = getOpenAMAddress() + getOpenAMQueryTemplate();
        String restResponse = openAMRestAPI.invokeOpenAmRestApi((String) representation, openAMQueryTemplateWithAddress);
        Map<String, String> attributeMap = parseUserAttributes(restResponse);

        String authMethod = attributeMap.get(PARAMETER_AUTH_METHOD);
        String authType = attributeMap.get(PARAMETER_AUTH_TYPE);
        if (log.isLoggable(Level.FINE)) {
            log.fine("AuthMethod=" + authMethod + ", AuthType=" + authType);
            log.fine("Valid AuthMethods=" + getValidAuthMethods() + ", Valid AuthTypes=" + getValidAuthTypes());
        }
        
        if(!isAuthMethodauthTypeDisabled()) {
	        if(authType == null || authMethod == null) {
	        	throw new PrincipalNotValidException("Invalid AuthMethod or AuthType. AuthMethod was " +authMethod + ". AuthType was " + authType);
	        }
	        
	        // check for valid authMethod or authType
	        if( !(getValidAuthMethods().contains(authMethod) || getValidAuthTypes().contains(authType)) ) {
	        	throw new PrincipalNotValidException("Invalid AuthMethod or AuthType. AuthMethod was " +authMethod +
	                    ", expected " + getValidAuthMethods() + ". AuthType was " + authType +
	                    ", expected " + getValidAuthTypes() +
	                    ". One of these must match expected values.");	        	
	        }

        } else {
        	if (log.isLoggable(Level.FINE)) {
                log.fine("Skipping check of AuthMethod and AuthType");
            }
        }
        
        String userId = attributeMap.get(PARAMETER_UID);
        String authorizedAs = userId;

        List<String> authLevelGroupIds = getGroupIdsFromKey(attributeMap.get(PARAMETER_SECURITY_LEVEL));

        if (log.isLoggable(Level.FINE)) {
            log.fine("A StelvioPrincipal will be created with the following values: "
                    + userId + ", " + authorizedAs + ", " + authLevelGroupIds + ", " + representation);
        }
        principal = new DefaultStelvioPrincipal(userId,
                authorizedAs,
                authLevelGroupIds, representation);
        return principal;
    }

    /**
     * Get configured LDAP groups for matching key
     * 
     * @param key
     * @return List with groups matching key
     */
    private List<String> getGroupIdsFromKey(String key) {

        if (key == null) {
            return null;
        }
        
        List<String> groups = new ArrayList<String>();

        String[] groupsFromGroupsKey = getGroupsFromMap(key);
        if (log.isLoggable(Level.FINE)) {
            log.fine("Getting the groupnames which correspond to the groupskey from the mapping table:");
            log.fine("Groupskey - " + key);
        }
        if (groupsFromGroupsKey != null) {
            for (String string : groupsFromGroupsKey) {
                groups.add(string);
                if (log.isLoggable(Level.FINE)) {
                    log.fine("Corresponding group - " + string);
                }
            }
            return groups;
        } else {
            return null;
        }
    }

    /**
     * Parses JSON response into a Map
     * 
     * @param response
     * @return Map with parsed JSON response
     */
    private Map<String, String> parseUserAttributes(String response) {
        try {
            JSONObject json = new JSONObject(response);
            Object o = json.get(PARAMETER_ATTRIBUTES);
            Map<String, String> attributeMap = new HashMap<String, String>();
            JSONArray array = (JSONArray) o;
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = (JSONObject) array.get(i);
                String name = obj.getString(PARAMETER_NAME);
                JSONArray values = (JSONArray) obj.get(PARAMETER_VALUES);
                if (values.length() > 0) {
                    String value = values.getString(0);
                    attributeMap.put(name, value);                    
                }                
            }
            return attributeMap;
        } catch (JSONException e) {
            log.logp(Level.SEVERE, getClass().getName(), "parseUserAttributes", e.getMessage(), e);
            throw new RuntimeException("Error parsing JSON response. ", e);
        }
    }
}
