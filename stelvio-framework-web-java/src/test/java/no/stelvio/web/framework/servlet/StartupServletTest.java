package no.stelvio.web.framework.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import junit.framework.AssertionFailedError;
import org.jmock.cglib.MockObjectTestCase;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.ServletContextAware;
import servletunit.ServletConfigSimulator;
import servletunit.ServletContextSimulator;

import no.stelvio.common.framework.config.Config;
import no.stelvio.web.framework.constants.Constants;
import no.stelvio.web.framework.servlet.StartupServlet;


/**
 * Unit test for {@link StartupServlet}.
 *
 * @author personf8e9850ed756
 * @version $Revision: 2598 $, $Date: 2005-10-31 13:31:23 +0100 (Mon, 31 Oct 2005) $
 */
public class StartupServletTest extends MockObjectTestCase {
	private StartupServlet startupServlet;
	private ServletContextSimulator servletContext;

	public void testInitializesTheGivenBeans() throws ServletException {
		startupServlet.startup();
		final Config config = (Config) servletContext.getAttribute(Config.PRESENTATION_SERVICES);
		final ServletContextAwareBean bean = (ServletContextAwareBean) config.getBean("ServletContextAwareBean");

		assertSame("Should be the same servlet context;", servletContext, bean.servletContext);
	}

	protected void setUp() {
		final ServletConfigSimulator servletConfig = new ServletConfigSimulator();
		servletConfig.setInitParameter("StartupHelperList", "ServletContextAwareBean,TestBean");
		servletConfig.setInitParameter(Constants.PRESENTATION_SERVICES, "presentation-services.xml");

		servletContext = new ServletContextSimulator();

		startupServlet = new StartupServlet() {
			public ServletConfig getServletConfig() {
				return servletConfig;
			}

			public ServletContext getServletContext() {
				return servletContext;
			}
		};
	}

	private static class ServletContextAwareBean implements ServletContextAware, InitializingBean {
		private ServletContext servletContext;

		public void setServletContext(ServletContext servletContext) {
			this.servletContext = servletContext;
		}

		public void afterPropertiesSet() {
			if (null == servletContext) {
				throw new AssertionFailedError(
						"servlet context should have been initialized before afterPropertiesSet is called");
			}
		}
	}
}
