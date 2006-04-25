package no.trygdeetaten.common.framework.monitor;

/**
 * Holds a single error report.
 * @author person356941106810, Accenture
 */
public class ErrorReport {
	/** Holds the number of calls for this report*/
	private long numCalls = 0l;

	/** Holds the number errors for this report */
	private long numErrors = 0l;

	/** Holds the time this report was created */
	private long reportTime = System.currentTimeMillis();

	/** Holds the error ratio for this report */
	private double errorRatio = 0.0d;

	/**
	 * Returns the error ratio for this log
	 * @return error ratio
	 */
	public double getErrorRatio() {
		return errorRatio;
	}

	/**
	 * Returns the number of calls
	 * @return the number of calls
	 */
	public long getNumCalls() {
		return numCalls;
	}

	/**
	 * Returns the number of errors
	 * @return the number of errors
	 */
	public long getNumErrors() {
		return numErrors;
	}

	/**
	 * Returns the time this report was submitted
	 * @return the report time
	 */
	public long getReportTime() {
		return reportTime;
	}

	/**
	 * Sets the error ratio of this report
	 * @param d the ratio
	 */
	public void setErrorRatio(double d) {
		errorRatio = d;
	}

	/**
	 * sets the number of calls for this report
	 * @param l the number of reports
	 */
	public void setNumCalls(long l) {
		numCalls = l;
	}

	/**
	 * Sets the number of errors of this report
	 * @param l the number of errors
	 */
	public void setNumErrors(long l) {
		numErrors = l;
	}
}
