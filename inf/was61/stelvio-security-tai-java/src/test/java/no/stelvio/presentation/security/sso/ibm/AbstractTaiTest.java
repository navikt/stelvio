package no.stelvio.presentation.security.sso.ibm;

import java.util.Properties;

import no.stelvio.presentation.security.sso.ConfigPropertyKeys;
import no.stelvio.presentation.security.sso.RequestValueKeys;
import no.stelvio.presentation.security.sso.RequestValueType;
import no.stelvio.presentation.security.sso.accessmanager.support.WebSealAccessManager;
import no.stelvio.presentation.security.sso.support.WebSealRequestHandler;

import org.springframework.mock.web.MockHttpServletRequest;

public abstract class AbstractTaiTest {
	
	protected boolean useTaiDebug = false;
	
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
	protected MockWebsphereSubjectMapper mapper = new MockWebsphereSubjectMapper();
	protected WebSealRequestHandler requestHandler = new WebSealRequestHandler();
	protected WebSealAccessManager accessManager = new WebSealAccessManager();
	
	protected StelvioTai initializeStelvioTaiComponents(StelvioTaiConfig config){
		StelvioTai tai = new StelvioTai();
		if(useTaiDebug){
			Properties props = new Properties();
			props.put("debug", "true");
			//tai.setDebug(props); 
		}
		tai.initStelvioTaiComponents(config);
		return tai;
	}	
	protected StelvioTaiPropertiesConfig getConfiguration(){
		StelvioTaiPropertiesConfig config = new StelvioTaiPropertiesConfig();
		//Can not test using the WebsphereSubjectMapper since it needs a connection to
		//the user-registry to retrieve ldapgroups.
		config.setSubjectMapper(mapper);
		Properties ldapGroups = config.loadPropertiesFromFile("test-" + config.getLdapGroupsPropertiesFileName());
		Properties common = config.loadPropertiesFromFile("test-" + config.getCommonPropertiesFileName());
		accessManager.setGroupMap(ldapGroups);
		config.setAccessManager(accessManager);
		requestHandler.setAccessManagerUser(common.getProperty(ConfigPropertyKeys.ACCESS_MANAGER_USERNAME));
		requestHandler.setUsePacHeader(false);
		requestHandler.setRequestValueType(RequestValueType.HEADER);
		RequestValueKeys reqKeys = new RequestValueKeys();
		reqKeys.setOriginalUserNameKey(common.getProperty(ConfigPropertyKeys.ORIGINAL_USER_NAME_REQUEST_VALUE_KEY));
		reqKeys.setAuthenticationLevelKey(common.getProperty(ConfigPropertyKeys.AUTHENTICATION_LEVEL_REQUEST_VALUE_KEY));
		reqKeys.setAccessManagerUserKey(common.getProperty(ConfigPropertyKeys.ACCESS_MANAGER_USER_REQUEST_VALUE_KEY));
		reqKeys.setAuthorizedAsKey(common.getProperty(ConfigPropertyKeys.AUTHORIZED_AS_REQUEST_VALUE_KEY));
		reqKeys.setAuthorizationTypeKey(common.getProperty(ConfigPropertyKeys.AUTHORIZATION_TYPE_REQUEST_VALUE_KEY));
		requestHandler.setRequestValueKeys(reqKeys);
		

		config.setRequestHandler(requestHandler);
		
		return config;
	}
	protected StelvioTaiPropertiesConfig changeRequestValueType(
			StelvioTaiPropertiesConfig config, RequestValueType type){
		
		((WebSealRequestHandler)config.getRequestHandler()).setRequestValueType(type);
		return config;
	}
	
	protected class MockTaiHttpServletRequest extends MockHttpServletRequest {
		
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
			} 
		}
	}
	
}
