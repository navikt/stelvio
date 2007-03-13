package no.stelvio.test.context;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.ReflectionUtils;

import no.stelvio.common.context.RequestContext;
import no.stelvio.common.context.RequestContextHolder;
import no.stelvio.common.security.SecurityContext;
import no.stelvio.common.security.SecurityContextHolder;
import no.stelvio.common.security.support.SimpleSecurityContext;

/**
 * Utility class intended to be used in testing which via reflection can set the RequestContext and SecurityContext on
 * the current thread.
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$ 
 */
public class StelvioContextSetter {
	private static Method setSecurityContextMethod;
	private static Method resetSecurityContextMethod;
	private static Method setRequestContextMethod;
	private static Method resetRequestContextMethod;
	
	/**
	 * Sets a new SecurityContext in the SecurityContextHolder.
	 * @param securityContext the securityContext to set
	 */
	public static void setSecurityContext(SecurityContext securityContext) {
		if(setSecurityContextMethod == null){
			setSecurityContextMethod = ReflectionUtils.findMethod(SecurityContextHolder.class, "setSecurityContext", new Class[]{SecurityContext.class});
			setSecurityContextMethod.setAccessible(true);
		}

		ReflectionUtils.invokeMethod(setSecurityContextMethod, SecurityContextHolder.class, new Object[]{ securityContext });
	}

	/**
	 * Sets a new SecurityContext on in the SecurityContextHolder with the supplied user and roles.
	 * @param userId the userId
	 * @param roles a commaseparated list of rolenames or an array of rolenames.
	 */
	public static void setSecurityContext(String userId, String... roles) {
		List<String> roleList = new ArrayList<String>();

		for (String string : roles) {
			roleList.add(string);
		}

		SecurityContext securityContext = new SimpleSecurityContext(userId, roleList);
		setSecurityContext(securityContext);
	}

	/**
	 * Sets a new RequestContext in the RequestContextHolder.
	 * @param requestContext the requestContext to set
	 */
	public static void setRequestContext(RequestContext requestContext) {
		if(setRequestContextMethod == null){
			setRequestContextMethod = ReflectionUtils.findMethod(RequestContextHolder.class, "setRequestContext", new Class[]{RequestContext.class});
			setRequestContextMethod.setAccessible(true);
		}

		ReflectionUtils.invokeMethod(setRequestContextMethod, null, new Object[]{ requestContext });
	}

	/**
	 * Resets the RequestContext by placing a null value in the RequestContextHolder
	 */
	public static void resetRequestContext(){
		if(resetRequestContextMethod == null){
			resetRequestContextMethod = ReflectionUtils.findMethod(RequestContextHolder.class, "resetRequestContext", new Class[]{});
			resetRequestContextMethod.setAccessible(true);
		}
		ReflectionUtils.invokeMethod(resetRequestContextMethod, null);
	}

	/**
	 *  Resets the SecurityContext by placing a null value in the SecurityContextHolder
	 */
	public static void resetSecurityContext(){
		if(resetSecurityContextMethod == null){
			resetSecurityContextMethod = ReflectionUtils.findMethod(SecurityContextHolder.class, "resetSecurityContext", new Class[]{});
			resetSecurityContextMethod.setAccessible(true);
		}

		ReflectionUtils.invokeMethod(resetSecurityContextMethod, null);
	}
}
