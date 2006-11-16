package no.stelvio.common.security.acegi;

import static org.junit.Assert.*;

import org.acegisecurity.vote.AccessDecisionVoter;
import org.junit.Before;
import org.junit.Test;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.SecurityConfig;
public class AlwaysAffirmativeVoterTest {

	private AccessDecisionVoter voter;
	@Before
	public void setUp() throws Exception {
		this.voter = new AlwaysAffirmativeVoter();
	}

	@Test
	public void testSupportsConfigAttribute() {
		assertTrue(voter.supports(new SecurityConfig("")));
	}

	@Test
	public void testSupportsClass() {
		assertTrue(voter.supports(Class.class));
	}

	@Test
	public void testVote() {
		assertEquals(AccessDecisionVoter.ACCESS_GRANTED,voter.vote(null, new Object(), new ConfigAttributeDefinition()));
	}

}
