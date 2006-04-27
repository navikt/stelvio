package no.nav.web.framework.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import no.nav.web.framework.action.FindDispatchAction;

/**
 * Used by RequestUtilsTest to test findForward
 *
 * @author Jonas Lindholm, Accenture
 * @version $Revision: 2365 $ $Author: skb2930 $ $Date: 2005-06-23 16:20:54 +0200 (Thu, 23 Jun 2005) $
 */
public class DummyAction extends FindDispatchAction {

	public ActionForward executeFindForwardThreePara(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		return mapping.findForward("Action2");
	}
}
