package no.stelvio.presentation.security.sso.ibm;

import java.util.Properties;

import no.stelvio.presentation.security.sso.SSORequestHandler;
import no.stelvio.presentation.security.sso.accessmanager.StelvioAccessManager;
import no.stelvio.presentation.security.sso.accessmanager.SubjectMapper;

/**
 * Represents the configuration and components that are needed by the StelvioTai.
 * 
 * @author persondab2f89862d3
 * @see StelvioAccessManager
 * @see SSORequestHandler
 * @see SubjectMapper
 */
public interface StelvioTaiConfig {
	
	/**
	 *  Set the custom properties for the TAI
	 * @param props
	 */
	void setProperties(Properties props);
	
	/**
	 * Loads the appropriate configuration needed to create the StelvioAccessManager,
	 * SSORequestHandler and SubjectMapper.
	 */
	void loadConfig();
	/**
	 * Gets the access-manager
	 * @return the access-manager
	 */
	StelvioAccessManager getAccessManager();
	/**
	 * Gets the request-handler
	 * @return the request-handler
	 */
	SSORequestHandler getRequestHandler();
	/**
	 * Gets the subject-mapper
	 * @return the subject-mapper
	 */
	SubjectMapper getSubjectMapper();
}
