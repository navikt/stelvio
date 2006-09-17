package no.stelvio.web.framework.action;

import org.apache.struts.action.ActionForward;


/**
 * Implementation of <code>ActionForward</code> that can be used to 
 * forward control to another resource in a specific state.
 * <p/>
 * The state is configured in <code>struts-config.xml</code>.
 * <pre>
 * <action path="/Oppgaveliste" ...>
 * 		...
 * 		<forward name="OppretteOppgave" path="/Oppgave.do" className="no.stelvio.web.framework.action.StateAwareActionForward">
 * 			<set-property property="state" value="opprette"/>
 * 		</forward>
 * 		<forward name="ViseOppgave" path="/Oppgave.do" className="no.stelvio.web.framework.action.StateAwareActionForward">
 * 			<set-property property="state" value="vise"/>
 * 		</forward>
 * 		<forward name="EndreOppgave" path="/Oppgave.do" className="no.stelvio.web.framework.action.StateAwareActionForward">
 * 			<set-property property="state" value="endre"/>
 * 		</forward>
 * 		...
 * 	</action>
 * </pre>
 * 
 * @author Christian Rømming, Accenture
 * @author person7553f5959484, Accenture
 * @version $Id: StateAwareActionForward.java 1668 2004-12-08 13:38:57Z psa2920 $
 */
public class StateAwareActionForward extends ActionForward {

	/** The state to be assigned to destination resource. */
	private String state;

	/**
	 * Returns the state to be assigned to destination resource.
	 * @return the state.
	 */
	public String getState() {
		return state;
	}

	/**
	 * Sets the state to be assigned to destination resource.
	 * @param string the state.
	 */
	public void setState(String string) {
		state = string;
	}

}
