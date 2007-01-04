package no.nav.presentation.pensjon.saksbehandling.web.tags;

import java.util.ArrayList;
import java.util.List;
import no.nav.presentation.pensjon.saksbehandling.stelvio.dao.Oppgave;

//import no.stelvio.common.tasklist.TasklistService;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.apache.myfaces.custom.tree2.TreeModel;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class TasklistBacker {
	
	private TasklistTreeModel tasklistTreeModel;
	
	public TasklistBacker(){

		FacesContext facesContext = FacesContext.getCurrentInstance();
		WebApplicationContext webAppContext = WebApplicationContextUtils.getWebApplicationContext((ServletContext)facesContext.getExternalContext().getContext());
		
		//Retrieval of data from the service
		//TasklistService tasklistService = (TasklistService) webAppContext.getBean("tasklist.tasklistService"); 
		//this.tasklistTreeModel = new TasklistTreeModel(tasklistService.getSaksbehandlerOppgaver(saksbehandlerID), tasklistService.getNAVEnhetOppgaver(enhetsID), tasklistService.getSaksbehandlerOppgaver(saksbehandlerID2));
		List<Oppgave> saksbehandlerOppgaver = getMockTasklistSaksbehandler();
		List<Oppgave> enhetsOppgaver = getMockTasklistSaksbehandler();
		List<Oppgave> saksbehandler2Oppgaver = getMockTasklistSaksbehandler();
		this.tasklistTreeModel = new TasklistTreeModel(saksbehandlerOppgaver, enhetsOppgaver, saksbehandler2Oppgaver);
	}
	
	public TreeModel getTreeModel(){
		return this.tasklistTreeModel.getTreeModel();
	}
	
	private List<Oppgave> getMockTasklistSaksbehandler(){
		Oppgave opg1 = new Oppgave("1", "Generell", "Bidrag");
		Oppgave opg2 = new Oppgave("1", "Generell", "Bidrag");
		Oppgave opg3 = new Oppgave("1", "Journalføring", "Bidrag");
		Oppgave opg4 = new Oppgave("1", "Journalføring", "Bidrag");
		Oppgave opg5 = new Oppgave("1", "Generell", "Bidrag");
		Oppgave opg6 = new Oppgave("1", "Generell", "Pensjon");
		Oppgave opg7 = new Oppgave("1", "Generell", "Pensjon");
		Oppgave opg8 = new Oppgave("1", "Generell", "Pensjon");
		Oppgave opg9 = new Oppgave("1", "Journalføring", "Pensjon");
		Oppgave opg10 = new Oppgave("1", "Journalføring", "Pensjon");
		
		List<Oppgave> oppgaver = new ArrayList<Oppgave>();
		oppgaver.add(opg1);
		oppgaver.add(opg2);
		oppgaver.add(opg3);
		oppgaver.add(opg4);
		oppgaver.add(opg5);
		oppgaver.add(opg6);
		oppgaver.add(opg7);
		oppgaver.add(opg8);
		oppgaver.add(opg9);
		oppgaver.add(opg10);
		
		return oppgaver;		
	}
}
