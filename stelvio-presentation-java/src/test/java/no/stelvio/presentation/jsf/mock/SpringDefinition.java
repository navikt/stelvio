package no.stelvio.presentation.jsf.mock;

import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.component.UIViewRoot;
import javax.faces.render.RenderKitFactory;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.renderkit.html.ext.HtmlGridRenderer;
import org.apache.myfaces.test.mock.MockApplication20;
import org.apache.myfaces.test.mock.MockExternalContext20;
import org.apache.myfaces.test.mock.MockFacesContext20;
import org.apache.myfaces.test.mock.MockHttpServletRequest;
import org.apache.myfaces.test.mock.MockHttpSession;
import org.apache.myfaces.test.mock.MockRenderKit;
import org.springframework.binding.message.DefaultMessageContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.webflow.context.ExternalContext;
import org.springframework.webflow.context.ExternalContextHolder;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;
import org.springframework.webflow.test.MockExternalContext;
import org.springframework.webflow.test.MockRequestContext;

/**
 * SpringDefinition.java.
 * 
 * @version $Id$
 * 
 */
public class SpringDefinition {
	private static ApplicationContext context;
	private static WebApplicationContext ctx;
	private static MockServletConfig config;
	private final Log log = LogFactory.getLog(this.getClass());

	/**
	 * Get context.
	 * 
	 * @return context
	 */
	public static ApplicationContext getContext() {
		setDivContexts(getConfigLocations());

		RequestContext requestContext = new MockRequestContext();
		MessageSource messageSource = (MessageSource) SpringDefinition.context.getBean("messageSource");
		((DefaultMessageContext) requestContext.getMessageContext()).setMessageSource(messageSource);
		RequestContextHolder.setRequestContext(requestContext);

		return SpringDefinition.context;
	}

	/**
	 * Set contexts.
	 * 
	 * @param configLocations
	 *            config locations
	 */
	private static void setDivContexts(String[] configLocations) {
		MockServletContext servletContext = new MockServletContext();
		MockHttpSession session = new MockHttpSession();
		session.setServletContext(servletContext);

		String[] configLoc = getConfigLocations();

		String initParam = "";
		for (String string : configLoc) {
			initParam += !initParam.equals("") ? "," : "";
			initParam += string;
		}

		servletContext.addInitParameter(ContextLoader.CONFIG_LOCATION_PARAM, initParam);

		ServletContextListener contextListener = new ContextLoaderListener();
		ServletContextEvent event = new ServletContextEvent(servletContext);
		contextListener.contextInitialized(event);

		ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		config = new MockServletConfig(servletContext);

		MockHttpServletRequest request = new MockHttpServletRequest(session);
		request.setServletContext(servletContext);

		if (SpringDefinition.context == null) {
			SpringDefinition.context = new ClassPathXmlApplicationContext(configLocations);
		}

		FactoryFinder.setFactory(FactoryFinder.APPLICATION_FACTORY, "org.apache.myfaces.test.mock.MockApplicationFactory");
		FactoryFinder.setFactory(FactoryFinder.FACES_CONTEXT_FACTORY, "org.apache.myfaces.test.mock.MockFacesContextFactory");
		FactoryFinder.setFactory(FactoryFinder.LIFECYCLE_FACTORY, "org.apache.myfaces.test.mock.lifecycle.MockLifecycleFactory");
		FactoryFinder.setFactory(FactoryFinder.RENDER_KIT_FACTORY, "org.apache.myfaces.test.mock.MockRenderKitFactory");

		ExternalContext context = new MockExternalContext();
		ExternalContextHolder.setExternalContext(context);

		FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);

		MockHttpServletResponse response = new MockHttpServletResponse();

		RenderKitFactory renderKitFactory = (RenderKitFactory) FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);

		if (!renderKitFactory.getRenderKitIds().hasNext()) {
			MockRenderKit renderKit = new MockRenderKit();
			renderKit.addRenderer("javax.faces.Panel", "javax.faces.Grid", new HtmlGridRenderer());
			renderKitFactory.addRenderKit(RenderKitFactory.HTML_BASIC_RENDER_KIT, renderKit);
		}

		MockApplication20 application = new MockApplication20();
		ApplicationFactory applicationFactory = (ApplicationFactory) FactoryFinder
				.getFactory(FactoryFinder.APPLICATION_FACTORY);
		applicationFactory.setApplication(application);

		MockExternalContext20 externalContext = new MockExternalContext20(servletContext, request, response);
		MockFacesContext20 facesContext = new MockFacesContext20();
		facesContext.setExternalContext(externalContext);
		facesContext.setApplication(application);

		UIViewRoot root = new UIViewRoot();
		root.setViewId("/viewId");
		root.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);
		facesContext.setViewRoot(root);

		MockFacesContext20.setCurrentInstance(facesContext);
	}

	/**
	 * Get config locations.
	 * 
	 * @return config locations
	 */
	private static String[] getConfigLocations() {
		String[] configLocations = new String[1];

		configLocations[0] = "classpath:test-context.xml";

		return configLocations;
	}

}
