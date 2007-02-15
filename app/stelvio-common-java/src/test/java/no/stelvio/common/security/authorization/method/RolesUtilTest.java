package no.stelvio.common.security.authorization.method;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

public class RolesUtilTest {

	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void getAnnotatedRolesMethodTest(){
		Method method = null;

        try {
            	method = MockService.class.getMethod("someProtectedMethod");
        } catch (NoSuchMethodException unexpected) {
            	fail("Should be a method called 'someProtectedMethod' on class!");
        }
        List<String> roles = RolesUtil.getAnnotatedRoles(method);
        Iterator<String> iter = roles.iterator();
        while(iter.hasNext()){
        	String element = iter.next();
        	System.out.println("Role: " + element);
        }
        assertNotNull("Should return a list of roles.",roles);
        assertFalse("Role list should not be empty.", roles.isEmpty());
        assertTrue("Should be 3 rolenames in the list.", roles.size() == 3);
        
	}
	/**
	 * Testing that RolesUtil returns an empty list of roles if no annotation is present.
	 */
	@Test
	public void getAnnotatedRolesMethodTest2(){
		Method method = null;

        try {
            	method = MockService2.class.getMethod("someProtectedMethod");
        } catch (NoSuchMethodException unexpected) {
            	fail("Should be a method called 'someProtectedMethod' on class!");
        }
        List<String> roles = RolesUtil.getAnnotatedRoles(method);
        assertNotNull("Should return a list of roles.",roles);
        assertTrue("Role list should be empty.", roles.isEmpty());
	}
	
	@Test
	public void getAnnotatedRolesClassTest(){
		
		List<String> roles = RolesUtil.getAnnotatedRoles(MockService.class);
        Iterator<String> iter = roles.iterator();
        while(iter.hasNext()){
        	String element = iter.next();
        	System.out.println("Role: " + element);
        }
        assertNotNull("Should return a list of roles.",roles);
        assertFalse("Role list should not be empty.", roles.isEmpty());
        assertTrue("Should be 1 rolename in the list.", roles.size() == 1);	
	}
}
