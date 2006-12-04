package no.stelvio.common.tasklist;

import java.util.ArrayList;
import java.util.List;

//import no.stelvio.business.domain.Oppgave;

/**
 * Class implementing the interface TasklistService, that retrives lists of 
 * tasks for an employee/case worker or a Nav unit.
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
//TODO: This is an EJB that should be placed outside of the common module
public class TasklistServiceImpl implements TasklistService {
	
	/** 
	 * {@inheritDoc}
	 */
//	TODO: Use this when the EJB is placed in the correct place : public List<Oppgave> getCaseworkerTasks(String caseworkerID){
	public List getCaseworkerTasks(String caseworkerID){
		
		//TODO - This method must be updated with a call to a service exposed
		//by the integration-layer, when the design of the service is ready, 
		//and it is decided how the service shall be called
		//An exception should be thrown if the caseworkers tasks cannot be retrieved
		
		//TODO - Retrieve the list of Oppgave's, assign them to the oppgaver list, and return them.
		//List<Oppgave> tasks = new ArrayList<Oppgave>();
		
		List tasks = new ArrayList();
		return tasks;
	}
	
	/** 
	 * {@inheritDoc}
	 */
//	TODO: Use this when the EJB is placed in the correct place : public List<Oppgave> getUnitTasks(String unitID){
	public List getUnitTasks(String unitID){
		
		//TODO - This method must be updated with a call to a service exposed
		//by the integration-layer, when the design of the service is ready, 
		//and it is decided how the service shall be called
		//An exception should be thrown if the units tasks cannot be retrieved.
		
		//TODO - Retrieve the list of Oppgave's  assign them to the oppgaver list, and return them.
		//List<Oppgave> tasks = new ArrayList<Oppgave>();
		
		List tasks = new ArrayList();
		
		return tasks;
	}
}