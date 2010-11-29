package no.stelvio.presentation.security.sso.ibm;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.Subject;

import org.springframework.mock.web.MockHttpServletRequest;

import no.stelvio.presentation.security.sso.DebugHelper;
import no.stelvio.presentation.security.sso.RequestValueType;
import no.stelvio.presentation.security.sso.accessmanager.StelvioPrincipal;
import no.stelvio.presentation.security.sso.accessmanager.SubjectMapper;
import no.stelvio.presentation.security.sso.support.WebSealRequestHandler;

import com.ibm.websphere.security.CustomRegistryException;
import com.ibm.websphere.security.EntryNotFoundException;
import com.ibm.websphere.security.UserRegistry;
import com.ibm.wsspi.security.token.AttributeNameConstants;

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
		/*Properties ldapGroups = config.loadPropertiesFromFile(config.getLdapGroupsPropertiesFileName());
		mapper.setGroupMap(ldapGroups);*/
		config.setSubjectMapper(mapper);
		
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
