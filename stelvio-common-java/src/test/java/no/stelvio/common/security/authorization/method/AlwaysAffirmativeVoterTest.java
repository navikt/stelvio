package no.stelvio.common.security.authorization.method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;

import java.util.ArrayList;

/**
 * Test class for AlwaysAffirmativeVoter.
 * 
 *
 */
public class AlwaysAffirmativeVoterTest {

	private AccessDecisionVoter voter;

	/**
	 * Set up voter.
	 *
	 */
	@Before
	public void setUp() {
		this.voter = new AlwaysAffirmativeVoter();
	}

	/**
	 * Test supports, config attribute.
	 */
	@Test
	public void testSupportsConfigAttribute() {
		assertTrue(voter.supports(new SecurityConfig("attributt")));
	}

	/**
	 * Test supports, class.
	 */
	@Test
	public void testSupportsClass() {
		assertTrue(voter.supports(Class.class));
	}

	/**
	 * Test vote.
	 */
	@Test
	public void testVote() {
		assertEquals(AccessDecisionVoter.ACCESS_GRANTED, voter.vote(null, new Object(), new ArrayList<>()));
	}

}
