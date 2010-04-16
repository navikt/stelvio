/*
 * Created on May 1, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package no.stelvio.common.systemavailability;

import java.util.Date;

/**
 * @author utvikler
 * 
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code Templates
 */
public class OperationAvailabilityRecord {
	public boolean unAvailable;
	public String operationName;
	public String unavailableReason;
	public Date unavailableFrom;
	public Date unavailableTo;
	public boolean stubbed;
	public boolean recordStubData;

	public String getStubbedString() {
		if (stubbed)
			return "YES";
		return " ";
	}

	public String getUnavailableString() {
		if (unAvailable)
			return "YES";
		return " ";
	}

	public String getRecordStubDataString() {
		if (recordStubData)
			return "YES";
		return " ";
	}

	public String getUnavailableReason() {
		return unavailableReason;
	}
}
