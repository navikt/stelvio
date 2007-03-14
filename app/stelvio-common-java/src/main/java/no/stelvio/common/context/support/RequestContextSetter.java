package no.stelvio.common.context.support;

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
 * Utility class to set and reset the RequestContext. Should only be used by the framework or when unit testing. 
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$ 
 */
public class RequestContextSetter {
	private static Method setRequestContextMethod;
	private static Method resetRequestContextMethod;
	
	/**
	 * Sets a new <code>RequestContext</code>.
	 * 
	 * @param requestContext the <code>RequestContext</code> to set.
	 */
	public static void setRequestContext(RequestContext requestContext) {
		if (setRequestContextMethod == null) {
			setRequestContextMethod = 
				ReflectionUtils.findMethod(RequestContextHolder.class, "setRequestContext", new Class[]{RequestContext.class});
			setRequestContextMethod.setAccessible(true);
		}

		ReflectionUtils.invokeMethod(setRequestContextMethod, null, new Object[]{ requestContext });
	}

	/**
	 * Sets a new <code>RequestContext</code> for use in unit tests. The <code>RequestContext</code> is hardcoded with
	 * the following values:
	 * <ul>
	 *   <li>processId - "processId"</li>
	 *   <li>screenId - "screenId"</li>
	 *   <li>moduleId - "moduleId"</li>
	 *   <li>transactionId - "transactionId"</li>
	 * </ul> 
	 */
	public static void setRequestContextForUnitTest() {
		setRequestContext(new SimpleRequestContext("one", "two", "three", "four"));
	}

	/**
	 * Resets the RequestContext.
	 */
	public static void resetRequestContext(){
		if (resetRequestContextMethod == null) {
			resetRequestContextMethod = ReflectionUtils.findMethod(RequestContextHolder.class, "resetRequestContext", new Class[]{});
			resetRequestContextMethod.setAccessible(true);
		}
		
		ReflectionUtils.invokeMethod(resetRequestContextMethod, null);
	}
}
