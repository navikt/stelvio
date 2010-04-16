/*
 * Created on Mar 27, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package no.stelvio.common.systemavailability;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author utvikler
 * 
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code Templates
 */
public class AvailabilityRecord {
	public static final int DEFAULT_MAX_SIMULTANEOUS_INVOCATIONS = 30;

	public String systemName;
	public List operations; // OperationAvailabilityRecord
	public int maxSimultaneousInvocations = DEFAULT_MAX_SIMULTANEOUS_INVOCATIONS;

	public String getUnavailableString() {
		OperationAvailabilityRecord allRec = this.findOrCreateOperation("ALL");
		if (allRec.unAvailable)
			return "YES";
		Iterator opIt = operations.iterator();
		while (opIt.hasNext()) {
			OperationAvailabilityRecord opRec = (OperationAvailabilityRecord) opIt.next();
			if (opRec.unAvailable)
				return "PARTLY";
		}
		return " ";

	}

	public void deleteOperationAvailabilityRecord(String operationName) {
		operations.remove(this.findOrCreateOperation(operationName));

	}

	/**
	 * @param string
	 * @return
	 */

	public OperationAvailabilityRecord findOperation(String operationName) {
		if (this.operations == null)
			this.operations = new ArrayList();
		Iterator ops = this.operations.iterator();
		OperationAvailabilityRecord foundRec = null;
		while (foundRec == null && ops.hasNext()) {
			OperationAvailabilityRecord opRec = (OperationAvailabilityRecord) ops.next();
			if (opRec.operationName.equals(operationName)) {
				foundRec = opRec;
			}
		}
		return foundRec;
	}

	@SuppressWarnings("unchecked")
	public OperationAvailabilityRecord findOrCreateOperation(String operationName) {
		OperationAvailabilityRecord foundRec = findOperation(operationName);
		if (foundRec == null) {
			foundRec = new OperationAvailabilityRecord();
			foundRec.unAvailable = false;
			foundRec.unavailableReason = "";
			foundRec.stubbed = false;
			foundRec.recordStubData = false;
			foundRec.operationName = operationName;
			boolean add = this.operations.add(foundRec);
			new SystemAvailabilityStorage().storeAvailabilityRecord(this);
		}
		return foundRec;
	}

	public String getUnavailableReasonString() {
		OperationAvailabilityRecord allRec = this.findOrCreateOperation("ALL");
		if (allRec.unavailableReason != null && !allRec.unavailableReason.equals(""))
			return allRec.unavailableReason;
		Iterator opIt = operations.iterator();
		while (opIt != null && opIt.hasNext()) {
			OperationAvailabilityRecord opRec = (OperationAvailabilityRecord) opIt.next();
			if (opRec.unavailableReason != null && !opRec.unavailableReason.equals(""))
				return opRec.unavailableReason;
		}
		return " ";

	}

	public String getStubbedString() {
		OperationAvailabilityRecord allRec = this.findOrCreateOperation("ALL");
		if (allRec.stubbed)
			return "YES";
		Iterator opIt = operations.iterator();
		while (opIt.hasNext()) {
			OperationAvailabilityRecord opRec = (OperationAvailabilityRecord) opIt.next();
			if (opRec.stubbed)
				return "PARTLY";
		}
		return " ";

	}

	public String getRecordStubDataString() {
		OperationAvailabilityRecord allRec = this.findOrCreateOperation("ALL");
		if (allRec.recordStubData)
			return "YES";
		Iterator opIt = operations.iterator();
		while (opIt.hasNext()) {
			OperationAvailabilityRecord opRec = (OperationAvailabilityRecord) opIt.next();
			if (opRec.recordStubData)
				return "PARTLY";
		}
		return " ";

	}

}
