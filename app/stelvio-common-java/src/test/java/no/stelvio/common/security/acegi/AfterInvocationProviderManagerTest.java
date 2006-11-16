package no.stelvio.common.security.acegi;

import static org.junit.Assert.*;

import org.acegisecurity.AccessDeniedException;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.SecurityConfig;
import org.acegisecurity.afterinvocation.AfterInvocationProvider;
import org.acegisecurity.vote.AccessDecisionVoter;
import org.junit.Test;

public class AfterInvocationProviderManagerTest {

	/**
	 * Add several valid AfterInvocationProviders represented by Strings.
	 * Expected outcome: All will be added to the list.
	 */
	@Test
	public void testAddProviders1() {
		
		ConfigAttributeDefinition config = new ConfigAttributeDefinition();
        config.addConfigAttribute(new SecurityConfig("no.stelvio.common.security.acegi.MockAfterInvocationProvider")); // grant
        config.addConfigAttribute(new SecurityConfig("no.stelvio.common.security.acegi.MockAfterInvocationProvider2")); // deny
        AfterInvocationProviderManager mgr = new AfterInvocationProviderManager();
        mgr.addProviders(config);
		assertEquals(config.size(), mgr.getProviders().size());    
	}
	
	/**
	 * Add a mix of AfterInvocationProviders and other classes represented by Strings.
	 * <p>Expected outcome: Only the AfterInvocationProviders will be added to the list.
	 */
	@Test
	public void testAddProviders2(){
		ConfigAttributeDefinition config = new ConfigAttributeDefinition();
        config.addConfigAttribute(new SecurityConfig("no.stelvio.common.security.acegi.MockAfterInvocationProvider")); // grant
        config.addConfigAttribute(new SecurityConfig("no.stelvio.common.security.acegi.MockAccessGrantedVoter")); // deny
        config.addConfigAttribute(new SecurityConfig("no.stelvio.common.security.acegi.MockAfterInvocationProvider2"));
        AfterInvocationProviderManager manager = new AfterInvocationProviderManager();
        manager.addProviders(config);	
        assertEquals(config.size() - 1, manager.getProviders().size());
	}
	
	/**
	 * Add a list of classes represented by Strings that doesn't implement the AfterInvocationProvider interface.
	 * <p><b>Expected outcome:</b> No classes should be added to the list. 
	 *
	 */
	@Test
	public void testAddProviders3(){
		ConfigAttributeDefinition config = new ConfigAttributeDefinition();
		config.addConfigAttribute(new SecurityConfig("no.stelvio.common.security.acegi.MockAccessDeniedVoter")); 
        config.addConfigAttribute(new SecurityConfig("no.stelvio.common.security.acegi.MockAccessGrantedVoter"));
        AfterInvocationProviderManager manager = new AfterInvocationProviderManager();
        manager.addProviders(config);	
        assertEquals(0,manager.getProviders().size());
	}
	
	
	/**
	 * Add several AccessDecsionVoters and a class that does not exist, 
	 * i.e. the string passed in is not an applicaple name of a class.
	 * <p>Expected outcome: An IllegalArgumentException should be thrown.
	 */
	@Test
	public void testAddProviders4(){
		ConfigAttributeDefinition config = new ConfigAttributeDefinition();
        config.addConfigAttribute(new SecurityConfig("no.stelvio.common.security.acegi.MockAfterInvocationProvider")); 
        config.addConfigAttribute(new SecurityConfig("AClassThatDoesntExist")); 
        config.addConfigAttribute(new SecurityConfig("no.stelvio.common.security.acegi.MockAfterInvocationProvider2"));
        AfterInvocationProviderManager manager = new AfterInvocationProviderManager();
        try{
        	manager.addProviders(config);
        	fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        	System.out.println("Exception caught:" + expected.getMessage());
            assertTrue(true);
        }    	
	}
	
	
	@Test
	public void testIsAfterInvocationProvider() {
		AccessDecisionVoter voter = new MockAccessGrantedVoter();
		AfterInvocationProvider provider = new MockAfterInvocationProvider();
		AfterInvocationProviderManager manager = new AfterInvocationProviderManager();
		
		assertTrue(!manager.isAfterInvocationProvider(voter.getClass()));
		assertTrue(manager.isAfterInvocationProvider(provider.getClass()));
	}
	
	@Test
	public void testDecideDoNotFilterObject(){
		ConfigAttributeDefinition config = new ConfigAttributeDefinition();
        config.addConfigAttribute(new SecurityConfig("no.stelvio.common.security.acegi.MockAfterInvocationProviderNoFiltering")); 
        AfterInvocationProviderManager manager = new AfterInvocationProviderManager();
		manager.addProviders(config);
		String returnedObject ="Before decide.";
		assertEquals(returnedObject, manager.decide(null, null, config, returnedObject));
	}
	
	@Test
	public void testDecideFilterObject(){
		ConfigAttributeDefinition config = new ConfigAttributeDefinition();
        config.addConfigAttribute(new SecurityConfig("no.stelvio.common.security.acegi.MockAfterInvocationProvider")); 
        AfterInvocationProviderManager manager = new AfterInvocationProviderManager();
        manager.addProviders(config);
		String originalObject ="Before decide.";
		assertNotSame(originalObject, manager.decide(null, null, config, originalObject));
        
	}
	@Test
	public void testDecideFilterObjectWithMultipleFilters(){
		ConfigAttributeDefinition config = new ConfigAttributeDefinition();
        config.addConfigAttribute(new SecurityConfig("no.stelvio.common.security.acegi.MockAfterInvocationProvider")); 
        config.addConfigAttribute(new SecurityConfig("no.stelvio.common.security.acegi.MockAfterInvocationProvider2"));
        AfterInvocationProviderManager manager = new AfterInvocationProviderManager();
        manager.addProviders(config);
		String originalObject ="Before decide.";
		assertNotSame(originalObject, manager.decide(null, null, config, originalObject));
	}
	@Test
	public void testDecideDenyAccess(){
		ConfigAttributeDefinition config = new ConfigAttributeDefinition();
        config.addConfigAttribute(new SecurityConfig("no.stelvio.common.security.acegi.MockAfterInvocationProviderDeniesAccess"));     
        AfterInvocationProviderManager manager = new AfterInvocationProviderManager();
        manager.addProviders(config);
        try{
        	manager.decide(null, null, config, new Object());
        	fail("Should have thrown AccessDeniedException");
        }catch(AccessDeniedException expected){
        	assertTrue(true);
        }
		
	}
	

}
