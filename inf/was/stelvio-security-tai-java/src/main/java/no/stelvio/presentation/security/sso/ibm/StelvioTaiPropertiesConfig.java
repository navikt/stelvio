package no.stelvio.presentation.security.sso.ibm;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import no.stelvio.presentation.security.sso.ConfigPropertyKeys;
import no.stelvio.presentation.security.sso.RequestValueKeys;
import no.stelvio.presentation.security.sso.RequestValueType;
import no.stelvio.presentation.security.sso.SSORequestHandler;
import no.stelvio.presentation.security.sso.accessmanager.StelvioAccessManager;
import no.stelvio.presentation.security.sso.accessmanager.SubjectMapper;
import no.stelvio.presentation.security.sso.accessmanager.support.OpenAmAccessManager;
import no.stelvio.presentation.security.sso.support.OpenAmRequestHandler;

/**
 * A configuration class for the StelvioTai which can read in properties from 2 properties files or use setter methods to create
 * <code>StelvioAccessManager, SSORequestHandler and SubjectMapper</code> objects. These can be retrieved with getter methods.
 * If no of the setters for the above mentioned objects have been used, two properties files will be used to create these
 * objects. There should be one file for the common properties of the TAI module and one with the authentication levels as
 * numbers with corresponding LDAP groups. Default names for these files are <code>stelvio-tai-common.properties</code> and
 * <code>stelvio-tai-ldap-groups.properties</code>, however these can be changed using setters.
 * 
 * For the common properties file the following keys are valid:
 * 
 * <li>REQUEST_VALUE_TYPE = Specifies what type of value which can be extracted from a request. Valid values are: [HEADER,
 * PARAMETER, ATTRIBUTE, SESSION_ATTRIBUTE]</li> <li>ACCESS_MANAGER_USER_NAME = The username of the access-manager user.</li>
 * <li>ORIGINAL_USER_NAME = The name of the original username value to be retrieved from the request.</li> <li>
 * AUTHENTICATION_LEVEL = The name of the authentication level value to be retrieved from the request.</li> <li>
 * ACCESS_MANAGER_USER = The name of the access-manager user value to be retrieved from the request.</li> <br/>
 * <br/>
 * For the LDAP groups file the following keys are valid: <br/>
 * <li>1 = [a ldap group for authentication level one]</li> <li>2 = [a ldap group for authentication level two]</li> <li>3 = [a
 * ldap group for authentication level three]</li> <li>4 = [a ldap group for authentication level four]</li> <br/>
 * <br/>
 * 
 * 
 * @author persondab2f89862d3
 */
public class StelvioTaiPropertiesConfig implements StelvioTaiConfig {
    private Logger log = Logger.getLogger("no.stelvio.presentation.security.sso.ibm.StelvioTaiPropertiesConfig");

    private static final String STELVIO_TAI_COMMON_PROPERTIES_FILE = "stelvio-tai-common.properties";
    private static final String STELVIO_TAI_LDAP_GROUPS_PROPERTIES_FILE = "stelvio-tai-ldap-groups.properties";

    private StelvioAccessManager accessManager;
    private SSORequestHandler requestHandler;
    private SubjectMapper subjectMapper;
    private String commonPropertiesFile = STELVIO_TAI_COMMON_PROPERTIES_FILE;
    private String ldapGroupsPropertiesFile = STELVIO_TAI_LDAP_GROUPS_PROPERTIES_FILE;

    private Properties commonProps;
    private Properties ldapProps;
    private Properties taiProps;

    public void setProperties(Properties props) {
        this.taiProps = props;
    }

    /**
     * Loads the two properties files into this class.
     */
    public void loadConfig() {
        this.commonProps = loadPropertiesFromFile(commonPropertiesFile);
        this.ldapProps = loadPropertiesFromFile(ldapGroupsPropertiesFile);
        if (log.isLoggable(Level.INFO)) {
            log.info("Configuring the Stelvio TrustAssociationInterceptor.");
            log.info("Loading the common properties file:");
            Set<Entry<Object, Object>> set = commonProps.entrySet();
            for (Entry<Object, Object> entry : set) {
                log.info(entry.getKey() + " = " + entry.getValue());
            }

            log.info("Loading the authentication level to ldap-groups properties file:");
            Set<Entry<Object, Object>> set2 = ldapProps.entrySet();
            for (Entry<Object, Object> entry1 : set2) {
                log.info(entry1.getKey() + " = " + entry1.getValue());
            }
        }
    }

    /**
     * Gets the StelvioAccessManager. If no access-manager is present a <code>OpenAmAccessManager</code> will be created.
     * 
     * @return the StelvioAccessManager
     */
    public StelvioAccessManager getAccessManager() {
        if (accessManager == null) {
            accessManager = createAccessManager();
        }
        return accessManager;
    }

    private OpenAmAccessManager createAccessManager() {
        OpenAmAccessManager accessManager = new OpenAmAccessManager();
        
        // perform various init of OpenAmAccessManager
        
        // set mappings of authenticationlevels to groups
        accessManager.setGroupMap(ldapProps);
        
        // set the address to OpenAM
        String openamaddress = taiProps.getProperty(ConfigPropertyKeys.OPENAM_URL);
        if (openamaddress != null) {
            accessManager.setOpenAMAddress(openamaddress);
        } else {
            log.logp(Level.SEVERE, this.getClass().getName(), "createAccessManager", "Missing TAI custom property: " + ConfigPropertyKeys.OPENAM_URL);
        }
        
        // set the query template for OpenAM requests 
        accessManager.setOpenAMQueryTemplate(getTrimmedProperty(commonProps, ConfigPropertyKeys.OPENAM_QUERY_TEMPLATE));
        
        // check if authMethod and authType checking should be disabled
        String authMethodauthTypeDisabled = taiProps.getProperty(ConfigPropertyKeys.AUTHTYPE_AUTHMETHOD_CHECK_DISABLED);
        if( authMethodauthTypeDisabled != null && authMethodauthTypeDisabled.equalsIgnoreCase("true") ) {
        	accessManager.setAuthMethodauthTypeDisabled(true);
        }
        
       // check if valid authMethod values has been overridden 
        String validAuthMethods = taiProps.getProperty(ConfigPropertyKeys.VALID_AUTHMETHODS);
        if( validAuthMethods != null ) {
        	validAuthMethods = validAuthMethods.replaceAll(" ", "");
        	accessManager.setValidAuthMethods(new HashSet<String>(Arrays.asList(validAuthMethods.split(","))));
        }
        
        // check if valid authType values has been overridden
        String validAuthTypes = taiProps.getProperty(ConfigPropertyKeys.VALID_AUTHTYPES);
        if( validAuthTypes != null ) {
        	validAuthTypes = validAuthTypes.replaceAll(" ", "");
        	accessManager.setValidAuthTypes(new HashSet<String>(Arrays.asList(validAuthTypes.split(","))));
        }
        return accessManager;
    }

    /**
     * Gets the SSORequestHandler. If no request-handler is present a <code>OpenAmRequestHandler</code> will be created from
     * the common properties file.
     * 
     * @return the SSORequestHandler
     */
    public SSORequestHandler getRequestHandler() {
        if (requestHandler == null) {
            requestHandler = createRequestHandler();
        }
        return requestHandler;
    }

    /**
     * Private helper method which creates a OpenAmRequestHandler from the properties in the common properties file.
     * 
     * @return a new OpenAmRequestHandler
     */
    private OpenAmRequestHandler createRequestHandler() {

        OpenAmRequestHandler handler = new OpenAmRequestHandler();

        handler.setRequestValueType(getRequestValueType(commonProps));

        RequestValueKeys reqKeys = new RequestValueKeys();
        reqKeys.setCookieKey(getTrimmedProperty(commonProps, ConfigPropertyKeys.COOKIE_REQUEST_VALUE_KEY));
        handler.setRequestValueKeys(reqKeys);

        return handler;
    }

    /**
     * Gets the SubjectMapper. If no subject-mapper is present a <code>WebsphereSubjectMapper</code> will be created from the
     * ldap groups properties file.
     * 
     * @return the SubjectMapper
     */
    public SubjectMapper getSubjectMapper() {
        if (subjectMapper == null) {
            subjectMapper = createSubjectMapper();
        }
        return subjectMapper;
    }

    /**
     * Private helper method which creates a WebsphereSubjectMapper from the properties in the ldap groups properties file.
     * 
     * @return a new WebsphereSubjectMapper
     */
    private WebsphereSubjectMapper createSubjectMapper() {
        WebsphereSubjectMapper mapper = new WebsphereSubjectMapper();
        return mapper;
    }

    /**
     * Sets the filename for the common properties file.
     * 
     * @param commonPropertiesFile
     *            the filename of the common properties file.
     */
    public void setCommonPropertiesFile(String commonPropertiesFile) {
        this.commonPropertiesFile = commonPropertiesFile;
    }

    /**
     * Sets the filename for the ldap groups properties file.
     * 
     * @param ldapGroupsPropertiesFile
     *            the filename for ldap groups properties file.
     */
    public void setLdapGroupsPropertiesFile(String ldapGroupsPropertiesFile) {
        this.ldapGroupsPropertiesFile = ldapGroupsPropertiesFile;
    }

    /**
     * Sets the access-manager.
     * 
     * @param accessManager
     *            the access-manager to set.
     */
    public void setAccessManager(StelvioAccessManager accessManager) {
        this.accessManager = accessManager;
    }

    /**
     * Sets the subject-mapper.
     * 
     * @param subjectMapper
     *            the subject-mapper to set.
     */
    public void setSubjectMapper(SubjectMapper subjectMapper) {
        this.subjectMapper = subjectMapper;
    }

    /**
     * Sets the request-handler.
     * 
     * @param requestHandler
     *            the request-handler to set.
     */
    public void setRequestHandler(SSORequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    /**
     * Private helper method for matching the string for the REQUEST_VALUE_TYPE property with the enum RequestValueType.
     * 
     * @param props
     *            the common properties file
     * @return the RequestValueType
     */
    private RequestValueType getRequestValueType(Properties props) {
        String prop = getTrimmedProperty(props, ConfigPropertyKeys.REQUEST_VALUE_TYPE);
        RequestValueType[] types = RequestValueType.values();
        for (RequestValueType type : types) {
            if (type.name().equalsIgnoreCase(prop)) {
                return type;
            }
        }

        return null;
    }

    /**
     * Private helper method which trims whitespaces for the property specied by the key.
     * 
     * @param props
     *            the properties containing the property to trim.
     * @param key
     *            the key of the property to trim.
     * @return a trimmed property specified by the key.
     */
    private String getTrimmedProperty(Properties props, String key) {
        return props.getProperty(key.trim()).trim();
    }

    /**
     * Helper method which loads a properties file into a Properties object and returns it.
     * 
     * @param file
     *            the filename of the properties file to load
     * @return a properties object containing the properties from the file.
     */
    public Properties loadPropertiesFromFile(String file) {
        Properties props = new Properties();
        String filename = file;
        URL url = Thread.currentThread().getContextClassLoader().getResource(filename);
        try {
            props.load(url.openStream());
        } catch (IOException io) {
            throw new RuntimeException(io);
        }
        return props;
    }

    /**
     * @return the commonPropertiesFile
     */
    public String getCommonPropertiesFileName() {
        return commonPropertiesFile;
    }

    /**
     * @return the ldapGroupsPropertiesFile
     */
    public String getLdapGroupsPropertiesFileName() {
        return ldapGroupsPropertiesFile;
    }

}
