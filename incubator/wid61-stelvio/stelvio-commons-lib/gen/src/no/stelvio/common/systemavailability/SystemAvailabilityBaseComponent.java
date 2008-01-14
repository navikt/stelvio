/*
 * Created on Mar 26, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package no.stelvio.common.systemavailability;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;

import com.ibm.websphere.bo.BOFactory;
import com.ibm.websphere.bo.BOXMLDocument;
import com.ibm.websphere.bo.BOXMLSerializer;
import com.ibm.websphere.sca.Service;
import com.ibm.websphere.sca.ServiceBusinessException;
import com.ibm.websphere.sca.ServiceCallback;
import com.ibm.websphere.sca.ServiceManager;
import com.ibm.websphere.sca.ServiceUnavailableException;
import com.ibm.websphere.sca.Ticket;
import com.ibm.websphere.sca.scdl.OperationType;

// LS, ikke øffentlig API og WPS 6.1 deprecated
//import com.ibm.ws.bo.impl.BusObjImpl;
//import com.ibm.ws.bo.impl.BusinessObjectTypeImpl;
import com.ibm.ws.sca.internal.multipart.impl.ManagedMultipartImpl;
import com.ibm.ws.sca.internal.scdl.impl.ManagedReferenceImpl;
import com.ibm.wsspi.sca.multipart.impl.MultipartImpl;

import commonj.sdo.DataObject;
import commonj.sdo.Property;

/**
 * @author utvikler
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SystemAvailabilityBaseComponent implements com.ibm.websphere.sca.ServiceImplSync{

	Service partnerService=null;
	String systemName=null;
	boolean interceptorEnabled=true;
	private com.ibm.websphere.bo.BOFactory boFactory = null;
	
	public SystemAvailabilityBaseComponent(){
		
		boFactory = (com.ibm.websphere.bo.BOFactory)ServiceManager.INSTANCE.locateService("com/ibm/websphere/bo/BOFactory");
	
		
		List references=ServiceManager.INSTANCE.getComponent().getReferences();
		String partnerServiceName=null;
		if (references.size()!=2)
			throw new RuntimeException("AvailabilityCheck: Component "+ServiceManager.INSTANCE.getComponent().getName()+" should have 1 reference (in addition to self). Actual: "+references.size());
		for (int i=0;i<2;i++){
			if (!((ManagedReferenceImpl)references.get(i)).getName().equals("self"))
				partnerServiceName=((ManagedReferenceImpl)references.get(i)).getName();				
		}
		partnerService=(Service) ServiceManager.INSTANCE.locateService(partnerServiceName);
		if (partnerService==null){
			throw new RuntimeException("AvailabilityCheck: Unable to locate partner service named "+partnerServiceName+". Make sure that the constructor is correctly initialized with the partner name (typically \"<InterfaceName>Partner\")");
		}
		String componentName=ServiceManager.INSTANCE.getComponent().getName();
		if (componentName.indexOf("Avail")==-1){
			throw new RuntimeException("AvailabilityCheck: Unable to extract the backend system name from the component name \""+componentName+"\". Make sure that the component name is on the form \"<SystemName>AvailabilityCheck\"");
		}
		systemName=componentName.substring(0,componentName.indexOf("Avail"));
		
		
	}
	
	protected final Object getMyService() {
		return (Object) ServiceManager.INSTANCE.locateService("self");
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.websphere.sca.ServiceImplSync#invoke(com.ibm.websphere.sca.scdl.OperationType, java.lang.Object)
	 */
	public Object invoke(OperationType arg0, Object arg1) throws ServiceBusinessException {
		/*if (new Random().nextFloat()>(.5f)){
			throw new ServiceBusinessException("Sorry dude, System \""+systemName+"\" is now unavailable!");
		}*/
		if (interceptorEnabled){
			OperationAvailabilityRecord availRec=null;
			try{
				availRec=new SystemAvailabilityStorage().calculateOperationAvailability(systemName, arg0.getName());
			}catch(Throwable t){
				System.err.println("AvailabilityCheck: got exception "+t.getMessage()+". Make sure that the stelvio-common-lib is available, and that the version of the StelvioSystemAvailabilityFramework config files matches the stelvio-lib version. Now disabling system availability check...");
				interceptorEnabled=false;
			}
			if (availRec != null){
				if (interceptorEnabled){
					if (availRec.unAvailable){			
						throw new ServiceUnavailableException("The system "+systemName+" is currently not available. Reason: "+availRec.unavailableReason+".");// Expected timeframe for downtime: "+availRec.unavailableFrom.toString()+" to "+availRec.unavailableTo.toString());
					}
					
					/*if (availRec.stubbed){
						StubObjectTemplate template=StaticSystemAvailabilityStorage.getInstance().getStubObjectTemplate(systemName, arg0, arg1);
					}*/
					
					
					if (availRec.stubbed){
						DataObject ret=findMatchingTestData(arg0,(ManagedMultipartImpl)arg1);
						return ret;
						
					}else{
						if (availRec.recordStubData){
							DataObject preRecorded=null;
							try{
								preRecorded=findMatchingTestData(arg0,(ManagedMultipartImpl)arg1);
							}catch(Throwable t){								
							}
							if (preRecorded==null){
							
								long timestamp=System.currentTimeMillis();
								String requestID=Long.toString(timestamp);
								try{
									// LS, quick hack
									//String requestObjectName=((com.ibm.ws.bo.impl.BusinessObjectPropertyImpl)((com.ibm.ws.bo.impl.BusinessObjectTypeImpl)arg0.getInputType()).eAllContents().next()).getName();
									String requestObjectName=((com.ibm.ws.bo.impl.BusObjSpecImpl)((com.ibm.ws.bo.impl.BusinessObjectDataTypeImpl)arg0.getInputType()).eAllContents().next()).getName();									
									recordStubData(arg0,requestID,(ManagedMultipartImpl)arg1,requestObjectName,"Request");
									Object ret=partnerService.invoke(arg0,arg1); 
									// LS, quick hack
									//String responseObjectName=((com.ibm.ws.bo.impl.BusinessObjectPropertyImpl)((com.ibm.ws.bo.impl.BusinessObjectTypeImpl)arg0.getOutputType()).eAllContents().next()).getName();
									String responseObjectName=((com.ibm.ws.bo.impl.BusObjSpecImpl)((com.ibm.ws.bo.impl.BusinessObjectDataTypeImpl)arg0.getOutputType()).eAllContents().next()).getName();
									//System.err.println("Normal");
									recordStubData(arg0,requestID,(ManagedMultipartImpl)ret,responseObjectName,"Response");
									return ret;
								}catch(ServiceBusinessException sbe){
									//System.err.println("Exception");
									recordStubDataException(arg0,requestID,(ManagedMultipartImpl)arg1,sbe);
									throw sbe;
								}
							}else{
								System.out.println("Found prerecorded matching stub data for "+systemName+"."+arg0.getName()+". Ignoring.");
								return partnerService.invoke(arg0,arg1);
							}
							
							
						}
					}
					
				}
			}
		}
		return partnerService.invoke(arg0,arg1);		
	}



	/**
	 * @param arg0
	 * @param impl
	 * @param sbe
	 */
	private void recordStubDataException(OperationType arg0, String requestID,ManagedMultipartImpl impl, ServiceBusinessException sbe) {
		try {
			storeObjectOrPrimitive(arg0,sbe,"exception",requestID,"Exception");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	/**
	 * @param impl
	 * @param requestObjectName
	 * @param string
	 */
	private void recordStubData(OperationType arg0,String requestID,ManagedMultipartImpl impl, String requestObjectName, String fileSuffix) {		
		try {
			//((BusinessObjectTypeImpl)request.getType()).eContents().listIterator().next()
			
			storeObjectOrPrimitive(arg0,impl,requestObjectName,requestID,fileSuffix);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

//	private void recordStubData(OperationType arg0, ManagedMultipartImpl request, ManagedMultipartImpl response) {
//		DataObject respObject=null;
//		if (response!=null){
//			try{
//				respObject=(DataObject) response.get(0);
//			}catch(Throwable t){}		
//		} 
//		recordStubData(arg0,request,respObject,false);
//	}
	
	
//	private void recordStubDataException(OperationType arg0, ManagedMultipartImpl impl, ServiceBusinessException sre) {
//		recordStubData(arg0,impl,(BusObjImpl)sre.getData(),true);
//		
//	}
//	
//	private void recordStubData(OperationType arg0,ManagedMultipartImpl request,DataObject response, boolean exception){	
//		try {
//			//BOXMLDocument doc=xmlSerializerService.createXMLDocument(input,null,null);
//			long tmstamp=System.currentTimeMillis();
//			System.out.println("Recording stubdata "+tmstamp);
//					
//			String dirName = getDirectory(arg0);		
//			
//			
//				
//			//((BusinessObjectTypeImpl)request.getType()).eContents().listIterator().next()
//			FileOutputStream requestfile=new FileOutputStream(dirName+"/Stub_"+tmstamp+"_Request.xml");
//			
//			BOXMLSerializer xmlSerializerService = storeObjectOrPrimitive(request, requestObjectName, requestfile);
//			BusinessObjectTypeImpl impl;			
//			if (exception || response!=null){ //((com.ibm.ws.bo.impl.BusinessObjectTypeImpl)arg0.getOutputType()).eAllContents().hasNext()){
//				String responseObjectName;
//				DataObject responseObject=response;
//				String responseTypeString;
//				if (exception){
//					responseObjectName="exception";
//					responseTypeString="Exception";
//				}else{
//					responseObjectName=((com.ibm.ws.bo.impl.BusinessObjectPropertyImpl)((com.ibm.ws.bo.impl.BusinessObjectTypeImpl)arg0.getOutputType()).eAllContents().next()).getName();
//					responseTypeString="Response";	
//				}
//				//MultipartFactory.eINSTANCE.convertToString((BusinessObjectTypeImpl)request.getType(),request);								
//				FileOutputStream responseFile=new FileOutputStream(dirName+"/Stub_"+tmstamp+"_"+responseTypeString+".xml");
//				storeObjectOrPrimitive(responseObject,responseObjectName,responseFile);
//				xmlSerializerService.writeDataObject(responseObject,"http://no.stelvio.stubdata/",responseObjectName,responseFile);
//				responseFile.close();
//			 }
//			
//			System.out.println("Recorded stubdata "+dirName+"/Stub_"+tmstamp);
//			requestfile.close();
//			
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}		
//		
//		
//	}

	/**
	 * @param request
	 * @param requestObjectName
	 * @param requestfile
	 * @return
	 * @throws IOException
	 */
	private void storeObjectOrPrimitive(OperationType arg0,Object object, String requestObjectName, String requestID,String fileSuffix) throws IOException {
		String dirName = getDirectory(arg0);
		FileOutputStream objectFile=new FileOutputStream(dirName+"/Stub_"+requestID+"_"+fileSuffix+".xml");
		if (object instanceof MultipartImpl){
			if (((ManagedMultipartImpl)object).get(0) instanceof DataObject){
				DataObject requestDataObject= (DataObject)((ManagedMultipartImpl) object).get(0);
				BOXMLSerializer xmlSerializerService=(BOXMLSerializer)new ServiceManager().locateService("com/ibm/websphere/bo/BOXMLSerializer");
				xmlSerializerService.writeDataObject(requestDataObject,"http://no.stelvio.stubdata/",requestObjectName,objectFile);
			}
			else{					
				BOXMLSerializer xmlSerializerService=(BOXMLSerializer)new ServiceManager().locateService("com/ibm/websphere/bo/BOXMLSerializer");
				xmlSerializerService.writeDataObject((ManagedMultipartImpl)object,"http://no.stelvio.stubdata/",requestObjectName,objectFile);

/*				PrintWriter pw=new PrintWriter(objectFile);
				pw.println("PRIMITIVE");
				((ManagedMultipartImpl)pw).get
				pw.close();*/
			}
		}
		else{
			if (object instanceof ServiceBusinessException){
				// LS, merkelig, hvorfor ikke DataObject?
				//BusObjImpl requestDataObject= (BusObjImpl)((ServiceBusinessException) object).getData();
				DataObject requestDataObject= (DataObject)((ServiceBusinessException) object).getData();
				BOXMLSerializer xmlSerializerService=(BOXMLSerializer)new ServiceManager().locateService("com/ibm/websphere/bo/BOXMLSerializer");
				xmlSerializerService.writeDataObject(requestDataObject,"http://no.stelvio.stubdata/",requestObjectName,objectFile);
			}
		}
		System.out.println("Recorded stubdata "+dirName+"/Stub_"+requestID+"_"+fileSuffix+".xml");
		
		objectFile.close();
		
	}

	/**
	 * @param arg0
	 * @return
	 */
	private String getDirectory(OperationType arg0) {
		String dirName=new SystemAvailabilityStorage().getSystemAvailabilityDirectory()+"/"+this.systemName+"/"+arg0.getName();
		File dir=new File(dirName);
		if (!dir.exists())
			dir.mkdirs();
		return dirName;
	}

	/**
	 * @param operationType
	 * @param impl
	 * @return
	 */
	private DataObject findMatchingTestData(OperationType operationType, ManagedMultipartImpl request) throws ServiceBusinessException {	
			BOXMLSerializer xmlSerializerService=(BOXMLSerializer)new ServiceManager().locateService("com/ibm/websphere/bo/BOXMLSerializer");				
			String dirName=getDirectory(operationType);
			try {
				File dir=new File(dirName);
				File[] files=listFilesAlphabetical(dir);
				File foundMatch=null;
				for (int i=0;i<files.length;i++){
					if (files[i].getName().endsWith("_Request.xml")){
						
						FileInputStream fis=new FileInputStream(files[i]);
						Object obj=readObjectOrPrimitive(fis);
						fis.close();
						
						DataObject storedInput=(DataObject)obj;
						if (request.get(0) instanceof DataObject){
							if (match(storedInput,(DataObject)request.get(0))){
								foundMatch=files[i];
								break;
							}
						}else //Primitive interface
						if (match(storedInput,request)){
							foundMatch=files[i];
							break;
						}
							
					}
				}
				if (foundMatch==null){
					throw new RuntimeException("No matching stub found for system "+systemName+", operation "+operationType.getName()+" in path "+dirName);
				}
				DataObject response=null;
				System.out.println("Found matching test data "+dirName+"/"+foundMatch.getName());
				String tmStamp=foundMatch.getName().substring(0,foundMatch.getName().length()-12);  // Deduct "_Request.xml"
	//			BOXMLDocument responseDoc=xmlSerializerService.readXMLDocument(new FileInputStream(new File(dirName,tmStamp+"_ResponseMultipart.xml")));		
	//			DataObject response=responseDoc.getDataObject();	
				File responseFile=new File(dirName,tmStamp+"_Response.xml");
				if (!responseFile.exists()){
					File exceptionFile=new File(dirName,tmStamp+"_Exception.xml");
					if (exceptionFile.exists()){
						FileInputStream fis=new FileInputStream(exceptionFile);
						BOXMLDocument responseObjectDoc=xmlSerializerService.readXMLDocument(fis);
						fis.close();
						DataObject responseObject=responseObjectDoc.getDataObject();
						throw new ServiceBusinessException(responseObject);
					}
				}
				
				if (responseFile.exists())
				{
					FileInputStream fis=new FileInputStream(responseFile);
					BOXMLDocument responseObjectDoc=xmlSerializerService.readXMLDocument(fis);
					fis.close();
					//String responseFeatureName=responseObjectDoc.getRootElementName();
					
						Object responseObject=responseObjectDoc.getDataObject();
					if ((!(responseObject instanceof ManagedMultipartImpl)) || responseObject==null){
			         	BOFactory dataFactory = (BOFactory)ServiceManager.INSTANCE.locateService("com/ibm/websphere/bo/BOFactory");
			         	response=dataFactory.createByType(operationType.getOutputType());
			         	response.set(0,responseObject);
					}else{ //Assume MultipartImpl, because use of primitives in interface
						response=(ManagedMultipartImpl)responseObject;
					}
				}
				//operationType.getOutputType()
				//ManagedMultipartFactoryImpl mmfi=new ManagedMultipartFactoryImpl();
				//MultipartFactory.eINSTANCE.
				//TypeHelper th;
				//mmfi.create(TypeHelper )
					
				//ManagedMultipartImpl response=(ManagedMultipartImpl) MultipartFactory.eINSTANCE.createFromString((EDataType) operationType.getOutputType(),"");//boFactory.createByClass(ManagedMultipartImpl.class);  //("http://stelvio-commons-lib/no/stelvio/common/systemavailability", "SystemAvailabilityRecord");boFactory.create("http://stelvio-commons-lib/no/stelvio/common/systemavailability", "SystemAvailabilityRecord");
	
				//response.//set(0,responseObject);
				//f.setName(responseFeatureName);
	
				
				return response;	
			} catch (IOException e) {
				throw new RuntimeException("Error while finding test data",e);
			}
					
	}
	
	
	/**
	 * @param fis
	 * @return
	 */
	private Object readObjectOrPrimitive(FileInputStream fis) {		
		BOXMLSerializer xmlSerializerService=(BOXMLSerializer)new ServiceManager().locateService("com/ibm/websphere/bo/BOXMLSerializer");				
		BOXMLDocument criteriaDoc;
		try {
			criteriaDoc = xmlSerializerService.readXMLDocument(fis);
			return criteriaDoc.getDataObject();
		} catch (IOException e) {
		
			e.printStackTrace();
			return null;
		}
		
	}

	private boolean match(DataObject criteriaObject, DataObject testObject){		
		
		for (Iterator i = criteriaObject.getType().getProperties().iterator(); i.hasNext();) {
			Property property = (Property) i.next();
			Object criteriaSubObject = criteriaObject.get(property);
			if (criteriaSubObject != null) {
				Object testSubObject=testObject.get(property);
				if (testSubObject==null)
					return false;
				if (criteriaSubObject instanceof DataObject) {					
					if (!(testSubObject instanceof DataObject))
						return false;
					if (!match((DataObject)criteriaSubObject,(DataObject) testSubObject))
						return false;				
				}
				else 
				if(criteriaSubObject instanceof EObjectContainmentEList){
					if (!(testSubObject instanceof EObjectContainmentEList)){
						return false;
					}
					if (((EObjectContainmentEList)testSubObject).basicList().size() != ((EObjectContainmentEList)criteriaSubObject).basicList().size())
						return false;
					Iterator criteriaIterator=((EObjectContainmentEList)criteriaSubObject).basicList().iterator();
					Iterator testIterator=((EObjectContainmentEList)testSubObject).basicList().iterator();
					while (criteriaIterator.hasNext()){
						if (!match((DataObject)criteriaIterator.next(),(DataObject)testIterator.next()))
							return false;
					}
				}
				else
					if (!matchPrimitive(testSubObject,criteriaSubObject))
						return false;			   			
				
			}
			
		}
		return true;		
	}
	

	
	
	
	/**
	 * @param testSubObject
	 * @param criteriaSubObject
	 * @return
	 */
	private boolean matchPrimitive(Object testSubObject, Object criteriaSubObject) {
		if (testSubObject instanceof Date && criteriaSubObject instanceof Date){
			if (!(Math.abs(((Date)testSubObject).getTime() -((Date)criteriaSubObject).getTime())<1000*4000)) //Accept up to an hour difference, due to problems with time zone/DST and serialization'
				return false;					
		}else 
		if (criteriaSubObject instanceof Integer && ((Integer)criteriaSubObject).intValue()==0){ 
								//When criteria of these types are removed,
								//they are still read up as 0 values (or false). This is a hack so that criteria value 0 matches everything
			return true;			
		}else
		if (criteriaSubObject instanceof Long && ((Long)criteriaSubObject).longValue()==0){
			return true;
		}else	
		if (criteriaSubObject instanceof Short && ((Short)criteriaSubObject).shortValue()==0){
			return true;
		}else
		if (criteriaSubObject instanceof Boolean && ((Boolean)criteriaSubObject).booleanValue()==false){
			return true;
		}else
		if (criteriaSubObject instanceof Float && ((Float)criteriaSubObject).floatValue()==0){
			return true;
		}else		
		if (criteriaSubObject instanceof Double&& ((Double)criteriaSubObject).doubleValue()==0){
			return true;
		}
		if(!testSubObject.equals(criteriaSubObject)){									
			return false;				
		}
		return true;
	}

	public File[] listFilesAlphabetical(File dir) {
		String[] ss = dir.list();
		Arrays.sort(ss);
		if (ss == null) return null;
		int n = ss.length;
		File[] fs = new File[n];
		for (int i = 0; i < n; i++) {
		    fs[i] = new File(dir.getPath(), ss[i]);
		}
		return fs;
		}

	/* (non-Javadoc)
	 * @see com.ibm.websphere.sca.ServiceImplAsync#invokeAsync(com.ibm.websphere.sca.scdl.OperationType, java.lang.Object, com.ibm.websphere.sca.ServiceCallback, com.ibm.websphere.sca.Ticket)
	 */
	public void invokeAsync(OperationType arg0, Object arg1, ServiceCallback arg2, Ticket arg3) {
		throw new RuntimeException("Dont know async yet.");
		/*if (new Random().nextFloat()>.5){
			throw new ServiceBusinessException("Sorry dude, System is now unavailable!");
		}	    
		partnerService.invokeAsync(arg0,arg1);*/
		
	}

	/* (non-Javadoc)
	 * @see com.ibm.websphere.sca.ServiceCallback#onInvokeResponse(com.ibm.websphere.sca.Ticket, java.lang.Object, java.lang.Exception)
	 */
	public void onInvokeResponse(Ticket arg0, Object arg1, Exception arg2) {
		// TODO Auto-generated method stub
		
	}

}
