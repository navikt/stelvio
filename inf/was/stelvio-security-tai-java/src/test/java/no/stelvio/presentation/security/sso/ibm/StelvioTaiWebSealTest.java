package no.stelvio.presentation.security.sso.ibm;

import static org.junit.Assert.*;

import java.util.Hashtable;

import javax.security.auth.Subject;

import no.stelvio.presentation.security.sso.AbstractTaiTest;
import no.stelvio.presentation.security.sso.RequestValueKeys;
import no.stelvio.presentation.security.sso.RequestValueType;
import no.stelvio.presentation.security.sso.accessmanager.PrincipalNotValidException;
import no.stelvio.presentation.security.sso.support.WebSealRequestHandler;

import org.junit.Before;
import org.junit.Test;

import com.ibm.wsspi.security.token.AttributeNameConstants;

public class StelvioTaiWebSealTest extends AbstractTaiTest{

	private String originalUser;
	private String accessManagerUser;
	private String authenticationLevel;
	private String authorizedAs;
	private String authorizationType;

	@Before
	public void setUp() throws Exception {
			
	}
	private void setRequestValues(String originalUser, String authenticationLevel, String accessManagerUser){
		this.originalUser =originalUser;
		this.authenticationLevel = authenticationLevel;
		this.accessManagerUser = accessManagerUser;
	}
	
	private void setAuthorizedAsRequestValues(String originalUser, String authenticationLevel, String accessManagerUser, String authorizedAs, String authorizationType){
		this.originalUser =originalUser;
		this.authenticationLevel = authenticationLevel;
		this.accessManagerUser = accessManagerUser;
		this.authorizedAs = authorizedAs;
		this.authorizationType = authorizationType;
	}
	
	public MockTaiHttpServletRequest getRequest(WebSealRequestHandler handler) {
		
		RequestValueType valueType = handler.getRequestValueType();
		RequestValueKeys keys = handler.getRequestValueKeys();
		MockTaiHttpServletRequest request = new MockTaiHttpServletRequest();
		
		request.addValueToRequest(valueType,
					"original-user-id", originalUser);
		request.addValueToRequest(valueType,
					"iv-user", 
					accessManagerUser);
		request.addValueToRequest(valueType, 
					"authentication-level", authenticationLevel);	
		request.addValueToRequest(valueType,keys.getAuthorizedAsKey(), authorizedAs);
		request.addValueToRequest(valueType,keys.getAuthorizationTypeKey(), authorizationType);
		return request;
	}
	
	/**
	 * Test the isTargetInterceptor with valid values in the request. Tests all the different 
	 * requestvalue types: Header, Attribute, Parameter and Session attribute.
	 */
	@Test
	public void testIsTargetInterceptorTrue() {
		
		StelvioTaiPropertiesConfig config = getConfigurationForWebSEAL();
		StelvioTai tai = initializeStelvioTaiComponents(config);
		WebSealRequestHandler handler = (WebSealRequestHandler)config.getRequestHandler();
		
		setRequestValues("12345678901", "1", handler.getAccessManagerUser());
		MockTaiHttpServletRequest request = getRequest(handler);
		
		try {
			
		//Test with RequestValueType from configuration 
		assertTrue("Testing with RequestValueType from config: " + handler.getRequestValueType()
						,tai.isTargetInterceptor(request));
		
		//		Test with RequestValueType - HEADER
		config = changeRequestValueType(config,RequestValueType.HEADER);
		tai = initializeStelvioTaiComponents(config);
		request = getRequest((WebSealRequestHandler)config.getRequestHandler());
		assertTrue("Testing with RequestValueType.HEADER", tai.isTargetInterceptor(request));
		
		//		Test with RequestValueType - ATTRIBUTE
		config = changeRequestValueType(config,RequestValueType.ATTRIBUTE);
		tai = initializeStelvioTaiComponents(config);
		request = getRequest((WebSealRequestHandler)config.getRequestHandler());
		assertTrue("Testing with RequestValueType.ATTRIBUTE",tai.isTargetInterceptor(request));
		
		//		Test with RequestValueType - PARAMETER
		config = changeRequestValueType(config,RequestValueType.PARAMETER);
		tai = initializeStelvioTaiComponents(config);
		request = getRequest((WebSealRequestHandler)config.getRequestHandler());
		assertTrue("Testing with RequestValueType.PARAMETER",tai.isTargetInterceptor(request));
		
		//		Test with RequestValueType - SESSION_ATTRIBUTE
		config = changeRequestValueType(config,RequestValueType.SESSION_ATTRIBUTE);
		tai = initializeStelvioTaiComponents(config);
		request = getRequest((WebSealRequestHandler)config.getRequestHandler());
		assertTrue("Testing with RequestValueType.SESSION_ATTRIBUTE",tai.isTargetInterceptor(request));
		
		} catch (Exception unexpected){
			unexpected.printStackTrace();
			fail("Unexpected exception occured.");			
		}
		
	}
	
	
	
	/**
	 * Test the isTargetInterceptor with invalid values in the request. Only tests the  
	 * requestvalue type that is configured in the stelvio-tai-common.properties file.
	 */
	@Test
	public void testIsTargetInterceptorFalse() {
		
		StelvioTaiPropertiesConfig config = getConfigurationForWebSEAL();
		StelvioTai tai = initializeStelvioTaiComponents(config);
		WebSealRequestHandler handler = (WebSealRequestHandler)config.getRequestHandler();
		
		setRequestValues(null, "1", handler.getAccessManagerUser());
		
		MockTaiHttpServletRequest request = getRequest(handler);
		
		try {
			 
		assertFalse("Testing with no original user value. Should return false.",tai.isTargetInterceptor(request));
		
		setRequestValues("12345678901", "1", null);
		request = getRequest(handler);
		assertFalse("Testing with no access-manager user value. Should return false.",tai.isTargetInterceptor(request));
		
		setRequestValues("12345678901", "1", "WrongUser");
		request = getRequest(handler);
		assertFalse("Testing with the wrong access-manager user value. Should return false.",tai.isTargetInterceptor(request));
		
		} catch (Exception unexpected){
			unexpected.printStackTrace();
			fail("Unexpected exception occured.");			
		}
		
	}
	@Test
	public void testAutenticate(){
		StelvioTaiPropertiesConfig config = getConfigurationForWebSEAL();
		StelvioTai tai = initializeStelvioTaiComponents(config);
		WebSealRequestHandler handler = (WebSealRequestHandler)config.getRequestHandler();
		setRequestValues("12345678901", "1", handler.getAccessManagerUser());	
		
		MockTaiHttpServletRequest request = getRequest(handler);
		try {
			Subject subject = tai.authenticate(request);
		} catch (Exception unexpected){
			unexpected.printStackTrace();
			fail("Unexpected exception occured.");
		}
	}
	
	@Test
	public void testAutenticateWithAuthorizedAsAnotherUser(){
		StelvioTaiPropertiesConfig config = getConfigurationForWebSEAL();
		StelvioTai tai = initializeStelvioTaiComponents(config);
		WebSealRequestHandler handler = (WebSealRequestHandler)config.getRequestHandler();
		setAuthorizedAsRequestValues("12345678901", "1", handler.getAccessManagerUser(),"22222222222", "FULLMAKT_FULLSTENDIG");
		
		MockTaiHttpServletRequest request = getRequest(handler);
		try {
			Subject subject = tai.authenticate(request);
			
			Hashtable hash = (Hashtable)subject.getPublicCredentials().iterator().next();
			System.out.println("WSCREDENTIAL_UNIQUEID: " + hash.get(AttributeNameConstants.WSCREDENTIAL_UNIQUEID));
			System.out.println("no.stelvio.presentation.security.sso.ibm.WebsphereSubjectMapper.AUTHORIZED_AS: " 
					+ hash.get("no.stelvio.presentation.security.sso.ibm.WebsphereSubjectMapper.AUTHORIZED_AS"));
			System.out.println("WSCREDENTIAL_GROUPS: " + hash.get(AttributeNameConstants.WSCREDENTIAL_GROUPS));
			
		} catch (Exception unexpected){
			unexpected.printStackTrace();
			fail("Unexpected exception occured.");
		}
	}
	
	@Test
	public void testAutenticateWithAuthorizationTypeNotFound(){
		StelvioTaiPropertiesConfig config = getConfigurationForWebSEAL();
		StelvioTai tai = initializeStelvioTaiComponents(config);
		WebSealRequestHandler handler = (WebSealRequestHandler)config.getRequestHandler();
		setAuthorizedAsRequestValues("12345678901", "1", handler.getAccessManagerUser(),"22222222222", "DOES_NOT_EXIST_IN_PROPS");
		
		MockTaiHttpServletRequest request = getRequest(handler);
		try {
			Subject subject = tai.authenticate(request);
		} catch (Exception expected){
			if(expected instanceof PrincipalNotValidException){
				assertTrue(true);
			} else {
				expected.printStackTrace();
				fail("Unexpected exception occured.");
			}
			
			
		}
	}
	
	@Test
	public void testAuthenticateThrowsPrincipalNotValidException(){
		StelvioTaiPropertiesConfig config = getConfigurationForWebSEAL();
		StelvioTai tai = initializeStelvioTaiComponents(config);
		WebSealRequestHandler handler = (WebSealRequestHandler)config.getRequestHandler();
		setRequestValues("12345678901", null , handler.getAccessManagerUser());	
		
		MockTaiHttpServletRequest request = getRequest(handler);
		try {
			Subject subject = tai.authenticate(request);
		} catch (PrincipalNotValidException expected){
			assertTrue(true);
		} catch (Exception unexpected){
			unexpected.printStackTrace();
			fail("Unexpected exception occured.");
		}
	}

}
