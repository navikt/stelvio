package no.stelvio.common.context.support;

import java.lang.reflect.Field;

import no.stelvio.common.context.RequestContext;
import no.stelvio.common.context.RequestContextHolder;
import no.stelvio.common.error.OperationalException;
import no.stelvio.common.log.MDCOperations;
import no.stelvio.common.util.RedeployableThreadLocalSubstitute;

import org.springframework.util.ReflectionUtils;

/**
 * Utility class to set and reset the RequestContext. Should only be used by the framework or when unit testing.
 * 
 * @version $Id$
 */
public class RequestContextSetter {

	/**
	 * Constructor.
	 */
	protected RequestContextSetter() {
	}

	private static Field requestContextHolder;

	/**
	 * Sets a new <code>RequestContext</code>.
	 * 
	 * @param requestContext
	 *            the <code>RequestContext</code> to set.
	 */
	public static void setRequestContext(RequestContext requestContext) {
		try {
			((RedeployableThreadLocalSubstitute<RequestContext>) getRequestContextHolder().get(null)).set(requestContext);
			MDCOperations.setMdcProperties();
		} catch (Exception e) {
			throw new OperationalException("Setting requestContext on RequestContextHolder failed.", e);
		}
	}

	/**
	 * Getting the request context holder need to be synchronized.
	 *
	 * @return the request context holder
	 */
	private static synchronized Field getRequestContextHolder() {
		if (requestContextHolder == null) {
			requestContextHolder = ReflectionUtils.findField(RequestContextHolder.class, "REQUEST_CONTEXT_HOLDER");
			requestContextHolder.setAccessible(true);
		}
		return requestContextHolder;
	}

	/**
	 * Sets a new <code>RequestContext</code> for use in unit tests. The <code>RequestContext</code> is hardcoded with the
	 * following values:
	 * <ul>
	 * <li>processId - "processId"</li>
	 * <li>screenId - "screenId"</li>
	 * <li>moduleId - "moduleId"</li>
	 * <li>transactionId - "transactionId"</li>
	 * <li>userId - "userId"</li>
	 * </ul>
	 */
	public static void setRequestContextForUnitTest() {
		setRequestContext(new SimpleRequestContext("one", "two", "three", "four", "userId"));
	}

	/**
	 * Resets the RequestContext.
	 */
	public static void resetRequestContext() {
		setRequestContext(null);
		MDCOperations.resetMdcProperties();
	}

}
