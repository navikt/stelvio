package no.stelvio.web.taglib.tasklist;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import junit.framework.TestCase;
import no.stelvio.common.tasklist.domain.Saksbehandler;
import no.stelvio.web.taglib.tasklist.action.TasklistMenuForm;
import no.stelvio.web.taglib.tasklist.action.TasklistTreeMenuAction;

import org.apache.myfaces.custom.tree2.TreeNode;
import org.apache.shale.test.mock.MockExternalContext;
import org.apache.shale.test.mock.MockFacesContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.webflow.engine.Flow;
import org.springframework.webflow.execution.FlowSession;
import org.springframework.webflow.test.MockFlowExecutionContext;

/**
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 */
public class TasklistTest extends TestCase {
	private static final String FLOW_CAPTION = "Test caption";
	private TasklistTreeMenuAction tasklistTreeMenuAction;
	private ApplicationContext appContext;
	private TasklistMenuForm tasklistMenuForm;
	private MockFlowExecutionContext flowExecutionContext;
	private FlowSession flowSession;
	private FacesContext facesContext;
	private Flow flow;
	private HttpServletRequest httpRequest;
	private HttpServletResponse httpResponse;
	private ServletContext servletContext;
	private ExternalContext externalContext;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		appContext = new ClassPathXmlApplicationContext("presentation-services.xml"); 
		tasklistTreeMenuAction = (TasklistTreeMenuAction) appContext.getBean("tasklistTreeBacker");
		
		flow = new Flow("testFlow_01");
		flow.setCaption(FLOW_CAPTION);
		
		flowExecutionContext = new MockFlowExecutionContext(flow);
		flowSession = flowExecutionContext.getActiveSession();
		
		servletContext = new MockServletContext();
		httpRequest = new MockHttpServletRequest(servletContext);
		httpResponse = new MockHttpServletResponse();
		externalContext = new MockExternalContext(servletContext, httpRequest, httpResponse);
		facesContext = new MockFacesContext(externalContext);
		
		HttpSession session = (HttpSession) externalContext.getSession(true);
		Saksbehandler saksbehandler = new Saksbehandler();
		saksbehandler.setFornavn("Ola");
		saksbehandler.setEtternavn("Nordmann");
		saksbehandler.setEnhet("NAV Oslo");
		saksbehandler.setSaksbehandlernr(2L);
		session.setAttribute("saksbehandler", saksbehandler);
		
		tasklistTreeMenuAction.setFacesContext(facesContext);
		tasklistTreeMenuAction.setFlowSession(flowSession);
		
		tasklistMenuForm = tasklistTreeMenuAction.setupTasklistMenu();
	}

	/**
	 * TODO: Document me
	 * @throws Exception 
	 */
	public void testTasklist() throws Exception {
		// Check if any caseworkers were returned
		assertFalse("No caseworkers returned", tasklistMenuForm.getOtherCaseWorkers() == null || tasklistMenuForm.getOtherCaseWorkers().size() <= 0);
		
		// Check if tree has any nodes
		TreeNode root = tasklistTreeMenuAction.getTreeData();
		assertTrue("Menu has no elements. Child count is: "+root.getChildCount(), root.getChildCount() > 0);
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
}