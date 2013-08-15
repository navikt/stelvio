package no.stelvio.presentation.security.sso;

import java.util.Properties;

import javax.servlet.http.Cookie;

import no.stelvio.presentation.security.sso.ConfigPropertyKeys;
import no.stelvio.presentation.security.sso.RequestValueKeys;
import no.stelvio.presentation.security.sso.RequestValueType;
import no.stelvio.presentation.security.sso.accessmanager.support.OpenAmAccessManager;
import no.stelvio.presentation.security.sso.accessmanager.support.WebSealAccessManager;
import no.stelvio.presentation.security.sso.ibm.MockWebsphereSubjectMapper;
import no.stelvio.presentation.security.sso.ibm.StelvioTai;
import no.stelvio.presentation.security.sso.ibm.StelvioTaiConfig;
import no.stelvio.presentation.security.sso.ibm.StelvioTaiPropertiesConfig;
import no.stelvio.presentation.security.sso.support.OpenAmRequestHandler;
import no.stelvio.presentation.security.sso.support.WebSealRequestHandler;

import org.springframework.mock.web.MockHttpServletRequest;

public abstract class AbstractTaiTest {
	
	/*protected SubjectMapper mapper = new SubjectMapper() {
				
		public Subject createSubject(StelvioPrincipal principal) throws Exception {
			Subject subject = new Subject();
			List<String> ldapGroups = new ArrayList<String>();
			//just for test purposes
			ldapGroups.add(principal.getAuthLevel());
			
			String uniqueid = principal.getUserId();
			//Use this + a timpestamp as cache_key
			String userId = principal.getUserId();
			
			String key = uniqueid + ldapGroups.toString();
			
			Hashtable<String, Object> hashtable = new Hashtable<String, Object>();
			hashtable.put(AttributeNameConstants.WSCREDENTIAL_UNIQUEID, uniqueid);
			hashtable.put(AttributeNameConstants.WSCREDENTIAL_SECURITYNAME, userId);
			hashtable.put(AttributeNameConstants.WSCREDENTIAL_GROUPS, ldapGroups);			
			hashtable.put(AttributeNameConstants.WSCREDENTIAL_CACHE_KEY, key);
			subject.getPublicCredentials().add(hashtable);
			return subject;
		}
		
	};*/
	protected MockWebsphereSubjectMapper mapper;
	protected WebSealRequestHandler webSealrequestHandler;
	protected WebSealAccessManager webSealaccessManager;
	protected OpenAmRequestHandler openAmRequestHandler;
	protected OpenAmAccessManager openAmAccessManager;
	
	public StelvioTai initializeStelvioTaiComponents(StelvioTaiConfig config){
		StelvioTai tai = new StelvioTai();		
		tai.initStelvioTaiComponents(config);
		return tai;
	}	
	public StelvioTaiPropertiesConfig getConfigurationForWebSEAL(){
		mapper = new MockWebsphereSubjectMapper();
		webSealrequestHandler = new WebSealRequestHandler();
		webSealaccessManager = new WebSealAccessManager();
		
		StelvioTaiPropertiesConfig config = new StelvioTaiPropertiesConfig();
		Properties ldapGroups = config.loadPropertiesFromFile("test-" + config.getLdapGroupsPropertiesFileName());
		Properties common = config.loadPropertiesFromFile("test-" + config.getCommonPropertiesFileName());
		//Can not test using the WebsphereSubjectMapper since it needs a connection to
		//the user-registry to retrieve ldapgroups.
		config.setSubjectMapper(mapper);
		
		webSealaccessManager.setGroupMap(ldapGroups);
		config.setAccessManager(webSealaccessManager);
		
		webSealrequestHandler.setAccessManagerUser(common.getProperty(ConfigPropertyKeys.ACCESS_MANAGER_USERNAME));
		webSealrequestHandler.setUsePacHeader(false);
		webSealrequestHandler.setRequestValueType(RequestValueType.HEADER);
		RequestValueKeys reqKeys = new RequestValueKeys();
		reqKeys.setOriginalUserNameKey(common.getProperty(ConfigPropertyKeys.ORIGINAL_USER_NAME_REQUEST_VALUE_KEY));
		reqKeys.setAuthenticationLevelKey(common.getProperty(ConfigPropertyKeys.AUTHENTICATION_LEVEL_REQUEST_VALUE_KEY));
		reqKeys.setAccessManagerUserKey(common.getProperty(ConfigPropertyKeys.ACCESS_MANAGER_USER_REQUEST_VALUE_KEY));
		reqKeys.setAuthorizedAsKey(common.getProperty(ConfigPropertyKeys.AUTHORIZED_AS_REQUEST_VALUE_KEY));
		reqKeys.setAuthorizationTypeKey(common.getProperty(ConfigPropertyKeys.AUTHORIZATION_TYPE_REQUEST_VALUE_KEY));
		webSealrequestHandler.setRequestValueKeys(reqKeys);
		config.setRequestHandler(webSealrequestHandler);
		
		return config;
	}
	public StelvioTaiPropertiesConfig getConfigurationForOpenAM(){
		mapper = new MockWebsphereSubjectMapper();
		openAmRequestHandler = new OpenAmRequestHandler();
		openAmAccessManager = new OpenAmAccessManager();
		
		StelvioTaiPropertiesConfig config = new StelvioTaiPropertiesConfig();
		Properties ldapGroups = config.loadPropertiesFromFile("test-" + config.getLdapGroupsPropertiesFileName());
		Properties common = config.loadPropertiesFromFile("test-" + config.getCommonPropertiesFileName());
		//Can not test using the WebsphereSubjectMapper since it needs a connection to
		//the user-registry to retrieve ldapgroups.
		config.setSubjectMapper(mapper);
		
		openAmAccessManager.setGroupMap(ldapGroups);
		config.setAccessManager(openAmAccessManager);
		
		openAmRequestHandler.setRequestValueType(RequestValueType.COOKIE);
		RequestValueKeys reqKeys = new RequestValueKeys();
		reqKeys.setCookieKey(common.getProperty(ConfigPropertyKeys.COOKIE_REQUEST_VALUE_KEY));
		openAmRequestHandler.setRequestValueKeys(reqKeys);
		config.setRequestHandler(openAmRequestHandler);
		
		return config;
	}
	public StelvioTaiPropertiesConfig changeRequestValueType(
			StelvioTaiPropertiesConfig config, RequestValueType type){
		
		if(config.getRequestHandler() instanceof WebSealRequestHandler) {
			((WebSealRequestHandler)config.getRequestHandler()).setRequestValueType(type);
		} else if (config.getRequestHandler() instanceof OpenAmRequestHandler) {
			((OpenAmRequestHandler)config.getRequestHandler()).setRequestValueType(type);
		}
		return config;
	}
	
	public class MockTaiHttpServletRequest extends MockHttpServletRequest {
		
		public void addValueToRequest(
				RequestValueType requestValueType, String key, String value){
			
			if (requestValueType == RequestValueType.HEADER){
				if(value != null){
					addHeader(key,value);
				}
			} else if( requestValueType == RequestValueType.ATTRIBUTE){
				setAttribute(key, value);
			} else if( requestValueType == RequestValueType.PARAMETER){
				addParameter(key, value);
			} else if( requestValueType == RequestValueType.SESSION_ATTRIBUTE){
				getSession().setAttribute(key, value);
			} else if( requestValueType == RequestValueType.COOKIE){
				setCookies(new Cookie[]{ new Cookie(key, value)});
			}  
		}
	}
	
}
