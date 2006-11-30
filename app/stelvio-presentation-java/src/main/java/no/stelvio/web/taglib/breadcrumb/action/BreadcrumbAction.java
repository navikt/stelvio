package no.stelvio.web.taglib.breadcrumb.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.context.FacesContext;

import no.stelvio.common.context.RequestContext;
import no.stelvio.web.taglib.breadcrumb.Breadcrumb;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.webflow.execution.FlowSession;
import org.springframework.webflow.executor.jsf.FlowExecutionHolderUtils;

/**
 * TODO: Document me
 * 
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 */
public class BreadcrumbAction implements Serializable, MessageSourceAware {
	// TODO: Add generated serialVersionUID
	private static final long serialVersionUID = 1L;
	private MessageSource messages;
	private FlowSession flowSession;

	/**
	 * TODO: Document me
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public  List<Breadcrumb> getBreadcrumb() {
		FlowSession session = getFlowSession();
		List<Breadcrumb> breadcrumbs = new ArrayList<Breadcrumb>();
		
		int index = 0;
		while(session != null) {
			String id = session.getDefinition().getId();
			String caption = session.getDefinition().getCaption();
			String i18nCaption = messages.getMessage(caption, null, caption, RequestContext.getLocale());
			
			breadcrumbs.add(new Breadcrumb((index++), id, i18nCaption));
			session = session.getParent();
		}
		
		int lastShowDividerIndex = breadcrumbs.size() == 1 ? 1 : 2;
		breadcrumbs.get(breadcrumbs.size()-lastShowDividerIndex).setHideCrumbDivider(true);
		
		Collections.sort(breadcrumbs);
		
		return breadcrumbs;
	}

	/**
	 * TODO: Document me
	 */
	public void setMessageSource(MessageSource messages) {
		this.messages = messages;
	}
	
	/**
	 * TODO: Document me
	 * 
	 * @param flowSession
	 */
	public void setFlowSession(FlowSession flowSession) {
		this.flowSession = flowSession;
	}
	
	/**
	 * TODO: Document me
	 * 
	 * @return
	 */
	public FlowSession getFlowSession() {
		if (flowSession == null) {
			flowSession = FlowExecutionHolderUtils.getFlowExecutionHolder(FacesContext.getCurrentInstance()).getFlowExecution().getActiveSession();
		}
		
		return flowSession;
	}
}