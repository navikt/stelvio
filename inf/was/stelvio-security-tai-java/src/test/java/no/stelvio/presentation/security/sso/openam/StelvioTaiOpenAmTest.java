package no.stelvio.presentation.security.sso.openam;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


import no.stelvio.presentation.security.sso.AbstractTaiTest;
import no.stelvio.presentation.security.sso.RequestValueType;
import no.stelvio.presentation.security.sso.ibm.StelvioTai;
import no.stelvio.presentation.security.sso.ibm.StelvioTaiPropertiesConfig;
import no.stelvio.presentation.security.sso.support.OpenAmRequestHandler;

import org.junit.Before;
import org.junit.Test;

public class StelvioTaiOpenAmTest extends AbstractTaiTest{

	private String originalUser;
	private String authenticationLevel;

	@Before
	public void setUp() throws Exception {
			
	}
	private void setRequestValues(String originalUser, String authenticationLevel){
		this.originalUser = originalUser;
		this.authenticationLevel = authenticationLevel;
	}
	
	public MockTaiHttpServletRequest getRequest(OpenAmRequestHandler handler) {
		
		RequestValueType valueType = handler.getRequestValueType();
		MockTaiHttpServletRequest request = new MockTaiHttpServletRequest();
		
		request.addValueToRequest(valueType,
					"nav-esso", originalUser + "-" + authenticationLevel);
		return request;
	}
	
	/**
	 * Test the isTargetInterceptor with valid values in the request. Tests all the different 
	 * requestvalue types: Header, Attribute, Parameter and Session attribute.
	 */
	@Test
	public void testIsTargetInterceptorTrue() {
		
		StelvioTaiPropertiesConfig config = getConfigurationForOpenAM();
		StelvioTai tai = initializeStelvioTaiComponents(config);
		OpenAmRequestHandler handler = (OpenAmRequestHandler)config.getRequestHandler();
		
		setRequestValues("12345678901", "3");
		MockTaiHttpServletRequest request = getRequest(handler);
		
		try {
			
		//Test with RequestValueType from configuration 
		assertTrue("Testing with RequestValueType from config: " + handler.getRequestValueType()
						,tai.isTargetInterceptor(request));
		
		//		Test with RequestValueType - COOKIE
		config = changeRequestValueType(config,RequestValueType.COOKIE);
		tai = initializeStelvioTaiComponents(config);
		request = getRequest((OpenAmRequestHandler)config.getRequestHandler());
		assertTrue("Testing with RequestValueType.COOKIE",tai.isTargetInterceptor(request));
		
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
		
		StelvioTaiPropertiesConfig config = getConfigurationForOpenAM();
		StelvioTai tai = initializeStelvioTaiComponents(config);
		
		MockTaiHttpServletRequest request = new MockTaiHttpServletRequest();
		
		try {			 
			assertFalse("Testing with no cookie. Should return false.", tai.isTargetInterceptor(request));		
		} catch (Exception unexpected){
			unexpected.printStackTrace();
			fail("Unexpected exception occured.");			
		}
		
	}

}
