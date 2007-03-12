package no.stelvio.consumer;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import com.ibm.ws.webservices.multiprotocol.AgnosticService;

public abstract class ConsumerFacadeBase<T> {
	
	Class<? extends AgnosticService> serviceLocatorClass;
	AgnosticService serviceLocator;
	protected T service=null;
	protected final Log log = LogFactory.getLog(this.getClass());
	
	public ConsumerFacadeBase(Class<? extends AgnosticService>  serviceLocatorClass){
		this.serviceLocatorClass=serviceLocatorClass;
		if (serviceLocatorClass==null){
			NullPointerException npe=new NullPointerException("serviceLocatorClass must be specified, null is not an acceptable value here.");
			log.error(npe.getMessage(),npe);
			throw npe;
		}
				
	}
	
	//Available in case of a later need to inject the generated interface
	public void setService(T serviceIf) {
		this.service=serviceIf;		
	}
	
	
	public final T getService(){
		if (this.service!=null)
			return this.service;
		
		if (serviceLocator==null){
			this.initServiceLocator();
		}
		
		String serviceLocatorName=serviceLocatorClass.getSimpleName();
		if (!serviceLocatorName.endsWith("ServiceLocator")){
			throw new RuntimeException("Class "+serviceLocatorName+" does not have the 'ServiceLocator' suffix, so its not usable for reflecting purposes");			
		}
		String serviceName=serviceLocatorName.substring(0, serviceLocatorName.length()-14);  //Length of "ServiceLocator"+1
		//System.out.println(serviceName);
		if (portFullAddress==null){
			String portAddress;
			try {
				Method getPortAddressMethod=serviceLocatorClass.getMethod("get"+serviceName+"PortAddress", new Class[0]);
				portAddress= (String) getPortAddressMethod.invoke(serviceLocator, new Object[0]);			
			} catch (Exception e) {			
				throw new RuntimeException("Unable to retrieve a port address using the service locator class "+serviceLocatorClass.getName(),e);			
			}
			if (!portAddress.startsWith("http://"))
				throw new RuntimeException("The port address '"+portAddress+"' does not start with 'http://'. Can not interpret this.");
			int urlSeparator=portAddress.indexOf("/", 8);
			if (urlSeparator==-1)
				throw new RuntimeException("Did not find additional occurences of / separator (after http://..) in the string '"+portAddress+"'. Unable to continue");
			//String defaultServerAddress=portAddress.substring(0, urlSeparator);
			String defaultLocalURL=portAddress.substring(urlSeparator+1,portAddress.length());
			//System.out.println(defaultServerAddress+" "+defaultLocalURL);
			if (portServerAddress==null){
				throw new RuntimeException("portServerAddress or portFullAddress not injected");
			}
			this.portFullAddress=portServerAddress+defaultLocalURL;			
		}
		log.debug("Service "+serviceLocator.getServiceName()+ " using URL "+portFullAddress);
		
		try {
			Method getServicePortMethod=serviceLocatorClass.getMethod("get"+serviceName+"Port", new Class[]{URL.class});
			T res= (T) getServicePortMethod.invoke(serviceLocator, new Object[]{new URL(portFullAddress)});
			if (res==null)
				throw new NullPointerException("Empty result from getServicePortMethod");
			return res;
		}catch(Exception e) {
			log.error("",e);
			RuntimeException re=new RuntimeException("Error while getting service port using service locator class "+serviceLocatorClass.getName()+" and port address "+portFullAddress,e);
			
			throw re;
		}
	}
	
	
	private void initServiceLocator() {
		try {
			serviceLocator=serviceLocatorClass.newInstance();
			//PSELVPersonWSEXP_PSELVPersonHttpServiceLocator loc=new PSELVPersonWSEXP_PSELVPersonHttpServiceLocator();
			ArrayList handlerList=new ArrayList();
			if (isContextEnabled()){
				HandlerInfo navContextHInfo=new HandlerInfo(ConsumerContextHandler.class,null,null);
				handlerList.add(navContextHInfo);
			}
			if (isSecurityEnabled()){
				Map<String,String> config=new HashMap<String,String>();
				config.put(LTPASecurityHandler.userNameConfigString, this.serviceUsername);
				config.put(LTPASecurityHandler.passwordConfigString, this.servicePassword);
				HandlerInfo securityHInfo=new HandlerInfo(LTPASecurityHandler.class,config,null);
				handlerList.add(securityHInfo);
			}
			
			
			Iterator portIterator=serviceLocator.getPorts();
			while (portIterator.hasNext()){
				//System.out.println("Adding handlerlist");
				QName portName=((QName)portIterator.next());
				serviceLocator.getHandlerRegistry().setHandlerChain(portName,handlerList);//TODO: check if already present
			}						
		} catch (Exception e) {
			log.error("Error while setting up handlerchain for outgoing service:"+serviceLocatorClass.getName(), e);
		}
		
	}

	public void setPortServerAddress(String serverAddress){
		this.portServerAddress=serverAddress;
	}
	
	public void setPortFullAddress(String portFullAddress){
		this.portFullAddress=portFullAddress;		
	}
	
	private String portServerAddress=null;
	private String portFullAddress=null;
	
	private String serviceUsername=null;
	private String servicePassword=null;
	
	private boolean contextEnabled=true;
	private boolean securityEnabled=false;

	public boolean isSecurityEnabled() {
		return securityEnabled;
	}

	public void setSecurityEnabled(boolean securityEnabled) {
		this.securityEnabled = securityEnabled;
	}

	public boolean isContextEnabled() {
		return contextEnabled;
	}

	public void setContextEnabled(boolean enableContext) {
		this.contextEnabled = enableContext;
	}

	public String getServicePassword() {
		return servicePassword;
	}

	public void setServicePassword(String servicePassword) {
		this.servicePassword = servicePassword;
	}

	public String getServiceUsername() {
		return serviceUsername;
	}

	public void setServiceUsername(String serviceUsername) {
		this.serviceUsername = serviceUsername;
		
	}
	
	
}
