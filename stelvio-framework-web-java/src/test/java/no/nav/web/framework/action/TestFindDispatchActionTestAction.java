package no.nav.web.framework.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import no.nav.common.framework.service.ServiceDelegate;
import no.nav.web.framework.action.FindDispatchAction;


/**
 * Description
 *
 * @author Jonas Lindholm
 * @version $Revision: 1202 $ $Author: tsb2920 $ $Date: 2004-08-30 15:20:07 +0200 (Mon, 30 Aug 2004) $
 */
public class TestFindDispatchActionTestAction extends FindDispatchAction {

	public ActionForward executeTest(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		log.info("executeTest");

		return mapping.findForward("success");
	}

	/**
	 * setServlet runs when the Action is created. This method test that the servlet is set. 
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward executeSetServlet(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		log.info("executeSetServlet - Start");

		return mapping.findForward("setServlet");

/*		if (getServlet() instanceof ActionServletMock) {
			log.info("executeSetServlet - ActionServletMock");
			return mapping.findForward("setServlet");
		} else {
			log.info("executeSetServlet - IKKE ActionServletMock");
			return mapping.findForward("failure");
		}
*/
	}

	public ActionForward executeGetBusinessDelegate(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		log.info("executeGetBusinessDelegate - Start");

//		log.info("executeGetBusinessDelegate - " + getBusinessDelegate().toString());

		if (getBusinessDelegate() instanceof ServiceDelegate) {
//		if ("dummy BD".equals(getBusinessDelegate())) {
			log.info("executeGetBusinessDelegate - Riktig");
			return mapping.findForward("getBusinessDelegate");
		} else {
			log.info("executeGetBusinessDelegate - FEIL !!");
			return mapping.findForward("failure");
		}
				

	}

}
