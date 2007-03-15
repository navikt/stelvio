/*
 * Copyright 2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package no.stelvio.presentation.security.page;

import java.net.URL;
import java.net.URLClassLoader;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.render.RenderKitFactory;
import javax.servlet.ServletContext;

import no.stelvio.common.context.support.RequestContextSetter;
import no.stelvio.common.context.support.SimpleRequestContext;
import no.stelvio.common.security.support.SecurityContextSetter;
import no.stelvio.common.security.support.SimpleSecurityContext;

import org.apache.shale.test.mock.MockApplication;
import org.apache.shale.test.mock.MockFacesContext;
import org.apache.shale.test.mock.MockHttpServletResponse;
import org.apache.shale.test.mock.MockHttpSession;
import org.apache.shale.test.mock.MockLifecycle;
import org.apache.shale.test.mock.MockLifecycleFactory;
import org.apache.shale.test.mock.MockPrincipal;
import org.apache.shale.test.mock.MockRenderKit;
import org.apache.shale.test.mock.MockServletConfig;
import org.apache.shale.test.mock.MockServletContext;
import org.junit.After;
import org.junit.Before;

/**
 * <p>Abstract JUnit test case base class, which sets up the JavaServer Faces
 * mock object environment for a particular simulated request.  The following
 * protected variables are initialized in the <code>setUp()</code> method, and
 * cleaned up in the <code>tearDown()</code> method:</p>
 * <ul>
 * <li><code>application</code> (<code>MockApplication</code>)</li>
 * <li><code>config</code> (<code>MockServletConfig</code>)</li>
 * <li><code>externalContext</code> (<code>MockExternalContext</code>)</li>
 * <li><code>facesContext</code> (<code>MockFacesContext</code>)</li>
 * <li><code>lifecycle</code> (<code>MockLifecycle</code>)</li>
 * <li><code>request</code> (<code>MockHttpServletRequest</code></li>
 * <li><code>response</code> (<code>MockHttpServletResponse</code>)</li>
 * <li><code>servletContext</code> (<code>MockServletContext</code>)</li>
 * <li><code>session</code> (<code>MockHttpSession</code>)</li>
 * </ul>
 *
 * <p>In addition, appropriate factory classes will have been registered with
 * <code>javax.faces.FactoryFinder</code> for <code>Application</code> and
 * <code>RenderKit</code> instances.  The created <code>FacesContext</code>
 * instance will also have been registered in the appropriate thread local
 * variable, to simulate what a servlet container would do.</p>
 *
 */
public class AbstractPhaselistenerTestCase {
	
	protected Principal principal;
	protected List<String> roles;

    // Mock object instances for our tests
    protected MockApplication         			application;
    protected MockServletConfig       			config;
    protected MockExternalContextExtended     	externalContext;
    protected MockFacesContext        			facesContext;
    protected MockFacesContextFactoryExtended	facesContextFactory;
    protected MockLifecycle           			lifecycle;
    protected MockLifecycleFactory    			lifecycleFactory;
    protected MockRenderKit           			renderKit;
    protected MockHttpServletRequestExtended	request;
    protected MockHttpServletResponse 			response;
    protected MockServletContext      			servletContext;
    protected MockHttpSession         			session;

    // Thread context class loader saved and restored after each test
    private ClassLoader threadContextClassLoader;

    @Before
    public final void setUp() throws Exception {
    	//Set up a new thread context class loader
        threadContextClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[0],
                this.getClass().getClassLoader()));
        
        //MockViewHandler viewHandler = new MockViewHandler();

        this.principal = new MockPrincipal("TestUser");
		this.roles = new ArrayList<String>();
		this.roles.add("role1");
		this.roles.add("role2");
		RequestContextSetter.setRequestContext(new SimpleRequestContext("","","",""));
		SecurityContextSetter.setSecurityContext(new SimpleSecurityContext(principal.getName(),roles));
		
		 // Set up Servlet API Objects
        servletContext = new MockServletContext(); 
        
        HashMap<String, String> map = getInitParameters();
        if(map != null){
        	Iterator<String> iter = map.keySet().iterator();
            while(iter.hasNext()){
            	String key = iter.next();
            	String value = map.get(key);
            	servletContext.addInitParameter(key, value);
            }
        }
              		
        config = new MockServletConfig(servletContext);
        
        session = new MockHttpSession();
        session.setServletContext(servletContext);
        request = new MockHttpServletRequestExtended(session, principal, roles);
        request.setServletContext(servletContext);
        response = new MockHttpServletResponse();

        // Set up JSF API Objects
        FactoryFinder.releaseFactories();
        FactoryFinder.setFactory(FactoryFinder.APPLICATION_FACTORY,
        "org.apache.shale.test.mock.MockApplicationFactory");
        FactoryFinder.setFactory(FactoryFinder.FACES_CONTEXT_FACTORY,
        "no.stelvio.presentation.security.page.MockFacesContextFactoryExtended");
        FactoryFinder.setFactory(FactoryFinder.LIFECYCLE_FACTORY,
        "org.apache.shale.test.mock.MockLifecycleFactory");
        FactoryFinder.setFactory(FactoryFinder.RENDER_KIT_FACTORY,
        "org.apache.shale.test.mock.MockRenderKitFactory");

        externalContext =
            new MockExternalContextExtended(servletContext, request, response);
        lifecycleFactory = (MockLifecycleFactory)
        FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        lifecycle = (MockLifecycle)
        lifecycleFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
        facesContextFactory = (MockFacesContextFactoryExtended)
        FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
        facesContext = (MockFacesContext)
        facesContextFactory.getFacesContext(servletContext, request, response, lifecycle);
	    externalContext = (MockExternalContextExtended) facesContext.getExternalContext();
        UIViewRoot root = new UIViewRoot();
        root.setViewId("/viewId");
        root.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);
        facesContext.setViewRoot(root);
        ApplicationFactory applicationFactory = (ApplicationFactory)
        FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        application = new MockApplication();
        applicationFactory.setApplication(application);
        facesContext.setApplication(application);
        RenderKitFactory renderKitFactory = (RenderKitFactory)
        FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        renderKit = new MockRenderKit();
        renderKitFactory.addRenderKit(RenderKitFactory.HTML_BASIC_RENDER_KIT, renderKit);
		
		//Principal principal = new Principal();
		//super.request.setUserPrincipal(principal)
    	
    	this.onSetUp();
    }
    
    @After
    public final void tearDown() throws Exception {
    	this.onTearDown();
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
    
    protected void onSetUp() throws Exception {
    	
    }
    
    protected void onTearDown() throws Exception {
    	
    }
    
    protected HashMap<String, String> getInitParameters(){
    	return null;
    }
    
    /**
     * Set the FacesContext viewRoot with param viewId
     * @param viewId
     */
    protected final void setView(String viewId){
		UIViewRoot root = new UIViewRoot();
        root.setViewId(viewId);
        root.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);
        FacesContext.getCurrentInstance().setViewRoot(root);
	}
}
