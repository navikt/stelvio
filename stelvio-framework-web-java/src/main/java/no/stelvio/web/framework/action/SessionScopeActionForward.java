package no.stelvio.web.framework.action;

import org.apache.struts.action.ActionForward;

/**
 * Implementation of <code>ActionForward</code> that can be used to 
 * forward control to another resource while enforcing session scope.
 * <p/>
 * The state is configured in <code>struts-config.xml</code>.
 * <pre>
 * <action path="/Oppgaveliste" ...>
 * 		...
 * 		<forward name="OppretteOppgave" path="/Oppgave.do" className="no.stelvio.web.framework.action.SessionScopeActionForward"/>
 * 		<forward name="ViseOppgave" path="/Oppgave.do" className="no.stelvio.web.framework.action.SessionScopeActionForward"/>
 * 		<forward name="EndreOppgave" path="/Oppgave.do" className="no.stelvio.web.framework.action.SessionScopeActionForward"/>
 * 		...
 * 	</action>
 * </pre>
 * 
 * @author person7553f5959484, Accenture
 * @version $Id: SessionScopeActionForward.java 1669 2004-12-08 13:39:20Z psa2920 $
 */
public class SessionScopeActionForward extends ActionForward implements SessionScope {

}
