package no.stelvio.presentation.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * PerformanceMonitorFilter is an implementation of the <i>Intercepting Filter</i> pattern that
 * is responsible for monitoring the total request/response processing time.
 * 
 * @author person7553f5959484, Accenture
 * @version $Id: 1.3
 * @todo we might need this in new framework.
 */
public final class PerformanceMonitorFilter extends AbstractFilter {

//	private static final MonitorKey MONITOR_KEY = new MonitorKey("Presentation", MonitorKey.PRESENTATION);

	/**
	 * Performs the following processing steps:
	 * <ol>
	 * 	<li> Starts the performance monitor </li>
	 * 	<li> Delegates processing to the next filter or resource in the chain </li>
	 * 	<li> Stops the performance monitor </li>
	 * </ol>
	 * 
	 * {@inheritDoc}
	 * @see no.stelvio.web.filter.AbstractFilter#doFilter(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
	 */
	protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
		throws IOException, ServletException {

//		PerformanceMonitor.start(MONITOR_KEY, RequestUtils.getScreenId(request));

		// Delegate processing to the next filter or resource in the chain
		try {
			chain.doFilter(request, response);
//			PerformanceMonitor.end(MONITOR_KEY);
		} catch (IOException ioe) {
//			PerformanceMonitor.fail(MONITOR_KEY);
			throw ioe;
		} catch (ServletException se) {
//			PerformanceMonitor.fail(MONITOR_KEY);
			throw se;
		} catch (RuntimeException re) {
//			PerformanceMonitor.fail(MONITOR_KEY);
			throw re;
		}
	}

}
