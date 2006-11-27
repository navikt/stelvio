package no.stelvio.web.taglib.tasklist.action;

import java.util.List;

import javax.faces.model.SelectItem;

/**
 * TODO: Document me
 * 
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 */
public class TasklistMenuForm {
	private List<SelectItem> otherCaseWorkers;
	private String otherCaseWorkerId;

	/**
	 * @return the otherCaseWorkerId
	 */
	public String getOtherCaseWorkerId() {
		return otherCaseWorkerId;
	}

	/**
	 * @param otherCaseWorkerId the otherCaseWorkerId to set
	 */
	public void setOtherCaseWorkerId(String otherCaseWorker) {
		this.otherCaseWorkerId = otherCaseWorker;
	}

	/**
	 * @return the otherCaseWorkers
	 */
	public List<SelectItem> getOtherCaseWorkers() {
		return otherCaseWorkers;
	}

	/**
	 * @param otherCaseWorkers the otherCaseWorkers to set
	 */
	public void setOtherCaseWorkers(List<SelectItem> otherCaseWorkers) {
		this.otherCaseWorkers = otherCaseWorkers;
	}
}