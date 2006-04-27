package no.nav.web.framework.action;


/**
 * Implementation of <code>ActionForward</code> that can be used to 
 * forward control to another resource in a specific state while enforcing session scope.
 * <p/>
 * The state is configured in <code>struts-config.xml</code>.
 * <pre>
 * <action path="/Oppgaveliste" ...>
 * 		...
 * 		<forward name="OppretteOppgave" path="/Oppgave.do" className="no.nav.web.framework.action.StateAwareSessionScopeActionForward">
 * 			<set-property property="state" value="opprette"/>
 * 		</forward>
 * 		<forward name="ViseOppgave" path="/Oppgave.do" className="no.nav.web.framework.action.StateAwareSessionScopeActionForward">
 * 			<set-property property="state" value="vise"/>
 * 		</forward>
 * 		<forward name="EndreOppgave" path="/Oppgave.do" className="no.nav.web.framework.action.StateAwareSessionScopeActionForward">
 * 			<set-property property="state" value="endre"/>
 * 		</forward>
 * 		...
 * 	</action>
 * </pre>
 * 
 * @author person7553f5959484, Accenture
 * @version $Id: StateAwareSessionScopeActionForward.java 1669 2004-12-08 13:39:20Z psa2920 $
 */
public class StateAwareSessionScopeActionForward extends StateAwareActionForward implements SessionScope {

}
