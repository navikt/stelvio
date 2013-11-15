package no.stelvio.presentation.jsf;

import java.net.URL;
import java.net.URLClassLoader;

import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.component.UIViewRoot;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.render.RenderKitFactory;

import org.apache.myfaces.test.mock.MockApplication;
import org.apache.myfaces.test.mock.MockExternalContext;
import org.apache.myfaces.test.mock.MockFacesContext20;
import org.apache.myfaces.test.mock.MockFacesContextFactory;
import org.apache.myfaces.test.mock.MockHttpServletRequest;
import org.apache.myfaces.test.mock.MockHttpServletResponse;
import org.apache.myfaces.test.mock.MockHttpSession;
import org.apache.myfaces.test.mock.lifecycle.MockLifecycle;
import org.apache.myfaces.test.mock.lifecycle.MockLifecycleFactory;
import org.apache.myfaces.test.mock.MockRenderKit;
import org.apache.myfaces.test.mock.MockServletConfig;
import org.apache.myfaces.test.mock.MockServletContext;
import org.junit.After;
import org.junit.Before;

/**
 * Abstract test case base class which sets ut the JavaServer Faces mock object environment for use in a Junit4 test case. Uses
 * the shale test framework for mocking.
 * 
 * The following protected variables are initialized in the <code>setUp()</code> method, and cleand up in the
 * <code>tearDown()</code> method:
 * 
 * <ul>
 * <li><code>application(MockApplication)</code></li>
 * <li><code>config(MockServletConfig)</code></li>
 * <li><code>externalContext(MockExternalContext)</code></li>
 * <li><code>facesContext(MockFacesContext20)</code></li>
 * <li><code>lifecycle(MockLifecycle)</code></li>
 * <li><code>request(MockHttpServletRequest)</code></li>
 * <li><code>response(MockHttpServletResponse)</code></li>
 * <li><code>servletContext(MockServletContext)</code></li>
 * <li><code>session(MockHttpSession)</code></li>
 * </ul>
 * 
 * This class is based on the implementation of <code>org.apache.myfaces.test.base.AbstractJsfTestCase</code> but does not extend
 * <code>junit.framework.TestCase</code>.
 * 
 * @author person6045563b8dec
 * @version $Id$
 * 
 */
public abstract class AbstractJsfTestCase {

	// Mock object instances for our tests
	/** Mock application. */
	protected MockApplication application = null;

	/** Mock servlet config. */
	protected MockServletConfig config = null;

	/** Mock external context. */
	protected MockExternalContext externalContext = null;

	/** Mock faces context. */
	protected MockFacesContext20 facesContext = null;

	/** Mock faces context factory. */
	protected MockFacesContextFactory facesContextFactory = null;

	/** Mock lifecycle. */
	protected MockLifecycle lifecycle = null;

	/** Mock lifecycle factory. */
	protected MockLifecycleFactory lifecycleFactory = null;

	/** Mock render kit. */
	protected MockRenderKit renderKit = null;

	/** Mock request. */
	protected MockHttpServletRequest request = null;

	/** Mock response. */
	protected MockHttpServletResponse response = null;

	/** Mock servlet context. */
	protected MockServletContext servletContext = null;

	/** Mock session. */
	protected MockHttpSession session = null;

	/** Mock thread context classloader. */
	protected ClassLoader threadContextClassLoader = null;

	/**
	 * The setUp method initializes the JSF mock context object so they are available during testing.
	 * 
	 */
	@Before
	public void setUp() {

		// Set up a new thread context class loader
		threadContextClassLoader = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[0], this.getClass().getClassLoader()));

		// Set up Servlet API Objects
		servletContext = new MockServletContext();
		config = new MockServletConfig(servletContext);
		session = new MockHttpSession();
		session.setServletContext(servletContext);
		request = new MockHttpServletRequest(session);
		request.setServletContext(servletContext);
		response = new MockHttpServletResponse();

		// Set up JSF API Objects
		FactoryFinder.releaseFactories();
		FactoryFinder.setFactory(FactoryFinder.APPLICATION_FACTORY, "org.apache.myfaces.test.mock.MockApplicationFactory");
		FactoryFinder.setFactory(FactoryFinder.FACES_CONTEXT_FACTORY, "org.apache.myfaces.test.mock.MockFacesContextFactory");
		FactoryFinder.setFactory(FactoryFinder.LIFECYCLE_FACTORY, "org.apache.myfaces.test.mock.lifecycle.MockLifecycleFactory");
		FactoryFinder.setFactory(FactoryFinder.RENDER_KIT_FACTORY, "org.apache.myfaces.test.mock.MockRenderKitFactory");

		externalContext = new MockExternalContext(servletContext, request, response);
		lifecycleFactory = (MockLifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
		lifecycle = (MockLifecycle) lifecycleFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
		facesContextFactory = (MockFacesContextFactory) FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
		facesContext = (MockFacesContext20) facesContextFactory.getFacesContext(servletContext, request, response, lifecycle);
		externalContext = (MockExternalContext) facesContext.getExternalContext();
		UIViewRoot root = new UIViewRoot();
		root.setViewId("/viewId");
		root.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);
		facesContext.setViewRoot(root);
		ApplicationFactory applicationFactory = (ApplicationFactory) FactoryFinder
				.getFactory(FactoryFinder.APPLICATION_FACTORY);
		application = new MockApplication();
		applicationFactory.setApplication(application);
		facesContext.setApplication(application);
		RenderKitFactory renderKitFactory = (RenderKitFactory) FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
		renderKit = new MockRenderKit();
		renderKitFactory.addRenderKit(RenderKitFactory.HTML_BASIC_RENDER_KIT, renderKit);
	}

	/**
	 * Tear down instance variables required by this test case.
	 * 
	 * @throws Exception
	 *             if an unexpected error occurs
	 */
	@After
	public void tearDown() throws Exception {

		application = null;
		config = null;
		externalContext = null;
		facesContext.release();
		facesContext = null;
		lifecycle = null;
		lifecycleFactory = null;
		renderKit = null;
		request = null;
		response = null;
		servletContext = null;
		session = null;
		FactoryFinder.releaseFactories();

		Thread.currentThread().setContextClassLoader(threadContextClassLoader);
		threadContextClassLoader = null;

	}

}
