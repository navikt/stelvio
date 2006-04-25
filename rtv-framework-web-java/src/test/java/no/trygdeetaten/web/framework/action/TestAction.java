package no.trygdeetaten.web.framework.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import no.trygdeetaten.common.framework.service.ServiceRequest;

/**
 * Action to use when testing.
 *
 * @author personf8e9850ed756
 * @version $Revision: 1546 $, $Date: 2004-11-15 18:54:32 +0100 (Mon, 15 Nov 2004) $
 */
public class TestAction extends FindDispatchAction {
	public ActionForward executeLoad(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	                             HttpServletResponse response) throws Exception {
		getBusinessDelegate().execute(new ServiceRequest());
		getCodesTableManager().getCodesTable(null);
		return mapping.findForward("ok");
	}
}
