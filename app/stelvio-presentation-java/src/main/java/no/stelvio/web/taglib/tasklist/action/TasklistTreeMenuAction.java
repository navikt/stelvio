package no.stelvio.web.taglib.tasklist.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import no.stelvio.web.taglib.tasklist.Area;
import no.stelvio.web.taglib.tasklist.Responsible;
import no.stelvio.web.taglib.tasklist.Saksbehandler;
import no.stelvio.web.taglib.tasklist.TaskType;
import no.stelvio.web.taglib.tasklist.util.SaksbehandlerUtil;
import no.stelvio.web.taglib.tasklist.util.TasklistTreeUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.custom.tree2.TreeNode;
import org.apache.myfaces.custom.tree2.TreeNodeBase;
import org.springframework.webflow.executor.jsf.FlowExecutionHolderUtils;
import org.springmodules.cache.annotations.Cacheable;

/**
 * TODO: Document me
 * TODO: Add logging
 * TODO: Decide where to apply caching
 * 
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 */
public class TasklistTreeMenuAction implements Serializable {
	// TODO: Add genereated serialVersionUID
	private static final long serialVersionUID = 1L;
	
	// Services which implemented classes are injected using Spring 
	private TasklistTreeUtil tasklistTreeUtil;
	private SaksbehandlerUtil saksbehandlerUtil;
	
	/**
	 * TODO: Document me
	 * TODO: Add correct error handling
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public TreeNode getTreeData() throws Exception {
		TreeNode rootTreeNode = new TreeNodeBase("rootFolder", "Ansvarlige", false);

		// Get the logged in saksbehandler object
		Saksbehandler saksbehandler = getLoggedInSaksbehandler();
		
		if (saksbehandler == null) {
			throw new Exception("Ingen saksbehandler logget inn.");
		}
		
		// Get TreeModel from service (TODO: Change getSaksbehandlerNr return type from Long to String)
		List<Responsible> responsibles = getTreeModel(""+saksbehandler.getSaksbehandlernr());
		
		// Add an additional case worker's task if specified in drop-down list
		Responsible newResponsible = getOtherSaksbehandler();
		if (newResponsible != null) {
			responsibles.add(newResponsible);
		}
		
		// Add responsibility nodes to root node
		for (Responsible responsible : responsibles) {
			TreeNode responsibleNode = new TreeNodeBase("responsible", responsible.getDescription(), false);
			responsibleNode.setIdentifier(responsible.getId());
			
			// Add area nodes to responsibility node
			for (Area area : responsible.getAreas()) {
				TreeNode areaNode = new TreeNodeBase("area", area.getDescription(), false);
				areaNode.setIdentifier(responsible.getId()+";"+area.getId());
				
				// Add task type nodes to area node
				for (TaskType taskType : area.getTaskTypes()) {
					TreeNode taskTypeNode = new TreeNodeBase("taskType", taskType.getDescription(), true);
					taskTypeNode.setIdentifier(responsible.getId()+";"+area.getId()+";"+taskType.getId());
					areaNode.getChildren().add(taskTypeNode);
				}
				
				responsibleNode.getChildren().add(areaNode);
			}
			
			rootTreeNode.getChildren().add(responsibleNode);
		}
		
		return rootTreeNode;
	}
	
	/**
	 * TODO: Document me
	 * TODO: Change modelId
	 * 
	 * @param saksbehandlerNr
	 * @return
	 */
	@Cacheable(modelId="persistent")
	private List<Responsible> getTreeModel(String saksbehandlerNr) {
		return tasklistTreeUtil.getTaskTreeModel(saksbehandlerNr);
	}
	
	/**
	 * TODO: Document me
	 */
	private Responsible getOtherSaksbehandler() {
		TasklistMenuForm form = (TasklistMenuForm) FlowExecutionHolderUtils.getFlowExecutionHolder(FacesContext.getCurrentInstance()).getFlowExecution().getActiveSession().getScope().get("tasklistMenuForm");
		if (form != null) {
			if (!StringUtils.isEmpty(form.getOtherCaseWorkerId())) {
				return getResponsible(form.getOtherCaseWorkerId());
			}
		}
		return null;
	}
	
	/**
	 * TODO: Document me
	 * TODO: Change modelId
	 */
	@Cacheable(modelId="persistent")
	private Responsible getResponsible(String responsibleId) {
		return saksbehandlerUtil.getSaksbehandler(responsibleId);
	}
	
	/**
	 * TODO: Document me
	 * TODO: Call from webflow configuration on page enter
	 * @return
	 */
	public TasklistMenuForm setupTasklistMenu() {
		TasklistMenuForm form = new TasklistMenuForm();
		
		List<Responsible> saksbehandlere = saksbehandlerUtil.getSaksbehandlere(getCurrentEnhet());
		
		List<SelectItem> selectItems = new ArrayList<SelectItem>();
		for (Responsible saksbehandler : saksbehandlere) {
			selectItems.add(new SelectItem(saksbehandler.getId(), saksbehandler.getDescription()));
		}
		
		form.setOtherCaseWorkers(selectItems);
		
		return form;
	}
	
	/**
	 * TODO: Document me
	 * TODO: Get real saksbehandler from session
	 * @return
	 */
	private Saksbehandler getLoggedInSaksbehandler() {
		return (Saksbehandler) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("saksbehandler");
	}
	
	/**
	 * TODO: Document me
	 * @return
	 */
	private String getCurrentEnhet() {
		if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("enhet")) {
			return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("enhet");
		}
		return null;
	}
	
	/**
	 * @param saksbehandlerUtil the saksbehandlerUtil to set
	 */
	public void setSaksbehandlerUtil(SaksbehandlerUtil saksbehandlerUtil) {
		this.saksbehandlerUtil = saksbehandlerUtil;
	}
	
	/**
	 * @param tasklistTreeUtil the tasklistTreeUtil to set
	 */
	public void setTasklistTreeUtil(TasklistTreeUtil tasklistTreeUtil) {
		this.tasklistTreeUtil = tasklistTreeUtil;
	}
}