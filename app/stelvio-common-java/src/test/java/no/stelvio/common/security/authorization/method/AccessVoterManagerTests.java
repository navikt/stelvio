package no.stelvio.common.security.authorization.method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import no.stelvio.common.security.authorization.method.AccessVoterManager;
import no.stelvio.common.security.authorization.method.AlwaysAffirmativeVoter;

import org.acegisecurity.AccessDeniedException;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.SecurityConfig;
import org.acegisecurity.afterinvocation.AfterInvocationProvider;
import org.acegisecurity.providers.TestingAuthenticationToken;
import org.acegisecurity.vote.AccessDecisionVoter;
import org.junit.Test;

public class AccessVoterManagerTests {

	
	@Test
	public void testCheckAllowIfAllAbstainDecisions() {
		AccessVoterManager mgr = new AccessVoterManager();
		try{
			mgr.checkAllowIfAllAbstainDecisions();
			fail("Should have thrown an AccessDeniedException.");
		}catch(AccessDeniedException expected){
			assertTrue(true);
		}
		mgr.setAllowIfAllAbstainDecisions(true);
		mgr.checkAllowIfAllAbstainDecisions();
		assertTrue(true);
	}

	/**
	 * Add several valid AccessDecisionVoters represented by Strings.
	 * Expected outcome: All will be added to the list.
	 */
	@Test
	public void testAddDecisionVoters1() {
		
		ConfigAttributeDefinition config = new ConfigAttributeDefinition();
        config.addConfigAttribute(new SecurityConfig("no.stelvio.common.security.acegi.MockAccessDeniedVoter")); // grant
        config.addConfigAttribute(new SecurityConfig("no.stelvio.common.security.acegi.MockAccessGrantedVoter")); // deny
        config.addConfigAttribute(new SecurityConfig("no.stelvio.common.security.acegi.MockAccessAbstainVoter"));
        AccessVoterManager manager = new AccessVoterManager();
        manager.addDecisionVoters(config);
		assertEquals(config.size(), manager.getDecisionVoters().size());    
	}
	
	/**
	 * Add a mix of AccessDecisionVoters and other classes represented by Strings.
	 * <p>Expected outcome: Only the AccessDecsionVoters will be added to the list.
	 */
	@Test
	public void testAddDecisionVoters2(){
		ConfigAttributeDefinition config = new ConfigAttributeDefinition();
        config.addConfigAttribute(new SecurityConfig("no.stelvio.common.security.acegi.MockAccessDeniedVoter")); // grant
        config.addConfigAttribute(new SecurityConfig("no.stelvio.common.security.acegi.MockAfterInvocationProvider")); // deny
        config.addConfigAttribute(new SecurityConfig("no.stelvio.common.security.acegi.MockAccessAbstainVoter"));
        AccessVoterManager manager = new AccessVoterManager();
        manager.addDecisionVoters(config);	
        assertEquals(config.size() - 1, manager.getDecisionVoters().size());
	}
	
	/**
	 * Add a list of classes represented by Strings that doesn't implement the AccessDecsionVoter interface.
	 * <p><b>Expected outcome:</b> Omits these classes and add one instance of the AlwaysAffirmativeVoter to the list. 
	 *
	 */
	@Test
	public void testAddDecisionVoters3(){
		ConfigAttributeDefinition config = new ConfigAttributeDefinition();
        config.addConfigAttribute(new SecurityConfig("no.stelvio.common.security.acegi.MockAfterInvocationProvider")); // grant
        config.addConfigAttribute(new SecurityConfig("no.stelvio.common.security.acegi.MockAfterInvocationProvider2")); // deny
        AccessVoterManager manager = new AccessVoterManager();
        manager.addDecisionVoters(config);	
        
        if(manager.getDecisionVoters().size() == 1){	
        	assertTrue(AlwaysAffirmativeVoter.class.isAssignableFrom(manager.getDecisionVoters().get(0).getClass()));
        }else{
        	fail("List should have included one instance of the AlwaysAffirmativeVoter.");
        }
	}
	
	
	/**
	 * Add several AccessDecsionVoters and a class that does not exist, 
	 * i.e. the string passed in is not an applicaple name of a class.
	 * <p>Expected outcome: An IllegalArgumentException should be thrown.
	 */
	@Test
	public void testAddDecisionVoters4(){
		ConfigAttributeDefinition config = new ConfigAttributeDefinition();
        config.addConfigAttribute(new SecurityConfig("no.stelvio.common.security.acegi.MockAccessDeniedVoter")); 
        config.addConfigAttribute(new SecurityConfig("AClassThatDoesntExist")); 
        config.addConfigAttribute(new SecurityConfig("no.stelvio.common.security.acegi.MockAccessAbstainVoter"));
        AccessVoterManager manager = new AccessVoterManager();
        try{
        	manager.addDecisionVoters(config);
        	fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            assertTrue(true);
        }    	
	}
	
	
	@Test
	public void testIsAccessDecisionVoter() {
		AccessDecisionVoter voter = new MockAccessGrantedVoter();
		AfterInvocationProvider provider = new MockAfterInvocationProvider();
		AccessVoterManager manager = new AccessVoterManager();
		
		assertTrue(manager.isAccessDecisionVoter(voter.getClass()));
		assertTrue(!manager.isAccessDecisionVoter(provider.getClass()));
	}

	private ConfigAttributeDefinition makeConfigAttributeDefinition() {
		ConfigAttributeDefinition config = new ConfigAttributeDefinition();
        config.addConfigAttribute(new SecurityConfig("no.stelvio.common.security.acegi.MockAccessDeniedVoter")); // grant
        config.addConfigAttribute(new SecurityConfig("no.stelvio.common.security.acegi.MockAccessGrantedVoter")); // deny
        config.addConfigAttribute(new SecurityConfig("no.stelvio.common.security.acegi.MockAccessAbstainVoter"));
        return config;
    }
	 private TestingAuthenticationToken makeTestToken() {
	        return new TestingAuthenticationToken("somebody", "password",
	            new GrantedAuthority[] {new GrantedAuthorityImpl("ROLE_1"), new GrantedAuthorityImpl("ROLE_2")});
	 }

	
	@Test
	public void testDecideVoterGrantsAccess() {
		AccessVoterManager mgr = new AccessVoterManager();
		TestingAuthenticationToken auth = makeTestToken();
		ConfigAttributeDefinition config = new ConfigAttributeDefinition();
        config.addConfigAttribute(new SecurityConfig("no.stelvio.common.security.acegi.MockAccessGrantedVoter"));
		mgr.decide(auth, new Object(), config);
		assertTrue(true);
	}
	
	@Test
	public void testDecideVoterDeniesAccess() {
		AccessVoterManager mgr = new AccessVoterManager();
		TestingAuthenticationToken auth = makeTestToken();
		ConfigAttributeDefinition config = new ConfigAttributeDefinition();
        config.addConfigAttribute(new SecurityConfig("no.stelvio.common.security.acegi.MockAccessDeniedVoter"));
		try{
			mgr.decide(auth, new Object(), config);
			fail("Should have thrown an AccessDeniedException.");
		}catch(AccessDeniedException expected){
			assertTrue(true);
		}	
	}
	@Test
	public void testDecideVoterAbstainAccessDoNotAllowAllAbstain() {
		AccessVoterManager mgr = new AccessVoterManager();
		TestingAuthenticationToken auth = makeTestToken();
		ConfigAttributeDefinition config = new ConfigAttributeDefinition();
        config.addConfigAttribute(new SecurityConfig("no.stelvio.common.security.acegi.MockAccessAbstainVoter"));
		try{
			mgr.decide(auth, new Object(), config);
			fail("Should have thrown an AccessDeniedException.");
		}catch(AccessDeniedException expected){
			assertTrue(true);
		}	
	}
	@Test
	public void testDecideVoterAbstainAccessAllowAllAbstain() {
		AccessVoterManager mgr = new AccessVoterManager();
		TestingAuthenticationToken auth = makeTestToken();
		ConfigAttributeDefinition config = new ConfigAttributeDefinition();
        config.addConfigAttribute(new SecurityConfig("no.stelvio.common.security.acegi.MockAccessAbstainVoter"));
        mgr.setAllowIfAllAbstainDecisions(true);  
		mgr.decide(auth, new Object(), config);
		assertTrue(true);		
	}
	
	@Test
	public void testGettersAndSetters(){
		AccessVoterManager mgr = new AccessVoterManager();
		ConfigAttributeDefinition config = makeConfigAttributeDefinition();
		mgr.addDecisionVoters(config);
		mgr.setAllowIfAllAbstainDecisions(true);
		ConfigAttribute attr = new SecurityConfig("test");
		
		assertEquals(config.size(), mgr.getDecisionVoters().size());
		assertEquals(true,mgr.isAllowIfAllAbstainDecisions());
		assertTrue(mgr.supports(attr));
		assertTrue(mgr.supports(new Object().getClass()));	
	}

	

}




