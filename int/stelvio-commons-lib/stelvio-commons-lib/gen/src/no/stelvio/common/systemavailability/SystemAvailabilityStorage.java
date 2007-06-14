/*
 * Created on Mar 27, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package no.stelvio.common.systemavailability;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ibm.websphere.bo.BOXMLDocument;
import com.ibm.websphere.bo.BOXMLSerializer;
import com.ibm.websphere.sca.ServiceManager;
import commonj.sdo.DataObject;





/**
 * @author utvikler
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SystemAvailabilityStorage{	
	String saDirName=null;
	private com.ibm.websphere.bo.BOFactory boFactory = null;
	BOXMLSerializer xmlSerializerService=null;
	
	public SystemAvailabilityStorage(){

		ServiceManager serviceManager = new com.ibm.websphere.sca.ServiceManager();
		xmlSerializerService=(BOXMLSerializer)serviceManager.locateService("com/ibm/websphere/bo/BOXMLSerializer");
		boFactory = (com.ibm.websphere.bo.BOFactory)serviceManager.locateService("com/ibm/websphere/bo/BOFactory");
		
		saDirName=System.getProperty("stelvio.systemavailability.directory");
		if (saDirName==null){
			saDirName=System.getProperty("user.dir")+"/StelvioSystemAvailabilityFramework";
		}
		File saDir=new File(saDirName);
		if (!saDir.exists())
			saDir.mkdirs();
		//System.out.println("Setting working directory to "+saDirName );
		
	}
	
	public String getSystemAvailabilityDirectory(){
		return saDirName;
	}
	
	public void close(){			
		//For potential DB support.
	}
	
	public List listAvailabilityRecordSystemNames(){
		ArrayList ret=new ArrayList();
		File dir=new File(saDirName);
		File[] sysList=dir.listFiles();
		for (int i=0;i<sysList.length;i++){
			if (sysList[i].isFile())
				ret.add(sysList[i].getName().substring(0,sysList[i].getName().length()-4));  //Deducts .xml-suffix
		}
		return ret;				
	}
	
	public void storeAvailabilityRecord(AvailabilityRecord record){
		try {
			DataObject obj=	 boFactory.create("http://stelvio-commons-lib/no/stelvio/common/systemavailability", "SystemAvailabilityRecord");
			obj.setString("SystemName",record.systemName);
			
			List operationList=new ArrayList();
			int i;
			if (record.operations==null)
				record.operations=new ArrayList();
			Iterator opIt=record.operations.iterator();
			while (opIt.hasNext()){
				OperationAvailabilityRecord opRec=(OperationAvailabilityRecord) opIt.next();
				
				DataObject operationObj=boFactory.create("http://stelvio-commons-lib/no/stelvio/common/systemavailability", "OperationAvailabilityRecord");
				operationObj.setString("OperationName",opRec.operationName);
				operationObj.setBoolean("Unavailable",opRec.unAvailable);
				operationObj.setString("Reason",opRec.unavailableReason);
				operationObj.setBoolean("Stubbed",opRec.stubbed);
				operationObj.setBoolean("RecordStubData",opRec.recordStubData);
				operationList.add(operationObj);
			}
			obj.setList("Operations",operationList);			
			File f=new File(saDirName,record.systemName+".xml");  //May not handle special characters
			FileOutputStream fos;			
			fos = new FileOutputStream(f);			
			xmlSerializerService.writeDataObject(obj,"http://no/stelvio/systemavailability/","SystemAvailabilityRecord",fos);
			fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public AvailabilityRecord getAvailabilityRecord(String systemName){
		
		try {
			File f=new File(saDirName,systemName+".xml");
			if (!f.exists())
				return null;
			FileInputStream fis=new FileInputStream(f);
			BOXMLDocument doc=xmlSerializerService.readXMLDocument(fis);
			DataObject obj=doc.getDataObject();
			AvailabilityRecord rec=new AvailabilityRecord();
			rec.systemName=obj.getString("SystemName");	
			rec.operations=new ArrayList();
			List storedOperationList=obj.getList("Operations");
			Iterator i=storedOperationList.iterator();
			while (i.hasNext()){
				DataObject storedOperation=(DataObject) i.next();						
				OperationAvailabilityRecord opRec=new OperationAvailabilityRecord();	
				opRec.operationName=storedOperation.getString("OperationName");
				opRec.unAvailable=storedOperation.getBoolean("Unavailable");
				opRec.unavailableReason=storedOperation.getString("Reason");
				opRec.stubbed=storedOperation.getBoolean("Stubbed");
				opRec.recordStubData=storedOperation.getBoolean("RecordStubData");
				rec.operations.add(opRec);			
			}
			fis.close();
			return rec;			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
		
	
	
	
	public OperationAvailabilityRecord calculateOperationAvailability(String systemName, String operationName){
		AvailabilityRecord sRec=findOrCreateSystemRecord(systemName);
		OperationAvailabilityRecord opRec=sRec.findOrCreateOperation(operationName);
		OperationAvailabilityRecord allOpRec=sRec.findOrCreateOperation("ALL");
		OperationAvailabilityRecord resultRec=new OperationAvailabilityRecord();
		resultRec.operationName=operationName;
		if (allOpRec.unAvailable){
			resultRec.unAvailable=allOpRec.unAvailable;
			resultRec.unavailableReason=allOpRec.unavailableReason;			
		}else{
			resultRec.unAvailable=opRec.unAvailable;
			resultRec.unavailableReason=opRec.unavailableReason;
		}
		resultRec.stubbed=allOpRec.stubbed||opRec.stubbed;
		resultRec.recordStubData=allOpRec.recordStubData||opRec.recordStubData;
		return resultRec;
	}
	
	
	public AvailabilityRecord findOrCreateSystemRecord(String systemName){
		AvailabilityRecord rec=this.getAvailabilityRecord(systemName);
		if (rec==null){			
			rec=new AvailabilityRecord();
			rec.systemName=systemName;	
			rec.operations=new ArrayList();	
			rec.findOrCreateOperation("ALL");
			storeAvailabilityRecord(rec);
		}
		return rec;
	}
	
	
	public void deleteAvailabilityRecord(String systemName){
		File f=new File(saDirName,systemName+".xml");
		if (!f.exists())
			return;
		f.delete();
		return;
	}

}
