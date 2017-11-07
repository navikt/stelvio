package no.stelvio.common.security.authorization.method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AfterInvocationProvider;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Test class for AccessVoterManager.
 * 
 * @author person08f1a7c6db2c, Accenture
 * 
 */
public class AccessVoterManagerTest {
	/**
	 * Test checkAllowIfAllAbstainDecisions.
	 */
	@Test
	public void testCheckAllowIfAllAbstainDecisions() {

		AccessVoterManager mgr = new AccessVoterManager();
		try {
			mgr.checkAllowIfAllAbstainDecisions(new Object());
			fail("Should have thrown an MethodAccessDeniedException.");
		} catch (MethodAccessDeniedException expected) {
			// should happen
		}

		mgr.setAllowIfAllAbstainDecisions(true);
		mgr.checkAllowIfAllAbstainDecisions(new Object());
	}

	/**
	 * Add several valid AccessDecisionVoters represented by Strings. Expected outcome: All will be added to the list.
	 */
	@Test
	public void testAddDecisionVoters1() {

        Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
        configAttributes.add(
                new SecurityConfig("no.stelvio.common.security.authorization.method.MockAccessDeniedVoter")); // grant
        configAttributes.add(
                new SecurityConfig("no.stelvio.common.security.authorization.method.MockAccessGrantedVoter")); // deny
        configAttributes.add(new SecurityConfig("no.stelvio.common.security.authorization.method.MockAccessAbstainVoter"));
		AccessVoterManager manager = new AccessVoterManager();
		manager.addDecisionVoters(configAttributes);
		assertEquals(configAttributes.size(), manager.getDecisionVoters().size());
	}

	/**
	 * Add a mix of AccessDecisionVoters and other classes represented by Strings.
	 * <p>
	 * Expected outcome: Only the AccessDecsionVoters will be added to the list.
	 */
	@Test
	public void testAddDecisionVoters2() {
        Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
        configAttributes.add(
                new SecurityConfig("no.stelvio.common.security.authorization.method.MockAccessDeniedVoter")); // grant
        configAttributes.add(new SecurityConfig(
                "no.stelvio.common.security.authorization.method.MockAfterInvocationProvider")); // deny
        configAttributes.add(new SecurityConfig("no.stelvio.common.security.authorization.method.MockAccessAbstainVoter"));
		AccessVoterManager manager = new AccessVoterManager();
		manager.addDecisionVoters(configAttributes);
		assertEquals(configAttributes.size() - 1, manager.getDecisionVoters().size());
	}

	/**
	 * Add a list of classes represented by Strings that doesn't implement the AccessDecsionVoter interface.
	 * <p>
	 * <b>Expected outcome:</b> Omits these classes and add one instance of the AlwaysAffirmativeVoter to the list.
	 * 
	 */
	@Test
	public void testAddDecisionVoters3() {
        Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
        configAttributes.add(new SecurityConfig(
                "no.stelvio.common.security.authorization.method.MockAfterInvocationProvider")); // grant
        configAttributes.add(new SecurityConfig(
                "no.stelvio.common.security.authorization.method.MockAfterInvocationProvider2")); // deny
		AccessVoterManager manager = new AccessVoterManager();
		manager.addDecisionVoters(configAttributes);

		if (manager.getDecisionVoters().size() == 1) {
			assertTrue(AlwaysAffirmativeVoter.class.isAssignableFrom(manager.getDecisionVoters().get(0).getClass()));
		} else {
			fail("List should have included one instance of the AlwaysAffirmativeVoter.");
		}
	}

	/**
	 * Add several AccessDecsionVoters and a class that does not exist, i.e. the string passed in is not an applicaple name of a
	 * class.
	 * <p>
	 * Expected outcome: An IllegalArgumentException should be thrown.
	 */
	@Test
	public void testAddDecisionVoters4() {
        Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
        configAttributes.add(new SecurityConfig("no.stelvio.common.security.authorization.method.MockAccessDeniedVoter"));
        configAttributes.add(new SecurityConfig("AClassThatDoesntExist"));
        configAttributes.add(new SecurityConfig("no.stelvio.common.security.authorization.method.MockAccessAbstainVoter"));
		AccessVoterManager manager = new AccessVoterManager();
		try {
			manager.addDecisionVoters(configAttributes);
			fail("Should have thrown AccessDecisionVoterNotFoundException");
		} catch (AccessDecisionVoterNotFoundException expected) {
			// should happen
		}
	}

	/**
	 * Test isAccessDecisionVoter.
	 */
	@Test
	public void testIsAccessDecisionVoter() {
		AccessDecisionVoter voter = new MockAccessGrantedVoter();
		AfterInvocationProvider provider = new MockAfterInvocationProvider();
		AccessVoterManager manager = new AccessVoterManager();

		assertTrue(manager.isAccessDecisionVoter(voter.getClass()));
		assertTrue(!manager.isAccessDecisionVoter(provider.getClass()));
	}

	/**
	 * Make config attribute definition.
	 * 
	 * @return definition
	 */
	private  Collection<ConfigAttribute> makeConfigAttributeDefinition() {
        Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
        configAttributes.add(
                new SecurityConfig("no.stelvio.common.security.authorization.method.MockAccessDeniedVoter")); // grant
        configAttributes.add(
                new SecurityConfig("no.stelvio.common.security.authorization.method.MockAccessGrantedVoter")); // deny
        configAttributes.add(new SecurityConfig("no.stelvio.common.security.authorization.method.MockAccessAbstainVoter"));
		return configAttributes;
	}

	/**
	 * Make test token.
	 * 
	 * @return token
	 */
	private TestingAuthenticationToken makeTestToken() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_1"));
        grantedAuthorities.add(new SimpleGrantedAuthority ("ROLE_2"));

		return new TestingAuthenticationToken("somebody", "password", grantedAuthorities);
	}

	/**
	 * Test decide with voter grants access.
	 */
	@Test
	public void testDecideVoterGrantsAccess() {
		AccessVoterManager mgr = new AccessVoterManager();
		TestingAuthenticationToken auth = makeTestToken();
        Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
        configAttributes.add(new SecurityConfig("no.stelvio.common.security.authorization.method.MockAccessGrantedVoter"));
		mgr.decide(auth, new Object(), configAttributes);
	}

	/**
	 * Test decide with voter denies access.
	 */
	@Test
	public void testDecideVoterDeniesAccess() {
		AccessVoterManager mgr = new AccessVoterManager();
		TestingAuthenticationToken auth = makeTestToken();
        Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
        configAttributes.add(new SecurityConfig("no.stelvio.common.security.authorization.method.MockAccessDeniedVoter"));
		try {
			mgr.decide(auth, new Object(), configAttributes);
			fail("Should have thrown an MethodAccessDeniedException.");
		} catch (MethodAccessDeniedException expected) {
			// should happen
		}
	}

	/**
	 * Test decide with voter abstain access.
	 */
	@Test
	public void testDecideVoterAbstainAccessDoNotAllowAllAbstain() {
		AccessVoterManager mgr = new AccessVoterManager();
		TestingAuthenticationToken auth = makeTestToken();
        Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
        configAttributes.add(new SecurityConfig("no.stelvio.common.security.authorization.method.MockAccessAbstainVoter"));
		try {
			mgr.decide(auth, new Object(), configAttributes);
			fail("Should have thrown an MethodAccessDeniedException.");
		} catch (MethodAccessDeniedException expected) {
			// should happen
		}
	}

	/**
	 * Test decide with voter abstain access.
	 */
	@Test
	public void testDecideVoterAbstainAccessAllowAllAbstain() {
		AccessVoterManager mgr = new AccessVoterManager();
		TestingAuthenticationToken auth = makeTestToken();
        Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
        configAttributes.add(new SecurityConfig("no.stelvio.common.security.authorization.method.MockAccessAbstainVoter"));
		mgr.setAllowIfAllAbstainDecisions(true);
		mgr.decide(auth, new Object(), configAttributes);
	}

	/**
	 * Test access methods.
	 */
	@Test
	public void testGettersAndSetters() {
		AccessVoterManager mgr = new AccessVoterManager();
        Collection<ConfigAttribute> configAttributes = makeConfigAttributeDefinition();
		mgr.addDecisionVoters(configAttributes);
		mgr.setAllowIfAllAbstainDecisions(true);
		ConfigAttribute attr = new SecurityConfig("test");

		assertEquals(configAttributes.size(), mgr.getDecisionVoters().size());
		assertEquals(true, mgr.isAllowIfAllAbstainDecisions());
		assertTrue(mgr.supports(attr));
		assertTrue(mgr.supports(Object.class));
	}

}
