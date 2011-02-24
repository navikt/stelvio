package no.stelvio.presentation.context;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Abstract class for making a FilterChain.
 * 
 * The assertion should be implemented as one would implement it inside a unit test class.
 * 
 * Example of how to implement it to assert that the session has been instansiated by a pervious executed filter is shown below:
 * 
 * <pre>
 * new AbstractMockFilterChain(){
 * 		public void assertion(){
 * 			assertNotNull(&quot;Session should not be null&quot;,(getHttpRequest().getSession(false);							
 * 		} 
 * }
 * </pre>
 * 
 * 
 * 
 * @author person983601e0e117 (Accenture)
 * 
 */
public abstract class AbstractMockFilterChain implements FilterChain {

	private ServletRequest request;
	private ServletResponse response;

	/**
	 * Do filter.
	 * 
	 * @param arg0
	 *            arg0
	 * @param arg1
	 *            arg1
	 * @throws IOException
	 *             io exception
	 * @throws ServletException
	 *             servlet exception
	 */
	public void doFilter(ServletRequest arg0, ServletResponse arg1) throws IOException, ServletException {
		this.request = arg0;
		this.response = arg1;
		assertion();
	}

	/**
	 * Implement this with assertions. The ServletRequest and ServletResponse is available through {@link #getRequest()} and
	 * {@link #getResponse()} or the more typed getters {@link #getHttpRequest()} and {@link #getHttpResponse()}.
	 * 
	 * 
	 */
	public abstract void assertion();

	/**
	 * Get http request.
	 * 
	 * @return request
	 */
	public HttpServletRequest getHttpRequest() {
		if (request instanceof HttpServletRequest) {
			return (HttpServletRequest) request;

		}
		throw new ClassCastException("The ServletRequest passed to doFilter is not of type HttpServletRequest");
	}

	/**
	 * Get http response.
	 * 
	 * @return response
	 */
	public HttpServletResponse getHttpResponse() {
		if (response instanceof HttpServletRequest) {
			return (HttpServletResponse) response;

		}
		throw new ClassCastException("The ServletResponse passed to doFilter is not of type HttpServletResponse");
	}

	/**
	 * Get request.
	 * 
	 * @return request
	 */
	public ServletRequest getRequest() {
		return request;
	}

	/**
	 * Get response.
	 * 
	 * @return response
	 */
	public ServletResponse getResponse() {
		return response;
	}

}
