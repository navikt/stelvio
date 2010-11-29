package no.stelvio.common.security.authorization.method;

import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.SecurityConfig;
import org.acegisecurity.vote.AccessDecisionVoter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for AlwaysAffirmativeVoter.
 * 
 * @author ??
 * 
 */
public class AlwaysAffirmativeVoterTest {

	private AccessDecisionVoter voter;

	/**
	 * Set up voter.
	 * 
	 * @throws Exception
	 *             if error
	 */
	@Before
	public void setUp() throws Exception {
		this.voter = new AlwaysAffirmativeVoter();
	}

	/**
	 * Test supports, config attribute.
	 */
	@Test
	public void testSupportsConfigAttribute() {
		assertTrue(voter.supports(new SecurityConfig("")));
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
		assertEquals(AccessDecisionVoter.ACCESS_GRANTED, voter.vote(null, new Object(), new ConfigAttributeDefinition()));
	}

}
