package no.stelvio.presentation.security.sso.accessmanager.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
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
 * @see AccessManagerConnector
 */
public class OpenAmAccessManager implements StelvioAccessManager {

    // Parameters exposed in JSON structure returned from OpenAM
    private static final String PARAMETER_NAME = "name";
    private static final String PARAMETER_VALUES = "values";
    private static final String PARAMETER_ATTRIBUTES = "attributes";
    private static final String PARAMETER_UID = "uid";
    private static final String PARAMETER_SECURITY_LEVEL = "SecurityLevel";

    private StelvioPrincipal principal;
    private Logger log = Logger.getLogger("no.stelvio.presentation.security.sso.accessmanager.OpenAmAccessManager");

    private Properties groupMap;
    private String openAMAddress;
    private String openAMQueryTemplate;

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
        String restResponse = invokeOpenAmRestApi((String) representation);
        Map<String, String> attributeMap = parseUserAttributes(restResponse);

        String userId = attributeMap.get(PARAMETER_UID);
        String authorizedAs = userId;

        List<String> authLevelGroupIds = getGroupIdsFromKey(attributeMap.get(PARAMETER_SECURITY_LEVEL));

        if (log.isLoggable(Level.FINE)) {
            log.fine("A StelvioPrincipal will be created with the following values: "
                    + userId + ", " + authorizedAs + ", " + authLevelGroupIds);
        }
        principal = new DefaultStelvioPrincipal(userId,
                authorizedAs,
                authLevelGroupIds);
        return principal;
    }

    /**
     * Get configured LDAP groups for matching key
     * 
     * @param key
     * @return List with groups matching key
     */
    private List<String> getGroupIdsFromKey(String key) {

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
                String value = values.getString(0);
                attributeMap.put(name, value);
            }
            return attributeMap;
        } catch (JSONException e) {
            log.logp(Level.SEVERE, getClass().getName(), "parseUserAttributes", e.getMessage(), e);
            throw new RuntimeException("Error parsing JSON response. ", e);
        }
    }

    /**
     * Invoke OpenAM REST API to get information for given session
     * 
     * @param sessionId
     *            OpenAM sessionid from cookie
     * @return response from OpenAM
     */
    private String invokeOpenAmRestApi(String sessionId) {

        StringBuffer result = new StringBuffer();
        URL url = null;
        String openAMQuery = getOpenAMAddress() + getOpenAMQueryTemplate().replace("[TOKENID]", sessionId);
        log.fine("Invoking OpenAM REST interface: " + openAMQuery);

        try {
            url = new URL(openAMQuery);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
                log.logp(Level.SEVERE, getClass().getName(), "invokeOpenAmRestApi",
                        "Error from openAM: " + conn.getResponseCode() + " " + conn.getResponseMessage());
                throw new RuntimeException("Failed: HTTP error code : " + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String output;
            while ((output = br.readLine()) != null) {
                result.append(output);
            }
            conn.disconnect();
        } catch (MalformedURLException e) {
            log.logp(Level.SEVERE, getClass().getName(), "invokeOpenAmRestApi", e.getMessage(), e);
            throw new RuntimeException("Malformed URL: " + url, e);
        } catch (IOException e) {
            log.logp(Level.SEVERE, getClass().getName(), "invokeOpenAmRestApi", e.getMessage(), e);
            throw new RuntimeException("Error when invoking openAM", e);
        }
        return result.toString();
    }
}
