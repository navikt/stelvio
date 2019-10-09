package no.stelvio.common.security.authorization.method;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for RolesUtil.
 * 
 * @author ??
 * 
 */
public class RolesUtilTest {

	/**
	 * Nothing to set up.
	 *
     */
	@Before
	public void setUp() {
	}

	/**
	 * Test getAnnotationRoles with a metod.
	 */
	@Test
	public void getAnnotatedRolesMethodTest() {
		Method method = null;

		try {
			method = MockService.class.getMethod("someProtectedMethod");
		} catch (NoSuchMethodException unexpected) {
			fail("Should be a method called 'someProtectedMethod' on class!");
		}
		List<String> roles = RolesUtil.getAnnotatedRoles(method);
		for (String element : roles) {
			System.out.println("Role: " + element);
		}
		assertNotNull("Should return a list of roles.", roles);
		assertFalse("Role list should not be empty.", roles.isEmpty());
		assertTrue("Should be 3 rolenames in the list.", roles.size() == 3);

	}

	/**
	 * Testing that RolesUtil returns an empty list of roles if no annotation is present.
	 */
	@Test
	public void getAnnotatedRolesMethodTest2() {
		Method method = null;

		try {
			method = MockService2.class.getMethod("someProtectedMethod");
		} catch (NoSuchMethodException unexpected) {
			fail("Should be a method called 'someProtectedMethod' on class!");
		}
		List<String> roles = RolesUtil.getAnnotatedRoles(method);
		assertNotNull("Should return a list of roles.", roles);
		assertTrue("Role list should be empty.", roles.isEmpty());
	}

	/**
	 * Test getAnnotationRoles with a class.
	 */
	@Test
	public void getAnnotatedRolesClassTest() {

		List<String> roles = RolesUtil.getAnnotatedRoles(MockService.class);
		for (String element : roles) {
			System.out.println("Role: " + element);
		}
		assertNotNull("Should return a list of roles.", roles);
		assertFalse("Role list should not be empty.", roles.isEmpty());
		assertTrue("Should be 1 rolename in the list.", roles.size() == 1);
	}
}
