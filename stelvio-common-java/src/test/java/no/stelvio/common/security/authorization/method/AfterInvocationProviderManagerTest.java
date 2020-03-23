package no.stelvio.common.security.authorization.method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AfterInvocationProvider;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Test class for AfterInvocationProviderManager.
 * 
 *
 */
public class AfterInvocationProviderManagerTest {

	/**
	 * Add several valid AfterInvocationProviders represented by Strings. Expected outcome: All will be added to the list.
	 */
	@Test
	public void testAddProviders1() {

        Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
        configAttributes.add(new SecurityConfig(
                "no.stelvio.common.security.authorization.method.MockAfterInvocationProvider")); // grant
        configAttributes.add(new SecurityConfig(
                "no.stelvio.common.security.authorization.method.MockAfterInvocationProvider2")); // deny
		AfterInvocationProviderManager mgr = new AfterInvocationProviderManager();
		mgr.addProviders(configAttributes);
		assertEquals(configAttributes.size(), mgr.getProviders().size());
	}

	/**
	 * Add a mix of AfterInvocationProviders and other classes represented by Strings.
	 * <p>
	 * Expected outcome: Only the AfterInvocationProviders will be added to the list.
	 */
	@Test
	public void testAddProviders2() {
        Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
        configAttributes.add(new SecurityConfig(
                "no.stelvio.common.security.authorization.method.MockAfterInvocationProvider")); // grant
        configAttributes.add(new SecurityConfig(
                "no.stelvio.common.security.authorization.method.MockAccessGrantedVoter")); // deny
        configAttributes.add(new SecurityConfig(
                "no.stelvio.common.security.authorization.method.MockAfterInvocationProvider2"));
		AfterInvocationProviderManager manager = new AfterInvocationProviderManager();
		manager.addProviders(configAttributes);
		assertEquals(configAttributes.size() - 1, manager.getProviders().size());
	}

	/**
	 * Add a list of classes represented by Strings that doesn't implement the AfterInvocationProvider interface.
	 * <p>
	 * <b>Expected outcome:</b> No classes should be added to the list.
	 * 
	 */
	@Test
	public void testAddProviders3() {
		Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
        configAttributes.add(new SecurityConfig("no.stelvio.common.security.authorization.method.MockAccessDeniedVoter"));
        configAttributes.add(new SecurityConfig("no.stelvio.common.security.authorization.method.MockAccessGrantedVoter"));
		AfterInvocationProviderManager manager = new AfterInvocationProviderManager();
		manager.addProviders(configAttributes);
		assertEquals(0, manager.getProviders().size());
	}

	/**
	 * Add several AccessDecsionVoters and a class that does not exist, i.e. the string passed in is not an applicaple name of a
	 * class.
	 * <p>
	 * Expected outcome: An IllegalArgumentException should be thrown.
	 */
	@Test
	public void testAddProviders4() {
		Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
        configAttributes.add(new SecurityConfig(
                "no.stelvio.common.security.authorization.method.MockAfterInvocationProvider"));
        configAttributes.add(new SecurityConfig("AClassThatDoesntExist"));
        configAttributes.add(new SecurityConfig(
                "no.stelvio.common.security.authorization.method.MockAfterInvocationProvider2"));
		AfterInvocationProviderManager manager = new AfterInvocationProviderManager();
		try {
			manager.addProviders(configAttributes);
			fail("Should have thrown AfterInvocationProviderNotFoundException");
		} catch (AfterInvocationProviderNotFoundException expected) {
			assertTrue(true);
		}
	}

	/**
	 * Test isAfter.
	 */
	@Test
	public void testIsAfterInvocationProvider() {
		AccessDecisionVoter voter = new MockAccessGrantedVoter();
		AfterInvocationProvider provider = new MockAfterInvocationProvider();
		AfterInvocationProviderManager manager = new AfterInvocationProviderManager();

		assertTrue(!manager.isAfterInvocationProvider(voter.getClass()));
		assertTrue(manager.isAfterInvocationProvider(provider.getClass()));
	}

	/**
	 * Test decide, do not filter object.
	 */
	@Test
	public void testDecideDoNotFilterObject() {
		Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
		configAttributes.add(new SecurityConfig(
                "no.stelvio.common.security.authorization.method.MockAfterInvocationProviderNoFiltering"));
		AfterInvocationProviderManager manager = new AfterInvocationProviderManager();
		manager.addProviders(configAttributes);
		String returnedObject = "Before decide.";
		assertEquals(returnedObject, manager.decide(null, null, configAttributes, returnedObject));
	}

	/**
	 * Test decide, filter object.
	 */
	@Test
	public void testDecideFilterObject() {
		Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
		configAttributes.add(new SecurityConfig(
                "no.stelvio.common.security.authorization.method.MockAfterInvocationProvider"));
		AfterInvocationProviderManager manager = new AfterInvocationProviderManager();
		manager.addProviders(configAttributes);
		String originalObject = "Before decide.";
		assertNotSame(originalObject, manager.decide(null, null, configAttributes, originalObject));

	}

	/**
	 * Test decide, filter object with multiple filters.
	 */
	@Test
	public void testDecideFilterObjectWithMultipleFilters() {
		Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
		configAttributes.add(new SecurityConfig(
                "no.stelvio.common.security.authorization.method.MockAfterInvocationProvider"));
		configAttributes.add(new SecurityConfig(
                "no.stelvio.common.security.authorization.method.MockAfterInvocationProvider2"));
		AfterInvocationProviderManager manager = new AfterInvocationProviderManager();
		manager.addProviders(configAttributes);
		String originalObject = "Before decide.";
		assertNotSame(originalObject, manager.decide(null, null, configAttributes, originalObject));
	}

	/**
	 * Test decide, deny access.
	 */
	@Test
	public void testDecideDenyAccess() {
		Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
		configAttributes.add(new SecurityConfig(
                "no.stelvio.common.security.authorization.method.MockAfterInvocationProviderDeniesAccess"));
		AfterInvocationProviderManager manager = new AfterInvocationProviderManager();
		manager.addProviders(configAttributes);
		try {
			manager.decide(null, null, configAttributes, new Object());
			fail("Should have thrown MethodAccessDeniedException");
		} catch (MethodAccessDeniedException expected) {
			assertTrue(true);
		}

	}

}
