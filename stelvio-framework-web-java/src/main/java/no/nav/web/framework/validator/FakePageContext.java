package no.nav.web.framework.validator;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

/**
 * A fake <code>PageContext</code> that is used by the evaluation manager used by the validateel validation rule.
 * Will fail-fast if one of the variables in the EL does not exist here.
 *
 * @author rhightower
 * @author personf8e9850ed756
 * @version $Revision: 2602 $, $Date: 2005-11-01 12:42:55 +0100 (Tue, 01 Nov 2005) $
 */
public class FakePageContext extends PageContext {
	private HttpServletRequest request;
	private Map pageScope = new HashMap();
	private Map applicationScope = new HashMap();

	public FakePageContext(HttpServletRequest request) {
		this.request = request;
	}

	public Object getAttribute(String key) {
		Object attribute = pageScope.get(key);

		// fail-fast
		if (null == attribute) {
			throw new IllegalArgumentException("The attribute " + key + " was not found in the page scope");
		}

		return attribute;
	}

	public void setAttribute(String key, Object value) {
		pageScope.put(key, value);
	}

	public void removeAttribute(String key) {
		// Removing from page scope
		Object value = pageScope.remove(key);

		// Removing from request scope if nothing removed from page scope
		if (null != value) {
			value = request.getAttribute(key);
			request.removeAttribute(key);
		}

		// Removing from session scope if nothing removed from request scope
		if (null != value) {
			value = request.getSession().getAttribute(key);
			request.getSession().removeAttribute(key);
		}

		// Removing from application scope if nothing removed from session scope
		if (null != value) {
			applicationScope.remove(key);
		}
	}

	public JspWriter getOut() {
		// no op
		return null;
	}

	public HttpSession getSession() {
		return request.getSession(true);
	}

	public Object getPage() {
		// no op
		return null;
	}

	public ServletRequest getRequest() {
		return this.request;
	}

	public ServletResponse getResponse() {
		// no op
		return null;
	}

	public Exception getException() {
		// no op
		return null;
	}

	public ServletConfig getServletConfig() {
		// no op
		return null;
	}

	public ServletContext getServletContext() {
		// no op
		return null;
	}

	public void forward(String arg0) throws ServletException, IOException {
		// no op
	}

	public void include(String arg0) throws ServletException, IOException {
		// no op
	}

	public void initialize(Servlet arg0, ServletRequest arg1, ServletResponse arg2, String arg3, boolean arg4, int arg5, boolean arg6)
	        throws IOException, IllegalStateException, IllegalArgumentException {
	}

	public void setAttribute(String key, Object value, int scope) {
		switch (scope) {
			case PageContext.PAGE_SCOPE:
				pageScope.put(key, value);
				break;

			case PageContext.REQUEST_SCOPE:
				request.setAttribute(key, value);
				break;

			case PageContext.SESSION_SCOPE:
				request.getSession().setAttribute(key, value);
				break;

			case PageContext.APPLICATION_SCOPE:
				applicationScope.put(key, value);
				break;

			default:
				throw new IllegalArgumentException("Scope " + scope + " is not legal");
		}
	}

	public Object getAttribute(String key, int scope) {
		Object attribute = null;

		switch (scope) {
			case PageContext.PAGE_SCOPE:
				attribute = pageScope.get(key);
				break;

			case PageContext.REQUEST_SCOPE:
				attribute = request.getAttribute(key);
				break;

			case PageContext.SESSION_SCOPE:
				attribute = request.getSession().getAttribute(key);
				break;

			case PageContext.APPLICATION_SCOPE:
				attribute = applicationScope.get(key);
				break;

			default:
				throw new IllegalArgumentException("Scope " + scope + " is not legal");
		}

		// fail-fast
		if (null == attribute) {
			throw new IllegalArgumentException("The attribute " + key + " was not found in scope " + scope);
		}

		return attribute;
	}

	public void removeAttribute(String key, int scope) {
		switch (scope) {
			case PageContext.PAGE_SCOPE:
				pageScope.remove(key);
				break;

			case PageContext.REQUEST_SCOPE:
				request.removeAttribute(key);
				break;

			case PageContext.SESSION_SCOPE:
				request.getSession().removeAttribute(key);
				break;

			case PageContext.APPLICATION_SCOPE:
				applicationScope.remove(key);
				break;

			default:
				// no op according to javadoc for page context
		}
	}

	public Enumeration getAttributeNamesInScope(int scope) {
		switch (scope) {
			case PageContext.PAGE_SCOPE:
				return Collections.enumeration(pageScope.keySet());

			case PageContext.REQUEST_SCOPE:
				return request.getAttributeNames();

			case PageContext.SESSION_SCOPE:
				return request.getSession().getAttributeNames();

			case PageContext.APPLICATION_SCOPE:
				return Collections.enumeration(applicationScope.keySet());

			default:
				return new EmptyEnumeration();
		}
	}

	public int getAttributesScope(String key) {
		// Checking page scope
		if (null != pageScope.get(key)) {
			return PageContext.PAGE_SCOPE;
		}

		// Checking request scope
		if (null != request.getAttribute(key)) {
			return PageContext.REQUEST_SCOPE;
		}

		// Checking session scope
		if (null != request.getSession().getAttribute(key)) {
			return PageContext.PAGE_SCOPE;
		}

		// Checking application scope
		if (null != applicationScope.get(key)) {
			return PageContext.APPLICATION_SCOPE;
		}

		return 0;
	}

	public Object findAttribute(String key) {
		// Checking page scope
		Object attribute = pageScope.get(key);

		// Checking request scope
		if (attribute == null) {
			attribute = request.getAttribute(key);
		}

		// Checking session scope
		if (attribute == null) {
			attribute = request.getSession().getAttribute(key);
		}

		// Checking application scope
		if (attribute == null) {
			attribute = applicationScope.get(key);
		}

		// fail-fast
		if (null == attribute) {
			throw new IllegalArgumentException("The attribute " + key + " was not found in any scope");
		}

		return attribute;
	}

	public void handlePageException(Exception arg0) throws ServletException, IOException {
		// no op
	}

	public void handlePageException(Throwable arg0) throws ServletException, IOException {
		// no op
	}

	public void release() {
		// no op
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return a string representation of the object.
	 */
	public String toString() {
		return "page scope:        " + pageScope + "\n" +
		        "request scope:     " + requestToString() + "\n" +
		        "session scope:     " + sessionToString() + "\n" +
		        "application scope: " + applicationScope;
	}

	/**
	 * Returns a string representation of session attributes.
	 *
	 * @return a string representation of session attributes.
	 */
	private String sessionToString() {
		final HttpSession session = request.getSession(true);
		StringBuffer buffer = new StringBuffer("{");

		for (Enumeration enumerator = session.getAttributeNames(); enumerator.hasMoreElements();) {
			String attributeName = (String) enumerator.nextElement();
			buffer.append(attributeName).append("=").append(session.getAttribute(attributeName));

			if (enumerator.hasMoreElements()) {
				buffer.append(", ");
			}
		}

		return buffer.append("}").toString();
	}

	/**
	 * Returns a string representation of request attributes.
	 *
	 * @return a string representation of request attributes.
	 */
	private String requestToString() {
		StringBuffer buffer = new StringBuffer("{");

		for (Enumeration enumerator = request.getAttributeNames(); enumerator.hasMoreElements();) {
			String attributeName = (String) enumerator.nextElement();
			buffer.append(attributeName).append("=").append(request.getAttribute(attributeName));

			if (enumerator.hasMoreElements()) {
				buffer.append(", ");
			}
		}

		return buffer.append("}").toString();
	}

	/**
	 * Empty enumerator
	 */
	private static class EmptyEnumeration implements Enumeration {
		public boolean hasMoreElements() {
			return false;
		}

		public Object nextElement() {
			throw new IllegalStateException("No more elements");
		}
	}
}
