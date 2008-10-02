package no.stelvio.common.systemavailability;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;

import com.ibm.websphere.bo.BOFactory;
import com.ibm.websphere.bo.BOXMLDocument;
import com.ibm.websphere.bo.BOXMLSerializer;
import com.ibm.websphere.sca.Service;
import com.ibm.websphere.sca.ServiceBusinessException;
import com.ibm.websphere.sca.ServiceCallback;
import com.ibm.websphere.sca.ServiceManager;
import com.ibm.websphere.sca.ServiceRuntimeException;
import com.ibm.websphere.sca.ServiceUnavailableException;
import com.ibm.websphere.sca.Ticket;
import com.ibm.websphere.sca.scdl.OperationType;
import com.ibm.ws.sca.internal.multipart.impl.ManagedMultipartImpl;
import com.ibm.ws.sca.internal.scdl.impl.ManagedReferenceImpl;
import com.ibm.wsspi.sca.multipart.impl.MultipartImpl;
import commonj.sdo.DataObject;
import commonj.sdo.Property;
import commonj.sdo.Type;

/**
 * @author person73874c7d71f8
 *
 */
public class SystemAvailabilityBaseComponent implements com.ibm.websphere.sca.ServiceImplSync{

	Service partnerService=null;
	String systemName=null;
	boolean interceptorEnabled=true;

	@SuppressWarnings("unused")
	private com.ibm.websphere.bo.BOFactory boFactory = null;
	
	static Properties unavailableExceptionProperties=null;
	
	private final static String className = SystemAvailabilityBaseComponent.class.getName();

	private final Logger log = Logger.getLogger(className);
	
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
	
		if (unavailableExceptionProperties==null){
			synchronized(SystemAvailabilityBaseComponent.class){
				if (unavailableExceptionProperties==null){
					unavailableExceptionProperties=new Properties();
					try {
						InputStream propertyFile = this.getClass().getResourceAsStream("SystemUnavailableExceptionList.properties");
						unavailableExceptionProperties.load(propertyFile);
					} catch (Exception e) {
						String logMessage = componentName + ": Unable to read SystemUnavailableExceptionList.properties from classpath. Will continue with no automatic resubmit flagging for this component";
						log.logp(Level.WARNING, className, "ServiceAvailabilityBaseComponent", logMessage);
					}
					
				}
			}
		}
		
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
				String logMessage = "AvailabilityCheck: got exception " + t.getMessage() + ". Make sure that the stelvio-commons-lib is available, and that the version of the StelvioSystemAvailabilityFramework config files matches the stelvio-lib version. Now disabling system availability check...";
				log.logp(Level.WARNING, className, "getMyService", logMessage);				
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
						
					} else if (availRec.recordStubData){
						DataObject preRecorded=null;
						try {
							preRecorded=findMatchingTestData(arg0,(ManagedMultipartImpl)arg1);
						}catch(ServiceBusinessException sbe){
						    //This is OK, as it is a prerecorded SBE
						}
						catch(ServiceRuntimeException sre){
						    //This is also OK as it is a prerecorded SRE
						}
						catch(RuntimeException re){
							//No prerecorded stub found
													
						
							long timestamp=System.currentTimeMillis();
							String requestID=Long.toString(timestamp);
							
							Type sdoTypeReq = ((DataObject)arg1).getType();
							String requestObjectName=sdoTypeReq.getName();
							recordStubData(arg0,requestID,(ManagedMultipartImpl)arg1,requestObjectName,"Request");
							Object ret;
							try{
								ret=partnerService.invoke(arg0,arg1); 
							}catch(ServiceBusinessException sbe){
								recordStubDataException(arg0,requestID,(ManagedMultipartImpl)arg1,sbe);
								throw sbe;
							}
							catch(ServiceRuntimeException sre){
							    recordStubDataRuntimeException(arg0,requestID,(ManagedMultipartImpl)arg1,sre);
								throw sre;
							}
							Type sdoTypeRes = ((DataObject)arg1).getType();
							String responseObjectName=sdoTypeRes.getName();
							
							recordStubData(arg0,requestID,(ManagedMultipartImpl)ret,responseObjectName,"Response");
							return ret;
							
						}
						String logMessage = "Found prerecorded matching stub data for " + systemName + "." + arg0.getName() + ". Ignoring.";
						log.logp(Level.FINE, className, "invoke", logMessage);
						return partnerService.invoke(arg0,arg1);
					}				
				}
			}
		}
		
		try{
			return partnerService.invoke(arg0,arg1);
		}catch (RuntimeException re){
			boolean wrapInServiceUnavailableException=false;
			String decidingToken="";
			StringWriter sw=new StringWriter();
			PrintWriter pw=new PrintWriter(sw,true);
			re.printStackTrace(pw);
			String stackTrace=sw.toString();
			for (Object key:unavailableExceptionProperties.keySet()){
				String keyString=(String) key;
				if (keyString.startsWith("SUBSTRING")){   //This is a substring match
					String token=unavailableExceptionProperties.getProperty(keyString);
					if (stackTrace.indexOf(token)!=-1){
						wrapInServiceUnavailableException=true;
						decidingToken=token;
					}
				}else{
					if (keyString.startsWith("EXCEPTION")){
						String exceptionName=unavailableExceptionProperties.getProperty(keyString);
						if (exceptionName.equals(re.getClass().getName())){
							wrapInServiceUnavailableException=true;
							decidingToken=exceptionName;
						}
						
					}
				}
			}
			
			if (wrapInServiceUnavailableException){ 
				throw new ServiceUnavailableException("System "+systemName+" received an exception:"+re.getMessage()+". This was automatically flagged as a temporary unavailability due to the presence of the token '"+decidingToken+"' :", re);				
			}
			else throw re;  //This was not an unavailable-exception, just rethrow the original exception.
		}
	}



	/**
	 * @param arg0
	 * @param impl
	 * @param sbe
	 */
	private void recordStubDataException(OperationType arg0, String requestID,ManagedMultipartImpl impl, Throwable sbe) {
		try {
			storeObjectOrPrimitive(arg0,sbe,"exception",requestID,"Exception");
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	private void recordStubDataRuntimeException(OperationType arg0, String requestID,ManagedMultipartImpl impl, Throwable sbe) {
		try {
			storeObjectOrPrimitive(arg0,sbe,"exception",requestID,"RuntimeException");
		} catch (IOException e) {
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
//			log.logp(Level.FINE, className, "recordStubData", "Recording stubdata " + tmstamp);
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
//           String logMessage = "Recorded stubdata " + dirName + "/Stub_" + tmstamp;
//			 log.logp(Level.FINE, className, "recordStubData", logMessage);
//			 requestfile.close();
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
				DataObject requestDataObject= (DataObject) ((ServiceBusinessException)object).getData();
				BOXMLSerializer xmlSerializerService=(BOXMLSerializer)new ServiceManager().locateService("com/ibm/websphere/bo/BOXMLSerializer");
				xmlSerializerService.writeDataObject(requestDataObject,"http://no.stelvio.stubdata/",requestObjectName,objectFile);
			}else 
			    if (object instanceof ServiceRuntimeException){
			        PrintWriter pw=new PrintWriter(objectFile);
			        pw.println(((ServiceRuntimeException)object).getMessage());
			        pw.close();
			    }
		}
		String logMessage = "Recorded stubdata " + dirName + "/Stub_" + requestID + "_" + fileSuffix + ".xml";
		log.logp(Level.FINE, className, "storeObjectOrPrimitive", logMessage);
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
					throw new RuntimeException("No matching stub found for system " + systemName + ", operation " + operationType.getName() + " in path " + dirName);
				}
				DataObject response=null;
				String logMessage = "Found matching test data " + dirName + "/" + foundMatch.getName();
				log.logp(Level.FINE, className, "findMatchingTestData", logMessage);
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
					File runtimeExceptionFile=new File(dirName,tmStamp+"_RuntimeException.xml");
					if (runtimeExceptionFile.exists()){
					    throw new ServiceRuntimeException("A ServiceRuntimeException was recorded, but unfortunately I'm not able yet to provide the original message. It can maybe be found in the recorded data, but I'm pretty dumb.");
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
					if (((EObjectContainmentEList)criteriaSubObject).basicList().size()!=0) //The list in criteria has size 0 if nothing is provided. This should match always.
					{
//						if (((EObjectContainmentEList)testSubObject).basicList().size() != ((EObjectContainmentEList)criteriaSubObject).basicList().size())
//							return false;
						Iterator criteriaIterator=((EObjectContainmentEList)criteriaSubObject).basicList().iterator();						
						while (criteriaIterator.hasNext()){
						    boolean foundmatchingelement=false;
						    DataObject criteriaElement=(DataObject) criteriaIterator.next();
						    Iterator testIterator=((EObjectContainmentEList)testSubObject).basicList().iterator();
						    while (testIterator.hasNext()){
						        if (match(criteriaElement,(DataObject)testIterator.next()))
						            foundmatchingelement=true;
						    }
						    if (!foundmatchingelement)
						        return false;
						}
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
		if (criteriaSubObject instanceof Boolean && ((Boolean)criteriaSubObject).booleanValue()==false){  //Special case: Booleans that are not included in the Stored Criteria are not Null, they are recorded as false. So we must accept this as always match until further notice
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
