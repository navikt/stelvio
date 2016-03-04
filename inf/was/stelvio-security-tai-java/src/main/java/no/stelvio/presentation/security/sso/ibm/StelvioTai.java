package no.stelvio.presentation.security.sso.ibm;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.stelvio.presentation.security.sso.SSORequestHandler;
import no.stelvio.presentation.security.sso.accessmanager.PrincipalNotValidException;
import no.stelvio.presentation.security.sso.accessmanager.StelvioAccessManager;
import no.stelvio.presentation.security.sso.accessmanager.StelvioPrincipal;
import no.stelvio.presentation.security.sso.accessmanager.SubjectMapper;

import com.ibm.websphere.security.WebTrustAssociationException;
import com.ibm.websphere.security.WebTrustAssociationFailedException;
import com.ibm.wsspi.security.tai.TAIResult;
import com.ibm.wsspi.security.tai.TrustAssociationInterceptor;

/**
 * StelvioTai is a TrustAssociationInterceptor implementation which is a single-sign-on mechanism that will be triggered when an
 * unauthenticated user attempts to access a protected resource. When this happens the interceptor can examine the values in the
 * request to determine if it should create a Subject and authenticate the user with the application server. The criteria for
 * authentication are based upon userid and authentication level, that is it is the authentication level which determines which
 * user-registry groups that will be added to the Subject.
 * 
 * @author persondab2f89862d3
 * @see TrustAssociationInterceptor
 * @see Subject
 * @see StelvioAccessManager
 * @see StelvioPrincipal
 * @see SSORequestHandler
 * @see SubjectMapper
 * @see StelvioTaiPropertiesConfig
 */
public class StelvioTai implements TrustAssociationInterceptor {

    private StelvioAccessManager accessManager;
    private SSORequestHandler requestHandler;
    private SubjectMapper mapper;
    private StelvioTaiConfig config;
    private Properties taiCustomProps;
    private Logger log = Logger.getLogger("no.stelvio.presentation.security.sso.ibm.StelvioTai");;

    /**
     * Cleans up references and close connections.
     */
    public void cleanup() {
    }

    /**
     * Gets the type of TrustAssociationInterceptor.
     * 
     * @return the name of this class.
     */
    public String getType() {
        return this.getClass().getName();
    }

    /**
     * Gets the version of this interceptor.
     * 
     * @return the version
     */
    public String getVersion() {
        return "1.1";
    }

    /**
     * Initializes the interceptor. This involves using properties set in the WebSphere admin console and initialization of the
     * components used by the interceptor. Currently the only available property is: debug = [ true | false ], which is false by
     * default.
     * 
     * @param props
     *            the properties set in the WebSphere admin console
     * @return a number indicating the success of the initialization, 0 is ok.
     * @throws WebTrustAssociationFailedException
     *             if the initialization fails.
     */
    public int initialize(Properties props)
            throws WebTrustAssociationFailedException {
        this.taiCustomProps = props;
        initStelvioTaiComponents();
        return 0;
    }

    /**
     * Initializes and sets the interceptor's StelvioAccessManager, SSORequestHandler and SubjectMapper components by using a
     * StelvioTaiPropertiesConfig to read in properties from properties files.
     * 
     * @see StelvioAccessManager
     * @see SSORequestHandler
     * @see SubjectMapper
     * @see StelvioTaiPropertiesConfig
     */
    public void initStelvioTaiComponents() {
        initStelvioTaiComponents(new StelvioTaiPropertiesConfig());
    }

    /**
     * Initializes and sets the interceptor's StelvioAccessManager, SSORequestHandler and SubjectMapper components by using a
     * StelvioTaiConfig.
     * 
     * @param configuration
     *            the configuration to use.
     * @see StelvioAccessManager
     * @see SSORequestHandler
     * @see SubjectMapper
     * @see StelvioTaiPropertiesConfig
     */
    public void initStelvioTaiComponents(StelvioTaiConfig configuration) {
        config = configuration;
        config.loadConfig();
        config.setProperties(taiCustomProps);
        this.accessManager = config.getAccessManager();
        this.requestHandler = config.getRequestHandler();
        this.mapper = config.getSubjectMapper();
    }

    /**
     * Authenticates the user by creating a Subject based on the authentication data from the request. The authentication data
     * will be extracted from the request using the SSORequestHandler, transformed into a StelvioPrincipal using the
     * StelvioAccessManager and mapped into a Subject along with the corresponding LDAP groups using the SubjectMapper. If the
     * authentication data is incomplete or invalid an Exception will be thrown.
     * 
     * @param req
     *            the current request
     * @return a Subject containing information about the current user.
     * @throws Exception
     *             in these cases: <li>PrincipalNotValidException</li> - the principal representation could not be converted
     *             into a StelvioPrincpal. <li>PrincipalNotValidException</li> - the StelvioPrincipal could not be mapped to a
     *             Subject <li>PrincipalNotValidException</li> - could not get the required authentication data from the request
     *             <li>Exception</li> - if an exception occurs while creating a Subject using the SubjectMapper, typically
     *             vendor specific exceptions.
     */
    public Subject authenticate(HttpServletRequest req) throws Exception {

        if (log.isLoggable(Level.FINE)) {
            log.entering(this.getClass().toString(), "authenticate(HttpServletRequest)");
            log.fine("Attempting to authenticate the request.");
        }
        Object principalRepresentation = requestHandler.getPrincipalRepresentation(req);
        if (principalRepresentation != null && requestHandler.actOnRequest(req)) {
            accessManager.setPrincipalRepresentation(principalRepresentation);
            StelvioPrincipal principal = accessManager.getPrincipal();
            if (log.isLoggable(Level.FINE)) {
                log.fine("Trust is established and a principal representation has been found in the request.");
            }
            if (principal == null) {
                throw new PrincipalNotValidException(
                        "The principal representation could not be converted into a StelvioPrincipal");
            }
            Subject subject = mapper.createSubject(principal);
            if (subject == null) {
                throw new PrincipalNotValidException(
                        "The StelvioPrincipal could not be mapped to a valid Subject.");
            }
            return subject;

        } else {
            throw new PrincipalNotValidException("Could not get required authentication data from the request.");
        }
    }

    /**
     * This method is invoked when an unauthenticated request attempts to access a protected resource. It determines whether or
     * not this interceptor should process the current request by using the {@link SSORequestHandler}'s actOnRequest() method.
     * 
     * @param req
     *            the current request
     * @return true if this interceptor should process the request, false otherwise.
     * @throws WebTrustAssociationException
     *             if there is a situation where noe definite response can be given.
     */
    public boolean isTargetInterceptor(HttpServletRequest req)
            throws WebTrustAssociationException {

        if (log.isLoggable(Level.FINE)) {
            log.entering(this.getClass().toString(), "isTargetInterceptor(HttpServletRequest)");
            log.fine("Determining whether or not the request should be acted on by this interceptor.");
        }
        if (requestHandler != null) {
            boolean actOnReq = this.requestHandler.actOnRequest(req);
            if (log.isLoggable(Level.FINE)) {
                log.fine("Act on request: " + actOnReq);
            }
            return actOnReq;
        }
        return false;
    }

    /**
     * This method is invoked after the isTargetInterceptor() method and is used to extract the information that WebSphere needs
     * to create a securitycontext for the current user. It will use the SSORequestHandler to extract the authentication data,
     * the StelvioAccessManager to transform this data into a StelvioPrincipal and the SubjectMapper to create a Subject that
     * will be returned in a TAIResult object to the server. The TAIResult object also contains a status code that will inform
     * WebSphere if the authentication data is valid.
     * 
     * @param req
     *            the HttpServletRequest to be processed
     * @param res
     *            the HttpServletResponse to be processed
     * @return a TAIResult object indicating the success of the authentication process
     * @throws WebTrustAssociationFailedException
     *             if the authentication process fails.
     */
    public TAIResult negotiateValidateandEstablishTrust(
            HttpServletRequest req, HttpServletResponse res)
            throws WebTrustAssociationFailedException {

        try {
            if (log.isLoggable(Level.FINE)) {
                log.entering(this.getClass().toString(), "negotiateValidateandEstablishTrust(HttpServletRequest,HttpServletResponse)");
            }
            Subject subject = authenticate(req);
            return TAIResult.create(HttpServletResponse.SC_OK, "notused", subject);

        } catch (Exception e) {
            throw new WebTrustAssociationFailedException(e.getMessage());
        }
    }

}
