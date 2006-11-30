package no.stelvio.web.taglib.breadcrumb;

import java.util.List;

import junit.framework.TestCase;
import no.stelvio.web.taglib.breadcrumb.action.BreadcrumbAction;
import no.stelvio.web.taglib.breadcrumb.domain.Breadcrumb;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.webflow.engine.Flow;
import org.springframework.webflow.execution.FlowSession;
import org.springframework.webflow.test.MockFlowExecutionContext;


/**
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 */
public class BreadcrumbTest extends TestCase {
	private static final String CAPTION = "Test caption";
	private BreadcrumbAction breadcrumbAction;
	private ApplicationContext appContext;
	private MockFlowExecutionContext flowExecutionContext;
	private FlowSession flowSession;
	private Flow flow;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		appContext = new ClassPathXmlApplicationContext("presentation-services.xml"); 
		breadcrumbAction = (BreadcrumbAction) appContext.getBean("breadcrumbAction");
		
		flow = new Flow("testFlow_01");
		flow.setCaption(CAPTION);
		
		flowExecutionContext = new MockFlowExecutionContext(flow);
		flowSession = flowExecutionContext.getActiveSession();
		
		breadcrumbAction.setFlowSession(flowSession);
	}

	/**
	 * TODO: Document me
	 */
	public void testBreadcrumb() {	
		List<Breadcrumb> crumbs = breadcrumbAction.getBreadcrumb();
		
		// Check if breadcrumb list is null
		assertFalse("No breadcrumb list returned.", crumbs == null);
		
		// Check if at least one breadcrumb is available
		assertTrue("No breadcrumbs in list", crumbs.size() > 0);
		
		// Check if caption is correct
		assertTrue("Caption is not correct", crumbs.get(0).getCaption().equals(CAPTION));
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
}