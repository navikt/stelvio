package no.stelvio.common.security.acegi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.acegisecurity.AccessDeniedException;
import org.acegisecurity.AfterInvocationManager;
import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.afterinvocation.AfterInvocationProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;


/**
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 */
public class AfterInvocationProviderManager implements AfterInvocationManager, InitializingBean {
    

    protected static final Log logger = LogFactory.getLog(AfterInvocationProviderManager.class);

    private List<AfterInvocationProvider> providers;

   

    /**
     * @param config
     */
    public void addProviders(ConfigAttributeDefinition config){
    	
    	this.providers = new ArrayList<AfterInvocationProvider>();
    	Iterator iterator = config.getConfigAttributes();     
    	ConfigAttribute configAttribute = null;
    	try {
    		 while(iterator.hasNext()){
             	configAttribute = (ConfigAttribute)iterator.next();	
             	Class clazz = Class.forName(configAttribute.getAttribute(), true, Thread.currentThread().getContextClassLoader());
             	System.out.println("configAttribute '" + configAttribute + "'is AfterInvocationProvider: " + isAfterInvocationProvider(clazz));
             	if(isAfterInvocationProvider(clazz)){
             		AfterInvocationProvider provider = (AfterInvocationProvider)clazz.newInstance();
             		this.providers.add(provider);
             	}   
             }
		 }catch (ClassNotFoundException ex) {
             throw new IllegalArgumentException("Class '" + configAttribute + "' not found");
         }catch (InstantiationException ine){
        	 throw new IllegalArgumentException("Class '" + configAttribute + "' cannot be instantiated. It is either am interface or abstract class.",ine);
         }catch(IllegalAccessException ile){
        	 throw new IllegalArgumentException("Could not create an instance of class '" + configAttribute + "'",ile);
         }
    }
    
    /* (non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
     
    }
    /* (non-Javadoc)
     * @see org.acegisecurity.AfterInvocationManager#decide(org.acegisecurity.Authentication, java.lang.Object, org.acegisecurity.ConfigAttributeDefinition, java.lang.Object)
     */
    public Object decide(Authentication authentication, Object object, ConfigAttributeDefinition config,
            Object returnedObject) throws AccessDeniedException {
            
    	//populate the provider list with respect to the config attributes
    	addProviders(config);
    	Iterator iter = this.providers.iterator();
            
        Object result = returnedObject;
        System.out.println("---------------- AfterInvocationProviderManager -------------------");
        while (iter.hasNext()) {
            AfterInvocationProvider provider = (AfterInvocationProvider) iter.next();
            result = provider.decide(authentication, object, config, result);
        }
        System.out.println("------------ End AfterInvocationProviderManager -------------------");
        return result;
    }
    
    
    /**
     * @return
     */
    public List<AfterInvocationProvider> getProviders() {
        return this.providers;
    }
      
    
    /**
     * @param clazz
     * @return
     */
    public boolean isAfterInvocationProvider(Class clazz){
    	return AfterInvocationProvider.class.isAssignableFrom(clazz) ? true : false;
    }


    /**
     * Iterates through all <code>AfterInvocationProvider</code>s and ensures each can support the presented
     * class.<p>If one or more providers cannot support the presented class, <code>false</code> is returned.</p>
     *
     * @param clazz the secure object class being queries
     *
     * @return if the <code>AfterInvocationProviderManager</code> can support the secure object class, which requires
     *         every one of its <code>AfterInvocationProvider</code>s to support the secure object class
     */
    public boolean supports(Class clazz) {
    	 if(this.providers != null){
    		Iterator<AfterInvocationProvider> iter = this.providers.iterator();
	        while (iter.hasNext()) {
	            AfterInvocationProvider provider = iter.next();
	            if (!provider.supports(clazz)) {
	                return false;
	            }
	        }
    	    return true;
    	 }else{
    		 return true;
    	 }
    		 
    	 
    	
    }

    public boolean supports(ConfigAttribute attribute) {
       if(this.providers != null){
    	   Iterator<AfterInvocationProvider> iter = this.providers.iterator();

           while (iter.hasNext()) {
               AfterInvocationProvider provider = iter.next();

               if (logger.isDebugEnabled()) {
                   logger.debug("Evaluating " + attribute + " against " + provider);
               }

               if (provider.supports(attribute)) {
                   return true;
               }
           }
    	   return false;
       }else{
    	   return true;
       }
    }
}
