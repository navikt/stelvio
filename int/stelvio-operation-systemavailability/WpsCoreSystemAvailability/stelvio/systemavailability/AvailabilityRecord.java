/*
 * Created on Mar 27, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package stelvio.systemavailability;

import java.util.Date;

/**
 * @author utvikler
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AvailabilityRecord {
	public boolean available;
	public String systemName;
	public String unavailableReason;
	public Date unavailableFrom;
	public Date unavailableTo;
	public boolean stubbed;
	public boolean recordStubData;
	

}
