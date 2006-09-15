package no.nav.web.framework.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * <b>For Test Only!</b> &nbsp; Sets the state and path. 
 *
 * @author Jonas Lindholm, Accenture
 * @version $Id: SetStateAction.java 2149 2005-03-30 09:52:04Z tkc2920 $
 */
public class SetStateAction extends FindDispatchAction {

	/**
	 * Load method. 
	 * 
	 * @param mapping the current action mapping.
	 * @param form the current action form.
	 * @param request the current HTTP request.
	 * @param response the current HTTP response.
	 * @return a <i>StateAwareSessionScopeActionForward</i> with specified state.
	 */
	public ActionForward executeLoad(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) {
		return mapping.getInputForward();
	}

	/**
	 * Forwards to <i>path</i> and <i>state</i>. 
	 * 
	 * @param mapping the current action mapping.
	 * @param form the current action form.
	 * @param request the current HTTP request.
	 * @param response the current HTTP response.
	 * @return a <i>StateAwareSessionScopeActionForward</i> with specified state.
	 */
	public ActionForward executeSetState(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) {

		DynaActionForm stateForm = (DynaActionForm) form;

		StateAwareSessionScopeActionForward forward = new StateAwareSessionScopeActionForward();
		forward.setState((String) stateForm.get("state"));
		forward.setPath((String) stateForm.get("path"));

		return forward;
	}
}
