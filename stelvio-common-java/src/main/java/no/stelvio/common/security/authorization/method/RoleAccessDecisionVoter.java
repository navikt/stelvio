package no.stelvio.common.security.authorization.method;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.framework.ReflectiveMethodInvocation;

import no.stelvio.common.security.SecurityContextHolder;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

/**
 * An access-controller, i.e. an <code>AccessDecisionVoter</code> implementation that participate in an access-controll decision
 * based on the roles found in the Roles annotation for the current intercepted method. If no such annotation is found the class
 * simply abstains from voting, i.e its vote method returns <code>AccessDecisionVoter.ACCESS_ABSTAIN</code>.
 * 
 * @version $Id$
 */
public class RoleAccessDecisionVoter implements AccessDecisionVoter<ReflectiveMethodInvocation> {
	private static final Log LOGGER = LogFactory.getLog(RoleAccessDecisionVoter.class);

	/**
	 * Indication of wheter or not this class supports the presented config attrbute.
	 * 
	 * @param attr
	 *            the config attribute
	 * @return always true
	 */
	public boolean supports(ConfigAttribute attr) {
		return true;
	}


    /**
	 * Indication of wheter or not this class supports the secure object represented by the clazz argument.
	 * 
	 * @param clazz
	 *            the secure object class.
	 * @return <code>true</code> if the presented class is a ReflectiveMethodInvocation, <code>false</code> otherwise.
	 */
	public boolean supports(Class clazz) {
		return ReflectiveMethodInvocation.class.isAssignableFrom(clazz);
	}

	/**
	 * Checks if the current user has the required role to proceed with the method invocation. The roles required are obtained
	 * from the intercepted method's Roles annotation. If no such annotation is present AccessDecisionVoter.ACCESS_ABSTAIN will
	 * be returned.
	 * 
	*/
    @Override
    public int vote(Authentication authentication, ReflectiveMethodInvocation secureObject, Collection<ConfigAttribute> configAttributes) {
        ReflectiveMethodInvocation invocation =  secureObject;
        Method method = invocation.getMethod();
        List<String> roles = RolesUtil.getAnnotatedRoles(method);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Getting roles from annotation for the intercepted method.");
            for (String role : roles) {
                LOGGER.debug("Role: " + role);
            }
        }

        if (roles == null || roles.isEmpty()) {
            return AccessDecisionVoter.ACCESS_ABSTAIN;
        } else if (SecurityContextHolder.currentSecurityContext().isUserInRoles(roles)) {
            return AccessDecisionVoter.ACCESS_GRANTED;
        } else {
            return AccessDecisionVoter.ACCESS_DENIED;
        }
    }
}
