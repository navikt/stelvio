package no.stelvio.domain.tasklist;

import java.io.Serializable;

/***
 * Domain object representing a Task
 * @author person983601e0e117 (Accenture)
 *
 */
public class Task implements Serializable {
	

	private static final long serialVersionUID = 0L;

	private long taskId;
	
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
