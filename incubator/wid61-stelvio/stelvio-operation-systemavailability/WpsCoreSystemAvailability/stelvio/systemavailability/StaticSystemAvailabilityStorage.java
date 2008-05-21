/*
 * Created on Mar 27, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package stelvio.systemavailability;

import java.util.HashMap;

/**
 * @author utvikler
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StaticSystemAvailabilityStorage extends HashMap{	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7059554968058865863L;
	
	private StaticSystemAvailabilityStorage(){};
	private static StaticSystemAvailabilityStorage instance=null;
	public static StaticSystemAvailabilityStorage getInstance(){
		//TODO: make thread-safe
		if (instance==null)
			instance=new StaticSystemAvailabilityStorage();
		return instance;
	}
	
	@SuppressWarnings("unchecked")
	public void addAvailabilityRecord(String systemName){
		AvailabilityRecord rec=new AvailabilityRecord();
		rec.systemName=systemName;
		rec.available=true;
		rec.unavailableReason="";
		this.put(systemName,rec);
	}
	
	public AvailabilityRecord checkCurrentAvailability(String systemName){
		if (!this.containsKey(systemName)){
			addAvailabilityRecord(systemName);
		}
		return (AvailabilityRecord) this.get(systemName);
	}

}
