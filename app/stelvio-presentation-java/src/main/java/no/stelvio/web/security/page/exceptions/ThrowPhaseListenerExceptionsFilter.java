package no.stelvio.web.security.page.exceptions;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.*;
import javax.servlet.http.*;
import org.springframework.beans.factory.InitializingBean;
import java.io.IOException;
import javax.security.auth.*;
import java.security.*;
import java.util.Iterator;

/**
 * TODO
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 */
public class ThrowPhaseListenerExceptionsFilter implements Filter
{
	
	
	
	
	/**
	 * TODO
	 * 
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	throws IOException, ServletException
	{
		
		System.out.println("----- Filter: før chain.doFilter ------");
		
		chain.doFilter(request, response);
		
		System.out.println("----- Filter: etter chain.doFilter ------");
		
		HttpServletRequest req = (HttpServletRequest)request;    
		// Get the session object from the request    
		HttpSession session = req.getSession(); 
		RuntimeException  e = (RuntimeException)session.getAttribute("PhaseListenerException");
		if(e != null)
		{
			session.setAttribute("PhaseListenerException", null);
			System.out.println("----- Filter: Kaster exception!! ------");
			throw e;
		}
		
	}
	
	  /**
	   * Does nothing. We use IoC container lifecycle services instead.
	   */
	   public void destroy() {}
	   /**
		* Does nothing. We use IoC container lifecycle services instead.
		*
		* @param filterConfig ignored
		*
		* @throws ServletException ignored
		*/
	   public void init(FilterConfig filterConfig) throws ServletException {}
}