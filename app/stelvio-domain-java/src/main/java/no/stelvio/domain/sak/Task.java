package no.stelvio.domain.sak;

import java.io.Serializable;

/***
 * Domain object representing a Task
 * @author person983601e0e117 (Accenture)
 *
 * @deprecated use <code>no.nav.domain.pensjon.common.saksbehandling.Oppgave<code>
 */
public class Task implements Serializable {
	

	private static final long serialVersionUID = 0L;

	private long taskId;
	
	/**
	 * Constructs a new task
	 * @param taskId uniquely identifies this task
	 */
	public Task(long taskId){
		this.taskId = taskId;
	}

	/**
	 * Gets task id that is unique for this task
	 * @return taskid 
	 */
	public long getTaskId() {
		return taskId;
	}

	/**
	 * Sets task id
	 * @param taskId that uniquely identifies this task
	 */
	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}


}
