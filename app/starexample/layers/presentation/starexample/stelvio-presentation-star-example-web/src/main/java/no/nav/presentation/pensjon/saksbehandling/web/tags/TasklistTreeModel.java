package no.nav.presentation.pensjon.saksbehandling.web.tags;

import java.util.ArrayList;
import java.util.List;

import org.apache.myfaces.custom.tree2.TreeModel;
import org.apache.myfaces.custom.tree2.TreeModelBase;
import org.apache.myfaces.custom.tree2.TreeNode;
import org.apache.myfaces.custom.tree2.TreeNodeBase;
import org.apache.myfaces.custom.tree2.TreeState;
import org.apache.myfaces.custom.tree2.TreeStateBase;

import no.nav.presentation.pensjon.saksbehandling.stelvio.dao.Oppgave;

/**
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public class TasklistTreeModel {
	
	private List<Oppgave> sbehandlerOppgaver;
	private List<Oppgave> enhetsOppgaver;
	private List<Oppgave> sbehandler2Oppgaver;
	private TreeState treeState;
	private TreeModel treeModel;
	
	public TasklistTreeModel(List<Oppgave> sbehandlerOppgaver, List<Oppgave> enhetsOppgaver, List<Oppgave> sbehandler2Oppgaver){
		this.sbehandlerOppgaver = sbehandlerOppgaver;
		this.enhetsOppgaver = enhetsOppgaver;
		this.sbehandler2Oppgaver = sbehandler2Oppgaver;
		this.treeState = new TreeStateBase();		
	}
	
	@SuppressWarnings("unchecked")
	public TreeModel getTreeModel(){
		TreeNode rootNode = new TreeNodeBase("root", null, false);
								
		//For alle oppgaver - se på fagområdekoden 
		for(Oppgave opg : sbehandlerOppgaver){
			addTasks(opg, sbehandlerOppgaver, rootNode);
		}
		
		//For alle oppgaver - se på fagområdekoden 
		for(Oppgave opg : enhetsOppgaver){
			addTasks(opg, enhetsOppgaver, rootNode);
		}
				
		//For alle oppgaver - se på fagområdekoden 
		for(Oppgave opg : sbehandler2Oppgaver){
			addTasks(opg, sbehandler2Oppgaver, rootNode);
		}
		
		this.treeModel = new TreeModelBase(rootNode);
		this.treeModel.setTreeState(this.treeState);
		
		return this.treeModel;
	}
	
	@SuppressWarnings("unchecked")
	public void addTasks(Oppgave oppgave, List<Oppgave> oppgaver, TreeNode rootNode){
		List<String> o = new ArrayList<String>();
		
		if(!o.contains(oppgave.getFagomrKode())){
			
			o.add(oppgave.getFagomrKode());
		
			TreeNodeBase tasklistItem = new TreeNodeBase("tasklistItem", oppgave.getFagomrKode(), false);
			rootNode.getChildren().add(tasklistItem);

			addChildren(rootNode, oppgave.getFagomrKode(), oppgaver);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void addChildren(TreeNode treeNode, String parent, List<Oppgave> oppgaver){
		
		List<String> o = new ArrayList<String>();
		
		/*for(Oppgave opg : oppgaver){
			
			if(!o.contains(opg.getOppgType())){
				TreeNodeBase tasklistItem = new TreeNodeBase("tasklistItem", opg.getOppgType(), false);
							
				treeNode.getChildren().add(tasklistItem);
				
				o.add(opg.getOppgType());
			}	
		}	*/
	}	
}
